package com.xunye.auth.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.util.StrUtil;
import com.xunye.auth.consts.AuthConstants;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.dto.AuthDTO;
import com.xunye.auth.dto.RoleDTO;
import com.xunye.auth.dto.RoleEditDTO;
import com.xunye.auth.em.DataAuthTypeEnum;
import com.xunye.auth.entity.Role;
import com.xunye.auth.service.IAuthService;
import com.xunye.auth.service.IRoleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/user/role")
@SaCheckRole("super-admin")
public class RoleController extends BaseController {

    @Resource
    private IRoleService roleService;

    @Resource
    private IAuthService authService;

    /**
     * 角色列表查询接口
     */
    @GetMapping
    //@ApiAuth("auth:role:list")
    //@PreAuthorize("hasAuthority('auth:role:list')")
    public R<List<RoleDTO>> list(@QuerydslPredicate(root = Role.class) Predicate predicate, Pageable pageable) {
        return roleService.queryRoleListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 角色创建接口
     */
    @PostMapping
    //@ApiAuth("auth:role:create")
    public R<String> create(@RequestBody RoleEditDTO addDTO) {
        return roleService.createRole(addDTO);
    }

    /**
     * 角色更新接口
     */
    @PutMapping
    //@ApiAuth("auth:role:update")
    public R<String> update(@RequestBody RoleEditDTO editDto) {
        return roleService.updateRole(editDto);
    }

    /**
     * 角色删除接口
     */
    @DeleteMapping("/{id}")
    //@ApiAuth("auth:role:delete")
    public R<Boolean> delete(@PathVariable String id) {
        return roleService.deleteRole(id);
    }

    /**
     * 角色批量删除接口

     */
    @DeleteMapping("/batch/{ids}")
    //@ApiAuth("auth:role:deleteBatch")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        return roleService.deleteRoleBatch(idList);
    }

    /**
     * 角色详情接口
     */
    @GetMapping("/{id}")
    //@ApiAuth("auth:role:detail")
    public R<RoleEditDTO> detail(@PathVariable String id) {
        return roleService.queryRoleById(id);
    }

    /**
     * 角色数据导出接口
     */
    @GetMapping("/export")
    //@ApiAuth("auth:role:export")
    public void export(@QuerydslPredicate(root = Role.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "角色列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), RoleDTO.class)
                .sheet("Sheet1")
                .doWrite(roleService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 角色表单数据初始化
     */
    @GetMapping("/initRoleFormData")
    public R<Map<String, Object>> initRoleFormData() {
        Map<String, Object> resultMap = new HashMap<>();

        // 权限TreeList
        List<AuthDTO> authTreeList = authService.queryAuthTreeList(new BooleanBuilder());
        resultMap.put(AuthConstants.AUTH_TREE_LIST_KEY, authTreeList);

        return R.success(resultMap);
    }

    /**
     * 更新数据权限
     *
     * @param roleEditDTO
     * @return
     */
    @PutMapping("/updateDataAuth")
    public R<Boolean> updateDataAuth(@RequestBody RoleEditDTO roleEditDTO) {
        if (CheckTools.isNullOrEmpty(roleEditDTO.getId())) {
            return R.failure("未指定角色ID");
        }
        if (CheckTools.isNullOrEmpty(roleEditDTO.getDataAuthType())) {
            return R.failure("数据权限类型为必填");
        }
        if (DataAuthTypeEnum.CUSTOMIZE.getValue().equals(roleEditDTO.getDataAuthType())) {
            if (CheckTools.isNullOrEmpty(roleEditDTO.getDataAuthType())) {
                return R.failure("未分配数据权限范围");
            }
        }
        return roleService.updateDataAuth(roleEditDTO.getId(), roleEditDTO.getDataAuthType(), roleEditDTO.getDataAuths());
    }


}
