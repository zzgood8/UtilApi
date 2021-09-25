package com.zbx.utilapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @创建人 zbx
 * @创建时间 2021/9/11
 * @描述
 */
@Configuration
public class Authentication implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new AuthInterceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns("/parse","/login");
    }
}
