package com.xunye.pay.web;

import com.xunye.core.result.R;
import com.xunye.order.dto.OrderInfoEditDTO;
import com.xunye.order.service.IOrderInfoService;
import com.xunye.pay.config.AliPayConfig;
import com.xunye.pay.config.WxPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AliPayController
 * @Description 支付宝支付Controller
 * @Author boyiz
 * @Date 2023/5/4 18:35
 * @Version 1.0
 **/

@RestController
@RequestMapping("/alipay")
public class AliPayController {

    @Autowired
    private IOrderInfoService orderService;

    @Autowired
    private AliPayConfig aliPayConfig;

    @GetMapping(value = "/{orderSn}")
    public R<String> aliPayOrder(@PathVariable("orderSn") String orderSn) {

        OrderInfoEditDTO orderInfoEditDTO = orderService.queryOrderInfoByOrderSn(orderSn);
        //aliPayConfig.initAlipayConfig().

        return R.success("");
    }
}
