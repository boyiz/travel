package com.xunye.order.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.order.dto.OrderItemDTO;
import com.xunye.order.dto.OrderItemEditDTO;
import com.xunye.order.entity.OrderItem;
import com.xunye.order.entity.QOrderItem;
import com.xunye.order.mapper.OrderItemMapper;
import com.xunye.order.repo.OrderItemRepository;
import com.xunye.order.service.IOrderItemService;
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
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItemEditDTO, OrderItemDTO, OrderItem, String> implements IOrderItemService {

    private static final QOrderItem qOrderItem = QOrderItem.orderItem;

    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemMapper orderItemMapper, OrderItemRepository orderItemRepository) {
        this.orderItemMapper = orderItemMapper;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * 创建订单详情信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrderItem(OrderItemEditDTO orderItemEditDto, User operatorUser) {
        // 转换为Entity实体
        OrderItem orderItemEntity = orderItemMapper.toEntity(orderItemEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            orderItemEntity.setCreateBy(operatorUser.getId());
            orderItemEntity.setCreateByName(operatorUser.getUserName());
            orderItemEntity.setUpdateBy(operatorUser.getId());
            orderItemEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("订单详情创建失败：创建人信息获取有误");
        }

        // 创建订单详情
        orderItemRepository.saveAndFlush(orderItemEntity);
        return orderItemEntity.getId();
    }

    /**
     * 更新订单详情信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderItem(OrderItemEditDTO orderItemEditDto, User operatorUser) {
        Optional<OrderItem> optional = orderItemRepository.findById(orderItemEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("订单详情不存在");
        }

        // 将EditDto属性合并到DB实体中
        OrderItem orderItemDB = optional.get();
        orderItemMapper.merge(orderItemEditDto, orderItemDB);
        // 设置更新信息
        orderItemDB.setUpdateBy(operatorUser.getId());
        orderItemDB.setUpdateByName(operatorUser.getUserName());
        // 更新订单详情
        orderItemRepository.saveAndFlush(orderItemDB);
    }

    /**
     * 删除订单详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderItem(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除订单详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderItemBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteOrderItem(id,operatorUser);
        }
    }

    /**
     * 分页查询订单详情列表
     */
    @Override
    public R<List<OrderItemDTO>> queryOrderItemListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<OrderItem> queryResults = getJpaQueryFactory()
                .select(qOrderItem)
                .from(qOrderItem)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOrderItem.createTime.desc())
                .fetchResults();

        List<OrderItemDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询订单详情信息
     */
    @Override
    public OrderItemEditDTO queryOrderItemById(String id) {
        Predicate predicate = qOrderItem.id.eq(id);
        List<OrderItemEditDTO> queryList = queryOrderItemEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出订单详情数据
     */
    @Override
    public List<OrderItemDTO> export(PredicateWrapper predicateWrapper) {
        long count = orderItemRepository.count();
        R<List<OrderItemDTO>> exportResult = this.queryOrderItemListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<OrderItemEditDTO> queryOrderItemEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qOrderItem)
                .where(predicate)
                .orderBy(qOrderItem.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<OrderItem, String> getRepository() {
        return orderItemRepository;
    }

    @Override
    public BaseMapper<OrderItem, OrderItemDTO, OrderItemEditDTO> getMapper() {
        return orderItemMapper;
    }

}
