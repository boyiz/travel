package com.xunye.order.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.order.entity.OrderInfo;
import com.xunye.order.dto.OrderInfoDTO;
import com.xunye.order.dto.OrderInfoEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 订单表Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface OrderInfoMapper extends BaseMapper<OrderInfo, OrderInfoDTO, OrderInfoEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(OrderInfoEditDTO editDto,@MappingTarget OrderInfo entity);

}
