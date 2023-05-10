//package com.xunye.auth.service;
//
//import com.xunye.auth.dto.RoleAuthDTO;
//import com.xunye.auth.dto.RoleAuthEditDTO;
//import com.xunye.auth.entity.RoleAuth;
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
// * 角色权限实体Service接口
// *
// * @Author: boyiz
// * @Date: 2023-05-08
// */
//public interface IRoleAuthService extends BaseService<RoleAuthEditDTO, RoleAuthDTO, RoleAuth,String> {
//
//    /**
//     * 创建角色权限实体
//     *
//     * @param createDto 编辑DTO
//     * @param operatorUser 当前操作用户
//     * @return ID
//     */
//    String createRoleAuth(RoleAuthEditDTO createDto, User operatorUser);
//
//    /**
//     * 更新角色权限实体
//     *
//     * @param updateDto 编辑DTO
//     * @param operatorUser 当前操作用户
//     */
//    void updateRoleAuth(RoleAuthEditDTO updateDto, User operatorUser);
//
//    /**
//     * 根据ID删除角色权限实体
//     *
//     * @param id 角色权限实体ID
//     */
//    void deleteRoleAuth(String id, User operatorUser);
//
//    /**
//     * 批量删除角色权限实体
//     *
//     * @param ids 角色权限实体ID集合
//     */
//    void deleteRoleAuthBatch(List<String> ids, User operatorUser);
//
//    /**
//     * 列表分页查询角色权限实体
//     *
//     * @param predicateWrapper 条件谓语包装类
//     * @param pageable         分页参数
//     * @return 统一结果集
//     */
//    R<List<RoleAuthDTO>> queryRoleAuthListByPage(PredicateWrapper predicateWrapper, Pageable pageable);
//
//    /**
//     * 根据ID查询角色权限实体
//     *
//     * @param id ID
//     * @return 编辑DTO
//     */
//    RoleAuthEditDTO queryRoleAuthById(String id);
//
//
//    /**
//     * 导出角色权限实体Excel
//     *
//     * @param predicateWrapper 查询条件包装类
//     * @return 数据集合
//     */
//    List<RoleAuthDTO> export(PredicateWrapper predicateWrapper);
//
//    // ======================================== 以上由代码生成器生成 ======================================== //
//
//    /**
//     * 根据predicate查询
//     *
//     * @param predicate 查询条件
//     * @return 数据集合
//     */
//    List<RoleAuthEditDTO> queryRoleAuthEditDTOListByPredicate(Predicate predicate);
//
//    //List<RoleAuthEditDTO> queryRoleAuthEditDTOListByRoleId(String roleId);
//
//}
