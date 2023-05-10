package com.xunye.notice.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.notice.entity.RouteNotice;
import com.xunye.notice.dto.RouteNoticeDTO;
import com.xunye.notice.dto.RouteNoticeEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 路线注意事项Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-14
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface RouteNoticeMapper extends BaseMapper<RouteNotice, RouteNoticeDTO, RouteNoticeEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(RouteNoticeEditDTO editDto,@MappingTarget RouteNotice entity);

}
