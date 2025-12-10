package com.kaoyan.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 社区服务启动类
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.kaoyan.community", "com.kaoyan.common"})
@MapperScan("com.kaoyan.community.mapper")
public class CommunityApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }
}
