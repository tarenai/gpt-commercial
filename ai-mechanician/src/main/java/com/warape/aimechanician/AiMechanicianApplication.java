package com.warape.aimechanician;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableScheduling
@MapperScan("com.warape.aimechanician.mapper")
@SpringBootApplication
public class AiMechanicianApplication {

  public static void main (String[] args) {
    SpringApplication.run(AiMechanicianApplication.class, args);
  }

}
