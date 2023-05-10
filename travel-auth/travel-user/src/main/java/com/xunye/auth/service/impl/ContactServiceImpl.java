package com.xunye.auth.service.impl;

import com.xunye.core.result.R;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.auth.dto.ContactDTO;
import com.xunye.auth.dto.ContactEditDTO;
import com.xunye.auth.entity.Contact;
import com.xunye.auth.entity.QContact;
import com.xunye.auth.mapper.ContactMapper;
import com.xunye.auth.repo.ContactRepository;
import com.xunye.auth.service.IContactService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.core.types.Predicate;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.entity.User;
import com.xunye.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@Service
public class ContactServiceImpl extends BaseServiceImpl<ContactEditDTO, ContactDTO, Contact, String> implements IContactService {

    private static final QContact qContact = QContact.contact;

    private final ContactMapper contactMapper;
    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactMapper contactMapper, ContactRepository contactRepository) {
        this.contactMapper = contactMapper;
        this.contactRepository = contactRepository;
    }

    /**
     * 创建关联出行人信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createContact(ContactEditDTO contactEditDto, User operatorUser) {
        // 转换为Entity实体
        Contact contactEntity = contactMapper.toEntity(contactEditDto);

        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            contactEntity.setCreateBy(operatorUser.getId());
            contactEntity.setCreateByName(operatorUser.getUserName());
            contactEntity.setUpdateBy(operatorUser.getId());
            contactEntity.setUpdateByName(operatorUser.getUserName());
        } else {
            throw new BusinessException("关联出行人创建失败：创建人信息获取有误");
        }

        // 创建关联出行人
        contactRepository.saveAndFlush(contactEntity);
        return contactEntity.getId();
    }

    /**
     * 更新关联出行人信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContact(ContactEditDTO contactEditDto, User operatorUser) {
        Optional<Contact> optional = contactRepository.findById(contactEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("关联出行人不存在");
        }

        // 将EditDto属性合并到DB实体中
        Contact contactDB = optional.get();
        contactMapper.merge(contactEditDto, contactDB);
        // 设置更新信息
        contactDB.setUpdateBy(operatorUser.getId());
        contactDB.setUpdateByName(operatorUser.getUserName());
        // 更新关联出行人
        contactRepository.saveAndFlush(contactDB);
    }

    /**
     * 删除关联出行人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContact(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除关联出行人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContactBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteContact(id,operatorUser);
        }
    }

    /**
     * 分页查询关联出行人列表
     */
    //@DataAuth
    @Override
    public R<List<ContactDTO>> queryContactListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<Contact> queryResults = getJpaQueryFactory()
                .select(qContact)
                .from(qContact)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qContact.createTime.desc())
                .fetchResults();

        List<ContactDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询关联出行人信息
     */
    @Override
    public ContactEditDTO queryContactById(String id) {
        Predicate predicate = qContact.id.eq(id);
        List<ContactEditDTO> queryList = queryContactEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出关联出行人数据
     */
    @Override
    public List<ContactDTO> export(PredicateWrapper predicateWrapper) {
        long count = contactRepository.count();
        R<List<ContactDTO>> exportResult = this.queryContactListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<ContactEditDTO> queryContactEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qContact)
                .where(predicate)
                .orderBy(qContact.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public BaseRepository<Contact, String> getRepository() {
        return contactRepository;
    }

    @Override
    public BaseMapper<Contact, ContactDTO, ContactEditDTO> getMapper() {
        return contactMapper;
    }

}
