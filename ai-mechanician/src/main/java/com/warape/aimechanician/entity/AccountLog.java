package com.warape.aimechanician.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 账户表日志
 * </p>
 *
 * @author warape
 * @since 2023-04-02 05:10:18
 */
@Getter
@Setter
@TableName("account_log")
public class AccountLog extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 用户ID
   */
  @TableField("user_id")
  private Long userId;

  /**
   * 变动金额
   */
  @TableField("amount")
  private BigDecimal amount;

  /**
   * 变动后余额
   */
  @TableField("balance_amount")
  private BigDecimal balanceAmount;

  /**
   * 请求ID
   */
  @TableField("request_id")
  private String requestId;
  /**
   * 订单号
   */
  @TableField("outside_code")
  private String outsideCode;

  /**
   * 描述
   */
  @TableField("log_description")
  private String logDescription;

  /**
   * 1: 充值 2: 失效回收 3: 退款 4: 消费
   */
  @TableField("log_description_type")
  private Integer logDescriptionType;

  /**
   * 类型 10: 转入 20:转出
   */
  @TableField("direction_type")
  private Integer directionType;


}
