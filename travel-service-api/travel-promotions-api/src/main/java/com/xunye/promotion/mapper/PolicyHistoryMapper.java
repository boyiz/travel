package com.xunye.promotion.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.promotion.entity.PolicyHistory;
import com.xunye.promotion.dto.PolicyHistoryDTO;
import com.xunye.promotion.dto.PolicyHistoryEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 优惠政策使用记录Mapper
 *
 * @Author: boyiz
 * @Date: 2023-05-02
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface PolicyHistoryMapper extends BaseMapper<PolicyHistory, PolicyHistoryDTO, PolicyHistoryEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(PolicyHistoryEditDTO editDto,@MappingTarget PolicyHistory entity);

}
