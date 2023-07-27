package com.warape.aimechanician.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author apeto
 * @create 2023/4/1 10:18 下午
 */
@Getter
@Setter
public class BaseEntity {

  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 创建人
   */
  @Schema(description = "创建人")
  @TableField(value = "create_by", fill = FieldFill.INSERT)
  private String createBy;

  /**
   * 创建时间
   */
  @Schema(description = "创建时间")
  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private Date createTime;

  /**
   * 修改人
   */
  @Schema(description = "修改人")
  @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
  private String updateBy;

  /**
   * 修改时间
   */
  @Schema(description = "修改时间")
  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  /**
   * 0无效1有效
   */
  @Schema(description = "0无效1有效")
  @TableField(value = "yn", fill = FieldFill.INSERT)
  @TableLogic
  private Integer yn;


}
