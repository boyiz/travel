package com.xunye.auth.mapper;

import com.xunye.auth.entity.User;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 用户实体Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-11
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface UserMapper extends BaseMapper<User, com.xunye.auth.dto.UserDTO, com.xunye.auth.dto.UserEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(com.xunye.auth.dto.UserEditDTO editDto,@MappingTarget User entity);

}
