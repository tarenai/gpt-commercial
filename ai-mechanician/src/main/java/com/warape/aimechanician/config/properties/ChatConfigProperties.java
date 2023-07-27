package com.warape.aimechanician.config.properties;

import java.util.List;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author apeto
 * @create 2023/4/19 10:38
 */
@Data
@Component
@ConfigurationProperties("chatgpt")
public class ChatConfigProperties {

  private List<String> apiKeys;
  private String apiHost;
}
