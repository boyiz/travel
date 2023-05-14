package com.xunye.auth.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.xunye.auth.repo.DeptRepository;
import com.xunye.auth.repo.SysUserRepository;
import com.google.common.base.Joiner;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.dto.DeptDTO;
import com.xunye.auth.dto.DeptEditDTO;
import com.xunye.auth.entity.Dept;
import com.xunye.auth.entity.QDept;
import com.xunye.auth.entity.QUser;
import com.xunye.auth.entity.User;
import com.xunye.auth.mapper.DeptMapper;
import com.xunye.auth.service.IDeptService;
import com.xunye.core.base.BaseMapper;
import com.xunye.core.base.BaseRepository;
import com.xunye.core.base.BaseServiceImpl;
import com.xunye.core.em.LabelValue;
import com.xunye.core.exception.BatchDeleteException;
import com.xunye.core.helper.QuerydslHelper;
import com.xunye.core.helper.TreeHelper;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 部门Service实现
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Slf4j
@Service
public class DeptServiceImpl extends BaseServiceImpl<DeptEditDTO, DeptDTO, Dept, String> implements IDeptService {

    private static final QDept Q_DEPT = QDept.dept;
    private static final QUser qUser = QUser.user;

    @Resource
    private DeptRepository deptRepository;
    @Resource
    private DeptMapper deptMapper;
    @Autowired
    private SysUserRepository sysUserRepository;

    /**
     * 处理负责人录入/更新
     */
    public void handleLeader(DeptEditDTO deptEditDTO, Dept deptEntity) {
        List<LabelValue> leaderUserVoList = deptEditDTO.getLeaderUserVoList();
        if (CheckTools.isNotNullOrEmpty(leaderUserVoList)) {
            List<String> ids = leaderUserVoList.stream().map(LabelValue::getValue).collect(Collectors.toList());
            List<String> names = leaderUserVoList.stream().map(LabelValue::getLabel).collect(Collectors.toList());
            deptEntity.setLeaderUserIds(Joiner.on(",").skipNulls().join(ids));
            deptEntity.setLeaderUserNames(Joiner.on(",").skipNulls().join(names));
        } else {
            deptEntity.setLeaderUserIds(null);
            deptEntity.setLeaderUserNames(null);
        }
    }

    /**
     * 部门创建
     *
     * @param createDto 编辑DTO
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<String> createDept(DeptEditDTO createDto) {
        Dept dept = deptMapper.toEntity(createDto);
        // 处理负责人转化
        handleLeader(createDto, dept);
        deptRepository.saveAndFlush(dept);
        return R.success(dept.getId());
    }

    /**
     * 部门更新
     *
     * @param updateDto 编辑DTO
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<String> updateDept(DeptEditDTO updateDto) {
        // 1.前置逻辑
        if (CheckTools.isNullOrEmpty(updateDto)) {
            return R.failure("部门id不存在");
        }

        Optional<Dept> optional = deptRepository.findById(updateDto.getId());
        if (!optional.isPresent()) {
            return R.failure("未查询到部门");
        }
        // 合并更新
        Dept dept = optional.get();
        deptMapper.merge(updateDto, dept);
        // 处理负责人更新
        handleLeader(updateDto, dept);

        // 落库
        deptRepository.saveAndFlush(dept);
        return R.success("更新成功", dept.getId());
    }

    /**
     * 部门删除
     *
     * @param id 部门ID
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<Boolean> deleteDept(String id) {
        /* 检查该部门下是否仍关联用户 */
        QUser qUser = QUser.user;
        long count = sysUserRepository.count(qUser.deptId.eq(id));
        if (count > 0) {
            return R.failure("删除失败，该部门下仍关联用户，请解绑后再试");
        }

