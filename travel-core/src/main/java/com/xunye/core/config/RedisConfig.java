package com.xunye.core.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson2.JSON;

import com.xunye.core.tools.CheckTools;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        //序列化
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        //开启事务支持
//        template.setEnableTransactionSupport(true);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


    //@Bean
    //public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
    //    // 针对1.2.25之后版本 autoType的修复
    //    // link：https://github.com/alibaba/fastjson/wiki/enable_autotype
    //    // ** 没有使用全局的解析配置，重写了fastjson的序列化器，内置ParserConfig，并配置security autoType的白名单
    //    return new CustomGenericFastJsonRedisSerializer();
    //}


    /**
     * Value 序列化
     *
     * @param <T>
     * @author /
     */
    class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
        private Class<T> clazz;

        FastJsonRedisSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }

        @Override
        public byte[] serialize(T t) {
            return JSON.toJSONString(t).getBytes(Charset.forName("UTF-8"));
        }

        @Override
        public T deserialize(byte[] bytes) {
            if (bytes==null){
                return null;
            }
            if (bytes.length <= 0) {
                return null;
            }
            String str = new String(bytes, Charset.forName("UTF-8"));
            return JSON.parseObject(str, clazz);
        }
    }

    /**
     * 重写序列化器
     *
     * @author /
     */
    class StringRedisSerializer implements RedisSerializer<Object> {

        private final Charset charset;

        StringRedisSerializer() {
            this(StandardCharsets.UTF_8);
        }

        private StringRedisSerializer(Charset charset) {
            this.charset = charset;
        }

        @Override
        public String deserialize(byte[] bytes) {
            return (bytes == null ? null : new String(bytes, charset));
        }

        @Override
        public byte[] serialize(Object object) {
            String string = JSON.toJSONString(object);
            if (CheckTools.isNullOrEmpty(string)) {
                return null;
            }
            string = string.replace("\"", "");
            return string.getBytes(charset);
        }
    }
}
