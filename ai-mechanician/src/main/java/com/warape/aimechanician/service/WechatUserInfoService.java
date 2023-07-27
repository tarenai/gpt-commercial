package com.warape.aimechanician.service;

import com.warape.aimechanician.entity.WechatUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author warape
 * @since 2023-04-01 10:17:34
 */
public interface WechatUserInfoService extends IService<WechatUserInfo> {

  WechatUserInfo getByOpenId (String openid);

  WechatUserInfo getByUserId (Long userId);
}
