package com.xunye.auth.web;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;

import com.querydsl.core.types.ExpressionUtils;
import com.xunye.auth.dto.UserDTO;
import com.xunye.auth.dto.UserEditDTO;
import com.xunye.auth.entity.QUser;
import com.xunye.auth.entity.User;
import com.xunye.auth.tool.SecurityTools;
import com.xunye.core.base.BaseController;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.auth.service.IUserService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@SaCheckLogin
@SaCheckRole("admin")
public class UserController extends BaseController {

    @Resource
    private IUserService userInfoService;
    @Resource
    private SecurityTools securityTools;

    /**
     * 用户实体列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping("/all")
    @SaCheckRole("super-admin")
    public R<List<UserDTO>> allList(@QuerydslPredicate(root = User.class) Predicate predicate,
                                         Pageable pageable) {
        return userInfoService.queryUserInfoListByPage(PredicateWrapper.of(predicate), pageable);
    }

    //@GetMapping
    //public R<List<UserDTO>> list(@QuerydslPredicate(root = User.class) Predicate predicate,
    //    Pageable pageable) {
    //    QUser qUser = QUser.user;
    //    predicate = ExpressionUtils.and(predicate, qUser.id.eq(StpUtil.getLoginIdAsString()));
    //    return userInfoService.queryUserInfoListByPage(PredicateWrapper.of(predicate), pageable);
    //}

    /**
     * 用户实体创建接口
     *
     * @param addDTO 创建信息
     */
    @PostMapping
    @SaCheckRole("super-admin")
    public R<String> create(@RequestBody UserEditDTO addDTO) {
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 创建并返回dbId
        String dbId = userInfoService.createUserInfo(addDTO, currentRealUser);
        return R.success("创建成功",dbId);
    }

    /**
     * 用户实体更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    public R<Boolean> update(@RequestBody UserEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("用户实体id不存在" );
        }

        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());

        // 更新用户实体
        userInfoService.updateUserInfo(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 用户实体删除接口
     *
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        // 获取当前操作用户
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 调用service删除
        userInfoService.deleteUserInfo(id,currentRealUser);
        return R.success("删除成功", true);
    }

    /**
     * 用户实体批量删除接口
     *
     * @param ids id集合
     */
    @DeleteMapping("/batch/{ids}")
    public R<Boolean> deleteBatch(@PathVariable String ids) {
        // 参数预处理
        if (StrUtil.isEmptyIfStr(ids)) return R.failure();

        // 获取当前操作用户
        User currentRealUser = new User();
        // 调用service删除
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        userInfoService.deleteUserInfoBatch(idList,currentRealUser);

        return R.success("批量删除成功", true);
    }

    /**
     * 根据ID查询用户实体接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    public R<UserEditDTO> queryUserInfoById(@PathVariable String id) {
        UserEditDTO userEditDTO = userInfoService.queryUserInfoById(id);
        return R.success("查询成功", userEditDTO);
    }

    /**
     * 用户实体数据导出接口
     *
     * @param predicate 条件谓语
     * @param response  相应
     * @throws IOException IO异常
     */
    @GetMapping("/export")
    public void export(@QuerydslPredicate(root = User.class) Predicate predicate, HttpServletResponse response) throws IOException {
        String fileName = "用户实体列表导出.xls";
        exportExcelBefore(response, fileName);
        EasyExcel.write(response.getOutputStream(), UserDTO.class)
                .sheet("Sheet1")
                .doWrite(userInfoService.export(PredicateWrapper.of(predicate)));
    }



}
