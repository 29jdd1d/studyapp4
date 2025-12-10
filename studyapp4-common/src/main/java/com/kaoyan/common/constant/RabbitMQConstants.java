package com.kaoyan.common.constant;

/**
 * RabbitMQ 常量
 */
public class RabbitMQConstants {
    
    /**
     * 视频转码 Exchange
     */
    public static final String VIDEO_TRANSCODE_EXCHANGE = "studyapp4.resource.exchange";
    
    /**
     * 视频转码队列
     */
    public static final String VIDEO_TRANSCODE_QUEUE = "studyapp4.resource.video.transcode";
    
    /**
     * 视频转码路由键
     */
    public static final String VIDEO_TRANSCODE_ROUTING_KEY = "resource.video.transcode";
    
    /**
     * 学习计划生成 Exchange
     */
    public static final String PLAN_GENERATE_EXCHANGE = "studyapp4.plan.exchange";
    
    /**
     * 学习计划生成队列
     */
    public static final String PLAN_GENERATE_QUEUE = "studyapp4.plan.generate";
    
    /**
     * 学习计划生成路由键
     */
    public static final String PLAN_GENERATE_ROUTING_KEY = "plan.generate";
    
    /**
     * 答题数据分析 Exchange
     */
    public static final String EXAM_ANALYSIS_EXCHANGE = "studyapp4.exam.exchange";
    
    /**
     * 答题数据分析队列
     */
    public static final String EXAM_ANALYSIS_QUEUE = "studyapp4.exam.analysis";
    
    /**
     * 答题数据分析路由键
     */
    public static final String EXAM_ANALYSIS_ROUTING_KEY = "exam.analysis";
    
    /**
     * 死信 Exchange
     */
    public static final String DEAD_LETTER_EXCHANGE = "studyapp4.dlx.exchange";
    
    /**
     * 死信队列
     */
    public static final String DEAD_LETTER_QUEUE = "studyapp4.dlx.queue";
}
