package com.warape.aimechanician.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/13 18:05
 */
@Schema(description = "购买会员VO")
@Data
public class BuyMemberVo {

  @Schema(description = "二维码链接")
  private String qrCode;

  @Schema(description = "订单号")
  private String orderNo;

}
