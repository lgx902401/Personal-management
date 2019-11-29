package com.nbclass.config;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * Swagger 自动接口文档配置
 * <p>
 * # 使用文档地址： https://github.com/SpringForAll/spring-boot-starter-swagger
 * <p>
 * # 接口文档地址： http://localhost:8180/swagger-ui.html
 *
 * @author Roger_Luo
 * @date 2018年3月23日 上午1:33:52
 * @version V1.0
 */
@Configuration
@EnableSwagger2Doc
@PropertySource(value = "classpath:/swagger.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
public class SwaggerConfiguration {

}
