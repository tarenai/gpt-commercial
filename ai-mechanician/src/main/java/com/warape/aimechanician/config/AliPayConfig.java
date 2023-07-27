package com.warape.aimechanician.config;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayClient;
import com.ijpay.alipay.AliPayApiConfig;
import com.warape.aimechanician.config.properties.AliPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apeto
 * @create 2023/4/13 16:48
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "ali-pay", name = "enable", havingValue = "true")
public class AliPayConfig {

  @Autowired
  private AliPayProperties aliPayProperties;

  @Bean
  public AlipayClient aliPayApiConfig () {
    log.info("开始加载支付宝支付相关配置:{}", JSONUtil.toJsonStr(aliPayProperties));
    return AliPayApiConfig.builder()
        .setAppId(aliPayProperties.getAppId())
        .setAliPayPublicKey(aliPayProperties.getAliPublicKey())
        .setCharset("UTF-8")
        .setPrivateKey(aliPayProperties.getAppPrivateKey())
        .setServiceUrl(aliPayProperties.getServerUrl())
        .setSignType("RSA2")
        .build().getAliPayClient();
  }

}
