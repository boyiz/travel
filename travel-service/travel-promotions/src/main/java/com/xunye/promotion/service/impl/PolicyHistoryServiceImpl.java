package com.xunye.promotion.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.promotion.dto.PolicyHistoryDTO;
import com.xunye.promotion.dto.PolicyHistoryEditDTO;
import com.xunye.promotion.entity.PolicyHistory;
import com.xunye.promotion.entity.QPolicyHistory;
import com.xunye.promotion.mapper.PolicyHistoryMapper;
import com.xunye.promotion.repo.PolicyHistoryRepository;
import com.xunye.promotion.service.IPolicyHistoryService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.core.types.Predicate;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.entity.User;
import com.xunye.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@Service
public class PolicyHistoryServiceImpl extends BaseServiceImpl<PolicyHistoryEditDTO, PolicyHistoryDTO, PolicyHistory, String> implements IPolicyHistoryService {

    private static final QPolicyHistory qPolicyHistory = QPolicyHistory.policyHistory;

    private final PolicyHistoryMapper policyHistoryMapper;
    private final PolicyHistoryRepository policyHistoryRepository;

    public PolicyHistoryServiceImpl(PolicyHistoryMapper policyHistoryMapper, PolicyHistoryRepository policyHistoryRepository) {
        this.policyHistoryMapper = policyHistoryMapper;
        this.policyHistoryRepository = policyHistoryRepository;
    }

    /**
     * 创建优惠政策使用记录信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPolicyHistory(PolicyHistoryEditDTO policyHistoryEditDto, User operatorUser) {
        // 转换为Entity实体
        PolicyHistory policyHistoryEntity = policyHistoryMapper.toEntity(policyHistoryEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            policyHistoryEntity.setCreateBy(operatorUser.getId());
            policyHistoryEntity.setCreateByName(operatorUser.getUserName());
            policyHistoryEntity.setUpdateBy(operatorUser.getId());
            policyHistoryEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("优惠政策使用记录创建失败：创建人信息获取有误");
        }

        // 创建优惠政策使用记录
        policyHistoryRepository.saveAndFlush(policyHistoryEntity);
        return policyHistoryEntity.getId();
    }

    /**
     * 更新优惠政策使用记录信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePolicyHistory(PolicyHistoryEditDTO policyHistoryEditDto, User operatorUser) {
        Optional<PolicyHistory> optional = policyHistoryRepository.findById(policyHistoryEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("优惠政策使用记录不存在");
        }

        // 将EditDto属性合并到DB实体中
        PolicyHistory policyHistoryDB = optional.get();
        policyHistoryMapper.merge(policyHistoryEditDto, policyHistoryDB);
        // 设置更新信息
        policyHistoryDB.setUpdateBy(operatorUser.getId());
        policyHistoryDB.setUpdateByName(operatorUser.getUserName());
        // 更新优惠政策使用记录
        policyHistoryRepository.saveAndFlush(policyHistoryDB);
    }

    /**
     * 删除优惠政策使用记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePolicyHistory(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除优惠政策使用记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePolicyHistoryBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deletePolicyHistory(id,operatorUser);
        }
    }

    /**
     * 分页查询优惠政策使用记录列表
     */
    //@DataAuth
    @Override
    public R<List<PolicyHistoryDTO>> queryPolicyHistoryListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<PolicyHistory> queryResults = getJpaQueryFactory()
                .select(qPolicyHistory)
                .from(qPolicyHistory)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPolicyHistory.createTime.desc())
                .fetchResults();

        List<PolicyHistoryDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询优惠政策使用记录信息
     */
    @Override
    public PolicyHistoryEditDTO queryPolicyHistoryById(String id) {
        Predicate predicate = qPolicyHistory.id.eq(id);
        List<PolicyHistoryEditDTO> queryList = queryPolicyHistoryEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出优惠政策使用记录数据
     */
    @Override
    public List<PolicyHistoryDTO> export(PredicateWrapper predicateWrapper) {
        long count = policyHistoryRepository.count();
        R<List<PolicyHistoryDTO>> exportResult = this.queryPolicyHistoryListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<PolicyHistoryEditDTO> queryPolicyHistoryEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qPolicyHistory)
                .where(predicate)
                .orderBy(qPolicyHistory.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<PolicyHistory, String> getRepository() {
        return policyHistoryRepository;
    }

    @Override
    public BaseMapper<PolicyHistory, PolicyHistoryDTO, PolicyHistoryEditDTO> getMapper() {
        return policyHistoryMapper;
    }

}
