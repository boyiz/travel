package com.xunye.group.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.core.helper.MapperHelper;
import com.xunye.group.dto.GroupInfoDTO;
import com.xunye.group.dto.GroupInfoEditDTO;
import com.xunye.group.entity.GroupInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


/**
 * groupMapper
 *
 * @Author: boyiz
 * @Date: 2023-04-13
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface GroupInfoMapper extends BaseMapper<GroupInfo, GroupInfoDTO, GroupInfoEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(GroupInfoEditDTO editDto,@MappingTarget GroupInfo entity);

}
