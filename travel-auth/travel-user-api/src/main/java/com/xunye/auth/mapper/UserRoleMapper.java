//package com.xunye.auth.mapper;
//
//import com.xunye.core.base.BaseMapper;
//import com.xunye.auth.entity.UserRole;
//import com.xunye.auth.dto.UserRoleDTO;
//import com.xunye.auth.dto.UserRoleEditDTO;
//import com.xunye.core.helper.MapperHelper;
//import org.mapstruct.*;
//
//
///**
// * 用户角色实体Mapper
// *
// * @Author: boyiz
// * @Date: 2023-05-08
// */
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
//public interface UserRoleMapper extends BaseMapper<UserRole, UserRoleDTO, UserRoleEditDTO> {
//
//    /* 合并非空属性 => Target */
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void merge(UserRoleEditDTO editDto,@MappingTarget UserRole entity);
//
//}
