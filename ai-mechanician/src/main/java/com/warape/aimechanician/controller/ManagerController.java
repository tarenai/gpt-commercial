package com.warape.aimechanician.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion.Model;
import com.warape.aimechanician.domain.ChatConfigEntity;
import com.warape.aimechanician.domain.Constants.LoginTypeEnum;
import com.warape.aimechanician.domain.Constants.ResponseEnum;
import com.warape.aimechanician.domain.Dict;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.dto.*;
import com.warape.aimechanician.domain.vo.TrendVO;
import com.warape.aimechanician.entity.*;
import com.warape.aimechanician.service.AdvertiseConfigService;
import com.warape.aimechanician.service.ChatDetailLogService;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.service.MemberCardService;
import com.warape.aimechanician.service.MemberRightsService;
import com.warape.aimechanician.service.PaymentInfoService;
import com.warape.aimechanician.service.TopBarConfigService;
import com.warape.aimechanician.service.UserInfoService;
import com.warape.aimechanician.utils.CommonUtils;
import com.warape.aimechanician.utils.StringRedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apeto
 * @create 2023/4/29 11:39 上午
 */
@Tag(name = "后台管理系统")
@Slf4j
@RestController
@RequestMapping("/api/manager")
@SaCheckLogin
public class ManagerController {

  @Autowired
  private PaymentInfoService paymentInfoService;
  @Autowired
  private ChatDetailLogService chatDetailLogService;
  @Autowired
  private MemberCardService memberCardService;
  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private MemberRightsService memberRightsService;
  @Autowired
  private AdvertiseConfigService advertiseConfigService;
  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;
  @Autowired
  private TopBarConfigService topBarConfigService;
  @Autowired(required = false)
  private OpenAiStreamClient openAiStreamClient;

  @Value("${ai-manager-config.name}")
  private String name;
  @Value("${ai-manager-config.password}")
  private String password;

  @SaIgnore
  @GetMapping("/login")
  @Operation(summary = "登录")
  public ResponseResult<?> login (@RequestParam("name") String name, @RequestParam("password") String password) {
    if (StrUtil.equals(this.name, name) && StrUtil.equals(this.password, password)) {
      StpUtil.login(name, TimeUnit.HOURS.toSeconds(3));
      return ResponseResultGenerator.success();
    }
    return ResponseResultGenerator.result(ResponseEnum.NOT_LOGIN);
  }


  @GetMapping("/logout")
  @Operation(summary = "登出")
  public ResponseResult<?> logout () {
    StpUtil.logout(name);
    return ResponseResultGenerator.success();
  }

  @GetMapping("/userInfoList")
  @Operation(summary = "用户列表")
  public ResponseResult<Page<UserInfo>> userInfoList (UserInfoListDTO userInfoListDTO) {

    String email = userInfoListDTO.getEmail();
    String phone = userInfoListDTO.getPhone();
    Long userId = userInfoListDTO.getUserId();
    LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(StrUtil.isNotBlank(email), UserInfo::getEmail, email);
    queryWrapper.eq(StrUtil.isNotBlank(phone), UserInfo::getPhone, phone);
    queryWrapper.eq(userId != null, UserInfo::getId, userId);
    queryWrapper.orderByDesc(BaseEntity::getId);
    Page<UserInfo> page = userInfoService.page(new Page<>(userInfoListDTO.getCurrent(), userInfoListDTO.getSize()), queryWrapper);
    return ResponseResultGenerator.success(page);
  }

  @PostMapping("/depositMember")
  @Operation(summary = "手动充值会员卡")
  public ResponseResult<?> depositMember (@RequestBody DepositMemberDTO depositMemberDTO) {
    exchangeCardDetailService.exchange(depositMemberDTO.getUserId(), memberCardService.getByCardCode(depositMemberDTO.getMemberCode()));
    return ResponseResultGenerator.success();
  }

  @GetMapping("/memberExchange")
  @Operation(summary = "会员兑换列表")
  public ResponseResult<Page<ExchangeCardDetail>> memberExchange(MemberExchangeDTO memberExchangeDTO){
    LambdaQueryWrapper<ExchangeCardDetail> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(memberExchangeDTO.getUserId() != null,ExchangeCardDetail::getUserId,memberExchangeDTO.getUserId());
    Page<ExchangeCardDetail> page = exchangeCardDetailService.page(new Page<>(memberExchangeDTO.getCurrent(), memberExchangeDTO.getSize()), queryWrapper);
    return ResponseResultGenerator.success(page);
  }


