package com.warape.aimechanician.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apeto
 * @create 2023/4/3 11:31
 */
@Getter
@AllArgsConstructor
public enum CommonRespCode implements StatusEnumSupport {

  SUCCESS(200, "SUCCESS"),
  ERROR(500, "服务器出了点小问题，请稍后再试试吧~"),
  REQUEST_NOT_SUPPORT(1001, "不支持的请求"),
  REQUEST_NOT_SUPPORT_METHOD(1002, "不支持的请求方式"),
  REQUEST_NOT_SUPPORT_MEDIA_TYPE(1003, "不支持的MediaType"),
  REQUEST_BIND_ARGUMENT_ERROR(1004, "请求绑定参数异常"),
  REQUEST_NOT_READABLE(1005, "请求消息不可读"),
  REQUEST_PARAM_TYPE_MISMATCH(1006, "请求参数类型转换异常"),
  VALID_BEAN_ILLEGAL_ARGUMENT(1007, "属性参数校验失败"),
  VALID_CLASS_ILLEGAL_ARGUMENT(1008, "类参数校验失败"),
  VALID_SERVICE_ILLEGAL_ARGUMENT(1009, "业务层参数校验失败"),
  VERIFICATION_CODE(1010, "图形验证码错误"),
  SMS_CODE_ERROR(1011, "短信验证码错误"),
  PASSWORD_ERROR(1012, "密码错误"),
  SMS_CODE_EXPIRE(1012, "短信验证码失效"),
  IMAGE_CODE_EXPIRE(1013, "图形验证码失效"),
  REQUEST_FREQUENTLY(2001, "亲，手速太快了！"),
  ;

  private Integer code;
  private String message;
}
