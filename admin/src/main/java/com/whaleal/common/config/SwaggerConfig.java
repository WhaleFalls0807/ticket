/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.whaleal.common.config;

import com.whaleal.common.constant.Constant;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class SwaggerConfig {

    // 访问地址：http://127.0.0.1:8080/doc.html#/home
    @Bean
    public OpenAPI createRestApi() {
        return new OpenAPI()
                .info(apiInfo())
                .security(security());
    }

    private Info apiInfo() {
        return new Info()
                .title("CRM")
                .description("crm系统文档")
                .version("5.x");
    }

    private List<SecurityRequirement> security() {
        SecurityRequirement key = new SecurityRequirement();
        key.addList(Constant.TOKEN_HEADER, Constant.TOKEN_HEADER);

        List<SecurityRequirement> list = new ArrayList<>();
        list.add(key);
        return list;
    }
}
