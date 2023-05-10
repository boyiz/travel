package com.xunye.pay.web;

import com.xunye.core.result.R;
import com.xunye.pay.config.WxPayConfig;
import com.xunye.pay.config.WxPayConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TC
 * @Description TODO
 * @Author boyiz
 * @Date 2023/5/4 18:11
 * @Version 1.0
 **/
@RestController
@RequestMapping("/test")
public class TC {


    @Autowired
    private WxPayConfigProperties wxPayConfigProperties;

    @RequestMapping("/wxConfig")
    public R<String> test() {
       return R.success("success", wxPayConfigProperties.getAppid());
    }
}
