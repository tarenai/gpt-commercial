package com.warape.aimechanician.domain;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apeto
 * @create 2023/4/6 10:17 下午
 */
public interface SystemConstants {


  @Getter
  @AllArgsConstructor
  enum RedisKeyEnum {
    QUESTIONS("questions:{}:{}", "提问缓存key userId,reqId"),
    QUESTIONS_LOCK("questionsLock:{}:{}", "提问缓存key userId,reqId"),
    SMS_IMAGE_CODE("SMS_IMAGE_CODE:{}", "图片验证码"),
    SMS_LOCK("SMS_LOCK:{}", "发送验证码锁"),
    SMS_CODE("SMS_CODE:{}:{}", "短信&游戏验证码key"),
    SMS_CODE_COUNT_LIMIT("SMS_CODE_COUNT_LIMIT:{}:{}", "短信验证码次数限制key"),
    SEE_KEY("SEE_KEY:{}:{}", "seeKey"),
    SMS_SIGN_UP_LOCK("SMS_SIGN_UP_LOCK:{}", "注册锁"),
    SMS_SEND_LOCK("SMS_SEND_LOCK:{}", "发送短信"),
    UPDATE_PASSWORD_LOCK("UPDATE_PASSWORD_LOCK:{}", "修改密码"),
    WECHAT_LOGIN_LOCK("WECHAT_LOGIN_LOCK:{}", "微信登录"),
    BUY_MEMBER_LOCK("BUY_MEMBER_LOCK:{}", "购买会员"),
    FEEDBACK_SEND_LOCK("FEEDBACK_SEND_LOCK:{}", "反馈建议"),
    WECHAT_QR_LOGIN_CODE("WECHAT_QR_LOGIN_CODE:{}", "微信扫码登录编码"),
    WECHAT_GIVE_LOCK("WECHAT_GIVE_LOCK:{}", "微信领取次数锁"),
    WEB_CONFIG("WEB_CONFIG", "站点配置"),
    CHAT_CONFIG("CHAT_CONFIG", "chatGPT参数配置"),
    ;

    private String key;
    private String desc;

    public String getKey (Object... params) {
      return StrUtil.format(this.key, params);
    }
  }
}
