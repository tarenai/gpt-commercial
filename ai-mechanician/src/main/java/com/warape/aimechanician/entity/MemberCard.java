package com.warape.aimechanician.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 会员卡
 * </p>
 *
 * @author warape
 * @since 2023-04-08 06:49:07
 */
@Schema(description = "会员卡信息")
@Getter
@Setter
@TableName("member_card")
public class MemberCard extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 会员卡编码
   */
  @Schema(description = "会员卡编码")
  @TableField("card_code")
  private String cardCode;

  /**
   * 会员卡名
   */
  @Schema(description = "会员卡名")
  @TableField("card_name")
  private String cardName;

  /**
   * 卡状态
   */
  @Schema(description = "卡状态")
  @TableField("card_state")
  private Integer cardState;

  /**
   * 卡类型 1: 普通类型 2: 拉新赠送
   */
  @Schema(description = "卡类型 1: 普通类型 2: 拉新赠送")
  @TableField("card_type")
  private Integer cardType;

  /**
   * 排序
   */
  @Schema(description = "排序")
  @TableField("card_sort")
  private Integer cardSort;

  /**
   * 显示类型 1: 前端 2: 后台
   */
  @Schema(description = "显示类型 1: 前端 2: 后台")
  @TableField("view_type")
  private Integer viewType;

  /**
   * 卡金额
   */
  @Schema(description = "卡金额")
  @TableField("amount")
  private BigDecimal amount;

  /**
   * 卡天数
   */
  @Schema(description = "卡天数")
  @TableField("card_day")
  private Integer cardDay;


}
