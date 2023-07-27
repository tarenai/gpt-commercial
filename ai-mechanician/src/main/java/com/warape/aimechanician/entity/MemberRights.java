package com.warape.aimechanician.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.warape.aimechanician.entity.BaseEntity;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 会员策略
 * </p>
 *
 * @author warape
 * @since 2023-05-01 12:47:39
 */
@Schema(description = "会员策略配置")
@Getter
@Setter
@TableName("member_rights")
public class MemberRights extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 策略名称
     */
    @Schema(description = "策略名称")
    @TableField("rights_name")
    private String rightsName;

    /**
     * 会员编码
     */
    @Schema(description = "会员编码")
    @TableField("member_code")
    private String memberCode;

    /**
     * 次数
     */
    @Schema(description = "次数")
    @TableField("count")
    private Integer count;


}
