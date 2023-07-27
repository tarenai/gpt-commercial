package com.warape.aimechanician.config;

import javax.servlet.http.HttpServletRequest;

import cn.dev33.satoken.exception.NotLoginException;
import com.warape.aimechanician.core.BaseExceptionHandler;
import com.warape.aimechanician.domain.Constants.ResponseEnum;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author apeto
 * @create 2023/4/3 11:37
 */
@RestControllerAdvice
public class RestExceptionHandler extends BaseExceptionHandler<ResponseResult<?>> {

  public RestExceptionHandler () {
  }

  protected ResponseResult<?> getResult (Integer code, String message) {
    return ResponseResultGenerator.result(code, message);
  }



}
