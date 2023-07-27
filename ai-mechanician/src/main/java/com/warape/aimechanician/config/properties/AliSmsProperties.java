package com.warape.aimechanician.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author apeto
 * @create 2023/4/17 14:15
 */
@Data
@Component
@ConfigurationProperties("ali-config.sms")
public class AliSmsProperties {

  private String accessKeySecret;
  private String accessKey;
  private String signName;
  private String templateCode;
}
