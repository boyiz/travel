package com.xunye.promotion.service;

import com.xunye.promotion.dto.CouponsProductRelationDTO;
import com.xunye.promotion.dto.CouponsProductRelationEditDTO;
import com.xunye.promotion.entity.CouponsProductRelation;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 优惠券-商品 关系表Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-29
 */
public interface ICouponsProductRelationService extends BaseService<CouponsProductRelationEditDTO, CouponsProductRelationDTO, CouponsProductRelation,String> {

    /**
     * 创建优惠券-商品 关系表
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createCouponsProductRelation(CouponsProductRelationEditDTO createDto, User operatorUser);

    /**
     * 更新优惠券-商品 关系表
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateCouponsProductRelation(CouponsProductRelationEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除优惠券-商品 关系表
     *
     * @param id 优惠券-商品 关系表ID
     */
    void deleteCouponsProductRelation(String id, User operatorUser);

    /**
     * 批量删除优惠券-商品 关系表
     *
     * @param ids 优惠券-商品 关系表ID集合
     */
    void deleteCouponsProductRelationBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询优惠券-商品 关系表
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<CouponsProductRelationDTO>> queryCouponsProductRelationListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询优惠券-商品 关系表
     *
     * @param id ID
     * @return 编辑DTO
     */
    CouponsProductRelationEditDTO queryCouponsProductRelationById(String id);


    /**
     * 导出优惠券-商品 关系表Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<CouponsProductRelationDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<CouponsProductRelationEditDTO> queryCouponsProductRelationEditDTOListByPredicate(Predicate predicate);

}
