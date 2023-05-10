package com.xunye.pay.config;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.partnerpayments.jsapi.JsapiServiceExtension;
import com.xunye.core.config.ThreadPoolConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @ClassName WxPayConfig
 * @Description 微信支付配置
 * @Author boyiz
 * @Date 2023/5/4 16:31
 * @Version 1.0
 **/

@EnableConfigurationProperties(WxPayConfigProperties.class)
@Component
public class WxPayConfig {

    @Autowired
    private WxPayConfigProperties wxPayConfigProperties;

    public JsapiServiceExtension initWxPayConfig() {
        // 初始化商户配置
        RSAConfig config =
            new RSAConfig.Builder()
                .merchantId(wxPayConfigProperties.getMerchantId())
                // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                .privateKeyFromPath(wxPayConfigProperties.getPrivateKeyPath())
                .merchantSerialNumber(wxPayConfigProperties.getMerchantSerialNo())
                .wechatPayCertificatesFromPath("/X/X/X")
                .build();
        // 初始化服务
        JsapiServiceExtension service =
            new JsapiServiceExtension.Builder()
                .config(config)
                .signType("RSA") // 不填则默认为RSA
                .build();
        return service;
    }

}
