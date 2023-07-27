package com.warape.aimechanician.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Schema(description = "CheckImageCodeDTO")
@Data
public class CheckImageCodeDTO {

    @NotBlank
    @Schema(description = "账户")
    private String accountNum;
    @NotBlank
    @Schema(description = "验证码")
    private String code;

}
