package com.xunye.pay.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.alipay.api.AlipayConstants.CHARSET;
import static com.alipay.api.AlipayConstants.SIGN_TYPE;

/**
 * @ClassName AliPayConfig
 * @Description TODO
 * @Author boyiz
 * @Date 2023/5/5 18:19
 * @Version 1.0
 **/

@EnableConfigurationProperties(AliPayConfigProperties.class)
@Component
public class AliPayConfig {

    @Autowired
    private AliPayConfigProperties aliPayConfigProperties;

    public AlipayClient initAlipayConfig() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(aliPayConfigProperties.getServerUrl());
        alipayConfig.setAppId(aliPayConfigProperties.getAppid());
        alipayConfig.setPrivateKey(aliPayConfigProperties.getPrivateKey());
        alipayConfig.setAlipayPublicKey(aliPayConfigProperties.getAlipayPublicKey());
        alipayConfig.setFormat("json");
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(SIGN_TYPE);
        //构造client
        return new DefaultAlipayClient(alipayConfig);
    }
}




