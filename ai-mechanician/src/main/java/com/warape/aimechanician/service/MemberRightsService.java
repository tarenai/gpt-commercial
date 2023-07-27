package com.warape.aimechanician.service;

import com.warape.aimechanician.entity.MemberRights;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员策略 服务类
 * </p>
 *
 * @author warape
 * @since 2023-05-01 12:47:39
 */
public interface MemberRightsService extends IService<MemberRights> {

  MemberRights getByMemberCode (String cardCode);

}
