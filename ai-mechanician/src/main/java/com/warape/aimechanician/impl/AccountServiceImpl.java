package com.warape.aimechanician.impl;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warape.aimechanician.domain.Constants.DescriptionTypeEnum;
import com.warape.aimechanician.domain.Constants.DirectionTypeTypeEnum;
import com.warape.aimechanician.entity.Account;
import com.warape.aimechanician.entity.AccountLog;
import com.warape.aimechanician.mapper.AccountMapper;
import com.warape.aimechanician.service.AccountLogService;
import com.warape.aimechanician.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 账户表 服务实现类
 * </p>
 *
 * @author warape
 * @since 2023-04-02 05:10:17
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

  @Autowired
  private AccountLogService logService;

  @Override
  public Account getByUserId (Long userId) {
    LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Account::getUserId, userId);
    return getOne(queryWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Account commonUpdateAccount (Long userId, BigDecimal changeAmount, String reqId, String outsideCode, DescriptionTypeEnum descriptionTypeEnum,
      DirectionTypeTypeEnum directionTypeTypeEnum,
      String operatorName) {

    Account account = getByUserId(userId);
    if (account == null) {
      account = new Account();
      account.setUserId(userId);
      account.setAccountBalance(BigDecimal.ZERO);

      if (!save(account)) {
        throw new RuntimeException("账户信息保存失败");
      }
    }
    BigDecimal realAmount = DirectionTypeTypeEnum.IN.equals(directionTypeTypeEnum) ? changeAmount : changeAmount.negate();
    if (baseMapper.commonUpdateAmount(account.getId(), realAmount, operatorName) < 1) {
      throw new RuntimeException("修改金额失败");
    }

    AccountLog accountLog = new AccountLog();
    accountLog.setUserId(userId);
    accountLog.setAmount(changeAmount);
    accountLog.setRequestId(reqId);
    accountLog.setOutsideCode(outsideCode);
    accountLog.setBalanceAmount(account.getAccountBalance().add(realAmount));
    accountLog.setLogDescription(descriptionTypeEnum.getDesc());
    accountLog.setLogDescriptionType(descriptionTypeEnum.getType());
    accountLog.setDirectionType(directionTypeTypeEnum.getType());
    if (!logService.save(accountLog)) {
      throw new RuntimeException("账户信息日志保存失败");
    }

    return account;
  }
}
