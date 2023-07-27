package com.warape.aimechanician.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warape.aimechanician.entity.ExchangeCardDetail;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.entity.UserInfo;

/**
 * <p>
 * 兑换卡详情 服务类
 * </p>
 *
 * @author warape
 * @since 2023-04-05 04:51:24
 */
public interface ExchangeCardDetailService extends IService<ExchangeCardDetail> {

  void consume (Long userId, Integer consumeCount);

  void exchange (Long userId, MemberCard memberCard);

  List<ExchangeCardDetail> selectByUserId (Long userId);

  int getSurplusCount (Long userId);

  List<ExchangeCardDetail> selectExpire ();


  boolean checkGive (Long userId, MemberCard memberCard);
}
