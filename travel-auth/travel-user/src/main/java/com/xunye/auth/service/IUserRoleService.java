//package com.xunye.auth.service;
//
//import com.xunye.auth.dto.UserRoleDTO;
//import com.xunye.auth.dto.UserRoleEditDTO;
//import com.xunye.auth.entity.UserRole;
//import com.xunye.core.base.BaseService;
//import com.xunye.core.model.PredicateWrapper;
//import com.xunye.core.result.R;
//import org.springframework.data.domain.Pageable;
//
//import com.xunye.auth.entity.User;
//import com.querydsl.core.types.Predicate;
//
//import java.util.List;
//
///**
// * 用户角色实体Service接口
// *
// * @Author: boyiz
// * @Date: 2023-05-08
// */
//public interface IUserRoleService extends BaseService<UserRoleEditDTO, UserRoleDTO, UserRole,String> {
//
//    /**
//     * 创建用户角色实体
//     *
//     * @param createDto 编辑DTO
//     * @param operatorUser 当前操作用户
//     * @return ID
//     */
//    String createUserRole(UserRoleEditDTO createDto, User operatorUser);
//
//    /**
//     * 更新用户角色实体
//     *
//     * @param updateDto 编辑DTO
//     * @param operatorUser 当前操作用户
//     */
//    void updateUserRole(UserRoleEditDTO updateDto, User operatorUser);
//
//    /**
//     * 根据ID删除用户角色实体
//     *
//     * @param id 用户角色实体ID
//     */
//    void deleteUserRole(String id, User operatorUser);
//
//    /**
//     * 批量删除用户角色实体
//     *
//     * @param ids 用户角色实体ID集合
//     */
//    void deleteUserRoleBatch(List<String> ids, User operatorUser);
//
//    /**
//     * 列表分页查询用户角色实体
//     *
//     * @param predicateWrapper 条件谓语包装类
//     * @param pageable         分页参数
//     * @return 统一结果集
//     */
//    R<List<UserRoleDTO>> queryUserRoleListByPage(PredicateWrapper predicateWrapper, Pageable pageable);
//
//    /**
//     * 根据ID查询用户角色实体
//     *
//     * @param id ID
//     * @return 编辑DTO
//     */
//    UserRoleEditDTO queryUserRoleById(String id);
//
//
//    /**
//     * 导出用户角色实体Excel
//     *
//     * @param predicateWrapper 查询条件包装类
//     * @return 数据集合
//     */
//    List<UserRoleDTO> export(PredicateWrapper predicateWrapper);
//
//    // ======================================== 以上由代码生成器生成 ======================================== //
//
//    /**
//     * 根据predicate查询
//     *
//     * @param predicate 查询条件
//     * @return 数据集合
//     */
//    List<UserRoleEditDTO> queryUserRoleEditDTOListByPredicate(Predicate predicate);
//
//    //List<UserRoleEditDTO> queryUserRoleEditDTOListByUserId(String userId);
//
//}
