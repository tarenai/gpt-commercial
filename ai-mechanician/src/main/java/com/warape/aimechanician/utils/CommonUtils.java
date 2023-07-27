package com.warape.aimechanician.utils;

import java.nio.charset.StandardCharsets;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.warape.aimechanician.config.properties.EmailConfigProperties;
import com.warape.aimechanician.domain.Constants.LoginTypeEnum;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.dto.SaveSettingDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author apeto
 * @create 2023/4/9 3:39 下午
 */
@Slf4j
public class CommonUtils {

  public static SymmetricCrypto inviteSymmetricCrypto () {

    String key = SpringUtil.getProperty("activity.invite-config.key");
    return new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes(StandardCharsets.UTF_8));
  }

  public static String md5Salt (String password) {
    return SecureUtil.md5(password + "ai");
  }

  public static void sendEmail (String from, String subject, String msg) {
    try {
      EmailConfigProperties emailConfigProperties = SpringUtil.getBean(EmailConfigProperties.class);
      MailAccount account = new MailAccount();
      account.setHost(emailConfigProperties.getHost());
      account.setPort(emailConfigProperties.getPort());
      account.setAuth(emailConfigProperties.getAuth());
      account.setFrom(emailConfigProperties.getFrom());
      account.setUser(emailConfigProperties.getUser());
      account.setPass(emailConfigProperties.getPass());
      account.setSslEnable(true);
      MailUtil.send(account, CollUtil.newArrayList(StrUtil.isBlank(from) ? emailConfigProperties.getFrom() : from), subject, msg, false);
    } catch (Exception e) {
      log.error("发送邮件异常", e);
    }

  }

  public static SaveSettingDTO getPackageWebConfig () {

    SaveSettingDTO saveSettingDTO;
    String confJson = StringRedisUtils.get(RedisKeyEnum.WEB_CONFIG.getKey());
    if (cn.hutool.core.util.StrUtil.isBlank(confJson)) {
      saveSettingDTO = new SaveSettingDTO();
    } else {
      saveSettingDTO = JSONUtil.toBean(confJson, SaveSettingDTO.class);
    }

    String domainUrl = SpringUtil.getApplicationContext().getEnvironment().getProperty("appServer.domain");
    if (saveSettingDTO.getLoginType() == null) {
      saveSettingDTO.setLoginType(LoginTypeEnum.EMAIL.getType());
    }
    if (StrUtil.isBlank(saveSettingDTO.getWebName())) {
      saveSettingDTO.setWebName("Genius AI");
    }
    if (StrUtil.isBlank(saveSettingDTO.getIconUrl())) {
      saveSettingDTO.setIconUrl(domainUrl + "/favicon.ico");
    }

    if (StrUtil.isBlank(saveSettingDTO.getSubTitle())) {
      saveSettingDTO.setSubTitle("老铁~科技改变成活!");
    }

    return saveSettingDTO;
  }
}
