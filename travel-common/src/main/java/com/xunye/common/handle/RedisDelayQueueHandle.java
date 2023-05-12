package com.xunye.common.handle;

/**
 * @ClassName RedisDelayQueueHandle
 * @Description 定义延迟队列执行器
 * @Author boyiz
 * @Date 2023/5/12 12:20
 * @Version 1.0
 **/
public interface RedisDelayQueueHandle<T> {
    void execute(T t);
}