  @GetMapping("/orders")
  @Operation(summary = "支付订单列表")
  public ResponseResult<Page<PaymentInfo>> orders (ManagerOrdersDTO managerOrdersDto) {
    Long userId = managerOrdersDto.getUserId();
    LambdaQueryWrapper<PaymentInfo> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(userId != null, PaymentInfo::getUserId, userId);
    queryWrapper.orderByDesc(BaseEntity::getId);
    Page<PaymentInfo> paymentInfoPage = paymentInfoService.page(new Page<>(managerOrdersDto.getCurrent(), managerOrdersDto.getSize()), queryWrapper);
    return ResponseResultGenerator.success(paymentInfoPage);
  }


  @GetMapping("/qLogs")
  @Operation(summary = "提问日志列表", description = "条件可根据reqId userId进行搜素")
  public ResponseResult<Page<ChatDetailLog>> qLogs (QLogsDTO qLogsDTO) {
    LambdaQueryWrapper<ChatDetailLog> queryWrapper = new LambdaQueryWrapper<>();
    Long userId = qLogsDTO.getUserId();
    String reqId = qLogsDTO.getReqId();
    queryWrapper.eq(userId != null, ChatDetailLog::getUserId, userId);
    queryWrapper.eq(StrUtil.isNotBlank(reqId), ChatDetailLog::getRequestId, reqId);
    queryWrapper.orderByDesc(BaseEntity::getId);
    Page<ChatDetailLog> page = chatDetailLogService.page(new Page<>(qLogsDTO.getCurrent(), qLogsDTO.getSize()), queryWrapper);

    return ResponseResultGenerator.success(page);
  }

  @GetMapping("/memberList")
  @Operation(summary = "会员卡列表")
  public ResponseResult<List<MemberCard>> memberList () {
    List<MemberCard> list = memberCardService.list();
    return ResponseResultGenerator.success(list);
  }

  @GetMapping("/memberRightsList")
  @Operation(summary = "会员权益列表")
  public ResponseResult<List<MemberRights>> memberRightsList () {
    List<MemberRights> list = memberRightsService.list();
    return ResponseResultGenerator.success(list);
  }

  @DeleteMapping("/memberRights/{id}")
  @Operation(summary = "删除权益")
  public ResponseResult<?> memberRightsDel (@PathVariable Long id) {
    memberRightsService.removeById(id);
    return ResponseResultGenerator.success();
  }


  @PostMapping("/memberConfigSaveOrUpdate")
  @Operation(summary = "修改或增加会员卡")
  public ResponseResult<?> memberConfigSaveOrUpdate (@RequestBody MemberConfigDTO memberConfigDTO) {
    MemberCard memberCard = new MemberCard();
    BeanUtils.copyProperties(memberConfigDTO, memberCard);
    return memberCardService.saveOrUpdate(memberCard) ? ResponseResultGenerator.success() : ResponseResultGenerator.error();
  }

  @DeleteMapping("/memberConfig/{id}")
  @Operation(summary = "删除会员")
  public ResponseResult<?> memberConfigDel (@PathVariable Long id) {
    return memberCardService.removeById(id) ? ResponseResultGenerator.success() : ResponseResultGenerator.error();
  }

  @PostMapping("/memberRightsConfigSaveOrUpdate")
  @Operation(summary = "修改或增加会员权益")
  public ResponseResult<?> memberRightsConfigSaveOrUpdate (@RequestBody MemberRights memberRights) {

    String memberCode = memberRights.getMemberCode();
    MemberRights memberRightsServiceByMemberCode = memberRightsService.getByMemberCode(memberCode);
    if (memberRights.getId() == null && memberRightsServiceByMemberCode != null) {
      return ResponseResultGenerator.result(ResponseEnum.MEMBER_RIGHTS_EXIST);
    }

    memberRightsService.saveOrUpdate(memberRights);
    return ResponseResultGenerator.success();
  }

  @DeleteMapping("/advertiseConfig/{id}")
  @Operation(summary = "删除活动")
  public ResponseResult<?> advertiseConfig (@PathVariable Long id) {
    return advertiseConfigService.removeById(id) ? ResponseResultGenerator.success() : ResponseResultGenerator.error();
  }

  @PostMapping("/advertiseConfigSaveOrUpdate")
  @Operation(summary = "修改或增加广告配置")
  public ResponseResult<?> advertiseConfigSaveOrUpdate (@RequestBody AdvertiseConfig advertiseConfig) {

    advertiseConfigService.saveOrUpdate(advertiseConfig);
    return ResponseResultGenerator.success();
  }


  @Operation(summary = "网站设置查询")
  @GetMapping("/getSetting")
  public ResponseResult<SaveSettingDTO> getSetting () {
    return ResponseResultGenerator.success(CommonUtils.getPackageWebConfig());
  }

