package com.warape.aimechanician.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;

/**
 * @author apeto
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DistributedLock {

  /**
   * 锁的Key前缀
   *
   * @return redis 锁key的前缀
   */
  RedisKeyEnum prefix();

  /**
   * 锁的Key，不包含前缀
   *
   * @return
   */
  String key() default "";

  /**
   * 过期秒数,默认为5秒
   *
   * @return 轮询锁的时间
   */
  int expire() default 5;

  /**
   * 等待时间
   *
   * @return
   */
  int waitFor() default 3;

  /**
   * 超时时间单位
   *
   * @return 秒
   */
  TimeUnit timeUnit() default TimeUnit.SECONDS;

  boolean isReqUserId() default false;

}
