package com.warape.aimechanician.domain;


/**
 * @author apeto
 * @create 2023/4/1 11:36 上午
 */
public class ResponseResultGenerator {


  public static <T> ResponseResult<T> success () {
    return new ResponseResult(CommonRespCode.SUCCESS);
  }

  public static <T> ResponseResult<T> success (T result) {
    return new ResponseResult(CommonRespCode.SUCCESS, result);
  }

  public static <T> ResponseResult<T> error () {
    return new ResponseResult(CommonRespCode.ERROR);
  }

  public static <T> ResponseResult<T> error (String message) {
    return new ResponseResult(CommonRespCode.ERROR.getCode(), message);
  }

  public static <T> ResponseResult<T> result (StatusEnumSupport statusEnumSupport) {

    return new ResponseResult(statusEnumSupport);
  }

  public static <T> ResponseResult<T> result (Integer code, String message) {
    return new ResponseResult(code, message);
  }

  public static <T> ResponseResult<T> result (Integer code, String message, T result) {
    return new ResponseResult(code, message, result);
  }
}
