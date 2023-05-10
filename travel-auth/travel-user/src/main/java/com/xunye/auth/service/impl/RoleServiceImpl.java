package com.xunye.auth.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.xunye.auth.dto.UserRoleEditDTO;
import com.xunye.auth.entity.QUserRole;
import com.xunye.auth.entity.UserRole;
import com.xunye.auth.repo.RoleRepository;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.helper.MapperHelper;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.dto.RoleDTO;
import com.xunye.auth.dto.RoleEditDTO;
import com.xunye.auth.entity.QRole;
import com.xunye.auth.entity.QRoleAuth;
import com.xunye.auth.entity.Role;
import com.xunye.auth.entity.RoleAuth;
import com.xunye.auth.mapper.RoleMapper;
import com.xunye.auth.service.IRoleService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 角色Service实现
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Slf4j
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleEditDTO, RoleDTO, Role, String> implements IRoleService {

    private static final QRole Q_ROLE = QRole.role;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MapperHelper mapperHelper;

    /**
     * 前置校验
     *
     * @param roleEditDTO
     * @return
     */
    public String preCheck(RoleEditDTO roleEditDTO) {
        /* 检查角色名称 */
        if (CheckTools.isNullOrEmpty(roleEditDTO.getRoleName())) {
            return "角色名称为必填项";
        }

        /* 检查角色标识 */
        if (CheckTools.isNullOrEmpty(roleEditDTO.getRoleKey())) {
            return "角色标识为必填项";
        }

        /* 检测角色名称、标识是否已存在 */
        Predicate checkPredicate = ExpressionUtils.or(
                Q_ROLE.roleName.eq(roleEditDTO.getRoleName()),
                Q_ROLE.roleKey.eq(roleEditDTO.getRoleKey())
        );
        if (CheckTools.isNotNullOrEmpty(roleEditDTO.getId())) {
            checkPredicate = ExpressionUtils.and(checkPredicate, Q_ROLE.id.ne(roleEditDTO.getId()));
        }
        long count = roleRepository.count(checkPredicate);
        if (count > 0) {
            return "角色名称/角色标识已存在";
        }

        return null;
    }

    /**
     * 角色创建
     *
     * @param createDto 编辑DTO
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<String> createRole(RoleEditDTO createDto) {
        /* 前置校验 */
        String checkResult = preCheck(createDto);
        if (CheckTools.isNotNullOrEmpty(checkResult)) {
            return R.failure(checkResult);
        }

        /* 保存角色信息 */
        Role entity = saveOrUpdate(createDto);

        /* 保存角色权限关联 */
        if (CheckTools.isNotNullOrEmpty(createDto.getMenuAuths())) {
            batchSaveRoleAuth(entity.getId(), createDto.getMenuAuths());
        }
        return R.success(entity.getId());
    }

    /**
     * 角色更新
     *
     * @param updateDto 编辑DTO
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<String> updateRole(RoleEditDTO updateDto) {
        if (CheckTools.isNullOrEmpty(updateDto.getId())) {
            return R.failure("角色id不存在");
        }
        /* 前置校验 */
        String checkResult = preCheck(updateDto);
        if (CheckTools.isNotNullOrEmpty(checkResult)) {
            return R.failure(checkResult);
        }

        Optional<Role> optional = roleRepository.findById(updateDto.getId());
        if (!optional.isPresent()) {
            return R.failure("未查询到角色");
        }
        // 合并更新
        Role role = optional.get();
        roleMapper.merge(updateDto, role);
        roleRepository.saveAndFlush(role);

        /* 更新角色权限关联 */
        if (CheckTools.isNotNullOrEmpty(updateDto.getMenuAuths())) {
            // 删除已有的角色权限关联
            deleteRoleAuthByRoleId(role.getId());
            batchSaveRoleAuth(role.getId(), updateDto.getMenuAuths());
        }

        return R.success("更新成功", role.getId());
    }

    /**
     * 角色删除
     *
     * @param id 角色ID
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<Boolean> deleteRole(String id) {
        deleteById(id);
        return R.success(true);
    }

    /**
     * 角色删除批量
     *
     * @param ids 角色ID集合
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<Boolean> deleteRoleBatch(List<String> ids) {
        // 1.前置逻辑

        R<Boolean> deleteOneResult;
        boolean deleteAll = true;
        for (String id : ids) {
            deleteOneResult = this.deleteRole(id);
            if (!deleteOneResult.getData()) {
                deleteAll = false;
                break;
            }
        }

        // 批量删除失败，抛异常回滚
        if (!deleteAll) {
            log.info("【角色-批量删除】：失败");
            throw new BatchDeleteException("批量删除失败");
        }

        // 2.后置逻辑
        return R.success(true);
    }

    /**
     * 角色列表分页查询
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    @Override
    public R<List<RoleDTO>> queryRoleListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        QRole qRole = QRole.role;

        // todo：条件查询处理
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<Role> queryResults = getJpaQueryFactory()
                .select(qRole)
                .from(qRole)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<RoleDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());

        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 角色查询
     *
     * @return 统一结果集
     */
    @Override
    public R<RoleEditDTO> queryRoleById(String id) {
        return roleRepository.findById(id).map(role -> {
            RoleEditDTO roleEditDTO = roleMapper.toEditDTO(role);
            /* 回显菜单权限 */
            List<RoleAuth> roleAuths = queryRoleAuthByRoleId(roleEditDTO.getId());
            if (CheckTools.isNotNullOrEmpty(roleAuths)) {
                List<String> authIds = roleAuths.stream().map(RoleAuth::getAuthId).collect(Collectors.toList());
                roleEditDTO.setMenuAuths(authIds);
            }
            return roleEditDTO;
        }).map(R::success).orElseGet(() -> R.failure("未查询到角色信息"));
    }

    /**
     * 角色数据导出
     *
     * @param predicateWrapper 查询条件包装类
     * @return 统一结果集
     */
    @Override
    @Transactional
    public List<RoleDTO> export(PredicateWrapper predicateWrapper) {

        // 全量查询
        long count = roleRepository.count();
        if (count <= 0) {
            count = 20;
        }
        R<List<RoleDTO>> exportResult = this.queryRoleListByPage(predicateWrapper, PageRequest.of(0, (int) count));

        return exportResult.getData();
    }

    /**
     * 更新数据权限
     *
     * @param dataAuthType 数据权限类型
     * @param dataAuths    数据权限集合
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<Boolean> updateDataAuth(String roleId, Integer dataAuthType, List<String> dataAuths) {
        return roleRepository.findById(roleId).map(role -> {
            role.setDataAuthType(dataAuthType);
            role.setDataAuths(mapperHelper.listToString(dataAuths));
            roleRepository.saveAndFlush(role);
            return true;
        }).map(R::success).orElseGet(() -> R.failure("未查询到角色信息"));
    }

    @Override
    public List<UserRole> queryUserRoleEditDTOListByUserId(String userId) {
        QUserRole qUserRole = QUserRole.userRole;
        return getJpaQueryFactory()
            .selectFrom(qUserRole)
            .where(qUserRole.userId.eq(userId))
            .fetch();
    }

    /**
     * 批量保存角色权限关系
     *
     * @param roleId  用户ID
     * @param authIds 权限ID集合
     */
    @Transactional
    public void batchSaveRoleAuth(String roleId, List<String> authIds) {
        String sql = "INSERT INTO " + RoleAuth.TABLE_NAME + " (role_id,auth_id) VALUES (?,?)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String authId = authIds.get(i);
                ps.setString(1, roleId);
                ps.setString(2, authId);
            }

            @Override
            public int getBatchSize() {
                return authIds.size();
            }
        });
    }

    /**
     * 根据角色ID
     * 删除角色权限关联
     *
     * @param roleId 角色ID
     */
    @Transactional
    public long deleteRoleAuthByRoleId(String roleId) {
        QRoleAuth qRoleAuth = QRoleAuth.roleAuth;
        return getJpaQueryFactory().delete(qRoleAuth)
                .where(qRoleAuth.roleId.eq(roleId)).execute();
    }

    /**
     * 根据角色ID查询角色权限关联集合
     *
     * @param roleId 角色ID
     * @return List<RoleAuth>
     */
    public List<RoleAuth> queryRoleAuthByRoleId(String roleId) {
        QRoleAuth qRoleAuth = QRoleAuth.roleAuth;
        return getJpaQueryFactory().selectFrom(qRoleAuth)
                .where(qRoleAuth.roleId.eq(roleId)).fetch();
    }

    @Override
    public BaseRepository<Role, String> getRepository() {
        return roleRepository;
    }

    @Override
    public BaseMapper<Role, RoleDTO, RoleEditDTO> getMapper() {
        return roleMapper;
    }

}
