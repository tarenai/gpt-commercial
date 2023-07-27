package com.warape.aimechanician.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 广告配置表
 * </p>
 *
 * @author warape
 * @since 2023-04-22 02:12:30
 */
@Schema(description = "广告配置")
@Getter
@Setter
@TableName("advertise_config")
public class AdvertiseConfig extends BaseEntity {

  private static final long serialVersionUID = 1L;

  /**
   * 广告名称
   */
  @Schema(description = "广告名称")
  @TableField("advertise_name")
  private String advertiseName;

  /**
   * 广告链接
   */
  @Schema(description = "广告链接")
  @TableField("advertise_link")
  private String advertiseLink;

  /**
   * 图片链接
   */
  @Schema(description = "图片链接")
  @TableField("img_link")
  private String imgLink;

  /**
   * 广告类型
   */
  @Schema(description = "广告类型 10: 加入我们(第一位) 11:加入我们(第二位) 20:右侧广告位列表")
  @TableField("advertise_type")
  private String advertiseType;

}
