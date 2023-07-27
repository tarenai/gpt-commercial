package com.warape.aimechanician.controller;

import java.util.HashMap;
import java.util.Map;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.vo.ActivityInfoVo;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.entity.MemberRights;
import com.warape.aimechanician.service.MemberCardService;
import com.warape.aimechanician.service.MemberRightsService;
import com.warape.aimechanician.utils.CommonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apeto
 * @create 2023/4/5 5:18 下午
 */
@Tag(name = "活动相关")
@RequestMapping("/api/activity")
@RestController
public class ActivityController {

  @Value("${activity.invite-config.url}")
  private String url;
  @Autowired
  private MemberCardService memberCardService;
  @Autowired
  private MemberRightsService memberRightsService;

  @Operation(summary = "获取邀请有礼链接信息")
  @GetMapping("/inviteGiftUrlInfo")
  public ResponseResult<Map<String, String>> inviteGiftUrlInfo () {

    String str = "当您通过分享您的专属二维码或链接邀请好友成功注册后，将获得{}次提问次数，有效期为{}天。快来邀请您的好友加入我们吧！";
    Map<String, String> map = new HashMap<>();
    long userId = StpUtil.getLoginIdAsLong();
    MemberCard memberCard = memberCardService.getByCardCode("invite01");
    String encryptHex = CommonUtils.inviteSymmetricCrypto().encryptHex(userId + "");
    MemberRights memberRights = memberRightsService.getByMemberCode(memberCard.getCardCode());
    map.put("url", url);
    map.put("code", encryptHex);
    map.put("desc", StrUtil.format(str, memberRights.getCount(), memberCard.getCardDay()));
    return ResponseResultGenerator.success(map);
  }

}
