package com.xunye;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName com.xunye.TravelApplication
 * @Description 项目启动类
 * @Author boyiz
 * @Date 2023/4/9 22:33
 * @Version 1.0
 **/
@SpringBootApplication
@EnableJpaRepositories
public class TravelApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("当前时间: " + df.format(new Date()));
        System.out.println(" The system started successfully ！！！ \n" +
            "___   ___  __    __  .__   __. ____    ____  _______\n" +
            "\\  \\ /  / |  |  |  | |  \\ |  | \\   \\  /   / |   ____|\n" +
            " \\  V  /  |  |  |  | |   \\|  |  \\   \\/   /  |  |___\n" +
            "  >   <   |  |  |  | |       |   \\_    _/   |   ___|\n" +
            " /  .  \\  |  '__'  | |  |\\   |     |  |     |  |____\n" +
            "/__/ \\__\\  \\______/  |__| \\__|     |__|     |_______|" +
            "\n");
    }

    /**
     * 借用SpringSecurity的加密
     * 将加密工具类加入IOC容器中
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Mybatis-Plus分页插件
     */

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
