package com.warape.aimechanician.controller;

import java.util.concurrent.TimeUnit;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.warape.aimechanician.annotations.DistributedLock;
import com.warape.aimechanician.config.properties.WxMpProperties;
import com.warape.aimechanician.domain.Constants.ResponseEnum;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.dto.WechatLoginReq;
import com.warape.aimechanician.domain.vo.AuthorizationVO;
import com.warape.aimechanician.domain.vo.TokenInfoVo;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.entity.UserInfo;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.service.InviteLogService;
import com.warape.aimechanician.service.MemberCardService;
import com.warape.aimechanician.service.UserInfoService;
import com.warape.aimechanician.utils.StringRedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apeto
 * @create 2023/3/28 11:23
 */
@Slf4j
@Tag(name = "微信相关")
@RestController
@RequestMapping("/api/wechat")
public class WechatController {

  @Autowired
  private WxMpService wxMpService;
  @Autowired
  private WxMpProperties wxMpProperties;
  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private InviteLogService inviteLogService;
  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;
  @Autowired
  private MemberCardService memberCardService;


  @Operation(summary = "获取授权url")
  @GetMapping("/authorizationUrl")
  public ResponseResult<AuthorizationVO> getAuthorizationUrl (String inviteCode) {
    String sceneStr = IdUtil.fastSimpleUUID();

    // 认证登录
//    AuthorizationVO vo = new AuthorizationVO();
//    String authorizationUrl = wxMpService.getOAuth2Service()
//        .buildAuthorizationUrl(wxMpProperties.getRedirectUri() + "/api/wechat/wechatLogin?inviteCode=" + inviteCode, OAuth2Scope.SNSAPI_USERINFO, null);
//    vo.setUrl(authorizationUrl);
//    vo.setSceneStr(sceneStr);
//    StringRedisUtils.setForTimeMIN(RedisKeyEnum.WECHAT_QR_LOGIN_CODE.getKey(sceneStr), Boolean.FALSE.toString(), 20);
//    return ResponseResultGenerator.success(vo);

    try {
      if (StrUtil.isNotBlank(inviteCode)) {
        sceneStr = sceneStr + "@" + inviteCode;
      }
      WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService()
          .qrCodeCreateTmpTicket(sceneStr, Integer.parseInt(TimeUnit.MINUTES.toSeconds(10) + ""));
      AuthorizationVO vo = new AuthorizationVO();

      String ticket = wxMpQrCodeTicket.getTicket();
      vo.setSceneStr(ticket);
      vo.setUrl(wxMpQrCodeTicket.getUrl());
      StringRedisUtils.setForTimeMIN(RedisKeyEnum.WECHAT_QR_LOGIN_CODE.getKey(ticket), Boolean.FALSE.toString(), 10);

      return ResponseResultGenerator.success(vo);
    } catch (Exception e) {
      log.error("getAuthorizationUrl 生成二维码失败", e);

    }
    return ResponseResultGenerator.error();
  }

  @Operation(summary = "查询登录状态返回token")
  @GetMapping("/loginState")
  public ResponseResult<String> loginState (@RequestParam("sceneStr") String sceneStr) {
    String uid = StringRedisUtils.get(RedisKeyEnum.WECHAT_QR_LOGIN_CODE.getKey(sceneStr));
    if (StrUtil.isBlank(uid)) {
      return ResponseResultGenerator.result(ResponseEnum.QR_INVALID);
    }

    if (uid.equals(Boolean.FALSE.toString())) {
      return ResponseResultGenerator.result(ResponseEnum.WAITING_FOLLOW);
    }

    UserInfo userInfo = userInfoService.getById(Long.parseLong(uid));
    String token = StpUtil.createLoginSession(userInfo.getId());
    return ResponseResultGenerator.success(token);
  }

  @Operation(summary = "微信注册登录")
  @PostMapping("/wechatLogin")
  @DistributedLock(prefix = RedisKeyEnum.UPDATE_PASSWORD_LOCK, key = "#wechatLoginReq.getCode()", waitFor = 0)
  public ResponseResult<TokenInfoVo> wechatLogin (@RequestBody @Validated WechatLoginReq wechatLoginReq) throws WxErrorException {

    WxOAuth2AccessToken wxOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(wechatLoginReq.getCode());

    WxOAuth2UserInfo wxMpUser = wxMpService.getOAuth2Service().getUserInfo(wxOAuth2AccessToken, null);
    Long userId = userInfoService.getOrCreateWechatUser(wxMpUser);
    StpUtil.login(userId);
    MemberCard memberCard = memberCardService.getByCardCode("register01");
    exchangeCardDetailService.exchange(userId, memberCard);
    inviteLogService.inviteHandler(wechatLoginReq.getInviteCode(), userId);
    SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
    TokenInfoVo tokenInfo = new TokenInfoVo();
    tokenInfo.setTokenName(saTokenInfo.getTokenName());
    tokenInfo.setTokenValue(saTokenInfo.getTokenValue());
    return ResponseResultGenerator.success(tokenInfo);
  }

}
