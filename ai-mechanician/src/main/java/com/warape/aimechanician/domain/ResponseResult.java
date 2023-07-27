package com.warape.aimechanician.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/3/31 10:51 上午
 */
@Data
@Schema()
public class ResponseResult<T> {

  @Schema(description = "编码")
  private Integer code;

  @Schema(description = "消息")
  private String message;

  @Schema(description = "结果")
  private T result;

  public ResponseResult (Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public ResponseResult (Integer code, String message, T result) {
    this.code = code;
    this.message = message;
    this.result = result;
  }

  public ResponseResult (StatusEnumSupport statusEnumSupport, T result) {
    this.code = statusEnumSupport.getCode();
    this.message = statusEnumSupport.getMessage();
    this.result = result;
  }

  public ResponseResult (StatusEnumSupport statusEnumSupport) {
    this.code = statusEnumSupport.getCode();
    this.message = statusEnumSupport.getMessage();
  }

}
