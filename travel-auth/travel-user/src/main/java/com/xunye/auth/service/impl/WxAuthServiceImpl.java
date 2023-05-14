package com.xunye.auth.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import com.xunye.auth.dto.UserEditDTO;
import com.xunye.auth.entity.User;
import com.xunye.auth.service.ISysUserService;
import com.xunye.auth.service.WxAuthService;

import com.xunye.core.tools.CheckTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName WxAuthServiceImpl
 * @Description 授权登录接口实现类
 * @Author boyiz
 * @Date 2021/11/7 18:10
 * @Update 2023/04/11 16:26
 * @Version 1.0
 **/

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WxAuthServiceImpl implements WxAuthService {

    @Resource
    private ISysUserService userService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserEditDTO wxLogin(String openId, String sessionKey, String unionId) {
        //判断用户表中是否存在该用户，不存在则进行解密得到用户信息，并进行新增用户
        UserEditDTO userEditDTO = userService.queryUserInfoByWxOpenid(openId);
        if (CheckTools.isNotNullOrEmpty(userEditDTO)) {
            return userEditDTO;
        }

        userEditDTO.setUserOpenid(openId);
        userEditDTO.setBirthday(new Date());

        User operatorUser = new User();
        operatorUser.setCreateBy(openId);
        userService.createUserInfo(userEditDTO,operatorUser);
        return userEditDTO;
    }

}