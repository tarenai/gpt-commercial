package com.warape.aimechanician.utils;

import java.util.concurrent.TimeUnit;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author apeto
 * @create 2023/4/7 12:03
 */
@Slf4j
public class RedissonUtils {

  private static final RedissonClient REDISSON_CLIENT = SpringUtil.getBean(RedissonClient.class);


  /**
   * 加锁
   *
   * @param lockKey
   * @return
   */
  public static boolean lockByTransaction (String lockKey) {
    // 尝试获取锁
    RLock lock = RedissonUtils.tryLock(lockKey, 3, 10);
    if (lock == null) {
      log.error("get lock timeout, lockKey： " + lockKey);
      return true;
    }
    // 事务处理完成后解锁
    TransactionUtil.afterCompletion((s) -> RedissonUtils.unlock(lock));
    return false;
  }

  public static void unlock (String key) {
    RLock lock = REDISSON_CLIENT.getLock(key);
    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
      lock.unlock();
    }
  }

  public static void unlock (RLock lock) {
    if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
      lock.unlock();
    }
  }

  /**
   * @param key
   * @param waitSeconds  等待时间，单位秒
   * @param leaseSeconds 自动释放时间，单位秒
   * @return
   */
  public static RLock tryLock (String key, long waitSeconds, long leaseSeconds) {
    RLock lock = REDISSON_CLIENT.getLock(key);
    try {
      boolean locked = lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS);
      if (locked) {
        return lock;
      }
    } catch (InterruptedException ignored) {
    }
    return null;
  }

  /**
   * @param key
   * @return
   */
  public static boolean tryLockBoolean (String key) {
    return tryLockBoolean(key, 3, 9);
  }

  /**
   * @param key
   * @return
   */
  public static boolean tryLockNotWaitBoolean (String key) {
    return tryLockBoolean(key, 0, 5);
  }

  /**
   * @param key
   * @param waitSeconds  等待时间，单位秒
   * @param leaseSeconds 自动释放时间，单位秒
   * @return
   */
  public static boolean tryLockBoolean (String key, long waitSeconds, long leaseSeconds) {
    RLock lock = REDISSON_CLIENT.getLock(key);
    try {
      return lock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS);
    } catch (InterruptedException ignored) {
    }
    return false;
  }

}
