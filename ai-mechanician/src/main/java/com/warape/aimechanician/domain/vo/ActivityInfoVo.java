package com.warape.aimechanician.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/9 5:56 下午
 */
@Schema(description = "活动信息")
@Data
public class ActivityInfoVo {

  @Schema(description = "次数")
  private Integer count;
  @Schema(description = "天数")
  private Integer day;
}
