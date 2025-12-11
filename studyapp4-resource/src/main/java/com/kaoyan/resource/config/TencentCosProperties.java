package com.kaoyan.resource.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${tencent.cos.secret-key:}")
    private String secretKey;

    @Value("${tencent.cos.region:}")
    private String region;

    @Value("${tencent.cos.bucket-name:}")
    private String bucketName;

    @Value("${tencent.cos.base-url:}")
    private String baseUrl;

    @Value("${tencent.cos.duration-seconds:1800}")
    private Integer durationSeconds;
    
    @Value("${tencent.cos.app-id:}")
    private String appId;
    
    /**
     * 是否使用 STS 临时密钥
     */
    @Value("${tencent.cos.use-sts:true}")
    private Boolean useSts;
    
    /**
     * 允许的操作列表
     */
    @Value("${tencent.cos.allow-actions:name/cos:PutObject,name/cos:PostObject,name/cos:InitiateMultipartUpload,name/cos:ListMultipartUploads,name/cos:ListParts,name/cos:UploadPart,name/cos:CompleteMultipartUpload}")
    private String allowActions;
}
