package com.warape.aimechanician.domain.vo;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/8 7:30 下午
 */
@Schema(description = "用户信息VO")
@Data
public class UserInfoVo {

  @Schema(description = "注册时间")
  private Date createTime;

  @Schema(description = "昵称")
  private String nikeName;

  @Schema(description = "头像")
  private String headImgUrl;
}
