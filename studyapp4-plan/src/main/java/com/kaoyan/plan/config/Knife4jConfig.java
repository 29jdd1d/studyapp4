package com.kaoyan.plan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j API 文档配置
 *
 * @author kaoyan
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("考研学习小程序 - 学习计划服务 API")
                        .description("学习计划服务接口文档，包括个性化学习计划生成、进度跟踪等功能")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("考研学习小程序团队")
                                .email("support@kaoyan.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
