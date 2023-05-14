package com.xunye.auth.service;

import java.util.List;

import com.querydsl.core.types.Predicate;
import com.xunye.auth.dto.CustomerDTO;
import com.xunye.auth.dto.CustomerEditDTO;
import com.xunye.auth.entity.Customer;
import com.xunye.auth.entity.User;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

/**
 * @ClassName ICustomerService
 * @Description Customer用户管理
 * @Author boyiz
 * @Date 2023/5/14 15:15
 * @Version 1.0
 **/
public interface ICustomerService extends BaseService<CustomerEditDTO, CustomerDTO, Customer,String> {

    /**
     * 更新用户实体
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateUserInfo(CustomerEditDTO updateDto, User operatorUser);

    /**
     * 列表分页查询用户实体
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<CustomerDTO>> queryUserInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询用户实体
     *
     * @param id ID
     * @return 编辑DTO
     */
    CustomerEditDTO queryUserInfoById(String id);


    /**
     * 导出用户实体Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<CustomerDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<CustomerEditDTO> queryUserInfoEditDTOListByPredicate(Predicate predicate);

    CustomerEditDTO queryUserInfoByWxOpenid(String openid);
}
