package com.kaoyan.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置属性
 */
@Data
@Component
@RefreshScope  // 支持配置动态刷新
@ConfigurationProperties(prefix = "wechat.miniapp")
public class WechatProperties {

    /**
     * 微信小程序 AppID
     */
    @Value("${wechat.miniapp.appid:}")
    private String appid;

    /**
     * 微信小程序 AppSecret
     */
    @Value("${wechat.miniapp.secret:}")
    private String secret;

    /**
     * 微信 API 配置
     */
    @Value("${wechat.miniapp.login-url:}")
    private String loginUrl;
}
