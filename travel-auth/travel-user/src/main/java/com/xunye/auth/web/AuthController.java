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

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.core.util.StrUtil;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.auth.consts.AuthConstants;
import com.xunye.auth.dto.AuthDTO;
import com.xunye.auth.dto.AuthEditDTO;
import com.xunye.auth.em.AuthTypeEnum;
import com.xunye.auth.entity.Auth;
import com.xunye.auth.entity.QAuth;
import com.xunye.auth.service.IAuthService;
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
@RequestMapping("/user/auth")
@SaCheckLogin
@SaCheckRole("super-admin")
public class AuthController extends BaseController {

    @Resource
    private IAuthService authService;

    /**
     * 权限列表查询接口
     */
    @GetMapping
    public R<List<AuthDTO>> list(@QuerydslPredicate(root = Auth.class) Predicate predicate, Pageable pageable) {
        return R.success(authService.queryAuthTreeList(predicate));
    }

    /**
     * 权限创建接口
     */
    @PostMapping
    public R<String> create(@RequestBody AuthEditDTO addDTO) {
        return authService.createAuth(addDTO);
    }

    /**
     * 权限更新接口
     */
    @PutMapping
    public R<String> update(@RequestBody AuthEditDTO editDto) {
        return authService.updateAuth(editDto);
    }

    /**
     * 权限删除接口
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return authService.deleteAuth(id);
    }

    /**
     * 权限批量删除接口

     */
    @DeleteMapping("/batch/{ids}")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        return authService.deleteAuthBatch(idList);
    }

    /**
     * 权限详情接口
     */
    @GetMapping("/{id}")
    public R<AuthEditDTO> detail(@PathVariable String id) {
        return authService.queryAuthById(id);
    }

    /**
     * 权限数据导出接口
     */
    @GetMapping("/export")
    public void export(@QuerydslPredicate(root = Auth.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "权限列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), AuthDTO.class)
                .sheet("Sheet1")
                .doWrite(authService.export(PredicateWrapper.of(predicate)));
    }

    /**
     * 权限表单数据初始化
     */
    @GetMapping("/initAuthFormData")
    public R<Map<String, Object>> initAuthFormData() {
        Map<String, Object> resultMap = new HashMap<>();
        // 权限TreeList
        List<AuthDTO> authTreeList = authService.queryAuthTreeList(QAuth.auth.authType.ne(AuthTypeEnum.BTN.getValue()));
        resultMap.put(AuthConstants.AUTH_TREE_LIST_KEY, authTreeList);
        return R.success(resultMap);
    }


}
