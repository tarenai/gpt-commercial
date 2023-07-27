package com.warape.aimechanician.config.properties;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * wechat mp properties
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
@ConfigurationProperties(prefix = "wx.mp")
public class WxMpProperties {

  /**
   * 是否使用redis存储access token
   */
  private boolean useRedis;
  /**
   * 设置微信公众号的appid
   */
  private String appId;

  /**
   * 设置微信公众号的app secret
   */
  private String secret;

  /**
   * 设置微信公众号的token
   */
  private String token;

  /**
   * 设置微信公众号的EncodingAESKey
   */
  private String aesKey;

  private String redirectUri;

  @Override
  public String toString () {
    return JSONUtil.toJsonStr(this);
  }
}
