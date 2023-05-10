//package com.xunye.auth.web;
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.excel.EasyExcel;
//import com.xunye.core.result.R;
////import com.xunye.core.annotation.ApiAuth;
//import com.xunye.core.base.BaseController;
//import com.xunye.core.model.PredicateWrapper;
//import com.xunye.auth.dto.UserRoleDTO;
//import com.xunye.auth.dto.UserRoleEditDTO;
//import com.xunye.auth.entity.UserRole;
//import com.xunye.auth.service.IUserRoleService;
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
//@RequestMapping("/auth/user-role")
//public class UserRoleController extends BaseController {
//
//    @Resource
//    private IUserRoleService userRoleService;
//    @Resource
//    private SecurityTools securityTools;
//    /**
//     * 用户角色实体列表查询接口
//     *
//     * @param predicate 条件谓语
//     * @param pageable  分页参数
//     */
//    @GetMapping
//    //@ApiAuth("auth:user-role:list")
//    public R<List<UserRoleDTO>> list(@QuerydslPredicate(root = UserRole.class) Predicate predicate,
//                                         Pageable pageable) {
//        return userRoleService.queryUserRoleListByPage(PredicateWrapper.of(predicate), pageable);
//    }
//
//    /**
//     * 用户角色实体创建接口
//     *
//     * @param addDTO 创建信息
//     */
//    @PostMapping
//    //@ApiAuth("auth:user-role:create")
//    public R<String> create(@RequestBody UserRoleEditDTO addDTO) {
//        User currentRealUser = securityTools.getLoginUser("");
//
//        // 创建并返回dbId
//        String dbId = userRoleService.createUserRole(addDTO, currentRealUser);
//        return R.success("创建成功",dbId);
//    }
//
//    /**
//     * 用户角色实体更新接口
//     *
//     * @param editDto 更新信息
//     */
//    @PutMapping
//    //@ApiAuth("auth:user-role:update")
//    public R<Boolean> update(@RequestBody UserRoleEditDTO editDto) {
//        // 基础校验
//        if (CheckTools.isNullOrEmpty(editDto.getId())) {
//            return R.failure("用户角色实体id不存在" );
//        }
//
//        User currentRealUser = securityTools.getLoginUser("");
//
//        // 更新用户角色实体
//        userRoleService.updateUserRole(editDto, currentRealUser);
//        return R.success("更新成功", true);
//    }
//
//    /**
//     * 用户角色实体删除接口
//     *
//     * @param id ID
//     */
//    @DeleteMapping("/{id}")
//    //@ApiAuth("auth:user-role:delete")
//    public R<Boolean> delete(@PathVariable String id) {
//        // 获取当前操作用户
//        User currentRealUser = securityTools.getLoginUser("");
//        // 调用service删除
//        userRoleService.deleteUserRole(id,currentRealUser);
//        return R.success("删除成功", true);
//    }
//
//    /**
//     * 用户角色实体批量删除接口
//     *
//     * @param ids id集合
//     */
//    @DeleteMapping("/batch/{ids}")
//    //@ApiAuth("auth:user-role:deleteBatch")
//    public R<Boolean> deleteBatch(@PathVariable String ids) {
//        // 参数预处理
//        if (StrUtil.isEmptyIfStr(ids)) return R.failure();
//
//        // 获取当前操作用户
//        User currentRealUser = securityTools.getLoginUser("");
//        // 调用service删除
//        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
//        userRoleService.deleteUserRoleBatch(idList,currentRealUser);
//
//        return R.success("批量删除成功", true);
//    }
//
//    /**
//     * 根据ID查询用户角色实体接口
//     *
//     * @param id ID
//     */
//    @GetMapping("/{id}")
//    //@ApiAuth("auth:user-role:queryById")
//    public R<UserRoleEditDTO> queryUserRoleById(@PathVariable String id) {
//        UserRoleEditDTO userRoleEditDTO = userRoleService.queryUserRoleById(id);
//        return R.success("查询成功",userRoleEditDTO);
//    }
//
//    /**
//     * 用户角色实体数据导出接口
//     *
//     * @param predicate 条件谓语
//     * @param response  相应
//     * @throws IOException IO异常
//     */
//    @GetMapping("/export")
//    //@ApiAuth("auth:user-role:export")
//    public void export(@QuerydslPredicate(root = UserRole.class) Predicate predicate, HttpServletResponse response) throws IOException {
//        String fileName = "用户角色实体列表导出.xls";
//        exportExcelBefore(response, fileName);
//        EasyExcel.write(response.getOutputStream(), UserRoleDTO.class)
//                .sheet("Sheet1")
//                .doWrite(userRoleService.export(PredicateWrapper.of(predicate)));
//    }
//
//    /**
//     * 用户角色实体表单数据初始化
//     *
//     * @return Map
//     */
//    @GetMapping("/initUserRoleFormData")
//    public R<Map<String, Object>> initUserRoleFormData() {
//        Map<String, Object> resultMap = new HashMap<>();
//        // TODO: 数据填充
//        return R.success(resultMap);
//    }
//
//}
