package com.warape.aimechanician.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ApiSwitchAspect {

  @Around("@annotation(com.warape.aimechanician.annotations.MethodInfo)")
  public Object methodInfo (ProceedingJoinPoint jp) throws Throwable {
    Signature signature = jp.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    long start = System.currentTimeMillis();
    Object proceed = jp.proceed();

    String methodName = methodSignature.getDeclaringTypeName() + "#" + methodSignature.getName();
    log.info("方法:{} 耗时:{} ", methodName, System.currentTimeMillis() - start);
    return proceed;
  }
}
