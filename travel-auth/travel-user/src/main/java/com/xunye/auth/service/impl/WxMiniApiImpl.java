package com.xunye.auth.service.impl;

import com.alibaba.fastjson2.JSONObject;

import com.xunye.auth.service.WxMiniApi;
import com.xunye.core.wx.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 微信小程序Api接口实现类
 *
 * @date 2021-11-07
 */

@Service
public class WxMiniApiImpl implements WxMiniApi {

    private  final Logger log = LoggerFactory.getLogger(WxMiniApiImpl.class);

    @Override
    public JSONObject authCode2Session(String appid, String secret, String jsCode) {

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=authorization_code";
        String str = WeChatUtil.httpRequest(url, "GET", null);
        log.info("api/wx-mini/getSessionKey:" + str);
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            return JSONObject.parseObject(str);
        }

    }
}
