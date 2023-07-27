package com.warape.aimechanician.exception;


import com.warape.aimechanician.domain.StatusEnumSupport;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author apeto
 * @create 2023/4/18 12:06
 */
@Getter
public class SseEmitterException extends ServiceException {

  private SseEmitter sseEmitter;

  public SseEmitterException (Integer code, String message) {
    super(code, message);
  }

  public SseEmitterException (StatusEnumSupport statusEnumSupport, SseEmitter sseEmitter) {
    super(statusEnumSupport);
    this.sseEmitter = sseEmitter;
  }

  public SseEmitterException (StatusEnumSupport statusEnumSupport, Throwable throwable) {
    super(statusEnumSupport, throwable);
  }

  public SseEmitterException (StatusEnumSupport statusEnumSupport, Object... params) {
    super(statusEnumSupport, params);
  }

  @Override
  public String toString () {
    return super.toString();
  }
}
