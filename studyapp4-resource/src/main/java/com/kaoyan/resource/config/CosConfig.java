package com.kaoyan.resource.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云 COS 配置
 */
@Data
@Configuration

public class CosConfig {
    private final TencentCosProperties tencentCosProperties;

    
    @Bean
    public COSClient cosClient() {
        System.out.println("Cos的ID: " +tencentCosProperties.getSecretId() + ", region: " + tencentCosProperties.getRegion());
        COSCredentials cred = new BasicCOSCredentials(tencentCosProperties.getSecretId(), tencentCosProperties.getSecretKey());
        Region regionObj = new Region(tencentCosProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(regionObj);
        return new COSClient(cred, clientConfig);
    }
}
