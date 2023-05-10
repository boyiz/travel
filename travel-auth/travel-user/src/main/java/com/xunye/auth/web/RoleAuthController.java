//package com.xunye.auth.web;
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.excel.EasyExcel;
//import com.xunye.core.result.R;
////import com.xunye.core.annotation.ApiAuth;
//import com.xunye.core.base.BaseController;
//import com.xunye.core.model.PredicateWrapper;
//import com.xunye.auth.dto.RoleAuthDTO;
//import com.xunye.auth.dto.RoleAuthEditDTO;
//import com.xunye.auth.entity.RoleAuth;
//import com.xunye.auth.service.IRoleAuthService;
//import com.querydsl.core.types.Predicate;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.querydsl.binding.QuerydslPredicate;
//import org.springframework.web.bind.annotation.*;
//
//import com.xunye.auth.entity.User;
//import com.xunye.core.tools.CheckTools;
//import com.xunye.auth.tool.SecurityTools;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import java.util.Map;
//import java.util.HashMap;
//
//@RestController
//@RequestMapping("/auth/role-auth")
//public class RoleAuthController extends BaseController {
//
//    @Resource
//    private IRoleAuthService roleAuthService;
//    @Resource
//    private SecurityTools securityTools;
//    /**
//     * 角色权限实体列表查询接口
//     *
//     * @param predicate 条件谓语
//     * @param pageable  分页参数
//     */
//    @GetMapping
//    //@ApiAuth("auth:role-auth:list")
//    public R<List<RoleAuthDTO>> list(@QuerydslPredicate(root = RoleAuth.class) Predicate predicate,
//                                         Pageable pageable) {
//        return roleAuthService.queryRoleAuthListByPage(PredicateWrapper.of(predicate), pageable);
//    }
//
//    /**
//     * 角色权限实体创建接口
//     *
//     * @param addDTO 创建信息
//     */
//    @PostMapping
//    //@ApiAuth("auth:role-auth:create")
//    public R<String> create(@RequestBody RoleAuthEditDTO addDTO) {
//        User currentRealUser = securityTools.getLoginUser("");
//
//        // 创建并返回dbId
//        String dbId = roleAuthService.createRoleAuth(addDTO, currentRealUser);
//        return R.success("创建成功",dbId);
//    }
//
//    /**
//     * 角色权限实体更新接口
//     *
//     * @param editDto 更新信息
//     */
//    @PutMapping
//    //@ApiAuth("auth:role-auth:update")
//    public R<Boolean> update(@RequestBody RoleAuthEditDTO editDto) {
//        // 基础校验
//        if (CheckTools.isNullOrEmpty(editDto.getId())) {
//            return R.failure("角色权限实体id不存在" );
//        }
//
//        User currentRealUser = securityTools.getLoginUser("");
//
//        // 更新角色权限实体
//        roleAuthService.updateRoleAuth(editDto, currentRealUser);
//        return R.success("更新成功", true);
//    }
//
//    /**
//     * 角色权限实体删除接口
//     *
//     * @param id ID
//     */
//    @DeleteMapping("/{id}")
//    //@ApiAuth("auth:role-auth:delete")
//    public R<Boolean> delete(@PathVariable String id) {
//        // 获取当前操作用户
//        User currentRealUser = securityTools.getLoginUser("");
//        // 调用service删除
//        roleAuthService.deleteRoleAuth(id,currentRealUser);
//        return R.success("删除成功", true);
//    }
//
//    /**
//     * 角色权限实体批量删除接口
//     *
//     * @param ids id集合
//     */
//    @DeleteMapping("/batch/{ids}")
//    //@ApiAuth("auth:role-auth:deleteBatch")
//    public R<Boolean> deleteBatch(@PathVariable String ids) {
//        // 参数预处理
//        if (StrUtil.isEmptyIfStr(ids)) return R.failure();
//
//        // 获取当前操作用户
//        User currentRealUser = securityTools.getLoginUser("");
//        // 调用service删除
//        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
//        roleAuthService.deleteRoleAuthBatch(idList,currentRealUser);
//
//        return R.success("批量删除成功", true);
//    }
//
//    /**
//     * 根据ID查询角色权限实体接口
//     *
//     * @param id ID
//     */
//    @GetMapping("/{id}")
//    //@ApiAuth("auth:role-auth:queryById")
//    public R<RoleAuthEditDTO> queryRoleAuthById(@PathVariable String id) {
//        RoleAuthEditDTO roleAuthEditDTO = roleAuthService.queryRoleAuthById(id);
//        return R.success("查询成功",roleAuthEditDTO);
//    }
//
//    /**
//     * 角色权限实体数据导出接口
//     *
//     * @param predicate 条件谓语
//     * @param response  相应
//     * @throws IOException IO异常
//     */
//    @GetMapping("/export")
//    //@ApiAuth("auth:role-auth:export")
//    public void export(@QuerydslPredicate(root = RoleAuth.class) Predicate predicate, HttpServletResponse response) throws IOException {
//        String fileName = "角色权限实体列表导出.xls";
//        exportExcelBefore(response, fileName);
//        EasyExcel.write(response.getOutputStream(), RoleAuthDTO.class)
//                .sheet("Sheet1")
//                .doWrite(roleAuthService.export(PredicateWrapper.of(predicate)));
//    }
//
//    /**
//     * 角色权限实体表单数据初始化
//     *
//     * @return Map
//     */
//    @GetMapping("/initRoleAuthFormData")
//    public R<Map<String, Object>> initRoleAuthFormData() {
//        Map<String, Object> resultMap = new HashMap<>();
//        // TODO: 数据填充
//        return R.success(resultMap);
//    }
//
//}
