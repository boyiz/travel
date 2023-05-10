package com.xunye.order.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.order.entity.OrderItem;
import com.xunye.order.dto.OrderItemDTO;
import com.xunye.order.dto.OrderItemEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 订单详情Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface OrderItemMapper extends BaseMapper<OrderItem, OrderItemDTO, OrderItemEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(OrderItemEditDTO editDto,@MappingTarget OrderItem entity);

}
