package com.xunye.auth.service;

import java.util.List;

import com.querydsl.core.types.Predicate;
import com.xunye.auth.entity.User;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;
import com.xunye.auth.dto.UserEditDTO;
import com.xunye.auth.dto.UserDTO;
/**
 * 用户实体Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-11
 */
public interface IUserService extends BaseService<UserEditDTO, UserDTO, User ,String> {

    /**
     * 创建用户实体
     *
     * @param createDto 编辑DTO
     * @param operatorUser 当前操作用户
     * @return ID
     */
    String createUserInfo(UserEditDTO createDto, User operatorUser);

    /**
     * 更新用户实体
     *
     * @param updateDto 编辑DTO
     * @param operatorUser 当前操作用户
     */
    void updateUserInfo(UserEditDTO updateDto, User operatorUser);

    /**
     * 根据ID删除用户实体
     *
     * @param id 用户实体ID
     */
    void deleteUserInfo(String id, User operatorUser);

    /**
     * 批量删除用户实体
     *
     * @param ids 用户实体ID集合
     */
    void deleteUserInfoBatch(List<String> ids, User operatorUser);

    /**
     * 列表分页查询用户实体
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<UserDTO>> queryUserInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 根据ID查询用户实体
     *
     * @param id ID
     * @return 编辑DTO
     */
    UserEditDTO queryUserInfoById(String id);


    /**
     * 导出用户实体Excel
     *
     * @param predicateWrapper 查询条件包装类
     * @return 数据集合
     */
    List<UserDTO> export(PredicateWrapper predicateWrapper);

    // ======================================== 以上由代码生成器生成 ======================================== //

    /**
     * 根据predicate查询
     *
     * @param predicate 查询条件
     * @return 数据集合
     */
    List<UserEditDTO> queryUserInfoEditDTOListByPredicate(Predicate predicate);

    UserEditDTO queryUserInfoByWxOpenid(String openid);

    List<User> queryUserListInDeptIds(List<String> deptIdList);


}
