package com.xunye.group.service.impl;

import com.xunye.common.constant.GroupConstant;
import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.tools.RedisUtil;
import com.xunye.group.dto.GroupInfoDTO;
import com.xunye.group.dto.GroupInfoEditDTO;
import com.xunye.group.entity.GroupInfo;
import com.xunye.group.entity.QGroupInfo;
import com.xunye.group.mapper.GroupInfoMapper;
import com.xunye.group.repo.GroupInfoRepository;
import com.xunye.group.service.IGroupInfoService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.entity.User;
import com.xunye.core.exception.BusinessException;


@Slf4j
@Service
public class GroupInfoServiceImpl extends BaseServiceImpl<GroupInfoEditDTO, GroupInfoDTO, GroupInfo, String> implements IGroupInfoService {

    private static final QGroupInfo qGroupInfo = QGroupInfo.groupInfo;

    @Resource
    private RedisUtil redisUtil;

    private final GroupInfoMapper groupInfoMapper;
    private final GroupInfoRepository groupInfoRepository;

    public GroupInfoServiceImpl(GroupInfoMapper groupInfoMapper, GroupInfoRepository groupInfoRepository) {
        this.groupInfoMapper = groupInfoMapper;
        this.groupInfoRepository = groupInfoRepository;
    }

    /**
     * 创建group信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createGroupInfo(GroupInfoEditDTO groupInfoEditDto, User operatorUser) {
        // 转换为Entity实体
        GroupInfo groupInfoEntity = groupInfoMapper.toEntity(groupInfoEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            groupInfoEntity.setCreateBy(operatorUser.getId());
            groupInfoEntity.setCreateByName(operatorUser.getUserName());
            groupInfoEntity.setUpdateBy(operatorUser.getId());
            groupInfoEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("group创建失败：创建人信息获取有误");
        }

        // 创建group
        groupInfoRepository.saveAndFlush(groupInfoEntity);
        // Redis设置库存
        //GROUP_STOCK:RouteID:GroupID
        redisUtil.set(GroupConstant.ROUTE_GROUP_STOCK_PREFIX + groupInfoEditDto.getRouteId() + ":" + groupInfoEditDto.getId(), groupInfoEntity.getMaxCount());
        return groupInfoEntity.getId();
    }

    /**
     * 更新group信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGroupInfo(GroupInfoEditDTO groupInfoEditDto, User operatorUser) {
        Optional<GroupInfo> optional = groupInfoRepository.findById(groupInfoEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("group不存在");
        }

        // 将EditDto属性合并到DB实体中
        GroupInfo groupInfoDB = optional.get();
        groupInfoMapper.merge(groupInfoEditDto, groupInfoDB);
        // 设置更新信息
        groupInfoDB.setUpdateBy(operatorUser.getId());
        groupInfoDB.setUpdateByName(operatorUser.getUserName());
        // 更新group
        groupInfoRepository.saveAndFlush(groupInfoDB);
    }

    /**
     * 删除group
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroupInfo(String id, User operatorUser) {
        // before
        
        deleteById(id);
    }

    /**
     * 批量删除group
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGroupInfoBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteGroupInfo(id,operatorUser);
        }
    }

    /**
     * 分页查询group列表
     */
    @Override
    public R<List<GroupInfoDTO>> queryGroupInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<GroupInfo> queryResults = getJpaQueryFactory()
                .select(qGroupInfo)
                .from(qGroupInfo)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qGroupInfo.createTime.desc())
                .fetchResults();

        List<GroupInfoDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询group信息
     */
    @Override
    public GroupInfoEditDTO queryGroupInfoById(String id) {
        Predicate predicate = qGroupInfo.id.eq(id);
        List<GroupInfoEditDTO> queryList = queryGroupInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出group数据
     */
    @Override
    public List<GroupInfoDTO> export(PredicateWrapper predicateWrapper) {
        long count = groupInfoRepository.count();
        R<List<GroupInfoDTO>> exportResult = this.queryGroupInfoListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<GroupInfoEditDTO> queryGroupInfoEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qGroupInfo)
                .where(predicate)
                .orderBy(qGroupInfo.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<GroupInfo, String> getRepository() {
        return groupInfoRepository;
    }

    @Override
    public BaseMapper<GroupInfo, GroupInfoDTO, GroupInfoEditDTO> getMapper() {
        return groupInfoMapper;
    }

}
