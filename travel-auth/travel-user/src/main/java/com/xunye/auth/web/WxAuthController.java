package com.xunye.auth.web;

import java.util.Date;

import com.alibaba.fastjson2.JSONObject;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.xunye.auth.dto.UserDTO;
import com.xunye.auth.dto.UserEditDTO;
import com.xunye.auth.entity.User;
import com.xunye.auth.service.IUserService;
import com.xunye.auth.service.WxAuthService;
import com.xunye.auth.service.impl.WxMiniApiImpl;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.core.wx.WxConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WxAuthController
 * @Description 微信小程序登录
 * @Author boyiz
 * @Date 2021/11/7 20:34
 * @Update 2023/04/11 13:31
 * @Version 1.0
 **/
@RestController
@RequestMapping("/wx")
public class WxAuthController {

    @Value("${wxMini.appid}")
    private String appid;
    @Value("${wxMini.secret}")
    private String secret;

    private final WxAuthService wxAuthService;
    private final IUserService userService;

    public WxAuthController(WxAuthService wxAuthService, IUserService userService) {
        this.wxAuthService = wxAuthService;
        this.userService = userService;
    }

    @GetMapping(value = "/login/{code}")
    public R<UserDTO> login(@PathVariable String code) {
        if (CheckTools.isNullOrEmpty(code)) {
            return R.failure("参数校验失败，请重试");
        }
        WxMiniApiImpl wxMiniApi = new WxMiniApiImpl();
        JSONObject jsonObject = wxMiniApi.authCode2Session(appid, secret, code);
        if (CheckTools.isNullOrEmpty(jsonObject)) {
            return R.failure("微信端授权登录异常，请重试");
        }
        String openId = jsonObject.getString(WxConstant.OPEN_ID);
        String sessionKey = jsonObject.getString(WxConstant.SESSION_KEY);
        String unionId = jsonObject.getString(WxConstant.UNION_ID);
        if (StringUtils.isEmpty(openId)) {
            return R.failure("微信端授权登录异常，请重试");
        }

        UserEditDTO loginUser = wxAuthService.wxLogin(openId, sessionKey, unionId);
        UserDTO user = new UserDTO();
        BeanUtils.copyProperties(loginUser, user);
        StpUtil.login(user.getId());
        String tokenValue = StpUtil.getTokenValue();
        user.setToken(tokenValue);
        // 在登录时缓存user对象
        StpUtil.getSession().set("user", user);
        return R.success("登录成功", user);
    }

    @PutMapping("/user")
    @SaCheckLogin
    public R<UserDTO> update(@RequestBody UserEditDTO userEditDTO) {
        // 拿到当前登录用户ID
        String loginUser = StpUtil.getLoginIdAsString();
        userEditDTO.setId(loginUser);
        User user = new User(new Date());
        userService.updateUserInfo(userEditDTO,user);
        return R.success("更新成功");
    }

    @GetMapping("/user")
    @SaCheckLogin
    public R<UserEditDTO> getUserInfo() {
        // 拿到当前登录用户ID
        String loginUser = StpUtil.getLoginIdAsString();
        return R.success(userService.queryUserInfoById(loginUser));
    }
}
