package com.xunye.order.service;

import com.xunye.order.dto.OrderItemDTO;
import com.xunye.order.dto.OrderItemEditDTO;
import com.xunye.order.entity.OrderItem;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 订单详情Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
public interface IOrderItemService extends BaseService<OrderItemEditDTO, OrderItemDTO, OrderItem,String> {

    /**
     * 创建订单详情
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createOrderItem(OrderItemEditDTO createDto, User operatorUser);

    /**
     * 更新订单详情
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateOrderItem(OrderItemEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除订单详情
     *
     * @param id 订单详情ID
     */
    void deleteOrderItem(String id, User operatorUser);

    /**
     * 批量删除订单详情
     *
     * @param ids 订单详情ID集合
     */
    void deleteOrderItemBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询订单详情
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<OrderItemDTO>> queryOrderItemListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询订单详情
     *
     * @param id ID
     * @return 编辑DTO
     */
    OrderItemEditDTO queryOrderItemById(String id);


    /**
     * 导出订单详情Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<OrderItemDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<OrderItemEditDTO> queryOrderItemEditDTOListByPredicate(Predicate predicate);

}
