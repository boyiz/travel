package com.xunye.promotion.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.promotion.entity.CouponsHistory;
import com.xunye.promotion.dto.CouponsHistoryDTO;
import com.xunye.promotion.dto.CouponsHistoryEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 优惠券历史记录Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-29
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface CouponsHistoryMapper extends BaseMapper<CouponsHistory, CouponsHistoryDTO, CouponsHistoryEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(CouponsHistoryEditDTO editDto,@MappingTarget CouponsHistory entity);

}
