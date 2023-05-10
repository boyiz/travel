package com.xunye.auth.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.core.helper.MapperHelper;
import com.xunye.auth.dto.RoleDTO;
import com.xunye.auth.dto.RoleEditDTO;
import com.xunye.auth.entity.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


/**
 * 角色Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface RoleMapper extends BaseMapper<Role, RoleDTO, RoleEditDTO> {

    /**
     * 合并editDto非空属性到entity
     *
     * @param editDto
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(RoleEditDTO editDto, @MappingTarget Role entity);

}
