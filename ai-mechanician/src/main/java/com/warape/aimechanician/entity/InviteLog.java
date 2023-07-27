package com.warape.aimechanician.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 邀请记录
 * </p>
 *
 * @author warape
 * @since 2023-04-06 12:10:07
 */
@Getter
@Setter
@TableName("invite_log")
public class InviteLog extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 邀请人用户ID
   */
  @TableField("invite_user_id")
  private Long inviteUserId;

  /**
   * 被邀请人用户ID
   */
  @TableField("to_invite_user_id")
  private Long toInviteUserId;

  /**
   * 商品ID
   */
  @TableField("product_info_id")
  private Long productInfoId;


}
