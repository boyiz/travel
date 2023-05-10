//package com.xunye.auth.mapper;
//
//import com.xunye.core.base.BaseMapper;
//import com.xunye.auth.entity.RoleAuth;
//import com.xunye.auth.dto.RoleAuthDTO;
//import com.xunye.auth.dto.RoleAuthEditDTO;
//import com.xunye.core.helper.MapperHelper;
//import org.mapstruct.*;
//
//
///**
// * 角色权限实体Mapper
// *
// * @Author: boyiz
// * @Date: 2023-05-08
// */
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
//public interface RoleAuthMapper extends BaseMapper<RoleAuth, RoleAuthDTO, RoleAuthEditDTO> {
//
//    /* 合并非空属性 => Target */
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void merge(RoleAuthEditDTO editDto,@MappingTarget RoleAuth entity);
//
//}
