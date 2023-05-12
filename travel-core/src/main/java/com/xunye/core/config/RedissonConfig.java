package com.xunye.core.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RedissonConfig
 * @Description Redisson配置类
 * @Author boyiz
 * @Date 2023/5/12 11:59
 * @Version 1.0
 **/
@Configuration
public class RedissonConfig {

    @Value("${redisson.config.nodeAddresses}")
    private String nodeAddresses;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress(nodeAddresses);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
