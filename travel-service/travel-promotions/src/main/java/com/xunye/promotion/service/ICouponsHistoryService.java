package com.xunye.promotion.service;

import com.xunye.promotion.dto.CouponsHistoryDTO;
import com.xunye.promotion.dto.CouponsHistoryEditDTO;
import com.xunye.promotion.entity.CouponsHistory;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 优惠券历史记录Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-29
 */
public interface ICouponsHistoryService extends BaseService<CouponsHistoryEditDTO, CouponsHistoryDTO, CouponsHistory,String> {

    /**
     * 创建优惠券历史记录
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createCouponsHistory(CouponsHistoryEditDTO createDto, User operatorUser);

    /**
     * 更新优惠券历史记录
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateCouponsHistory(CouponsHistoryEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除优惠券历史记录
     *
     * @param id 优惠券历史记录ID
     */
    void deleteCouponsHistory(String id, User operatorUser);

    /**
     * 批量删除优惠券历史记录
     *
     * @param ids 优惠券历史记录ID集合
     */
    void deleteCouponsHistoryBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询优惠券历史记录
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<CouponsHistoryDTO>> queryCouponsHistoryListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询优惠券历史记录
     *
     * @param id ID
     * @return 编辑DTO
     */
    CouponsHistoryEditDTO queryCouponsHistoryById(String id);


    /**
     * 导出优惠券历史记录Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<CouponsHistoryDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<CouponsHistoryEditDTO> queryCouponsHistoryEditDTOListByPredicate(Predicate predicate);

}
