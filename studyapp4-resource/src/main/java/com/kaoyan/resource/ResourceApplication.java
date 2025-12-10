package com.kaoyan.resource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 资源服务启动类
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.kaoyan.resource", "com.kaoyan.common"})
@MapperScan("com.kaoyan.resource.mapper")
public class ResourceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }
}
