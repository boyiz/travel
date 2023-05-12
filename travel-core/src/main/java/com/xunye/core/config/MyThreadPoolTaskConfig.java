package com.xunye.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @ClassName MyThreadPoolTaskConfig
 * @Description TODO
 * @Author boyiz
 * @Date 2023/5/12 13:35
 * @Version 1.0
 **/
@Slf4j
@EnableAsync
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
@Configuration
public class MyThreadPoolTaskConfig implements AsyncConfigurer {

    @Autowired
    private ThreadPoolConfigProperties pool;

    @Bean(name = "asyncExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        threadPoolTaskExecutor.setCorePoolSize(pool.getCoreSize());
        // 最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(pool.getMaxSize());
        // 阻塞队列容量
        threadPoolTaskExecutor.setQueueCapacity(pool.getQueueCapacity());
        // 待任务在关机时完成--表明等待所有线程执行完
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程名称前缀
        //threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        // 设置拒绝策略
        //threadPoolTaskExecutor.setRejectedExecutionHandler(getRejectedExecutionHandler(rejectedExecutionHandler));

        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, obj) -> {
            log.error("[ThreadPool Exception]：Message [{}], Method [{}]", throwable.getMessage(), method.getName());
            for (Object param : obj) {
                log.error("Parameter value [{}] ", param);
            }
        };
    }

    /**
     * 根据传入的参数获取拒绝策略
     *
     * @param rejectedName 拒绝策略名，比如 CallerRunsPolicy
     * @return RejectedExecutionHandler 实例对象，没有匹配的策略时，默认取 CallerRunsPolicy 实例
     */
    public RejectedExecutionHandler getRejectedExecutionHandler(String rejectedName) {
        Map<String, RejectedExecutionHandler> rejectedExecutionHandlerMap = new HashMap<>(16);
        rejectedExecutionHandlerMap.put("CallerRunsPolicy", new ThreadPoolExecutor.CallerRunsPolicy());
        rejectedExecutionHandlerMap.put("AbortPolicy", new ThreadPoolExecutor.AbortPolicy());
        rejectedExecutionHandlerMap.put("DiscardPolicy", new ThreadPoolExecutor.DiscardPolicy());
        rejectedExecutionHandlerMap.put("DiscardOldestPolicy", new ThreadPoolExecutor.DiscardOldestPolicy());
        return rejectedExecutionHandlerMap.getOrDefault(rejectedName, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
