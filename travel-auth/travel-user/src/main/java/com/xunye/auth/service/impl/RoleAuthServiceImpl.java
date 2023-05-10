//package com.xunye.auth.service.impl;
//
//import com.xunye.core.result.R;
//import com.xunye.core.base.BaseMapper;
//import com.xunye.core.base.BaseRepository;
//import com.xunye.core.base.BaseServiceImpl;
//import com.xunye.core.exception.BatchDeleteException;
//import com.xunye.core.model.PredicateWrapper;
//import com.xunye.auth.dto.RoleAuthDTO;
//import com.xunye.auth.dto.RoleAuthEditDTO;
//import com.xunye.auth.entity.RoleAuth;
//import com.xunye.auth.entity.QRoleAuth;
//import com.xunye.auth.mapper.RoleAuthMapper;
//import com.xunye.auth.repo.RoleAuthRepository;
//import com.xunye.auth.service.IRoleAuthService;
//import com.querydsl.core.QueryResults;
//import com.querydsl.core.types.Predicate;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.xunye.core.annotation.DataAuth;
//import com.querydsl.core.types.Predicate;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import java.util.Optional;
//import com.xunye.core.tools.CheckTools;
//import com.xunye.auth.entity.User;
//import com.xunye.core.exception.BusinessException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
//@Slf4j
//@Service
//public class RoleAuthServiceImpl extends BaseServiceImpl<RoleAuthEditDTO, RoleAuthDTO, RoleAuth, String> implements IRoleAuthService {
//
//    private static final QRoleAuth qRoleAuth = QRoleAuth.roleAuth;
//
//    private final RoleAuthMapper roleAuthMapper;
//    private final RoleAuthRepository roleAuthRepository;
//
//    public RoleAuthServiceImpl(RoleAuthMapper roleAuthMapper, RoleAuthRepository roleAuthRepository) {
//        this.roleAuthMapper = roleAuthMapper;
//        this.roleAuthRepository = roleAuthRepository;
//    }
//
//    /**
//     * 创建角色权限实体信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String createRoleAuth(RoleAuthEditDTO roleAuthEditDto, User operatorUser) {
//        // 转换为Entity实体
//        RoleAuth roleAuthEntity = roleAuthMapper.toEntity(roleAuthEditDto);
//
//        // 设置创建信息
//        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
//            roleAuthEntity.setCreateBy(operatorUser.getId());
//            roleAuthEntity.setCreateByName(operatorUser.getUserName());
//            roleAuthEntity.setUpdateBy(operatorUser.getId());
//            roleAuthEntity.setUpdateByName(operatorUser.getUserName());
//        } else {
//            throw new BusinessException("角色权限实体创建失败：创建人信息获取有误");
//        }
//
//        // 创建角色权限实体
//        roleAuthRepository.saveAndFlush(roleAuthEntity);
//        return roleAuthEntity.getId();
//    }
//
//    /**
//     * 更新角色权限实体信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateRoleAuth(RoleAuthEditDTO roleAuthEditDto, User operatorUser) {
//        Optional<RoleAuth> optional = roleAuthRepository.findById(roleAuthEditDto.getId());
//        if (!optional.isPresent()) {
//            throw new BusinessException("角色权限实体不存在");
//        }
//
//        // 将EditDto属性合并到DB实体中
//        RoleAuth roleAuthDB = optional.get();
//        roleAuthMapper.merge(roleAuthEditDto, roleAuthDB);
//        // 设置更新信息
//        roleAuthDB.setUpdateBy(operatorUser.getId());
//        roleAuthDB.setUpdateByName(operatorUser.getUserName());
//        // 更新角色权限实体
//        roleAuthRepository.saveAndFlush(roleAuthDB);
//    }
//
//    /**
//     * 删除角色权限实体
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteRoleAuth(String id, User operatorUser) {
//        // before
//        deleteById(id);
//    }
//
//    /**
//     * 批量删除角色权限实体
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteRoleAuthBatch(List<String> ids, User operatorUser) {
//        // 遍历删除
//        for (String id : ids) {
//            this.deleteRoleAuth(id,operatorUser);
//        }
//    }
//
//    /**
//     * 分页查询角色权限实体列表
//     */
//    //@DataAuth
//    @Override
//    public R<List<RoleAuthDTO>> queryRoleAuthListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
//        Predicate predicate = predicateWrapper.getPredicate();
//
//        QueryResults<RoleAuth> queryResults = getJpaQueryFactory()
//                .select(qRoleAuth)
//                .from(qRoleAuth)
//                .where(predicate)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(qRoleAuth.createTime.desc())
//                .fetchResults();
//
//        List<RoleAuthDTO> dtoList = queryResults.getResults()
//                .stream().map(entity -> getMapper().toBasicDTO(entity))
//                .collect(Collectors.toList());
//        return R.success(dtoList).setTotal(queryResults.getTotal());
//    }
//
//    /**
//     * 根据Id查询角色权限实体信息
//     */
//    @Override
//    public RoleAuthEditDTO queryRoleAuthById(String id) {
//        //Predicate predicate = qRoleAuth.id.eq(id);
//        //List<RoleAuthEditDTO> queryList = queryRoleAuthEditDTOListByPredicate(predicate);
//        //if (queryList.size() == 1) {
//        //    return queryList.get(0);
//        //}
//        return null;
//    }
//
//    /**
//     * 导出角色权限实体数据
//     */
//    @Override
//    public List<RoleAuthDTO> export(PredicateWrapper predicateWrapper) {
//        long count = roleAuthRepository.count();
//        R<List<RoleAuthDTO>> exportResult = this.queryRoleAuthListByPage(predicateWrapper, PageRequest.of(0, (int) count));
//        return exportResult.getData();
//    }
//
//    @Override
//    public List<RoleAuthEditDTO> queryRoleAuthEditDTOListByPredicate(Predicate predicate) {
//        return getJpaQueryFactory()
//                .selectFrom(qRoleAuth)
//                .where(predicate)
//                .orderBy(qRoleAuth.createTime.asc())
//                .fetch()
//                .stream()
//                .map(entity -> getMapper().toEditDTO(entity))
//                .collect(Collectors.toList());
//    }
//
//    //@Override
//    //public List<RoleAuthEditDTO> queryRoleAuthEditDTOListByRoleId(String roleId) {
//    //    Predicate predicate = qRoleAuth.roleId.eq(roleId);
//    //    return queryRoleAuthEditDTOListByPredicate(predicate);
//    //}
//
//    @Override
//    public BaseRepository<RoleAuth, String> getRepository() {
//        return roleAuthRepository;
//    }
//
//    @Override
//    public BaseMapper<RoleAuth, RoleAuthDTO, RoleAuthEditDTO> getMapper() {
//        return roleAuthMapper;
//    }
//
//}
