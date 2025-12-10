package com.kaoyan.resource.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * COS 上传凭证VO
 */
@Data
public class CosCredentialVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 临时密钥 ID
     */
    private String tmpSecretId;
    
    /**
     * 临时密钥 Key
     */
    private String tmpSecretKey;
    
    /**
     * 临时 Token
     */
    private String sessionToken;
    
    /**
     * 过期时间戳
     */
    private Long expiration;
    
    /**
     * Bucket 名称
     */
    private String bucket;
    
    /**
     * 地域
     */
    private String region;
    
    /**
     * 上传路径前缀
     */
    private String prefix;
}
