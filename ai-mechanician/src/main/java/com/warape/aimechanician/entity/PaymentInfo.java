package com.warape.aimechanician.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
@Getter
@Setter
@TableName("payment_info")
@Schema(description = "用户信息表")
public class PaymentInfo extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "用户ID")
  @TableField("user_id")
  private Long userId;

  @Schema(description = "支付流水")
  @TableField("pay_sn")
  private String paySn;
  @Schema(description = "商品Code")
  @TableField("goods_code")
  private String goodsCode;
  @Schema(description = "三方支付流水")
  @TableField("out_pay_sn")
  private String outPaySn;
  @Schema(description = "支付金额")
  @TableField("pay_amount")
  private BigDecimal payAmount;

  @Schema(description = "支付状态 0: 支付中 1: 已支付 2: 支付失败 3: 支付取消")
  @TableField("pay_state")
  private Integer payState;

  @Schema(description = "支付类型 10:微信 20:支付宝")
  @TableField("pay_type")
  private Integer payType;

  @Schema(description = "商户号")
  @TableField("pay_merchant")
  private String payMerchant;

  @Schema(description = "支付时间")
  @TableField("pay_time")
  private Date payTime;

}
