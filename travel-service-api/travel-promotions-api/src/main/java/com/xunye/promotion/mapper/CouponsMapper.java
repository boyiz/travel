package com.xunye.promotion.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.promotion.entity.Coupons;
import com.xunye.promotion.dto.CouponsDTO;
import com.xunye.promotion.dto.CouponsEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 优惠券Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface CouponsMapper extends BaseMapper<Coupons, CouponsDTO, CouponsEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(CouponsEditDTO editDto,@MappingTarget Coupons entity);

}
