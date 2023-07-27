package com.warape.aimechanician.service;

import com.warape.aimechanician.entity.InviteLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 邀请记录 服务类
 * </p>
 *
 * @author warape
 * @since 2023-04-06 12:10:07
 */
public interface InviteLogService extends IService<InviteLog> {

  void inviteHandler (String inviteCode, Long signUpUserId);
}
