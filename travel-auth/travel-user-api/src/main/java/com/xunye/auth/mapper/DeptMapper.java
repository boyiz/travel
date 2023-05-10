package com.xunye.auth.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.core.helper.MapperHelper;
import com.xunye.auth.dto.DeptDTO;
import com.xunye.auth.dto.DeptEditDTO;
import com.xunye.auth.entity.Dept;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


/**
 * 部门Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface DeptMapper extends BaseMapper<Dept, DeptDTO, DeptEditDTO> {

    /**
     * 合并editDto非空属性到entity
     * @param editDto
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(DeptEditDTO editDto,@MappingTarget Dept entity);

}
