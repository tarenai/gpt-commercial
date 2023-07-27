package com.warape.aimechanician.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warape.aimechanician.domain.Constants.DescriptionTypeEnum;
import com.warape.aimechanician.domain.Constants.DirectionTypeTypeEnum;
import com.warape.aimechanician.entity.Account;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author warape
 * @since 2023-04-02 05:10:17
 */
public interface AccountService extends IService<Account> {

  Account getByUserId (Long userId);

  Account commonUpdateAccount (Long userId, BigDecimal changeAmount, String reqId, String outsideCode, DescriptionTypeEnum descriptionTypeEnum,
      DirectionTypeTypeEnum directionTypeTypeEnum,
      String operatorName);
}
