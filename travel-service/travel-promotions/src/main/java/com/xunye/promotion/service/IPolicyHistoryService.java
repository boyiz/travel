package com.xunye.promotion.service;

import com.xunye.promotion.dto.PolicyHistoryDTO;
import com.xunye.promotion.dto.PolicyHistoryEditDTO;
import com.xunye.promotion.entity.PolicyHistory;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 优惠政策使用记录Service接口
 *
 * @Author: boyiz
 * @Date: 2023-05-02
 */
public interface IPolicyHistoryService extends BaseService<PolicyHistoryEditDTO, PolicyHistoryDTO, PolicyHistory,String> {

    /**
     * 创建优惠政策使用记录
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createPolicyHistory(PolicyHistoryEditDTO createDto, User operatorUser);

    /**
     * 更新优惠政策使用记录
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updatePolicyHistory(PolicyHistoryEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除优惠政策使用记录
     *
     * @param id 优惠政策使用记录ID
     */
    void deletePolicyHistory(String id, User operatorUser);

    /**
     * 批量删除优惠政策使用记录
     *
     * @param ids 优惠政策使用记录ID集合
     */
    void deletePolicyHistoryBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询优惠政策使用记录
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<PolicyHistoryDTO>> queryPolicyHistoryListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询优惠政策使用记录
     *
     * @param id ID
     * @return 编辑DTO
     */
    PolicyHistoryEditDTO queryPolicyHistoryById(String id);


    /**
     * 导出优惠政策使用记录Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<PolicyHistoryDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<PolicyHistoryEditDTO> queryPolicyHistoryEditDTOListByPredicate(Predicate predicate);

}
