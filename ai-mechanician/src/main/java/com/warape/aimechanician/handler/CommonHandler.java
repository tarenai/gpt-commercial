package com.warape.aimechanician.handler;

import cn.hutool.core.util.StrUtil;
import com.warape.aimechanician.domain.Constants.LoginTypeEnum;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.utils.CommonUtils;
import com.warape.aimechanician.utils.SmsUtils;
import com.warape.aimechanician.utils.StringRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author apeto
 * @create 2023/4/17 14:22
 */
@Slf4j
@Component
public class CommonHandler {

  @Async("chat")
  public void sendCode (String code, String from, String type, LoginTypeEnum loginTypeEnum) {

    String smsCodeKey = RedisKeyEnum.SMS_CODE.getKey(from, type);
    String smsCodeCountLimitKey = RedisKeyEnum.SMS_CODE_COUNT_LIMIT.getKey(from, type);
    try {
      if (loginTypeEnum.getType().equals(LoginTypeEnum.EMAIL.getType())) {
        CommonUtils.sendEmail(from, "验证码", "您好,您的验证码为: " + code);
      } else if (loginTypeEnum.getType().equals(LoginTypeEnum.PHONE.getType())) {
        if (!SmsUtils.sendSms(code, from)) {
          throw new RuntimeException(StrUtil.format("发送短信验证码失败: {}", from));
        }
      } else {
        throw new RuntimeException("sendType无此类型");
      }

      StringRedisUtils.setForTimeMIN(smsCodeKey, code, 5);
      StringRedisUtils.setForTimeMIN(smsCodeCountLimitKey, code, 1);
      log.info("发送验证码成功 类型:{} 验证码:{}", loginTypeEnum.getDesc(), code);
    } catch (Exception e) {
      log.error("验证码发送异常", e);
      StringRedisUtils.delete(smsCodeKey);
      StringRedisUtils.delete(smsCodeCountLimitKey);
    }
  }


}
