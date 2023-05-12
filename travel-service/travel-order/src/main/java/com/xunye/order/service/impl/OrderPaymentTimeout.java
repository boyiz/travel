package com.xunye.order.service.impl;


import java.util.Map;

import javax.annotation.Resource;

import com.xunye.common.handle.RedisDelayQueueHandle;
import com.xunye.order.em.PaymentTypeEnum;
import com.xunye.order.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderPaymentTimeout
 * @Description 订单支付超时处理类
 * @Author boyiz
 * @Date 2023/5/12 12:21
 * @Version 1.0
 **/
@Component
@Slf4j
public class OrderPaymentTimeout implements RedisDelayQueueHandle<Map> {

    @Resource
    private IOrderInfoService orderInfoService;

    @Override
    public void execute(Map map) {
        log.info("(收到超时订单延迟消息) {}", map);
        log.info("开始处理超时订单");
        // 订单相关，处理业务逻辑...
        // 1.调用第三方（微信，支付宝）的支付接口，查询订单是否已经支付，如果确认没支付则，调用关闭订单支付的api,并修改订单的状态为关闭，同时回滚库存数量。
        // 2.如果支付状态为已支付则需要做补偿操作，修改订单的状态为已支付，订单历史记录

        // 查询支付结果
        //String orderId = map.get("orderId").toString();
        //String orderSn = map.get("orderSn").toString();
        //String paymentType = map.get("paymentType").toString();
        //if (paymentType.equals(PaymentTypeEnum.WECHAT.getValue())) {
        //    // 向微信发起支付结果查询
        //
        //} else {
        //    // 向支付宝发起支付结果查询
        //
        //}

        log.info("处理结束...");

    }
}
