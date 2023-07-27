package com.warape.aimechanician.domain.dto;

import com.warape.aimechanician.domain.ReqPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wanmingyu
 * @create 2023/4/30 12:03 上午
 */
@Schema(description = "根据用户信息搜索")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoListDTO extends ReqPage {

  @Schema(description = "电话")
  private String phone;
  @Schema(description = "邮箱")
  private String email;
  @Schema(description = "userId")
  private Long userId;

}
