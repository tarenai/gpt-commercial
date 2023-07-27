package com.warape.aimechanician.config;

import java.util.Collections;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//Spring boot方式
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

  @Bean
  public PaginationInnerInterceptor paginationInnerInterceptor () {
    PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
    // 设置最大单页限制数量，默认 500 条，-1 不受限制
    paginationInterceptor.setMaxLimit(-1L);
    paginationInterceptor.setDbType(DbType.MYSQL);
    // 开启 count 的 join 优化,只针对部分 left join
    paginationInterceptor.setOptimizeJoin(true);
    return paginationInterceptor;
  }

  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor () {
    MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
    mybatisPlusInterceptor.setInterceptors(Collections.singletonList(paginationInnerInterceptor()));
    return mybatisPlusInterceptor;
  }
}
