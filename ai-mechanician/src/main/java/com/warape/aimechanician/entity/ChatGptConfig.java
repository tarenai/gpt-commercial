package com.warape.aimechanician.entity;

import java.io.Serializable;

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
@TableName("chat_gpt_config")
@Schema(description = "用户信息表")
public class ChatGptConfig extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;



  @Schema(description = "apiKey")
  @TableField("api_key")
  private String apiKey;


}
