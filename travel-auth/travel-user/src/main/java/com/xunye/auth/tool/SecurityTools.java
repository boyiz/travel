package com.xunye.auth.tool;

import com.alibaba.fastjson2.JSON;

import com.xunye.auth.dto.UserDTO;
import com.xunye.auth.entity.User;
import com.xunye.auth.service.ISysUserService;
import com.xunye.auth.unserialize.UserUnserializeJsonRootEntity;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.tools.CheckTools;
import com.xunye.core.tools.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName SecurityTools
 * @Description 登录信息工具类
 * @Author boyiz
 * @Date 2023/4/20 18:08
 * @Version 1.0
 **/
@Component
public class SecurityTools {

    @Value("${sa-token.session-prefix}")
    private String SESSION_PREFIX;

    private final RedisUtil redisUtil;
    private final ISysUserService userService;

    public SecurityTools(RedisUtil redisUtil, ISysUserService userService) {
        this.redisUtil = redisUtil;
        this.userService = userService;
    }

    public User getLoginUser(String userId) {
        Object o = redisUtil.get(SESSION_PREFIX + ":" + userId);
        UserUnserializeJsonRootEntity unserializeJsonRootEntity = JSON.parseObject(o.toString(),
            UserUnserializeJsonRootEntity.class);
        UserDTO userDTO = unserializeJsonRootEntity.getDataMap().getUser();
        if (CheckTools.isNullOrEmpty(userDTO.getId())) {
            throw new BusinessException("用户信息获取异常，请重新登录");
        }
        return userService.getMapper().toEntity(userDTO);
    }

}
