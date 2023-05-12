package com.xunye.order.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xunye.common.em.RedisDelayQueueEnum;
import com.xunye.core.tools.RedisDelayQueueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RedisDelayQueueController
 * @Description TODO
 * @Author boyiz
 * @Date 2023/5/12 13:45
 * @Version 1.0
 **/
@RestController
public class TRedisDelayQueueController {

    @Autowired
    private RedisDelayQueueUtil redisDelayQueueUtil;

    @PostMapping("/addQueue")
    public void addQueue() {
        for (int i = 0; i <100000 ; i++) {
            Map<String, String> map1 = new HashMap<>();
            map1.put("orderId", IdWorker.getTimeId());
            map1.put("remark", "订单支付超时，自动取消订单");
            redisDelayQueueUtil.addDelayQueue(map1, 10, TimeUnit.MINUTES, RedisDelayQueueEnum.ORDER_PAYMENT_TIMEOUT.getCode());

        }

        // 为了测试效果，延迟10秒钟
    }
}
