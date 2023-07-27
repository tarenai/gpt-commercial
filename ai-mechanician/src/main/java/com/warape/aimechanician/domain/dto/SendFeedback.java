package com.warape.aimechanician.domain.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/9 1:02 下午
 */
@Schema(description = "发送建议or投诉")
@Data
public class SendFeedback {

  @NotBlank
  @Schema(description = "描述")
  private String desc;

}
