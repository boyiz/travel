package com.xunye.order.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.order.entity.OrderUserRelation;
import com.xunye.order.dto.OrderUserRelationDTO;
import com.xunye.order.dto.OrderUserRelationEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 用户订单关联用户Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface OrderUserRelationMapper extends BaseMapper<OrderUserRelation, OrderUserRelationDTO, OrderUserRelationEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(OrderUserRelationEditDTO editDto,@MappingTarget OrderUserRelation entity);

}
