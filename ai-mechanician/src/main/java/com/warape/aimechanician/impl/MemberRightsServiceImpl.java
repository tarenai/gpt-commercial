package com.warape.aimechanician.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.warape.aimechanician.entity.MemberRights;
import com.warape.aimechanician.mapper.MemberRightsMapper;
import com.warape.aimechanician.service.MemberRightsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员策略 服务实现类
 * </p>
 *
 * @author warape
 * @since 2023-05-01 12:47:39
 */
@Service
public class MemberRightsServiceImpl extends ServiceImpl<MemberRightsMapper, MemberRights> implements MemberRightsService {

  @Override
  public MemberRights getByMemberCode (String cardCode) {
    LambdaQueryWrapper<MemberRights> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(MemberRights::getMemberCode, cardCode);
    return getOne(queryWrapper);
  }
}
