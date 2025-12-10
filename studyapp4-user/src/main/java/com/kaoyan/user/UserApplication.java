package com.kaoyan.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户服务启动类
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.kaoyan.user", "com.kaoyan.common"})
@MapperScan("com.kaoyan.user.mapper")
public class UserApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
