package com.warape.aimechanician.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.warape.aimechanician.entity.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 上拦配置表
 * </p>
 *
 * @author warape
 * @since 2023-05-05 03:57:39
 */
@Getter
@Setter
@TableName("top_bar_config")
public class TopBarConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 按钮名称
     */
    @TableField("button_name")
    private String buttonName;

    /**
     * 按钮描述
     */
    @TableField("button_desc")
    private String buttonDesc;

    /**
     * 10:弹窗 20:跳转
     */
    @TableField("button_type")
    private Integer buttonType;

    /**
     * 跳转url
     */
    @TableField("jump_url")
    private String jumpUrl;

    /**
     * 图片
     */
    @TableField("image_url")
    private String imageUrl;


}
