package com.kaoyan.resource.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云 COS 配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CosClientConfig {
    
    private final TencentCosProperties cosProperties;
    
    @Bean
    public COSClient cosClient() {
        // 1. 初始化用户身份信息
        COSCredentials cred = new BasicCOSCredentials(
                cosProperties.getSecretId(),
                cosProperties.getSecretKey()
        );
        
        // 2. 设置 bucket 的地域
        ClientConfig clientConfig = new ClientConfig(new Region(cosProperties.getRegion()));
        
        // 3. 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        
        log.info("腾讯云 COS 客户端初始化成功: bucket={}, region={}", 
                cosProperties.getBucketName(), cosProperties.getRegion());
        
        return cosClient;
    }
}
