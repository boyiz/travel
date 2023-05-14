package com.xunye.auth.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.biz.IDeptBiz;
import com.xunye.auth.consts.AuthConstants;
import com.xunye.auth.dto.DeptDTO;
import com.xunye.auth.dto.DeptEditDTO;
import com.xunye.auth.entity.Dept;
import com.xunye.auth.entity.User;
import com.xunye.auth.service.IDeptService;
import com.xunye.auth.service.ISysUserService;
import com.xunye.auth.vo.DeptUserMixTreeNodeVo;
import com.xunye.core.base.BaseController;
import com.xunye.core.base.BaseEntity;
import com.xunye.core.em.UserTypeEnum;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/dept")
@SaCheckRole("super-admin")
public class DeptController extends BaseController {

    @Resource
    private IDeptService deptService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IDeptBiz deptBiz;

    /**
     * 部门列表查询接口
     *
     * @param predicate 条件谓语
     */
    @GetMapping
    //@ApiAuth("auth:dept:list")
    public R<List<DeptDTO>> list(@QuerydslPredicate(root = Dept.class) Predicate predicate) {
        return deptService.queryDeptTreeList(predicate);
    }

    /**
     * 部门创建接口
     */
    @PostMapping
    //@ApiAuth("auth:dept:create")
    public R<String> create(@RequestBody DeptEditDTO addDTO) {
        return deptService.createDept(addDTO);
    }

    /**
     * 部门更新接口
     */
    @PutMapping
    //@ApiAuth("auth:dept:update")
    public R<String> update(@RequestBody DeptEditDTO editDto) {
        return deptService.updateDept(editDto);
    }

    /**
     * 部门删除接口
     */
    @DeleteMapping("/{id}")
    //@ApiAuth("auth:dept:delete")
    public R<Boolean> delete(@PathVariable String id) {
        return deptService.deleteDept(id);
    }

