package com.warape.aimechanician.domain;

/**
 * @author apeto
 * @create 2023/4/1 11:39 上午
 */
public interface StatusEnumSupport {

  Integer getCode ();

  String getMessage ();

  default String toJsonStr () {
    return "{\"code\":" + getCode() + ",\"message\":\"" + getMessage() + "\"}";
  }
}
