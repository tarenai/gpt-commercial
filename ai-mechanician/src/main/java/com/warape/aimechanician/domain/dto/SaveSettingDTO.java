package com.warape.aimechanician.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wanmingyu
 * @create 2023/5/5 13:45
 */

@Schema(description = "SaveSettingDTO")
@Data
public class SaveSettingDTO {

  @Schema(description = "1:邮箱 2:手机号 3:微信授权")
  private Integer loginType = 1;
  @Schema(description = "网站名称")
  private String webName;
  @Schema(description = "二级名称")
  private String subTitle;
  @Schema(description = "icon地址")
  private String iconUrl;
  @Schema(description = "备案号")
  private String filingNumber;
}
