package com.warape.aimechanician.domain.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/11 20:29
 */
@Schema(description = "短信")
@Data
public class SendSmsDTO {

  @NotBlank
  @Schema(description = "发送账号")
  private String sendAccount;

  @NotBlank
  @Schema(description = "类型 1: 注册 2: 修改密码")
  private String type;

  @Schema(description = "类型 1: 邮箱 2: 手机号")
  private Integer sendType;

}
