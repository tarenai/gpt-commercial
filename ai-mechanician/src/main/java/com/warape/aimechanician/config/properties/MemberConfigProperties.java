package com.warape.aimechanician.config.properties;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wanmingyu
 * @create 2023/4/8 7:01 下午
 */
@Data
@Component
@ConfigurationProperties(prefix = "member-config")
public class MemberConfigProperties {

  /**
   * 权益
   */
  private Map<String, Integer> rights = new HashMap<>();
}
