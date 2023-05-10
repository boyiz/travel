package com.xunye.auth.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.xunye.auth.entity.RoleAuth;
import com.xunye.auth.repo.AuthRepository;
import com.xunye.auth.vo.AuthVo;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.helper.TreeHelper;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.dto.AuthDTO;
import com.xunye.auth.dto.AuthEditDTO;
import com.xunye.auth.entity.Auth;
import com.xunye.auth.entity.QAuth;
import com.xunye.auth.entity.QRoleAuth;
import com.xunye.auth.entity.QUserRole;
import com.xunye.auth.mapper.AuthMapper;
import com.xunye.auth.service.IAuthService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 权限Service实现
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Slf4j
@Service
public class AuthServiceImpl extends BaseServiceImpl<AuthEditDTO, AuthDTO, Auth, String> implements IAuthService {

    private static final QAuth Q_AUTH = QAuth.auth;

    @Resource
    private AuthRepository authRepository;

    @Resource
    private AuthMapper authMapper;

    /**
     * 权限创建
     *
     * @param createDto 编辑DTO
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<String> createAuth(AuthEditDTO createDto) {
        // 1.前置逻辑

        Auth entity = saveOrUpdate(createDto);

        // 2.后置逻辑
        return R.success(entity.getId());
    }

    /**
     * 权限更新
     *
     * @param updateDto 编辑DTO
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<String> updateAuth(AuthEditDTO updateDto) {
        // 1.前置逻辑
        if (CheckTools.isNullOrEmpty(updateDto)) {
            return R.failure("权限id不存在");
        }

        Optional<Auth> optional = authRepository.findById(updateDto.getId());
        if (!optional.isPresent()) {
            return R.failure("未查询到权限");
        }
        // 合并更新
        Auth auth = optional.get();
        authMapper.merge(updateDto, auth);

        // 2.后置逻辑
        return R.success("更新成功", auth.getId());
    }

    /**
     * 权限删除
     *
     * @param id 权限ID
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<Boolean> deleteAuth(String id) {
        deleteById(id);
        return R.success(true);
    }

    /**
     * 权限删除批量
     *
     * @param ids 权限ID集合
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<Boolean> deleteAuthBatch(List<String> ids) {
        // 1.前置逻辑

        R<Boolean> deleteOneResult;
        boolean deleteAll = true;
        for (String id : ids) {
            deleteOneResult = this.deleteAuth(id);
            if (!deleteOneResult.getData()) {
                deleteAll = false;
                break;
            }
        }

        // 批量删除失败，抛异常回滚
        if (!deleteAll) {
            log.info("【权限-批量删除】：失败");
            throw new BatchDeleteException("批量删除失败");
        }

        // 2.后置逻辑
        return R.success(true);
    }

    /**
     * 权限列表分页查询
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    @Override
    public R<List<AuthDTO>> queryAuthListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        QAuth qAuth = QAuth.auth;

        // todo：条件查询处理
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<Auth> queryResults = getJpaQueryFactory()
                .select(qAuth)
                .from(qAuth)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AuthDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());

        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 权限查询
     *
     * @return 统一结果集
     */
    @Override
    public R<AuthEditDTO> queryAuthById(String id) {
        AuthEditDTO editDto = findById(id);
        return R.success(editDto);
    }

    /**
     * 权限数据导出
     *
     * @param predicateWrapper 查询条件包装类
     * @return 统一结果集
     */
    @Override
    @Transactional
    public List<AuthDTO> export(PredicateWrapper predicateWrapper) {
        // 全量查询
        long count = authRepository.count();
        R<List<AuthDTO>> exportResult = this.queryAuthListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<AuthDTO> queryAuthTreeList(Predicate predicate) {
        List<AuthDTO> authDTOList = getJpaQueryFactory()
                .select(Q_AUTH)
                .from(Q_AUTH)
                .where(predicate)
                .fetch()
                .stream()
                .map(authMapper::toBasicDTO)
                .collect(Collectors.toList());
        return TreeHelper.toTree(authDTOList);
    }

    /**
     * 根据用户ID查询拥有的权限集合
     *
     * @param userId 用户ID
     * @return 权限集
     */
    @Override
    public List<AuthVo> queryAuthVoListByUserId(String userId) {
        QUserRole qUserRole = QUserRole.userRole;
        QRoleAuth qRoleAuth = QRoleAuth.roleAuth;

        /* 根据用户查询拥有的角色 */
        List<String> roleIds = getJpaQueryFactory()
                .select(qUserRole.roleId)
                .from(qUserRole)
                .where(qUserRole.userId.eq(userId))
                .fetch();

        /* 根据角色关联拥有的权限 */
        QueryResults<Tuple> queryResults = getJpaQueryFactory()
                .select(qRoleAuth, Q_AUTH)
                .from(qRoleAuth)
                .leftJoin(Q_AUTH).on(qRoleAuth.authId.eq(Q_AUTH.id))
                .where(qRoleAuth.roleId.in(roleIds))
                .fetchResults();

        /* 数据处理 */
        List<AuthVo> authVoList = queryResults.getResults().stream().map(tuple -> {
            Auth auth = tuple.get(Q_AUTH);
            return authMapper.toAuthVo(auth);
        }).collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AuthVo::getId))),
                ArrayList::new
        ));
        return authVoList;
    }

    @Override
    public List<RoleAuth> queryRoleAuthEditDTOListByRoleId(String roleId) {
        QRoleAuth qRoleAuth = QRoleAuth.roleAuth;
        return getJpaQueryFactory()
            .selectFrom(qRoleAuth)
            .where(qRoleAuth.roleId.eq(roleId))
            .fetch();
    }

    @Override
    public BaseRepository<Auth, String> getRepository() {
        return authRepository;
    }

    @Override
    public BaseMapper<Auth, AuthDTO, AuthEditDTO> getMapper() {
        return authMapper;
    }

}
