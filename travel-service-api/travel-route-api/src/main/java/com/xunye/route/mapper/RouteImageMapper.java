package com.xunye.route.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.route.entity.RouteImage;
import com.xunye.route.dto.RouteImageDTO;
import com.xunye.route.dto.RouteImageEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 路线介绍图Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-18
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface RouteImageMapper extends BaseMapper<RouteImage, RouteImageDTO, RouteImageEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(RouteImageEditDTO editDto,@MappingTarget RouteImage entity);

}
