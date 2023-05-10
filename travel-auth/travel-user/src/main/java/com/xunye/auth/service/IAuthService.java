package com.xunye.auth.service;

import java.util.List;


import com.querydsl.core.types.Predicate;
import com.xunye.auth.dto.AuthDTO;
import com.xunye.auth.dto.AuthEditDTO;
import com.xunye.auth.dto.RoleAuthEditDTO;
import com.xunye.auth.entity.Auth;
import com.xunye.auth.entity.RoleAuth;
import com.xunye.auth.vo.AuthVo;
import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import org.springframework.data.domain.Pageable;

/**
 * 权限Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
public interface IAuthService extends BaseService<AuthEditDTO, AuthDTO, Auth, String> {

    /**
     * 权限创建
     *
     * @param createDto 编辑DTO
     * @return 统一结果集
     */
    R<String> createAuth(AuthEditDTO createDto);

    /**
     * 权限更新
     *
     * @param updateDto 编辑DTO
     * @return 统一结果集
     */
    R<String> updateAuth(AuthEditDTO updateDto);

    /**
     * 权限批量删除
     *
     * @param id 权限ID
     * @return 统一结果集
     */
    R<Boolean> deleteAuth(String id);

    /**
     * 权限批量删除
     *
     * @param ids 权限ID集合
     * @return 统一结果集
     */
    R<Boolean> deleteAuthBatch(List<String> ids);

    /**
     * 权限列表分页查询
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<AuthDTO>> queryAuthListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 权限查询
     *
     * @return 统一结果集
     */
    R<AuthEditDTO> queryAuthById(String id);


    /**
     * 权限数据导出
     *
     * @param predicateWrapper 查询条件包装类
     * @return 统一结果集
     */
    List<AuthDTO> export(PredicateWrapper predicateWrapper);

    // ========================================业务方法在下方声明======================================== //

    /**
     * 查询权限列表树
     *
     * @param predicate 查询谓语
     * @return 统一结果集
     */
    List<AuthDTO> queryAuthTreeList(Predicate predicate);

    /**
     * 根据用户ID查询拥有的权限集合
     *
     * @param userId 用户ID
     * @return 权限集
     */
    List<AuthVo> queryAuthVoListByUserId(String userId);


    List<RoleAuth> queryRoleAuthEditDTOListByRoleId(String roleId);

}
