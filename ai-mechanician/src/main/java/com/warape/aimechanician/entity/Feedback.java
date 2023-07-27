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
 * 用户反馈表
 * </p>
 *
 * @author warape
 * @since 2023-04-09 12:59:00
 */
@Getter
@Setter
@TableName("feedback")
public class Feedback extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 建议
     */
    @TableField("proposals")
    private String proposals;


}
