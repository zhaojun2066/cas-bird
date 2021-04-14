package com.bird.cas.core.config;

import com.bird.cas.core.context.LoginContextHandlerInterceptorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-21 14:32
 **/
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {


    @Bean
    public LoginContextHandlerInterceptorAdapter loginContextHandlerInterceptorAdapter(){
        return  new LoginContextHandlerInterceptorAdapter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //指定拦截器，指定拦截路径 gclogin 适配之前的项目
        registry.addInterceptor(loginContextHandlerInterceptorAdapter()).addPathPatterns("/login","/login/**")
        .excludePathPatterns("/css/**","/js/**","/images/**");
    }
}
