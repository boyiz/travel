package com.xunye.auth.mapper;

import com.xunye.auth.dto.CustomerDTO;
import com.xunye.auth.dto.CustomerEditDTO;
import com.xunye.auth.entity.Customer;
import com.xunye.auth.entity.User;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * 用户实体Mapper
 *
 * @Author: boyiz
 * @Date: 2023-04-11
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface CustomerMapper extends BaseMapper<Customer, CustomerDTO, CustomerEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(CustomerEditDTO editDto,@MappingTarget Customer entity);

}
