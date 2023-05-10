package com.xunye.pay.config;

import com.alipay.api.AlipayConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName AliPayConfigProperties
 * @Description 支付宝支付参数
 * @Author boyiz
 * @Date 2023/5/4 16:26
 * @Version 1.0
 **/

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AliPayConfigProperties {

    private String serverUrl;
    private String appid;
    private String privateKey;
    private String alipayPublicKey;

    //private String signType;
}
