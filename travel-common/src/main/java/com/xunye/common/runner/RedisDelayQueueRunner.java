package com.xunye.common.runner;

import javax.annotation.Resource;

import cn.hutool.extra.spring.SpringUtil;
import com.xunye.common.em.RedisDelayQueueEnum;
import com.xunye.common.handle.RedisDelayQueueHandle;
import com.xunye.core.tools.RedisDelayQueueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @ClassName RedisDelayQueueRunner
 * @Description 创建延迟队列消费线程，项目启动完成后开启
 * @Author boyiz
 * @Date 2023/5/12 12:38
 * @Version 1.0
 **/
@Slf4j
@Component
public class RedisDelayQueueRunner implements CommandLineRunner {

    @Autowired
    private RedisDelayQueueUtil redisDelayQueueUtil;
    @Resource
    private ThreadPoolTaskExecutor asyncExecutor;

    //ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 50, 30, TimeUnit.SECONDS,
    //    new LinkedBlockingQueue<Runnable>(1000), Executors.defaultThreadFactory());

    @Override
    public void run(String... args) {
        asyncExecutor.execute(() -> {
            while (true) {
                try {
                    RedisDelayQueueEnum[] queueEnums = RedisDelayQueueEnum.values();
                    for (RedisDelayQueueEnum queueEnum : queueEnums) {
                        Object value = redisDelayQueueUtil.getDelayQueue(queueEnum.getCode());
                        if (value != null) { //orderPaymentTimeout
                            RedisDelayQueueHandle redisDelayQueueHandle = SpringUtil.getBean(queueEnum.getBeanId());
                            redisDelayQueueHandle.execute(value);
                        }
                    }
                } catch (InterruptedException e) {
                    log.error("Redis延迟队列异常中断 {}", e.getMessage());
                }
            }
        });
        log.info("Redis延迟队列启动成功!");
    }
}
