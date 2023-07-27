package com.warape.aimechanician.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author apeto
 * @create 2023/4/13 16:52
 */
@Data
@Component
@ConfigurationProperties("ali-pay.conf")
public class AliPayProperties {

  private String appPublicKey;
  private String appPrivateKey;
  private String aliPublicKey;
  private String appId;
  private String serverUrl;
  private String payCallbackUrl;
}
