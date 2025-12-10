package com.kaoyan.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 后台管理服务启动类
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.kaoyan.admin", "com.kaoyan.common"})
public class AdminApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
