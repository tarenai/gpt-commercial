package com.warape.aimechanician.utils;

import java.util.function.Consumer;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author lynn
 * @date 2018/12/25
 */
public class TransactionUtil {

  /**
   * 事务提交/回滚执行
   * @param consumer
   */
  public static void afterCompletion (Consumer<Integer> consumer) {
    if (TransactionSynchronizationManager.isActualTransactionActive()) {
      TransactionSynchronizationManager.registerSynchronization(
          new TransactionSynchronization() {
            @Override
            public void afterCompletion (int status) {
              consumer.accept(status);
            }
          }
      );
    } else {
      consumer.accept(TransactionSynchronization.STATUS_COMMITTED);
    }
  }

  /**
   * 事务提交后执行
   * @param consumer
   */
  public static void afterCommit (Consumer<Void> consumer) {
    if (TransactionSynchronizationManager.isActualTransactionActive()) {
      TransactionSynchronizationManager.registerSynchronization(
          new TransactionSynchronization() {
            @Override
            public void afterCommit () {
              consumer.accept(null);
            }
          }
      );
    } else {
      consumer.accept(null);
    }
  }

}
