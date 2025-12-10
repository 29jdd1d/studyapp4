package com.kaoyan.resource.config;

import com.kaoyan.common.constant.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 */
@Configuration
public class RabbitMQConfig {
    
    /**
     * 视频转码 Exchange
     */
    @Bean
    public TopicExchange videoTranscodeExchange() {
        return new TopicExchange(RabbitMQConstants.VIDEO_TRANSCODE_EXCHANGE, true, false);
    }
    
    /**
     * 视频转码队列
     */
    @Bean
    public Queue videoTranscodeQueue() {
        return QueueBuilder.durable(RabbitMQConstants.VIDEO_TRANSCODE_QUEUE)
                .deadLetterExchange(RabbitMQConstants.DEAD_LETTER_EXCHANGE)
                .build();
    }
    
    /**
     * 绑定视频转码队列到 Exchange
     */
    @Bean
    public Binding videoTranscodeBinding() {
        return BindingBuilder.bind(videoTranscodeQueue())
                .to(videoTranscodeExchange())
                .with(RabbitMQConstants.VIDEO_TRANSCODE_ROUTING_KEY);
    }
    
    /**
     * 死信 Exchange
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(RabbitMQConstants.DEAD_LETTER_EXCHANGE, true, false);
    }
    
    /**
     * 死信队列
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(RabbitMQConstants.DEAD_LETTER_QUEUE).build();
    }
    
    /**
     * 绑定死信队列
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("");
    }
}
