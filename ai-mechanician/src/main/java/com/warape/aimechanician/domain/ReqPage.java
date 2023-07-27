package com.warape.aimechanician.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wanmingyu
 * @create 2023/4/29 11:45 上午
 */
@Data
public class ReqPage {

  /**
   * 每页显示条数，默认 10
   */
  @Schema(description = "每页显示条数，默认 10")
  private long size = 10;

  /**
   * 当前页
   */
  @Schema(description = "当前页，默认 1")
  private long current = 1;

}
