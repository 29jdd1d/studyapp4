package com.kaoyan.plan.config;

import com.kaoyan.common.constant.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 */
@Configuration
public class RabbitMQConfig {
    
    /**
     * 消息转换器（使用 JSON 格式）
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * 学习计划生成 Exchange
     */
    @Bean
    public TopicExchange planGenerateExchange() {
        return new TopicExchange(RabbitMQConstants.PLAN_GENERATE_EXCHANGE, true, false);
    }
    
    /**
     * 学习计划生成队列
     */
    @Bean
    public Queue planGenerateQueue() {
        return QueueBuilder.durable(RabbitMQConstants.PLAN_GENERATE_QUEUE)
                .deadLetterExchange(RabbitMQConstants.DEAD_LETTER_EXCHANGE)
                .build();
    }
    
    /**
     * 绑定计划生成队列
     */
    @Bean
    public Binding planGenerateBinding() {
        return BindingBuilder.bind(planGenerateQueue())
                .to(planGenerateExchange())
                .with(RabbitMQConstants.PLAN_GENERATE_ROUTING_KEY);
    }
}
