package com.xunye.auth.service;

import com.xunye.auth.dto.ContactDTO;
import com.xunye.auth.dto.ContactEditDTO;
import com.xunye.auth.entity.Contact;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;

import java.util.List;

/**
 * 关联出行人Service接口
 *
 * @Author: boyiz
 * @Date: 2023-05-04
 */
public interface IContactService extends BaseService<ContactEditDTO, ContactDTO, Contact,String> {

    /**
     * 创建关联出行人
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createContact(ContactEditDTO createDto, User operatorUser);

    /**
     * 更新关联出行人
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateContact(ContactEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除关联出行人
     *
     * @param id 关联出行人ID
     */
    void deleteContact(String id, User operatorUser);

    /**
     * 批量删除关联出行人
     *
     * @param ids 关联出行人ID集合
     */
    void deleteContactBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询关联出行人
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<ContactDTO>> queryContactListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询关联出行人
     *
     * @param id ID
     * @return 编辑DTO
     */
    ContactEditDTO queryContactById(String id);


    /**
     * 导出关联出行人Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<ContactDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<ContactEditDTO> queryContactEditDTOListByPredicate(Predicate predicate);

}
