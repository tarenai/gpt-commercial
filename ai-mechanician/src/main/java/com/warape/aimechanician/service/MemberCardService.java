package com.warape.aimechanician.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warape.aimechanician.domain.Constants.MemberPermissionTypeEnum;
import com.warape.aimechanician.entity.MemberCard;

/**
 * <p>
 * 会员卡 服务类
 * </p>
 *
 * @author warape
 * @since 2023-04-08 06:49:07
 */
public interface MemberCardService extends IService<MemberCard> {

  List<MemberCard> selectByViewType (MemberPermissionTypeEnum view);

  MemberCard getByCardCode (String memberCardCode);

}
