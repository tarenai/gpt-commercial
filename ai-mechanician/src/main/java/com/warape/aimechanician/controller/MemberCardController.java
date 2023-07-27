package com.warape.aimechanician.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.warape.aimechanician.domain.Constants.MemberPermissionTypeEnum;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.vo.MemberCardVo;
import com.warape.aimechanician.domain.vo.MemberUserInfo;
import com.warape.aimechanician.entity.ExchangeCardDetail;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.entity.MemberRights;
import com.warape.aimechanician.entity.UserInfo;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.service.MemberCardService;
import com.warape.aimechanician.service.MemberRightsService;
import com.warape.aimechanician.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 会员卡 前端控制器
 * </p>
 *
 * @author warape
 * @since 2023-04-08 06:49:07
 */
@Tag(name = "会员卡相关")
@RestController
@RequestMapping("/api/memberCard")
public class MemberCardController {

  @Autowired
  private MemberCardService memberCardService;
  @Autowired
  private MemberRightsService memberRightsService;
  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;
  @Autowired
  private UserInfoService userInfoService;

  @Operation(summary = "会员卡列表")
  @GetMapping("/members")
  public ResponseResult<List<MemberCardVo>> members () {
    List<MemberCard> memberCards = memberCardService.selectByViewType(MemberPermissionTypeEnum.VIEW);
    List<MemberCardVo> result = memberCards.stream().map(memberCard -> {

      MemberRights memberRights = memberRightsService.getByMemberCode(memberCard.getCardCode());
      if (memberRights != null) {
        MemberCardVo vo = new MemberCardVo();
        vo.setCardName(memberCard.getCardName());
        vo.setAmount(memberCard.getAmount());
        vo.setCardDay(memberCard.getCardDay());
        vo.setMemberId(memberCard.getId());
        vo.setLimitCount(memberRights.getCount());
        return vo;
      }else {
        return null;
      }

    }).filter(Objects::nonNull).collect(Collectors.toList());
    return ResponseResultGenerator.success(result);
  }


  @Operation(summary = "用户会员信息")
  @GetMapping("/memberUserInfo")
  public ResponseResult<MemberUserInfo> memberUserInfo () {

    Long userId = StpUtil.getLoginIdAsLong();

    int totalCount = 0;
    int surplusCount = 0;
    List<ExchangeCardDetail> exchangeCardDetails = exchangeCardDetailService.selectByUserId(userId);
    for (ExchangeCardDetail exchangeCardDetail : exchangeCardDetails) {
      surplusCount = surplusCount + exchangeCardDetail.getSurplusCount();
      totalCount = totalCount + exchangeCardDetail.getTotalCount();
    }

    UserInfo userInfo = userInfoService.getById(userId);
    MemberUserInfo memberUserInfo = new MemberUserInfo();
    if (userInfo.getMemberValidTime() != null) {
      memberUserInfo.setDeadline((DateUtil.betweenDay(userInfo.getMemberValidTime(), new Date(), true)));
    }
    memberUserInfo.setTotalCount(totalCount);
    memberUserInfo.setSurplusCount(surplusCount);
    return ResponseResultGenerator.success(memberUserInfo);
  }

}
