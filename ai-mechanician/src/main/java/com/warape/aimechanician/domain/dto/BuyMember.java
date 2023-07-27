package com.warape.aimechanician.domain.dto;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/8 6:12 下午
 */
@Schema(description = "购买会员DTO")
@Data
public class BuyMember {

  @NotNull
  @Schema(description = "会员卡ID")
  private Long memberId;

  @NotNull
  @Schema(description = "支付类型 10:微信 20:支付宝")
  private Integer payType;

}
