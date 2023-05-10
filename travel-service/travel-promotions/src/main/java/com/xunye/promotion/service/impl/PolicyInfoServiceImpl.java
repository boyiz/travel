package com.xunye.promotion.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.entity.User;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.promotion.dto.PolicyInfoDTO;
import com.xunye.promotion.dto.PolicyInfoEditDTO;
import com.xunye.promotion.entity.PolicyInfo;
import com.xunye.promotion.entity.QPolicyInfo;
import com.xunye.promotion.mapper.PolicyInfoMapper;
import com.xunye.promotion.repo.PolicyInfoRepository;
import com.xunye.promotion.service.IPolicyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class PolicyInfoServiceImpl extends BaseServiceImpl<PolicyInfoEditDTO, PolicyInfoDTO, PolicyInfo, String> implements IPolicyInfoService {

    private static final QPolicyInfo qPolicyInfo = QPolicyInfo.policyInfo;

    private final PolicyInfoMapper policyInfoMapper;
    private final PolicyInfoRepository policyInfoRepository;

    public PolicyInfoServiceImpl(PolicyInfoMapper policyInfoMapper, PolicyInfoRepository policyInfoRepository) {
        this.policyInfoMapper = policyInfoMapper;
        this.policyInfoRepository = policyInfoRepository;
    }

    /**
     * 创建优惠政策实体信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPolicyInfo(PolicyInfoEditDTO policyInfoEditDto, User operatorUser) {
        // 转换为Entity实体
        PolicyInfo policyInfoEntity = policyInfoMapper.toEntity(policyInfoEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            policyInfoEntity.setCreateBy(operatorUser.getId());
            policyInfoEntity.setCreateByName(operatorUser.getUserName());
            policyInfoEntity.setUpdateBy(operatorUser.getId());
            policyInfoEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("优惠政策实体创建失败：创建人信息获取有误");
        }

        // 创建优惠政策实体
        policyInfoRepository.saveAndFlush(policyInfoEntity);
        return policyInfoEntity.getId();
    }

    /**
     * 更新优惠政策实体信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePolicyInfo(PolicyInfoEditDTO policyInfoEditDto, User operatorUser) {
        Optional<PolicyInfo> optional = policyInfoRepository.findById(policyInfoEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("优惠政策实体不存在");
        }

        // 将EditDto属性合并到DB实体中
        PolicyInfo policyInfoDB = optional.get();
        policyInfoMapper.merge(policyInfoEditDto, policyInfoDB);
        // 设置更新信息
        policyInfoDB.setUpdateBy(operatorUser.getId());
        policyInfoDB.setUpdateByName(operatorUser.getUserName());
        // 更新优惠政策实体
        policyInfoRepository.saveAndFlush(policyInfoDB);
    }

    /**
     * 删除优惠政策实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePolicyInfo(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除优惠政策实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePolicyInfoBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deletePolicyInfo(id,operatorUser);
        }
    }

    /**
     * 分页查询优惠政策实体列表
     */
    @Override
    public R<List<PolicyInfoDTO>> queryPolicyInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<PolicyInfo> queryResults = getJpaQueryFactory()
                .select(qPolicyInfo)
                .from(qPolicyInfo)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPolicyInfo.createTime.desc())
                .fetchResults();

        List<PolicyInfoDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询优惠政策实体信息
     */
    @Override
    public PolicyInfoEditDTO queryPolicyInfoById(String id) {
        Predicate predicate = qPolicyInfo.id.eq(id);
        List<PolicyInfoEditDTO> queryList = queryPolicyInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出优惠政策实体数据
     */
    @Override
    public List<PolicyInfoDTO> export(PredicateWrapper predicateWrapper) {
        long count = policyInfoRepository.count();
        R<List<PolicyInfoDTO>> exportResult = this.queryPolicyInfoListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<PolicyInfoEditDTO> queryPolicyInfoEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qPolicyInfo)
                .where(predicate)
                .orderBy(qPolicyInfo.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<PolicyInfo, String> getRepository() {
        return policyInfoRepository;
    }

    @Override
    public BaseMapper<PolicyInfo, PolicyInfoDTO, PolicyInfoEditDTO> getMapper() {
        return policyInfoMapper;
    }

}
