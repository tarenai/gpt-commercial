package com.warape.aimechanician.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author apeto
 * @create 2023/4/21 14:36
 */
@Data
@Component
@ConfigurationProperties("email-config")
public class EmailConfigProperties {

  private String host;
  private Integer port;
  private Boolean auth = true;
  private String from;
  private String user;
  private String pass;
}
