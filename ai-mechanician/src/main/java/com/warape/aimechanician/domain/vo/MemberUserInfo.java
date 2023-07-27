package com.warape.aimechanician.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/8 7:28 下午
 */
@Schema(description = "会员用户信息")
@Data
public class MemberUserInfo {

  @Schema(description = "期限")
  private Long deadline = 0L;

  @Schema(description = "总次数")
  private Integer totalCount;

  @Schema(description = "剩余次数")
  private Integer surplusCount;


}
