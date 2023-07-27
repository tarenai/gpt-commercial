package com.warape.aimechanician.aop;

import java.lang.reflect.Method;

import cn.dev33.satoken.stp.StpUtil;
import com.warape.aimechanician.annotations.DistributedLock;
import com.warape.aimechanician.domain.CommonRespCode;
import com.warape.aimechanician.exception.ServiceException;
import com.warape.aimechanician.utils.RedissonUtils;
import com.warape.aimechanician.utils.SpringElUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 分布式锁拦截：自定义注解支持SPEL
 * </p>
 *
 * @author apeto
 */
@Aspect
@Component
public class DistributedLockAspect {

  @Around("@annotation(com.warape.aimechanician.annotations.DistributedLock)")
  public Object around (ProceedingJoinPoint p) throws Throwable {
    String totalKey = null;

    MethodSignature sign = (MethodSignature) p.getSignature();
    Method method = sign.getMethod();

    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
    try {
      String keyBySpEL = SpringElUtil.generateKeyBySpEL(distributedLock.key(), p);
      boolean reqUserId = distributedLock.isReqUserId();
      totalKey = distributedLock.prefix().getKey(keyBySpEL, (reqUserId ? StpUtil.getLoginIdAsLong() : ""));
    } catch (Exception ignored) {
    }
    if (StringUtils.isBlank(totalKey)) {
      return p.proceed();
    }

    try {
      boolean lock = RedissonUtils.tryLockBoolean(totalKey, distributedLock.waitFor(), distributedLock.expire());
      if (!lock) {
        throw new ServiceException(CommonRespCode.REQUEST_FREQUENTLY);
      }
      return p.proceed();
    } finally {
      RedissonUtils.unlock(totalKey);
    }
  }

}
