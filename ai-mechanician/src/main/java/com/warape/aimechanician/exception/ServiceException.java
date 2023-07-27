package com.warape.aimechanician.exception;

import cn.hutool.core.util.StrUtil;
import com.warape.aimechanician.domain.StatusEnumSupport;
import lombok.Getter;
import lombok.Setter;

/**
 * @author apeto
 * @create 2023/4/2 11:30 下午
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {

  private final Integer code;
  private final String message;


  public ServiceException (Integer code, String message) {
    super(message);
    this.code = code;
    this.message = message;
  }

  public ServiceException (StatusEnumSupport statusEnumSupport) {
    super(statusEnumSupport.getMessage());
    this.code = statusEnumSupport.getCode();
    this.message = super.getMessage();
  }

  public ServiceException (StatusEnumSupport statusEnumSupport, Throwable throwable) {
    super(statusEnumSupport.getMessage(), throwable);
    this.code = statusEnumSupport.getCode();
    this.message = super.getMessage();
  }

  public ServiceException (StatusEnumSupport statusEnumSupport, Object... params) {
    super(StrUtil.format(statusEnumSupport.getMessage(), params));
    this.code = statusEnumSupport.getCode();
    this.message = super.getMessage();
  }

  @Override
  public String toString () {
    return "{\"code\":" + code + ",\"message\":\"" + message + "\"}";
  }
}
