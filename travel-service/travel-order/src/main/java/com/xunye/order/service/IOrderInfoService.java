package com.xunye.order.service;

import com.xunye.order.dto.OrderInfoDTO;
import com.xunye.order.dto.OrderInfoEditDTO;
import com.xunye.order.entity.OrderInfo;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.order.cmd.OrderSubmitCmd;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 订单表Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
public interface IOrderInfoService extends BaseService<OrderInfoEditDTO, OrderInfoDTO, OrderInfo,String> {

    /**
     * 创建订单表
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createOrderInfo(OrderSubmitCmd createDto, User operatorUser) throws Exception;

    /**
     * 更新订单表
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateOrderInfo(OrderInfoEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除订单表
     *
     * @param id 订单表ID
     */
    void deleteOrderInfo(String id, User operatorUser);

    /**
     * 批量删除订单表
     *
     * @param ids 订单表ID集合
     */
    void deleteOrderInfoBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询订单表
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<OrderInfoDTO>> queryOrderInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询订单表
     *
     * @param id ID
     * @return 编辑DTO
     */
    OrderInfoEditDTO queryOrderInfoById(String id);


    /**
     * 导出订单表Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<OrderInfoDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<OrderInfoEditDTO> queryOrderInfoEditDTOListByPredicate(Predicate predicate);

    // 根据Sn查询订单信息
    OrderInfoEditDTO queryOrderInfoByOrderSn(String orderSn);
}
