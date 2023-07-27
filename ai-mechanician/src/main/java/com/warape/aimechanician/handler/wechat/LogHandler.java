package com.warape.aimechanician.handler.wechat;

import java.util.List;
import java.util.Map;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.entity.MemberRights;
import com.warape.aimechanician.entity.UserInfo;
import com.warape.aimechanician.entity.WechatUserInfo;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.service.InviteLogService;
import com.warape.aimechanician.service.MemberCardService;
import com.warape.aimechanician.service.MemberRightsService;
import com.warape.aimechanician.service.UserInfoService;
import com.warape.aimechanician.service.WechatUserInfoService;
import com.warape.aimechanician.utils.RedissonUtils;
import com.warape.aimechanician.utils.StringRedisUtils;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Component
public class LogHandler extends AbstractHandler {

  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;
  @Autowired
  private MemberCardService memberCardService;
  @Autowired
  private MemberRightsService memberRightsService;
  @Autowired
  private WechatUserInfoService wechatUserInfoService;
  @Autowired
  private InviteLogService inviteLogService;

  @Override
  public WxMpXmlOutMessage handle (WxMpXmlMessage wxMessage,
      Map<String, Object> context, WxMpService wxMpService,
      WxSessionManager sessionManager) {
    this.logger.info("\n接收到请求消息，内容：{}", JSONUtil.toJsonStr(wxMessage));

    // 微信授权登录
    mpLogin(wxMessage, wxMpService);

    UserInfo userInfo = emailUserInfo(wxMessage);
    if (userInfo != null) {
      logger.info("获取到了userInfo {}", userInfo);
      WxMpXmlOutTextMessage wxMpXmlOutTextMessage = mpGive(userInfo.getId(), wxMessage);
      logger.info("返回信息:{}", wxMpXmlOutTextMessage);
      return wxMpXmlOutTextMessage;
    } else {
      return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).content("没有找到您的账号哦~ 请按照标准格式发送 领取ChatGPT次数:您的账号").build();
    }
  }


  private void mpLogin (WxMpXmlMessage wxMessage, WxMpService wxMpService) {

    try {
      String ticket = wxMessage.getTicket();
      String eventKey = wxMessage.getEventKey();
      String event = wxMessage.getEvent();
      logger.info("ticket:{} eventKey:{} event:{}", ticket, eventKey, event);
      if (StrUtil.isBlank(ticket)) {
        return;
      }
      String result = StringRedisUtils.get(RedisKeyEnum.WECHAT_QR_LOGIN_CODE.getKey(ticket));
      logger.info("缓存结果:{}", result);
      if (StrUtil.isBlank(result) || !StrUtil.equals(result, Boolean.FALSE.toString())) {
        return;
      }
      String fromUser = wxMessage.getFromUser();
      WechatUserInfo wechatUserInfo = wechatUserInfoService.getByOpenId(fromUser);
      Long userId;
      if (wechatUserInfo == null) {
        userId = userInfoService.createWechatUserByOpenId(fromUser).getId();

        List<String> split = StrUtil.split(event, "eventKey@");
        if (CollUtil.isNotEmpty(split)) {
          inviteLogService.inviteHandler(split.get(1), userId);
        }
      } else {
        userId = wechatUserInfo.getUserId();
      }

//      WxMpUser wxMpUser = wxMpService.getUserService().userInfo(fromUser);

      StpUtil.login(userId);

      StringRedisUtils.set(RedisKeyEnum.WECHAT_QR_LOGIN_CODE.getKey(ticket), userId.toString());
    } catch (Exception e) {
      logger.error("微信授权登录异常", e);
    }

  }

  private UserInfo emailUserInfo (WxMpXmlMessage wxMessage) {
    String content = wxMessage.getContent();
    if (StrUtil.containsIgnoreCase(content, "领取ChatGPT次数")) {
      String[] split = content.split(":");
      String[] split1 = content.split("：");
      String accountNum = "";
      if (split.length > 1) {
        accountNum = split[1];
      } else if (split1.length > 1) {
        accountNum = split1[1];
      }

      if (StrUtil.isBlank(accountNum)) {
        return null;
      }
      return userInfoService.getByEmail(accountNum);
    }
    return null;
  }

  private WxMpXmlOutTextMessage mpGive (Long userId, WxMpXmlMessage wxMessage) {
    String giveLockKey = RedisKeyEnum.WECHAT_GIVE_LOCK.getKey(userId);
    TextBuilder textBuilder = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser());

    if (!RedissonUtils.tryLockBoolean(giveLockKey, 5, 10)) {
      logger.warn("分部署锁异常");
      return null;
    }
    try {
      MemberCard memberCard = memberCardService.getByCardCode("mp_give01");
      if (memberCard == null) {
        return textBuilder.content("活动以结束~").build();
      }
      if (exchangeCardDetailService.checkGive(userId, memberCard)) {
        return textBuilder.content("您今天已经领取过了哦~请明天再来叭。").build();
      }

      exchangeCardDetailService.exchange(userId, memberCard);
      logger.info("领取次数成功: 会员编码:{}", memberCard.getCardCode());
      MemberRights memberRights = memberRightsService.getByMemberCode(memberCard.getCardCode());
      return textBuilder.content(StrUtil.format("领取成功 次数:{} 天数:{}", memberRights.getCount(), memberCard.getCardDay())).build();
    } finally {
      RedissonUtils.unlock(giveLockKey);
    }
  }


}
