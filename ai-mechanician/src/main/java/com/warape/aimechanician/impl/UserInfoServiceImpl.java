package com.warape.aimechanician.impl;

import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.warape.aimechanician.domain.CommonRespCode;
import com.warape.aimechanician.domain.Constants.LoginTypeEnum;
import com.warape.aimechanician.domain.Constants.ResponseEnum;
import com.warape.aimechanician.domain.vo.TrendVO;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.entity.UserInfo;
import com.warape.aimechanician.entity.WechatUserInfo;
import com.warape.aimechanician.exception.ServiceException;
import com.warape.aimechanician.mapper.UserInfoMapper;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.service.MemberCardService;
import com.warape.aimechanician.service.UserInfoService;
import com.warape.aimechanician.service.WechatUserInfoService;
import com.warape.aimechanician.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

  @Autowired
  private WechatUserInfoService wechatUserInfoService;
  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;
  @Autowired
  private MemberCardService memberCardService;


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Long getOrCreateWechatUser (WxOAuth2UserInfo wxMpUser) {

    WechatUserInfo wechatUserInfo = wechatUserInfoService.getByOpenId(wxMpUser.getOpenid());

    if (wechatUserInfo != null) {
      return wechatUserInfo.getUserId();
    }

    UserInfo userInfo = createUser(LoginTypeEnum.MP.getType(), null, null);
    wechatUserInfo = new WechatUserInfo();
    wechatUserInfo.setUserId(userInfo.getId());
    wechatUserInfo.setOpenId(wxMpUser.getOpenid());
    wechatUserInfo.setUnionId(wxMpUser.getUnionId());
    wechatUserInfo.setNickName(wxMpUser.getNickname());
    wechatUserInfo.setCity(wxMpUser.getCity());
    wechatUserInfo.setProvince(wxMpUser.getProvince());
    wechatUserInfo.setCountry(wxMpUser.getCountry());
    wechatUserInfo.setHeadImgUrl(wxMpUser.getHeadImgUrl());
    wechatUserInfo.setSex(wxMpUser.getSex());
    wechatUserInfo.setSnapshotUser(wxMpUser.getSnapshotUser());
    if (!wechatUserInfoService.save(wechatUserInfo)) {
      throw new RuntimeException("保存WechatUserInfo失败");
    }
    return userInfo.getId();
  }

  @Override
  public UserInfo createWechatUserByOpenId (String openId) {

    UserInfo userInfo = createUser(LoginTypeEnum.MP.getType(), null, null);
    WechatUserInfo wechatUserInfo = new WechatUserInfo();
    wechatUserInfo.setUserId(userInfo.getId());
    wechatUserInfo.setOpenId(openId);
    wechatUserInfo.setNickName("最贵会员");
    if (!wechatUserInfoService.save(wechatUserInfo)) {
      throw new RuntimeException("保存WechatUserInfo失败");
    }

    return userInfo;
  }


  @Override
  public UserInfo getByPhone (String phone) {
    LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(UserInfo::getPhone, phone);
    return baseMapper.selectOne(queryWrapper);
  }

  @Override
  public UserInfo getByEmail (String email) {
    LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(UserInfo::getEmail, email);
    return baseMapper.selectOne(queryWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Long doSmsSignUp (String accountNum, String password, Integer type) {
    UserInfo userInfo = createUser(type, accountNum, password);
    return userInfo.getId();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public UserInfo createUser (Integer type, String accountNum, String password) {

    UserInfo entity = new UserInfo();

    UserInfo userInfo;
    if (type.equals(LoginTypeEnum.PHONE.getType())) {
      userInfo = getByPhone(accountNum);
      entity.setPhone(accountNum);
    } else if (type.equals(LoginTypeEnum.EMAIL.getType())) {
      userInfo = getByEmail(accountNum);
      entity.setEmail(accountNum);
    } else if (type.equals(LoginTypeEnum.MP.getType())) {
      userInfo = null;
    } else {
      throw new ServiceException(CommonRespCode.VALID_SERVICE_ILLEGAL_ARGUMENT);
    }

    if (userInfo != null) {
      throw new ServiceException(ResponseEnum.PHONE_EXIST);
    }
    if (StrUtil.isNotBlank(password)) {
      String md5SaltPass = CommonUtils.md5Salt(password);
      entity.setUserPassword(md5SaltPass);
    }
    entity.setRegisterType(type);
    if (!save(entity)) {
      throw new RuntimeException("保存UserInfo失败");
    }

    MemberCard memberCard = memberCardService.getByCardCode("register01");
    if (memberCard != null) {
      exchangeCardDetailService.exchange(entity.getId(), memberCard);
    } else {
      log.warn("没有注册有礼会员卡");
    }
    return entity;
  }

  @Override
  public List<TrendVO> trend (Integer day) {
    Date end = new Date();
    DateTime start = DateUtil.offsetDay(end, -day);
    if (day == 0) {
      start = DateUtil.beginOfDay(end);
      end = DateUtil.endOfDay(end);
    }
    return baseMapper.trend(start, end);
  }
}
