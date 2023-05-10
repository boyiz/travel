package com.xunye.auth.service;

import java.util.List;

import com.xunye.core.base.BaseService;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.auth.dto.DeptDTO;
import com.xunye.auth.dto.DeptEditDTO;
import com.xunye.auth.entity.Dept;
import com.xunye.auth.entity.User;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;

/**
 * 部门Service接口
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
public interface IDeptService extends BaseService<DeptEditDTO, DeptDTO, Dept, String> {

    /**
     * 部门创建
     *
     * @param createDto 编辑DTO
     * @return 统一结果集
     */
    R<String> createDept(DeptEditDTO createDto);

    /**
     * 部门更新
     *
     * @param updateDto 编辑DTO
     * @return 统一结果集
     */
    R<String> updateDept(DeptEditDTO updateDto);

    /**
     * 部门批量删除
     *
     * @param id 部门ID
     * @return 统一结果集
     */
    R<Boolean> deleteDept(String id);

    /**
     * 部门批量删除
     *
     * @param ids 部门ID集合
     * @return 统一结果集
     */
    R<Boolean> deleteDeptBatch(List<String> ids);

    /**
     * 部门列表分页查询
     *
     * @param predicateWrapper 条件谓语包装类
     * @param pageable         分页参数
     * @return 统一结果集
     */
    R<List<DeptDTO>> queryDeptListByPage(PredicateWrapper predicateWrapper, Pageable pageable);

    /**
     * 部门查询
     *
     * @return 统一结果集
     */
    R<DeptEditDTO> queryDeptById(String id);

    Dept queryDeptByUserId(String userId);


    /**
     * 部门数据导出
     *
     * @param predicateWrapper 查询条件包装类
     * @return 统一结果集
     */
    List<DeptDTO> export(PredicateWrapper predicateWrapper);

    // ========================================业务方法在下方声明======================================== //

    /**
     * 查询部门列表树
     *
     * @param predicate 查询谓语
     * @return 统一结果集
     */
    R<List<DeptDTO>> queryDeptTreeList(Predicate predicate);

    /**
     * 查询用户负责的部门
     */
    List<Dept> queryResponsibleDeptListByUser(String userId);

    /**
     * 查询x部门的所有子部门
     */
    List<Dept> queryChildDeptListByParentDept(String parentDeptId);

    /**
     * 部门负责人查询
     *
     * @param deptId 部门 ID
     * @return 统一结果集
     */
    R<List<User>> queryLeadersByDeptId(String deptId);
    
    /**
     * 查询部门列表
     */
    List<Dept> queryDeptListByPredicate(Predicate predicate);

    R<List<DeptDTO>> queryAll();

}
