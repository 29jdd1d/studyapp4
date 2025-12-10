package com.kaoyan.common.constant;

/**
 * 通用常量
 */
public class CommonConstants {
    
    /**
     * JWT token 请求头
     */
    public static final String TOKEN_HEADER = "Authorization";
    
    /**
     * JWT token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    
    /**
     * JWT 用户ID
     */
    public static final String JWT_USER_ID = "userId";
    
    /**
     * JWT 过期时间（7天，单位毫秒）
     */
    public static final long JWT_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;
    
    /**
     * Redis Key 前缀
     */
    public static final String REDIS_PREFIX = "studyapp4:";
    
    /**
     * 用户信息缓存前缀
     */
    public static final String REDIS_USER_PREFIX = REDIS_PREFIX + "user:";
    
    /**
     * 学习计划缓存前缀
     */
    public static final String REDIS_PLAN_PREFIX = REDIS_PREFIX + "plan:";
    
    /**
     * 热门资源缓存前缀
     */
    public static final String REDIS_HOT_RESOURCE_PREFIX = REDIS_PREFIX + "hot:resource:";
    
    /**
     * 缓存过期时间（30分钟，单位秒）
     */
    public static final long CACHE_EXPIRE_TIME = 30 * 60L;
    
    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 最大分页大小
     */
    public static final int MAX_PAGE_SIZE = 100;
}
