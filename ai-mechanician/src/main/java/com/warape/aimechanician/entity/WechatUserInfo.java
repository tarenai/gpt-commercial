package com.warape.aimechanician.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author warape
 * @since 2023-04-01 10:17:34
 */
@Getter
@Setter
@Schema(description = "微信用户信息")
@TableName("wechat_user_info")
public class WechatUserInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    /**
     * openId
     */
    @Schema(description = "openId")
    @TableField("open_id")
    private String openId;

    /**
     * 用户统一标识
     */
    @Schema(description = "用户统一标识")
    @TableField("unionId")
    private String unionId;

    /**
     * 普通用户昵称
     */
    @Schema(description = "普通用户昵称")
    @TableField("nick_name")
    private String nickName;

    /**
     * 普通用户个人资料填写的城市
     */
    @Schema(description = "普通用户个人资料填写的城市")
    @TableField("city")
    private String city;

    /**
     * 普通用户个人资料填写的省份
     */
    @Schema(description = "普通用户个人资料填写的省份")
    @TableField("province")
    private String province;

    /**
     * 国家
     */
    @Schema(description = "国家")
    @TableField("country")
    private String country;

    /**
     * 头像
     */
    @Schema(description = "头像")
    @TableField("headImgUrl")
    private String headImgUrl;

    /**
     * 普通用户性别，1为男性，2为女性
     */
    @Schema(description = "普通用户性别，1为男性，2为女性")
    @TableField("sex")
    private Integer sex;

    /**
     * 是否为快照页模式虚拟账号，值为0时是普通用户，1时是虚拟帐号
     */
    @Schema(description = "是否为快照页模式虚拟账号，值为0时是普通用户，1时是虚拟帐号")
    @TableField("snapshotUser")
    private Integer snapshotUser;



}
