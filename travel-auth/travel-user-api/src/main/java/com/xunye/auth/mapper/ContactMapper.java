package com.xunye.auth.mapper;

import com.xunye.core.base.BaseMapper;
import com.xunye.auth.entity.Contact;
import com.xunye.auth.dto.ContactDTO;
import com.xunye.auth.dto.ContactEditDTO;
import com.xunye.core.helper.MapperHelper;
import org.mapstruct.*;


/**
 * 关联出行人Mapper
 *
 * @Author: boyiz
 * @Date: 2023-05-04
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperHelper.class)
public interface ContactMapper extends BaseMapper<Contact, ContactDTO, ContactEditDTO> {

    /* 合并非空属性 => Target */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(ContactEditDTO editDto,@MappingTarget Contact entity);

}
