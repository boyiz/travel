package com.xunye.auth.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.dto.CustomerDTO;
import com.xunye.auth.dto.CustomerEditDTO;
import com.xunye.auth.entity.Customer;
import com.xunye.auth.entity.QCustomer;
import com.xunye.auth.entity.User;
import com.xunye.auth.mapper.CustomerMapper;
import com.xunye.auth.repo.CustomerRepository;
import com.xunye.auth.service.ICustomerService;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @ClassName CustomerServiceImpl
 * @Description Customer用户管理
 * @Author boyiz
 * @Date 2023/5/14 15:15
 * @Version 1.0
 **/
@Slf4j
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerEditDTO, CustomerDTO, Customer,String> implements
    ICustomerService {

    private static final QCustomer qCustomer = QCustomer.customer;

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public void updateUserInfo(CustomerEditDTO userEditDto, User operatorUser) {
        Optional<Customer> optional = customerRepository.findById(userEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("用户实体不存在");
        }

        // 将EditDto属性合并到DB实体中
        Customer userDB = optional.get();
        customerMapper.merge(userEditDto, userDB);
        // 设置更新信息
        userDB.setUpdateBy(operatorUser.getId());
        // 更新用户实体
        customerRepository.saveAndFlush(userDB);
    }

    @Override
    public R<List<CustomerDTO>> queryUserInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();
        QueryResults<Customer> queryResults = getJpaQueryFactory()
            .select(qCustomer)
            .from(qCustomer)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(qCustomer.createTime.desc())
            .fetchResults();

        List<CustomerDTO> dtoList = queryResults.getResults()
            .stream().map(entity -> getMapper().toBasicDTO(entity))
            .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    @Override
    public CustomerEditDTO queryUserInfoById(String id) {
        Predicate predicate = qCustomer.id.eq(id);
        List<CustomerEditDTO> queryList = queryUserInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    @Override
    public List<CustomerDTO> export(PredicateWrapper predicateWrapper) {
        long count = customerRepository.count();
        R<List<CustomerDTO>> exportResult = this.queryUserInfoListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<CustomerEditDTO> queryUserInfoEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
            .selectFrom(qCustomer)
            .where(predicate)
            .orderBy(qCustomer.createTime.asc())
            .fetch()
            .stream()
            .map(entity -> getMapper().toEditDTO(entity))
            .collect(Collectors.toList());
    }

    @Override
    public CustomerEditDTO queryUserInfoByWxOpenid(String openid) {
        Predicate predicate = qCustomer.userOpenid.eq(openid);
        List<CustomerEditDTO> queryList = queryUserInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    @Override
    public BaseRepository<Customer, String> getRepository() {
        return customerRepository;
    }

    @Override
    public BaseMapper<Customer, CustomerDTO, CustomerEditDTO> getMapper() {
        return customerMapper;
    }
}