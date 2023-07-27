package com.warape.aimechanician.domain.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/6 11:02
 */
@Data
@Schema(description = "微信登录请求参数")
public class WechatLoginReq {

  @NotBlank
  @Schema(description = "wechatCode")
  private String code;

  @Schema(description = "邀请Code")
  private String inviteCode;

}
