package com.warape.aimechanician.domain.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wanmingyu
 * @create 2023/4/29 12:32 下午
 */
@Schema(description = "MemberConfig")
@Data
public class MemberConfigDTO {

  @Schema(description = "id")
  private Long id;

  /**
   * 会员卡编码
   */
  @Schema(description = "会员卡编码")
  private String cardCode;

  /**
   * 会员卡名
   */
  @Schema(description = "会员卡名")
  private String cardName;

  /**
   * 卡状态
   */
  @Schema(description = "卡状态")
  private Integer cardState;

  /**
   * 卡类型 1: 普通类型 2: 拉新赠送
   */
  @Schema(description = "卡类型 1: 普通类型 2: 拉新赠送")
  private Integer cardType;

  /**
   * 排序
   */
  @Schema(description = "排序")
  private Integer cardSort;

  /**
   * 显示类型 1: 前端 2: 后台
   */
  @Schema(description = "显示类型 1: 前端 2: 后台")
  private Integer viewType;

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

}
