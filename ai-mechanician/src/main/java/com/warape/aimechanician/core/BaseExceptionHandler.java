package com.warape.aimechanician.core;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import cn.dev33.satoken.exception.NotLoginException;
import com.warape.aimechanician.domain.CommonRespCode;
import com.warape.aimechanician.domain.Constants.ResponseEnum;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.StatusEnumSupport;
import com.warape.aimechanician.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.ContentCachingRequestWrapper;

public abstract class BaseExceptionHandler<R> {

  protected static final Logger log = LoggerFactory.getLogger(BaseExceptionHandler.class);

  /**
   * 当前环境
   */
  @Value("${spring.profiles.active}")
  private String profile;

  /**
   * 业务异常
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(ServiceException.class)
  public R handleServiceException (ServiceException ex) {
    if (log.isDebugEnabled()) {
      log.debug(ex.getMessage(), ex);
    }
    return getResult(ex.getCode(), ex.getMessage());
  }


  /**
   * http请求方式不支持
   */
  @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
  public R handleHttpMethodException (HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
    log.error("不支持此请求方式: requestUri={}, errorMsg={}", request.getRequestURI(), ex.getMessage());
    return getResult(CommonRespCode.REQUEST_NOT_SUPPORT_METHOD);
  }

  /**
   * http请求MediaType不支持
   */
  @ExceptionHandler({HttpMediaTypeException.class})
  public R handleHttMediaTypeException (HttpMediaTypeException ex, HttpServletRequest request) {
    log.error("不支持此MediaType: requestUri={}, errorMsg={}", request.getRequestURI(), ex.getMessage());
    return getResult(CommonRespCode.REQUEST_NOT_SUPPORT_MEDIA_TYPE);
  }

  /**
   * requestBind参数缺失
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(ServletRequestBindingException.class)
  public R handleServletRequestBindingException (ServletRequestBindingException ex, HttpServletRequest request) {
    log.error("requestBind参数缺失: requestUri={}, errorMsg={}", request.getRequestURI(), ex.getMessage());
    return getResult(CommonRespCode.REQUEST_BIND_ARGUMENT_ERROR);
  }

  /**
   * HttpMessageNotReadable消息不可读
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public R handleHttpMessageNotReadableException (HttpMessageNotReadableException ex, HttpServletRequest request) {
    log.error("HttpMessageNotReadable消息不可读: requestUri={}, errorMsg={}", request.getRequestURI(), ex.getMessage());
    return getResult(CommonRespCode.REQUEST_NOT_READABLE);
  }

  /**
   * http请求参数类型转换失败
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public R handleMethodArgumentTypeMismatchException (MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
    log.error("http参数类型转换失败: requestUri={} argumentName={} errorMsg={}", request.getRequestURI(), ex.getName(), ex.getLocalizedMessage());
    return getResult(CommonRespCode.REQUEST_PARAM_TYPE_MISMATCH);
  }

  /**
   * servletException
   *
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(ServletException.class)
  public R handleServletException (ServletException ex, HttpServletRequest request) {
    log.error("请求不被支持: requestUri={} errorMsg={}", request.getRequestURL(), ex.getMessage(), ex);
    return getResult(CommonRespCode.REQUEST_NOT_SUPPORT);
  }

  /**
   * valid 请求参数校验异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(BindException.class)
  public R handleBindException (BindException e) {

    return wrapperBindingResult(e.getBindingResult());
  }

  /**
   * valid MethodArgumentNotValid请求参数校验
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public R handleMethodArgumentNotValidException (MethodArgumentNotValidException ex) {
    return wrapperBindingResult(ex.getBindingResult());
  }

  /**
   * valid constraintViolation参数校验失败
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public R handleConstraintViolationException (ConstraintViolationException ex) {
    String message = ex.getConstraintViolations().stream().map(constraintViolation -> {
      String className = constraintViolation.getRootBeanClass().toString();
      Path propertyPath = constraintViolation.getPropertyPath();
      String messageStr = constraintViolation.getMessage();
      return className + "#" + propertyPath + ": " + messageStr;
    }).collect(Collectors.joining("|"));

    log.error("constraintViolation参数校验失败: {}", message, ex);
    CommonRespCode validServiceIllegalArgument = CommonRespCode.VALID_SERVICE_ILLEGAL_ARGUMENT;
    return "prod".equals(profile) ? getResult(validServiceIllegalArgument) : getResult(validServiceIllegalArgument.getCode(), message);
  }

  /**
   * 登录
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(NotLoginException.class)
  public ResponseResult<?> handleException (NotLoginException ex, HttpServletRequest request) {
//    log.error("未登录: requestUri={} errorMsg={}", request.getRequestURI(), ex.getMessage());
    return ResponseResultGenerator.result(ResponseEnum.NOT_LOGIN);
  }

  /**
   * 系统异常
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(Exception.class)
  public R handleException (Exception ex, HttpServletRequest request) {
    log.error("系统异常: requestUri={} errorMsg={}", request.getRequestURI(), ex.getMessage(), ex);
    return getResult(CommonRespCode.ERROR);
  }


  /**
   * 返回结果
   *
   * @param code
   * @param message
   * @return
   */
  protected abstract R getResult (Integer code, String message);

  /**
   * 返回结果
   *
   * @param statusEnumSupport
   * @return
   */
  private R getResult (StatusEnumSupport statusEnumSupport) {
    return getResult(statusEnumSupport.getCode(), statusEnumSupport.getMessage());
  }

  /**
   * 包装绑定异常结果
   *
   * @param bindingResult 绑定结果
   * @return 异常结果
   */
  protected R wrapperBindingResult (BindingResult bindingResult) {
    StringBuilder msg = new StringBuilder();

    for (ObjectError error : bindingResult.getAllErrors()) {
      msg.append(", ");
      if (error instanceof FieldError) {
        msg.append(((FieldError) error).getField()).append(": ");
      }
      msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
    }
    log.error("bindException请求参数校验失败:{}", msg);
    if ("prod".equals(profile)) {
      return getResult(CommonRespCode.REQUEST_PARAM_TYPE_MISMATCH);
    } else {
      return getResult(CommonRespCode.REQUEST_PARAM_TYPE_MISMATCH.getCode(), msg.toString());
    }

  }

  private String de (HttpServletRequest request) {
    StringBuilder buf = new StringBuilder();
    HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    buf.append(req.getMethod()).append(" ").append(req.getRequestURI());
    if (req.getQueryString() != null) {
      buf.append("?").append(req.getQueryString());
    }
    buf.append("\nheader:");
    Enumeration<String> headerNames = req.getHeaderNames();
    while (headerNames != null && headerNames.hasMoreElements()) {// 防止空指针
      String headerName = headerNames.nextElement();
      buf.append("\n\t").append(headerName).append("=").append(req.getHeader(headerName));
    }
    buf.append("\nparams:");
    Enumeration<String> params = req.getParameterNames();
    while (params != null && params.hasMoreElements()) {
      String name = params.nextElement();
      buf.append("\n\t").append(name).append("=").append(req.getParameter(name));
    }
    if (request != null && request instanceof ContentCachingRequestWrapper) {
      ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
      try {
        buf.append("\nbody:\n\t").append(new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8));
      } catch (Exception e1) {
        log.error("decode request body failed", e1);
      }
    }

    return buf.toString();
  }
}
