package com.xunye.route.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.route.entity.RouteSchedule;
import com.xunye.route.dto.RouteScheduleDTO;
import com.xunye.route.dto.RouteScheduleEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 路线-每天计划Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-15
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface RouteScheduleMapper extends BaseMapper<RouteSchedule, RouteScheduleDTO, RouteScheduleEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(RouteScheduleEditDTO editDto,@MappingTarget RouteSchedule entity);

}
