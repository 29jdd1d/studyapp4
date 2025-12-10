package com.kaoyan.plan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 学习计划服务启动类
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.kaoyan.plan", "com.kaoyan.common"})
@MapperScan("com.kaoyan.plan.mapper")
public class PlanApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PlanApplication.class, args);
    }
}
