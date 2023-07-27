package com.warape.aimechanician.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.warape.aimechanician.domain.Constants.MemberPermissionTypeEnum;
import com.warape.aimechanician.domain.Constants.MemberStateEnum;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.mapper.MemberCardMapper;
import com.warape.aimechanician.service.MemberCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员卡 服务实现类
 * </p>
 *
 * @author warape
 * @since 2023-04-08 06:49:07
 */
@Service
public class MemberCardServiceImpl extends ServiceImpl<MemberCardMapper, MemberCard> implements MemberCardService {

  @Override
  public List<MemberCard> selectByViewType (MemberPermissionTypeEnum view) {
    LambdaQueryWrapper<MemberCard> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(MemberCard::getViewType, view.getType());
    queryWrapper.eq(MemberCard::getCardState, MemberStateEnum.ONLINE.getState());
    queryWrapper.orderByAsc(MemberCard::getCardSort);
    return list(queryWrapper);
  }

  @Override
  public MemberCard getByCardCode (String memberCardCode) {
    LambdaQueryWrapper<MemberCard> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(MemberCard::getCardCode, memberCardCode);
    queryWrapper.eq(MemberCard::getCardState, MemberStateEnum.ONLINE.getState());
    return getOne(queryWrapper);
  }
}
