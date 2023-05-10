package com.xunye.auth.service;

import com.xunye.auth.dto.UserEditDTO;

/**
     * 登录授权服务接口
     */
    public interface WxAuthService {
        /**
         * 登录授权
         */
        UserEditDTO wxLogin(String openId,String sessionKey,String unionId);

    }
