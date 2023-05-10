package com.xunye.order.service;

import com.xunye.order.dto.OrderUserRelationDTO;
import com.xunye.order.dto.OrderUserRelationEditDTO;
import com.xunye.order.entity.OrderUserRelation;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 用户订单关联用户Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
public interface IOrderUserRelationService extends BaseService<OrderUserRelationEditDTO, OrderUserRelationDTO, OrderUserRelation,String> {

    /**
     * 创建用户订单关联用户
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createOrderUserRelation(OrderUserRelationEditDTO createDto, User operatorUser);

    /**
     * 更新用户订单关联用户
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateOrderUserRelation(OrderUserRelationEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除用户订单关联用户
     *
     * @param id 用户订单关联用户ID
     */
    void deleteOrderUserRelation(String id, User operatorUser);

    /**
     * 批量删除用户订单关联用户
     *
     * @param ids 用户订单关联用户ID集合
     */
    void deleteOrderUserRelationBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询用户订单关联用户
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<OrderUserRelationDTO>> queryOrderUserRelationListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询用户订单关联用户
     *
     * @param id ID
     * @return 编辑DTO
     */
    OrderUserRelationEditDTO queryOrderUserRelationById(String id);


    /**
     * 导出用户订单关联用户Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<OrderUserRelationDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<OrderUserRelationEditDTO> queryOrderUserRelationEditDTOListByPredicate(Predicate predicate);

}
