package com.warape.aimechanician.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/2 11:35 下午
 */
@Schema(description = "token实体")
@Data
public class TokenInfoVo {

  @Schema(description = "token名称")
  public String tokenName;
  @Schema(description = "token值")
  public String tokenValue;
}