        /* 删除部门 */
        deleteById(id);
        return R.success(true);
    }

    /**
     * 部门删除批量
     *
     * @param ids 部门ID集合
     * @return 统一结果集
     */
    @Override
    @Transactional
    public R<Boolean> deleteDeptBatch(List<String> ids) {
        // 1.前置逻辑

        R<Boolean> deleteOneResult;
        boolean deleteAll = true;
        for (String id : ids) {
            deleteOneResult = this.deleteDept(id);
            if (!deleteOneResult.getData()) {
                deleteAll = false;
                break;
            }
        }

        // 批量删除失败，抛异常回滚
        if (!deleteAll) {
            log.info("【部门-批量删除】：失败");
            throw new BatchDeleteException("批量删除失败");
        }

        // 2.后置逻辑
        return R.success(true);
    }

    /**
     * 部门列表分页查询
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    @Override
    public R<List<DeptDTO>> queryDeptListByPage(PredicateWrapper predicateWrapper, Pageable pageable) {
        QDept qDept = QDept.dept;

        // todo：条件查询处理
        Predicate predicate = predicateWrapper.getPredicate();

        QueryResults<Dept> queryResults = getJpaQueryFactory()
                .select(qDept)
                .from(qDept)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<DeptDTO> dtoList = queryResults.getResults()
                .stream().map(entity -> getMapper().toBasicDTO(entity))
                .collect(Collectors.toList());

        return R.success(dtoList).setTotal(queryResults.getTotal());
    }

    /**
     * 部门查询
     *
     * @return 统一结果集
     */
    @Override
    public R<DeptEditDTO> queryDeptById(String id) {
        DeptEditDTO editDto = findById(id);
        if (CheckTools.isNullOrEmpty(editDto)) {
            return R.failure("部门不存在");
        }

        // 处理负责人回显为labelvalue列表
        if (CheckTools.isNotNullOrEmpty(editDto.getLeaderUserIds())
                && CheckTools.isNotNullOrEmpty(editDto.getLeaderUserNames())) {
            List<String> ids = Arrays.asList(editDto.getLeaderUserIds().split(","));
            List<String> names = Arrays.asList(editDto.getLeaderUserNames().split(","));
            for (int j = 0; j < ids.size(); j++) {
                LabelValue labelValue = LabelValue.instance(names.get(j), ids.get(j));
                editDto.getLeaderUserVoList().add(labelValue);
            }
        }
        return R.success(editDto);
    }

    @Override
    public Dept queryDeptByUserId(String userId) {
        List<Dept> deptDTOList = getJpaQueryFactory()
                .select(Q_DEPT)
                .from(Q_DEPT)
                .leftJoin(qUser).on(Q_DEPT.id.eq(qUser.deptId))
                .where(qUser.id.eq(userId)).fetch();
        if (deptDTOList.size() == 0) {
            return null;
        }
        return deptDTOList.get(0);
    }

    /**
     * 部门数据导出
     *
     * @param predicateWrapper 查询条件包装类
     * @return 统一结果集
     */
    @Override
    @Transactional
    public List<DeptDTO> export(PredicateWrapper predicateWrapper) {
        // 全量查询
        long count = deptRepository.count();
        R<List<DeptDTO>> exportResult = this.queryDeptListByPage(predicateWrapper, PageRequest.of(0, (int) count));
        return exportResult.getData();
    }

    /**
     * 查询部门列表树
     *
     * @param predicate 查询谓语
     * @return 统一结果集
     */
    @Override
    public R<List<DeptDTO>> queryDeptTreeList(Predicate predicate) {
        List<DeptDTO> deptDTOList = getJpaQueryFactory()
                .select(Q_DEPT)
                .from(Q_DEPT)
                .where(predicate)
                .fetch()
                .stream()
                .map(deptMapper::toBasicDTO)
                .collect(Collectors.toList());
        List<DeptDTO> deptTreeL = TreeHelper.toTree(deptDTOList);
        return R.success(deptTreeL);
    }

    @Override
    public List<Dept> queryResponsibleDeptListByUser(String userId) {
        if (CheckTools.isNullOrEmpty(userId)) {
            return Lists.newArrayList();
        }

        QDept qDept = QDept.dept;
        Predicate predicate = QuerydslHelper.findInSet(userId, qDept.leaderUserIds);

        List<Dept> deptList = getJpaQueryFactory()
                .select(Q_DEPT)
                .from(Q_DEPT)
                .where(predicate)
                .fetch();

        return deptList;
    }

