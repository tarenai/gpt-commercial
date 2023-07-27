package com.warape.aimechanician.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
public class AsyncTaskConfiguration {

  /**
   * 自定义异步线程池 chat
   *
   * @return AsyncTaskExecutor
   */
  @Bean
  public AsyncTaskExecutor chat () {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("chat-Executor");
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(50);
    executor.setKeepAliveSeconds(60);
    executor.setQueueCapacity(50);

    // 设置拒绝策略
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();
    return executor;
  }


}
