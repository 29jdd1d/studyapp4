package com.kaoyan.user.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope  // 支持配置动态刷新
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    @Value("${jwt.secret:}")
    private String secret;
    @Value("${jwt.expiration:}")
    private Long expiration;

}
