package com.xunye.promotion.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.promotion.entity.PolicyInfo;
import com.xunye.promotion.dto.PolicyInfoDTO;
import com.xunye.promotion.dto.PolicyInfoEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 优惠政策实体Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-22
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface PolicyInfoMapper extends BaseMapper<PolicyInfo, PolicyInfoDTO, PolicyInfoEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(PolicyInfoEditDTO editDto,@MappingTarget PolicyInfo entity);

}
