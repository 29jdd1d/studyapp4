package com.kaoyan.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 */
@Slf4j
public class JwtUtil {
    
    /**
     * 生成 JWT Token
     *
     * @param subject    主题（通常是用户ID）
     * @param claims     自定义声明
     * @param expiration 过期时间（毫秒）
     * @param secret     密钥
     * @return token
     */
    public static String generateToken(String subject, Map<String, Object> claims, long expiration, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }
    
    /**
     * 解析 JWT Token
     *
     * @param token  token
     * @param secret 密钥
     * @return Claims
     */
    public static Claims parseToken(String token, String secret) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("JWT 解析失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 验证 Token 是否过期
     *
     * @param claims Claims
     * @return true-已过期 false-未过期
     */
    public static boolean isTokenExpired(Claims claims) {
        if (claims == null) {
            return true;
        }
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
    
    /**
     * 从 Token 中获取用户ID
     *
     * @param token  token
     * @param secret 密钥
     * @return 用户ID
     */
    public static Long getUserId(String token, String secret) {
        Claims claims = parseToken(token, secret);
        if (claims == null) {
            return null;
        }
        return claims.get("userId", Long.class);
    }
}