    /**
     * 部门批量删除接口
     */
    @DeleteMapping("/batch/{ids}")
    //@ApiAuth("auth:dept:deleteBatch")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        return deptService.deleteDeptBatch(idList);
    }

    /**
     * 部门详情接口
     */
    @GetMapping("/{id}")
    //@ApiAuth("auth:dept:detail")
    public R<DeptEditDTO> detail(@PathVariable String id) {
        return deptService.queryDeptById(id);
    }

    /**
     * 部门数据导出接口
     */
    @GetMapping("/export")
    //@ApiAuth("auth:dept:export")
    public void export(@QuerydslPredicate(root = Dept.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "部门列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), DeptDTO.class)
                .sheet("Sheet1")
                .doWrite(deptService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 部门表单数据初始化
     */
    @GetMapping("/initDeptFormData")
    public R<Map<String, Object>> initDeptFormData(@QuerydslPredicate(root = Dept.class) Predicate predicate) {
        Map<String, Object> resultMap = new HashMap<>();
        // 部门TreeList
        List<DeptDTO> treeList = deptService.queryDeptTreeList(predicate).getData();
        resultMap.put(AuthConstants.DEPT_TREE_LIST_KEY, treeList);
        return R.success(resultMap);
    }

    /**
     * 部门负责人查询
     */
    @GetMapping("/leaders")
    public R<List<User>> queryLeaders(String deptId) {
        return deptService.queryLeadersByDeptId(deptId);
    }


    /**
     * 查询用户负责的部门
     */
    @GetMapping("/queryResponsibleDeptListByUser")
    public R<List<Dept>> queryResponsibleDeptListByUser(String userId) {
        List<Dept> deptList = deptService.queryResponsibleDeptListByUser(userId);
        return R.success(deptList);
    }

    /**
     * 查询用户负责的部门和子部门
     */
    @GetMapping("/queryResponsibleAndChildDeptListByUser")
    public R<List<Dept>> queryResponsibleAndChildDeptListByUser(String userId) {
        List<Dept> responsibleDept = deptService.queryResponsibleDeptListByUser(userId);

        List<Dept> allDept = new ArrayList<>(responsibleDept);
        for (Dept dept : responsibleDept) {
            List<Dept> depts = deptService.queryChildDeptListByParentDept(dept.getId());
            allDept.addAll(depts);
        }
        return R.success(allDept);
    }

    /**
     * 查询用户负责部门的所有业务员
     */
    @GetMapping("/queryResponsibleDeptAllUserListByUser")
    public R<List<User>> queryResponsibleDeptAllUserListByUser(String userId) {

        User user = userService.getRepository().findById(userId).get();
        if (CheckTools.isNullOrEmpty(user)) {
            return R.failure("用户不存在");
        }

        if (CheckTools.isNotNullOrEmpty(user.getType()) && user.getType().equals(UserTypeEnum.SUPER_ADMIN.getValue())) {
            // 查询所有的部门
            List<Dept> deptList = deptService.queryDeptListByPredicate(new BooleanBuilder());
            List<String> deptIdList = deptList.stream().map(BaseEntity::getId).collect(Collectors.toList());
            // 根据部门查询对应的userList
            List<User> userList = userService.queryUserListInDeptIds(deptIdList);
            return R.success(userList);
        }


        Boolean isLeader = true;
        List<Dept> deptList = deptService.queryResponsibleDeptListByUser(user.getId());
        if (CheckTools.isNullOrEmpty(deptList)) {
            isLeader = false;
        }
        // 首先包含自己
        List<User> userList = Lists.newArrayList(user);
        // 如果是leader，扩充其他负责的部门
        if (isLeader) {
            // 查询负责部门的所有子部门
            List<Dept> allSubDeptList = new ArrayList<>();
            for (Dept dept : deptList) {
                List<Dept> subList = deptService.queryChildDeptListByParentDept(dept.getId());
                allSubDeptList.addAll(subList);
            }
            // 负责部门 + 负责部门的全部子部门
            List<Dept> targetList = new ArrayList<>();
            targetList.addAll(deptList);
            targetList.addAll(allSubDeptList);

            // 查询所有相关部门的所有用户
            List<String> deptIdList = targetList.stream().map(BaseEntity::getId).collect(Collectors.toList());
            List<User> list = userService.queryUserListInDeptIds(deptIdList);
            userList.addAll(list);
        }
        return R.success(userList);
    }


    /**
     * 查询部门mix用户多选树
     * 可指定哪些部门
     */
    @GetMapping("/queryDeptUserMixTreeList")
    public R<List<DeptUserMixTreeNodeVo>> queryDeptUserMixTreeList(String deptIds) {
        User currentRealUser = new User(new Date());
        List<DeptUserMixTreeNodeVo> mixTreeNodeList = deptBiz.queryMultipleDeptUserMixTreeList(deptIds, currentRealUser);
        return R.success(mixTreeNodeList);
    }


    // 递归将所有部门节点，置为disabled
    private void processDeptNodeDisabled(List<DeptUserMixTreeNodeVo> mixTreeNodeList) {
        for (DeptUserMixTreeNodeVo mixTreeNode : mixTreeNodeList) {
            List<DeptUserMixTreeNodeVo> childrenTreeList = mixTreeNode.getChildren();
            if (CheckTools.isNotNullOrEmpty(childrenTreeList)) {
                processDeptNodeDisabled(childrenTreeList);
            }

            // 对具体节点的处理
            if (mixTreeNode.getType().equals(11)) {
                mixTreeNode.setDisabled(true);
            }
        }
    }


    /**
     * 查询部门用户mix 单选树
     * 可指定哪些部门
     */
    @GetMapping("/queryRadioUserMixList")
    public R<List<DeptUserMixTreeNodeVo>> queryRadioUserMixList(String deptIds) {
        User currentRealUser = new User(new Date());
        List<DeptUserMixTreeNodeVo> mixTreeNodeList = deptBiz.queryMultipleDeptUserMixTreeList(deptIds, currentRealUser);
        // 将所有部门级节点，均disabled
        processDeptNodeDisabled(mixTreeNodeList);
        return R.success(mixTreeNodeList);
    }


    /**
     * 查询部门单选树
     */
    @GetMapping("/queryDeptTreeList")
    public R<List<DeptUserMixTreeNodeVo>> queryDeptTreeList(Integer isBizDept) {
        User currentRealUser = new User(new Date());
        List<DeptUserMixTreeNodeVo> mixTreeNodeList = deptBiz.queryRadioDeptUserMixTreeList(currentRealUser, isBizDept);
        return R.success(mixTreeNodeList);
    }

    /**
     * 检索所有部门
     */
    @GetMapping("/all")
    public R<List<DeptDTO>> queryAll() {
        return deptService.queryAll();
    }

    /**
     * 根据用户 ID 检索部门
     */
    @GetMapping("/queryDeptByUserId")
    public R<Dept> queryDeptByUserId(String userId) {
        return R.success(deptService.queryDeptByUserId(userId));
    }

}
