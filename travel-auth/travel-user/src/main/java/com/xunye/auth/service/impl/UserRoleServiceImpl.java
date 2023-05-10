//package com.xunye.auth.service.impl;
//
//import com.xunye.core.result.R;
//import com.xunye.core.base.BaseMapper;
//import com.xunye.core.base.BaseRepository;
//import com.xunye.core.base.BaseServiceImpl;
//import com.xunye.core.exception.BatchDeleteException;
//import com.xunye.core.model.PredicateWrapper;
//import com.xunye.auth.dto.UserRoleDTO;
//import com.xunye.auth.dto.UserRoleEditDTO;
//import com.xunye.auth.entity.UserRole;
//import com.xunye.auth.entity.QUserRole;
//import com.xunye.auth.mapper.UserRoleMapper;
//import com.xunye.auth.repo.UserRoleRepository;
//import com.xunye.auth.service.IUserRoleService;
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
//public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleEditDTO, UserRoleDTO, UserRole, String> implements IUserRoleService {
//
//    private static final QUserRole qUserRole = QUserRole.userRole;
//
//    private final UserRoleMapper userRoleMapper;
//    private final UserRoleRepository userRoleRepository;
//
//    public UserRoleServiceImpl(UserRoleMapper userRoleMapper, UserRoleRepository userRoleRepository) {
//        this.userRoleMapper = userRoleMapper;
//        this.userRoleRepository = userRoleRepository;
//    }
//
//    /**
//     * 创建用户角色实体信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String createUserRole(UserRoleEditDTO userRoleEditDto, User operatorUser) {
//        // 转换为Entity实体
//        UserRole userRoleEntity = userRoleMapper.toEntity(userRoleEditDto);
//
//        // 设置创建信息
//        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
//            userRoleEntity.setCreateBy(operatorUser.getId());
//            userRoleEntity.setCreateByName(operatorUser.getUserName());
//            userRoleEntity.setUpdateBy(operatorUser.getId());
//            userRoleEntity.setUpdateByName(operatorUser.getUserName());
//        } else {
//            throw new BusinessException("用户角色实体创建失败：创建人信息获取有误");
//        }
//
//        // 创建用户角色实体
//        userRoleRepository.saveAndFlush(userRoleEntity);
//        return userRoleEntity.getId();
//    }
//
//    /**
//     * 更新用户角色实体信息
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateUserRole(UserRoleEditDTO userRoleEditDto, User operatorUser) {
//        Optional<UserRole> optional = userRoleRepository.findById(userRoleEditDto.getId());
//        if (!optional.isPresent()) {
//            throw new BusinessException("用户角色实体不存在");
//        }
//
//        // 将EditDto属性合并到DB实体中
//        UserRole userRoleDB = optional.get();
//        userRoleMapper.merge(userRoleEditDto, userRoleDB);
//        // 设置更新信息
//        userRoleDB.setUpdateBy(operatorUser.getId());
//        userRoleDB.setUpdateByName(operatorUser.getUserName());
//        // 更新用户角色实体
//        userRoleRepository.saveAndFlush(userRoleDB);
//    }
//
//    /**
//     * 删除用户角色实体
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteUserRole(String id, User operatorUser) {
//        // before
//        deleteById(id);
//    }
//
//    /**
//     * 批量删除用户角色实体
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteUserRoleBatch(List<String> ids, User operatorUser) {
//        // 遍历删除
//        for (String id : ids) {
//            this.deleteUserRole(id,operatorUser);
//        }
//    }
//
//    /**
//     * 分页查询用户角色实体列表
//     */
//    //@DataAuth
//    @Override
//    public R<List<UserRoleDTO>> queryUserRoleListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
//        Predicate predicate = predicateWrapper.getPredicate();
//
//        QueryResults<UserRole> queryResults = getJpaQueryFactory()
//                .select(qUserRole)
//                .from(qUserRole)
//                .where(predicate)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(qUserRole.createTime.desc())
//                .fetchResults();
//
//        List<UserRoleDTO> dtoList = queryResults.getResults()
//                .stream().map(entity -> getMapper().toBasicDTO(entity))
//                .collect(Collectors.toList());
//        return R.success(dtoList).setTotal(queryResults.getTotal());
//    }
//
//    /**
//     * 根据Id查询用户角色实体信息
//     */
//    @Override
//    public UserRoleEditDTO queryUserRoleById(String id) {
//        //Predicate predicate = qUserRole.id.eq(id);
//        //List<UserRoleEditDTO> queryList = queryUserRoleEditDTOListByPredicate(predicate);
//        //if (queryList.size() == 1) {
//        //    return queryList.get(0);
//        //}
//        return null;
//    }
//
//    /**
//     * 导出用户角色实体数据
//     */
//    @Override
//    public List<UserRoleDTO> export(PredicateWrapper predicateWrapper) {
//        long count = userRoleRepository.count();
//        R<List<UserRoleDTO>> exportResult = this.queryUserRoleListByPage(predicateWrapper, PageRequest.of(0, (int) count));
//        return exportResult.getData();
//    }
//
//    @Override
//    public List<UserRoleEditDTO> queryUserRoleEditDTOListByPredicate(Predicate predicate) {
//        return getJpaQueryFactory()
//                .selectFrom(qUserRole)
//                .where(predicate)
//                .orderBy(qUserRole.createTime.asc())
//                .fetch()
//                .stream()
//                .map(entity -> getMapper().toEditDTO(entity))
//                .collect(Collectors.toList());
//    }
//
//    //@Override
//    //public List<UserRoleEditDTO> queryUserRoleEditDTOListByUserId(String userId) {
//    //    Predicate predicate = qUserRole.userId.eq(userId);
//    //    return queryUserRoleEditDTOListByPredicate(predicate);
//    //}
//
//    @Override
//    public BaseRepository<UserRole, String> getRepository() {
//        return userRoleRepository;
//    }
//
//    @Override
//    public BaseMapper<UserRole, UserRoleDTO, UserRoleEditDTO> getMapper() {
//        return userRoleMapper;
//    }
//
//}
