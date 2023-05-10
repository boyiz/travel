package com.xunye.promotion.service;

import com.xunye.promotion.dto.CouponsDTO;
import com.xunye.promotion.dto.CouponsEditDTO;
import com.xunye.promotion.entity.Coupons;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 优惠券Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-29
 */
public interface ICouponsService extends BaseService<CouponsEditDTO, CouponsDTO, Coupons,String> {

    /**
     * 创建优惠券
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createCoupons(CouponsEditDTO createDto, User operatorUser);

    /**
     * 更新优惠券
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateCoupons(CouponsEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除优惠券
     *
     * @param id 优惠券ID
     */
    void deleteCoupons(String id, User operatorUser);

    /**
     * 批量删除优惠券
     *
     * @param ids 优惠券ID集合
     */
    void deleteCouponsBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询优惠券
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<CouponsDTO>> queryCouponsListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询优惠券
     *
     * @param id ID
     * @return 编辑DTO
     */
    CouponsEditDTO queryCouponsById(String id);


    /**
     * 导出优惠券Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<CouponsDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<CouponsEditDTO> queryCouponsEditDTOListByPredicate(Predicate predicate);

}
