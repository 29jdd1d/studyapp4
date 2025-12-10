package com.kaoyan.gateway.filter;

import com.kaoyan.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * JWT 认证过滤器
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("#{'${auth.exclude-paths}'.split(',')}")
    private List<String> excludePaths;
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 检查是否在排除路径中
        boolean shouldExclude = excludePaths.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
        
        if (shouldExclude) {
            return chain.filter(exchange);
        }
        
        // 获取 Token
        String token = request.getHeaders().getFirst("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            return unauthorized(exchange.getResponse(), "未授权，请登录");
        }
        
        // 去除 Bearer 前缀
        token = token.substring(7);
        
        // 验证 Token
        Claims claims = JwtUtil.parseToken(token, jwtSecret);
        
        if (claims == null) {
            return unauthorized(exchange.getResponse(), "无效的token");
        }
        
        if (JwtUtil.isTokenExpired(claims)) {
            return unauthorized(exchange.getResponse(), "登录已过期");
        }
        
        // 将用户ID添加到请求头，传递给下游服务
        Long userId = claims.get("userId", Long.class);
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", String.valueOf(userId))
                .build();
        
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
    
    @Override
    public int getOrder() {
        return -100;
    }
    
    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        
        String result = String.format(
                "{\"code\":401,\"message\":\"%s\",\"data\":null,\"timestamp\":%d}",
                message, System.currentTimeMillis()
        );
        
        DataBuffer buffer = response.bufferFactory()
                .wrap(result.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Mono.just(buffer));
    }
}
