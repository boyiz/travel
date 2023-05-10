package com.xunye.auth.service;

import java.util.List;

import com.xunye.auth.dto.RoleDTO;
import com.xunye.auth.dto.RoleEditDTO;
import com.xunye.auth.dto.UserRoleEditDTO;
import com.xunye.auth.entity.Role;
import com.xunye.auth.entity.UserRole;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

/**
 * 角色Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
public interface IRoleService extends BaseService<RoleEditDTO, RoleDTO, Role, String> {

    /**
     * 角色创建
     *
     * @param createDto 编辑DTO
     * @return 统一结果集
     */
    R<String> createRole(RoleEditDTO createDto);

    /**
     * 角色更新
     *
     * @param updateDto 编辑DTO
     * @return 统一结果集
     */
    R<String> updateRole(RoleEditDTO updateDto);

    /**
     * 角色批量删除
     *
     * @param id 角色ID
     * @return 统一结果集
     */
    R<Boolean> deleteRole(String id);

    /**
     * 角色批量删除
     *
     * @param ids 角色ID集合
     * @return 统一结果集
     */
    R<Boolean> deleteRoleBatch(List<String> ids);

    /**
     * 角色列表分页查询
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<RoleDTO>> queryRoleListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 角色查询
     *
     * @return 统一结果集
     */
    R<RoleEditDTO> queryRoleById(String id);


    /**
     * 角色数据导出
     *
     * @param predicateWrapper 查询条件包装类
     * @return 统一结果集
     */
    List<RoleDTO> export(PredicateWrapper predicateWrapper);

    // ========================================业务方法在下方声明======================================== //

    /**
     * 更新数据权限
     *
     * @param roleId       角色ID
     * @param dataAuthType 数据权限类型
     * @param dataAuths    数据权限集合
     * @return 统一结果集
     */
    R<Boolean> updateDataAuth(String roleId, Integer dataAuthType, List<String> dataAuths);

    List<UserRole>  queryUserRoleEditDTOListByUserId(String userId);

}
