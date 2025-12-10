package com.kaoyan.exam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 题库服务启动类
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.kaoyan.exam", "com.kaoyan.common"})
@MapperScan("com.kaoyan.exam.mapper")
public class ExamApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExamApplication.class, args);
    }
}
