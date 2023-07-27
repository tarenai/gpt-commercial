package com.warape.aimechanician.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 问答记录表
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
@Getter
@Setter
@TableName("chat_detail_log")
@Schema(title = "ChatDetailLog对象", description = "问答记录表")
public class ChatDetailLog extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "用户ID")
  @TableField("user_id")
  private Long userId;

  @Schema(description = "reqId")
  @TableField("request_id")
  private String requestId;

  @Schema(description = "信息")
  @TableField("content")
  private String content;

  @Schema(description = "角色")
  @TableField("chat_role")
  private String chatRole;

}
