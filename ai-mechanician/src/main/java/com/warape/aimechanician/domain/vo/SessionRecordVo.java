package com.warape.aimechanician.domain.vo;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author apeto
 * @create 2023/4/2 5:19 下午
 */
@Schema(description = "会话记录")
@Data
public class SessionRecordVo {

  @Schema(description = "请求ID")
  private String reqId;

  @Schema(description = "文案")
  private String content;

  @Schema(description = "数量")
  private Long reqCount;

  @Schema(description = "角色")
  private String  chatRole;

  @Schema(description = "创建时间")
  private Date createTime;

}
