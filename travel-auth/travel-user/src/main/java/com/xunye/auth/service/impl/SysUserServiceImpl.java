package com.xunye.auth.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.xunye.auth.repo.SysUserRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.dto.UserDTO;
import com.xunye.auth.dto.UserEditDTO;
import com.xunye.auth.entity.QUser;
import com.xunye.auth.entity.User;
import com.xunye.auth.mapper.SysUserMapper;
import com.xunye.auth.service.ISysUserService;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SysUserServiceImpl extends BaseServiceImpl<UserEditDTO, UserDTO, User, String> implements
    ISysUserService {

    private static final QUser qUserInfo = QUser.user;

    private final SysUserMapper sysUserMapper;
    private final SysUserRepository sysUserRepository;

    public SysUserServiceImpl(SysUserMapper sysUserMapper, SysUserRepository sysUserRepository) {
        this.sysUserMapper = sysUserMapper;
        this.sysUserRepository = sysUserRepository;
    }

    /**
     * 创建用户实体信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createUserInfo(UserEditDTO userEditDto, User operatorUser) {
        // 转换为Entity实体
        User userEntity = sysUserMapper.toEntity(userEditDto);
        //设置密码,加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(userEditDto.getPassword());
        userEntity.setPassword(encode);
        // 设置创建信息
        if (CheckTools.isNotNullOrEmpty(operatorUser)) {
            userEntity.setCreateBy(operatorUser.getId());
            userEntity.setCreateByName(operatorUser.getUserName());
            userEntity.setUpdateBy(operatorUser.getId());
        } else {
            throw new BusinessException("用户实体创建失败：创建人信息获取有误");
        }

        // 创建用户实体
        sysUserRepository.saveAndFlush(userEntity);
        return userEntity.getId();
    }

    /**
     * 更新用户实体信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserEditDTO userEditDto, User operatorUser) {
        Optional<User> optional = sysUserRepository.findById(userEditDto.getId());
        if (!optional.isPresent()) {
            throw new BusinessException("用户实体不存在");
        }

        // 将EditDto属性合并到DB实体中
        User userDB = optional.get();
        sysUserMapper.merge(userEditDto, userDB);
        // 设置更新信息
        userDB.setUpdateBy(operatorUser.getId());
        // 更新用户实体
        sysUserRepository.saveAndFlush(userDB);
    }

    /**
     * 删除用户实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserInfo(String id, User operatorUser) {
        // before
        deleteById(id);
    }

    /**
     * 批量删除用户实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserInfoBatch(List<String> ids, User operatorUser) {
        // 遍历删除
        for (String id : ids) {
            this.deleteUserInfo(id,operatorUser);
        }
    }

    /**
     * 分页查询用户实体列表
     */
    @Override
    public R<List<UserDTO>> queryUserInfoListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<User> queryResults = getJpaQueryFactory()
                .select(qUserInfo)
                .from(qUserInfo)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qUserInfo.createTime.desc())
                .fetchResults();

        List<UserDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());
        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 根据Id查询用户实体信息
     */
    @Override
    public UserEditDTO queryUserInfoById(String id) {
        Predicate predicate = qUserInfo.id.eq(id);
        List<UserEditDTO> queryList = queryUserInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    /**
     * 导出用户实体数据
     */
    @Override
    public List<UserDTO> export(PredicateWrapper predicateWrapper) {
        long count = sysUserRepository.count();
        R<List<UserDTO>> exportResult = this.queryUserInfoListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    @Override
    public List<UserEditDTO> queryUserInfoEditDTOListByPredicate(Predicate predicate) {
        return getJpaQueryFactory()
                .selectFrom(qUserInfo)
                .where(predicate)
                .orderBy(qUserInfo.createTime.asc())
                .fetch()
                .stream()
                .map(entity -> getMapper().toEditDTO(entity))
                .collect(Collectors.toList());
    }

    @Override
    public UserEditDTO queryUserInfoByWxOpenid(String openid) {
        Predicate predicate = qUserInfo.userOpenid.eq(openid);
        List<UserEditDTO> queryList = queryUserInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    @Override
    public List<User> queryUserListInDeptIds(List<String> deptIdList) {
        if (CheckTools.isNullOrEmpty(deptIdList)) {
            return Lists.newArrayList();
        }

        QUser qUser = QUser.user;
        List<User> userList = getJpaQueryFactory()
            .select(qUser)
            .from(qUser)
            .where(qUser.deptId.in(deptIdList))
            .fetch();

        return userList;
    }

    @Override
    public UserEditDTO queryUserInfoByUsername(String username) {
        Predicate predicate = qUserInfo.userName.eq(username);
        List<UserEditDTO> queryList = queryUserInfoEditDTOListByPredicate(predicate);
        if (queryList.size() == 1) {
            return queryList.get(0);
        }
        return null;
    }

    @Override
    public BaseRepository<User, String> getRepository() {
        return sysUserRepository;
    }

    @Override
    public BaseMapper<User, UserDTO, UserEditDTO> getMapper() {
        return sysUserMapper;
    }

}
