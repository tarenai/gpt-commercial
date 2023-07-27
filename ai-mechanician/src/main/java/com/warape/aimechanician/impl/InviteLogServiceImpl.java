package com.warape.aimechanician.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warape.aimechanician.entity.InviteLog;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.entity.UserInfo;
import com.warape.aimechanician.mapper.InviteLogMapper;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.service.InviteLogService;
import com.warape.aimechanician.service.MemberCardService;
import com.warape.aimechanician.service.UserInfoService;
import com.warape.aimechanician.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邀请记录 服务实现类
 * </p>
 *
 * @author warape
 * @since 2023-04-06 12:10:07
 */
@Slf4j
@Service
public class InviteLogServiceImpl extends ServiceImpl<InviteLogMapper, InviteLog> implements InviteLogService {

  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;
  @Autowired
  private MemberCardService memberCardService;
  @Autowired
  private UserInfoService userInfoService;

  @Async("chat")
  @Override
  public void inviteHandler (String inviteCode, Long signUpUserId) {

    try {

      if (StrUtil.isBlank(inviteCode)) {
        return;
      }
      Long inviteUserId = Long.valueOf(CommonUtils.inviteSymmetricCrypto().decryptStr(inviteCode));

      if (inviteUserId.equals(signUpUserId)) {
        return;
      }
      UserInfo userInfo = userInfoService.getById(inviteUserId);
      if (userInfo == null) {
        log.warn("邀请用户不存在");
        return;
      }
      MemberCard memberCard = memberCardService.getByCardCode("invite01");
      exchangeCardDetailService.exchange(inviteUserId, memberCard);

      InviteLog inviteLog = new InviteLog();
      inviteLog.setInviteUserId(inviteUserId);
      inviteLog.setToInviteUserId(signUpUserId);

      Long memberCardId = memberCard.getId();
      inviteLog.setProductInfoId(memberCardId);
      if (!save(inviteLog)) {
        log.error("邀请记录插入失败! inviteCode:{} signUpUserId:{} productInfoId:{}", inviteCode, signUpUserId, memberCardId);
      }
    } catch (Exception e) {
      log.error("邀请有礼插入异常 inviteCode:{} signUpUserId:{}", inviteCode, signUpUserId, e);
    }

  }

}
