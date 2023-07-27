package com.warape.aimechanician.entity;

import java.io.Serializable;
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
@TableName("user_info")
@Schema(description = "用户信息表")
public class UserInfo extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "状态 1: 正常 2: 注销 3: 冻结")
  @TableField("user_status")
  private Integer userStatus;


  @Schema(description = "会员有效时间")
  @TableField("member_valid_time")
  private Date memberValidTime;

  @Schema(description = "电话号码")
  @TableField("phone")
  private String phone;

  @Schema(description = "电话号码")
  @TableField("email")
  private String email;

  @Schema(description = "用户密码")
  @TableField("user_password")
  private String userPassword;

  @Schema(description = "注册类型")
  @TableField("register_type")
  private Integer registerType;
}
