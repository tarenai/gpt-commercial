package com.warape.aimechanician.domain.vo;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/8 6:56 下午
 */
@Data
public class MemberCardVo {

  /**
   * 卡天数
   */
  @Schema(description = "会员卡ID")
  private Long memberId;
  /**
   * 会员卡名
   */
  @Schema(description = "会员卡名")
  private String cardName;

  /**
   * 卡金额
   */
  @Schema(description = "卡金额")
  private BigDecimal amount;

  /**
   * 卡天数
   */
  @Schema(description = "卡天数")
  private Integer cardDay;

  @Schema(description = "次数")
  private Integer limitCount;
}
