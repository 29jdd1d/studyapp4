package com.kaoyan.resource.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 腾讯云 COS 配置属性
 */
@Data
@Component
@RefreshScope  // 支持配置动态刷新
@ConfigurationProperties(prefix = "tencent.cos")
public class TencentCosProperties {
    
    @Value("${tencent.cos.secret-id:}")
    private String secretId;

    // 显式绑定secret-key
    @Value("${tencent.cos.secret-key:}")
    private String secretKey;

    @Value("${tencent.cos.region:}")
    private String region;

    @Value("${tencent.cos.bucket-name:}")
    private String bucketName;

    @Value("${tencent.cos.base-url:}")
    private String baseUrl;

    @Value("${tencent.cos.duration-seconds:}")
    private Integer durationSeconds;
}
