package com.xunye.route.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.route.entity.RouteInfo;
import com.xunye.route.dto.RouteInfoDTO;
import com.xunye.route.dto.RouteInfoEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 路线Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-12
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface RouteInfoMapper extends BaseMapper<RouteInfo, RouteInfoDTO, RouteInfoEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(RouteInfoEditDTO editDto,@MappingTarget RouteInfo entity);

}
