package com.xunye.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName RedisDelayQueueEnum
 * @Description
 * @Author boyiz
 * @Date 2023/5/12 12:17
 * @Version 1.0
 **/
@Getter

@AllArgsConstructor
public enum RedisDelayQueueEnum {

    ORDER_PAYMENT_TIMEOUT("ORDER_PAYMENT_TIMEOUT", "超时订单自动关闭队列", "orderPaymentTimeout"),
    ;

    /**
     * 延迟队列 Redis Key
     */
    private String code;

    /**
     * 中文描述
     */
    private String name;

    /**
     * 延迟队列具体业务实现的 Bean
     * 可通过 Spring 的上下文获取
     */
    private String beanId;
    }
