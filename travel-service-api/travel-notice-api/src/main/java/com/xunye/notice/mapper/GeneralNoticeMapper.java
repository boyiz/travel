package com.xunye.notice.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.notice.entity.GeneralNotice;
import com.xunye.notice.dto.GeneralNoticeDTO;
import com.xunye.notice.dto.GeneralNoticeEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 通用注意事项Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-13
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface GeneralNoticeMapper extends BaseMapper<GeneralNotice, GeneralNoticeDTO, GeneralNoticeEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(GeneralNoticeEditDTO editDto,@MappingTarget GeneralNotice entity);

}