  @Operation(summary = "网站设置")
  @PostMapping("/saveSetting")
  public ResponseResult<?> saveSetting (@RequestBody SaveSettingDTO saveSettingDTO) {

    StringRedisUtils.set(RedisKeyEnum.WEB_CONFIG.getKey(), JSONUtil.toJsonStr(saveSettingDTO));

    return ResponseResultGenerator.success();
  }

  @Operation(summary = "上拦列表")
  @GetMapping("/topBarConfig")
  public ResponseResult<List<TopBarConfig>> topBarConfig () {
    List<TopBarConfig> list = topBarConfigService.list();
    return ResponseResultGenerator.success(list);
  }


  @Operation(summary = "上拦按钮配置")
  @PostMapping("/topBarConfigSaveOrUpdate")
  public ResponseResult<?> topBarConfigSaveOrUpdate (@RequestBody TopBarConfig topBarConfig) {
    topBarConfigService.saveOrUpdate(topBarConfig);
    return ResponseResultGenerator.success();
  }

  @Operation(summary = "删除上拦按钮配置")
  @DeleteMapping("/topBarConfig/{id}")
  public ResponseResult<?> topBarConfig (@PathVariable Long id) {
    return topBarConfigService.removeById(id) ? ResponseResultGenerator.success() : ResponseResultGenerator.error();
  }

  @Operation(summary = "登录类型列表")
  @GetMapping("/loginType")
  public ResponseResult<List<Dict<Integer>>> loginType () {
    List<Dict<Integer>> result = Arrays.stream(LoginTypeEnum.values())
        .map(loginTypeEnum -> new Dict<>(loginTypeEnum.getType(), loginTypeEnum.getDesc())).collect(Collectors.toList());
    return ResponseResultGenerator.success(result);
  }

  @Operation(summary = "获取chat配置")
  @GetMapping("/chatConfig")
  public ResponseResult<ChatConfigEntity> chatConfig () {
    String v = StringRedisUtils.get(RedisKeyEnum.CHAT_CONFIG.getKey());
    return ResponseResultGenerator.success(JSONUtil.toBean(v, ChatConfigEntity.class));
  }

  @Operation(summary = "chat配置添加或修改")
  @PostMapping("/saveOrUpdateChatConfig")
  public ResponseResult<?> saveOrUpdateChatConfig (@RequestBody ChatConfigEntity chatConfig) {
    StringRedisUtils.set(RedisKeyEnum.CHAT_CONFIG.getKey(), JSONUtil.toJsonStr(chatConfig));
    return ResponseResultGenerator.success();
  }
//
//  @Operation(summary = "查询余额")
//  @PostMapping("/chat")
//  public ResponseResult<?> saveOrUpdateChatConfig (String key) {
//    openAiStreamClient.subscription()
//    StringRedisUtils.set(RedisKeyEnum.WEB_CONFIG.getKey(), JSONUtil.toJsonStr(chatConfig));
//    return ResponseResultGenerator.success();
//  }

  @Operation(summary = "GPT模型列表")
  @GetMapping("/chatModel")
  public ResponseResult<List<Dict<String>>> chatModel () {
    List<Dict<String>> collect = Arrays.stream(Model.values()).map(model -> {
      Dict<String> dict = new Dict<>();
      dict.setKey(model.name());
      dict.setValue(model.getName());
      return dict;
    }).collect(Collectors.toList());

    return ResponseResultGenerator.success(collect);
  }

  @Operation(summary = "注册用户趋势图")
  @GetMapping("/registerUserTrend")
  public ResponseResult<List<TrendVO>> registerUserTrend (Integer day) {

    if (day == null) {
      day = 7;
    }

    List<TrendVO> list = userInfoService.trend(day);
    return ResponseResultGenerator.success(list);
  }

  @Operation(summary = "提问趋势图")
  @GetMapping("/qATrend")
  public ResponseResult<List<TrendVO>> qATrend (Integer day) {

    if (day == null) {
      day = 7;
    }

    List<TrendVO> list = chatDetailLogService.trend(day);
    return ResponseResultGenerator.success(list);
  }


  @Operation(summary = "支付趋势图")
  @GetMapping("/payTrend")
  public ResponseResult<List<TrendVO<Integer>>> payTrend (Integer day, Integer payState) {

    if (day == null) {
      day = 7;
    }

    List<TrendVO<Integer>> list = paymentInfoService.trend(day, payState);
    return ResponseResultGenerator.success(list);
  }

  @Operation(summary = "支付趋势图")
  @GetMapping("/payAmountTrend")
  public ResponseResult<List<TrendVO<BigDecimal>>> payAmountTrend (Integer day) {

    if (day == null) {
      day = 7;
    }

    List<TrendVO<BigDecimal>> list = paymentInfoService.payAmountTrend(day);
    return ResponseResultGenerator.success(list);
  }

}
