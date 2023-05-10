package com.xunye.promotion.service;

import com.xunye.promotion.dto.PolicyInfoDTO;
import com.xunye.promotion.dto.PolicyInfoEditDTO;
import com.xunye.promotion.entity.PolicyInfo;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 优惠政策实体Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-22
 */
public interface IPolicyInfoService extends BaseService<PolicyInfoEditDTO, PolicyInfoDTO, PolicyInfo,String> {

    /**
     * 创建优惠政策实体
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createPolicyInfo(PolicyInfoEditDTO createDto, User operatorUser);

    /**
     * 更新优惠政策实体
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updatePolicyInfo(PolicyInfoEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除优惠政策实体
     *
     * @param id 优惠政策实体ID
     */
    void deletePolicyInfo(String id, User operatorUser);

    /**
     * 批量删除优惠政策实体
     *
     * @param ids 优惠政策实体ID集合
     */
    void deletePolicyInfoBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询优惠政策实体
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<PolicyInfoDTO>> queryPolicyInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询优惠政策实体
     *
     * @param id ID
     * @return 编辑DTO
     */
    PolicyInfoEditDTO queryPolicyInfoById(String id);


    /**
     * 导出优惠政策实体Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<PolicyInfoDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<PolicyInfoEditDTO> queryPolicyInfoEditDTOListByPredicate(Predicate predicate);



}
