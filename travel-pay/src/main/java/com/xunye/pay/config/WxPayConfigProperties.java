package com.xunye.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName WxPayConfigProperties
 * @Description 微信支付参数
 * @Author boyiz
 * @Date 2023/5/4 16:25
 * @Version 1.0
 **/

@ConfigurationProperties(prefix = "wxpay")
@Component
@Data
public class WxPayConfigProperties {

    private String merchantId;
    private String merchantSerialNo;
    private String privateKeyPath;
    private String apiV3Key;
    private String appid;
    private String domain;
    private String notifyDomain;
}