//    private void search(List<Dept> resultList, String parentId) {
//        for (Dept dept : resultList) {
//            if (dept.getId().equals(parentId)) {
//                resultList.add(dept);
//            }
//        }
//    }
//
//    private List<Dept> recursionQueryDeptList(List<Dept> resultList, String parentId) {
//        if (CheckTools.isNullOrEmpty(parentId)) {
//            return new ArrayList<>();
//        }
//
//        log.info("查询部门");
//
//        QDept qDept = QDept.dept;
//        List<Dept> subDeptList = getJpaQueryFactory()
//                .select(Q_DEPT)
//                .from(Q_DEPT)
//                .where(qDept.parentId.eq(parentId))
//                .fetch();
//        if (CheckTools.isNullOrEmpty(subDeptList)) {
//            return Lists.newArrayList();
//        }
//
//        resultList.addAll(subDeptList);
//        for (Dept dept : subDeptList) {
//            recursionQueryDeptList(resultList, dept.getId());
//        }
//
//        return new ArrayList<>();
//    }


    // 搜索子部门
    private List<Dept> queryChildrenDeptByParentId(List<Dept> deptList, String parentId) {
        List<Dept> childrenDeptList = deptList.stream().filter(dept -> {
            if (CheckTools.isNotNullOrEmpty(dept.getParentId()) && dept.getParentId().equals(parentId)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return childrenDeptList;
    }

    /**
     * 递归查询子部门
     * @param allDeptList
     * @param parentId
     * @return
     */
    private List<Dept> recursionSearchChildrenDept(List<Dept> allDeptList, String parentId) {
        // 1.直接搜索是否存在子部门
        List<Dept> childrenDeptList = queryChildrenDeptByParentId(allDeptList, parentId);
        if (CheckTools.isNullOrEmpty(childrenDeptList)) {
            return new ArrayList<>();
        }

        // 递归查找
        List<Dept> targetList = Lists.newArrayList(childrenDeptList);
        for (Dept dept : childrenDeptList) {
            List<Dept> subTargetList = recursionSearchChildrenDept(allDeptList, dept.getId());
            targetList.addAll(subTargetList);
        }
        return targetList;
    }

    @Override
    public List<Dept> queryChildDeptListByParentDept(String parentDeptId) {
        List<Dept> allDeptList = deptRepository.findAll();
        List<Dept> targetList = recursionSearchChildrenDept(allDeptList, parentDeptId);
        return targetList;
    }

    /**
     * 部门负责人查询
     *
     * @param deptId 部门 ID
     * @return 统一结果集
     */
    @Override
    public R<List<User>> queryLeadersByDeptId(String deptId) {
        // 新建用户列表
        List<User> leaderList = new ArrayList<User>();

        // 查询管理者 ID 列表
        DeptEditDTO deptEditDTO = queryDeptById(deptId).getData();
        if (CheckTools.isNullOrEmpty(deptEditDTO)) {
            return R.failure("部门不存在");
        }

        if (StringUtils.isNotEmpty(deptEditDTO.getLeaderUserIds())
                && deptEditDTO.getLeaderUserIds().split(",").length > 0) {
            for (String leaderUserId : deptEditDTO.getLeaderUserIds().split(",")) {
                User leader = sysUserRepository.findById(leaderUserId).orElse(null);
                if (CheckTools.isNotNullOrEmpty(leader)) {
                    leaderList.add(leader);
                } else {
                    return R.failure("部门负责人在系统中已经不存在：" + leaderUserId);
                }
            }
        }
        return R.success(leaderList);
    }

    @Override
    public List<Dept> queryDeptListByPredicate(Predicate predicate) {
        List<Dept> deptList = getJpaQueryFactory()
                .select(Q_DEPT)
                .from(Q_DEPT)
                .where(predicate)
                .fetch();

        return deptList;
    }

    @Override
    public R<List<DeptDTO>> queryAll() {
        List<DeptDTO> deptDTOList = getJpaQueryFactory()
                .select(Q_DEPT)
                .from(Q_DEPT)
                .fetch()
                .stream()
                .map(deptMapper::toBasicDTO)
                .collect(Collectors.toList());
        return R.success(deptDTOList);
    }

    @Override
    public BaseRepository<Dept, String> getRepository() {
        return deptRepository;
    }

    @Override
    public BaseMapper<Dept, DeptDTO, DeptEditDTO> getMapper() {
        return deptMapper;
    }

}
