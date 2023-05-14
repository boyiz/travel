package com.xunye.auth.web;

import java.util.List;

import javax.annotation.Resource;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.querydsl.core.types.Predicate;
import com.xunye.auth.dto.CustomerDTO;
import com.xunye.auth.dto.CustomerEditDTO;
import com.xunye.auth.entity.User;
import com.xunye.auth.service.ICustomerService;
import com.xunye.auth.tool.SecurityTools;
import com.xunye.core.model.PredicateWrapper;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CustomerController
 * @Description Customer用户管理
 * @Author boyiz
 * @Date 2023/5/14 15:14
 * @Version 1.0
 **/
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Resource
    private ICustomerService customerService;
    @Resource
    private SecurityTools securityTools;


    /**
     * 用户实体列表查询接口
     *
     * @param predicate 条件谓语
     * @param pageable  分页参数
     */
    @GetMapping("/all")
    @SaCheckRole("admin")
    public R<List<CustomerDTO>> allList(@QuerydslPredicate(root = User.class) Predicate predicate,
        Pageable pageable) {
        return customerService.queryUserInfoListByPage(PredicateWrapper.of(predicate), pageable);
    }

    /**
     * 用户实体更新接口
     *
     * @param editDto 更新信息
     */
    @PutMapping
    @SaCheckRole("admin")
    public R<Boolean> update(@RequestBody CustomerEditDTO editDto) {
        // 基础校验
        if (CheckTools.isNullOrEmpty(editDto.getId())) {
            return R.failure("用户实体id不存在" );
        }
        User currentRealUser = securityTools.getLoginUser(StpUtil.getLoginIdAsString());
        // 更新用户实体
        customerService.updateUserInfo(editDto, currentRealUser);
        return R.success("更新成功", true);
    }

    /**
     * 根据ID查询用户实体接口
     *
     * @param id ID
     */
    @GetMapping("/{id}")
    @SaCheckRole("admin")
    public R<CustomerEditDTO> queryUserInfoById(@PathVariable String id) {
        CustomerEditDTO customerEditDTO = customerService.queryUserInfoById(id);
        return R.success("查询成功", customerEditDTO);
    }

}
