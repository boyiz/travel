package com.xunye.promotion.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.promotion.entity.CouponsProductRelation;
import com.xunye.promotion.dto.CouponsProductRelationDTO;
import com.xunye.promotion.dto.CouponsProductRelationEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 优惠券-商品 关系表Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-29
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface CouponsProductRelationMapper extends BaseMapper<CouponsProductRelation, CouponsProductRelationDTO, CouponsProductRelationEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(CouponsProductRelationEditDTO editDto,@MappingTarget CouponsProductRelation entity);

}
