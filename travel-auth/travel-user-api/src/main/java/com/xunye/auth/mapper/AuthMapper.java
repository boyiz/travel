package com.xunye.auth.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.core.helper.MapperHelper;
import com.xunye.core.tools.EnumTools;
import com.xunye.auth.dto.AuthDTO;
import com.xunye.auth.dto.AuthEditDTO;
import com.xunye.auth.vo.AuthVo;
import com.xunye.auth.em.AuthTypeEnum;
import com.xunye.auth.entity.Auth;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


/**
 * 权限Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface AuthMapper extends BaseMapper<Auth, AuthDTO, AuthEditDTO> {

    /**
     * 合并editDto非空属性到entity
     *
     * @param editDto
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(AuthEditDTO editDto, @MappingTarget Auth entity);

    @Mappings({
            @Mapping(target = "authTypeStr", source = "authType", qualifiedByName = "convertAuthType"),
    })
    @Override
    AuthDTO toBasicDTO(Auth entity);

    /* 转换权限类型 */
    @Named("convertAuthType")
    default String convertAuthType(Integer type) {
        return EnumTools.getNameByValue(AuthTypeEnum.values(), type);
    }

    /**
     * 从Auth => AuthVo
     *
     * @param auth 权限实体
     * @return authVo
     */
    AuthVo toAuthVo(Auth auth);

}
