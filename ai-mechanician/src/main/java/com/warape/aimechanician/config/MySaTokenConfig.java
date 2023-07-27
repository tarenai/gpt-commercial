package com.warape.aimechanician.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author apeto
 * @create 2023/4/2 10:50 下午
 */
@Configuration
public class MySaTokenConfig implements WebMvcConfigurer {

  @Bean
  public ApplicationRunner saTokenInit () {
    return args -> SaManager.getConfig();
  }

  // 注册 Sa-Token 拦截器，打开注解式鉴权功能
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
  }
}
