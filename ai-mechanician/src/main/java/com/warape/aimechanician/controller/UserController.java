package com.warape.aimechanician.controller;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.warape.aimechanician.annotations.DistributedLock;
import com.warape.aimechanician.domain.CommonRespCode;
import com.warape.aimechanician.domain.Constants.LoginTypeEnum;
import com.warape.aimechanician.domain.Constants.ResponseEnum;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.dto.*;
import com.warape.aimechanician.domain.vo.UserInfoVo;
import com.warape.aimechanician.entity.UserInfo;
import com.warape.aimechanician.entity.WechatUserInfo;
import com.warape.aimechanician.handler.CommonHandler;
import com.warape.aimechanician.service.InviteLogService;
import com.warape.aimechanician.service.UserInfoService;
import com.warape.aimechanician.service.WechatUserInfoService;
import com.warape.aimechanician.utils.CommonUtils;
import com.warape.aimechanician.utils.RedissonUtils;
import com.warape.aimechanician.utils.StringRedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apeto
 * @create 2023/4/8 6:07 下午
 */
@Slf4j
@Tag(name = "用户相关")
@RequestMapping("/api/user")
@RestController
public class UserController {

  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private WechatUserInfoService wechatUserInfoService;
  @Autowired
  private InviteLogService inviteLogService;
  @Autowired
  private CommonHandler commonHandler;

  @Operation(summary = "校验token")
  @GetMapping("/checkToken")
  public ResponseResult<?> checkToken () {
    StpUtil.checkLogin();
    return ResponseResultGenerator.success();
  }


  @Operation(summary = "发送验证码")
  @PutMapping("/sendSms")
  @DistributedLock(prefix = RedisKeyEnum.SMS_SIGN_UP_LOCK, key = "#sendSmsDto.getSendAccount()", waitFor = 0)
  public ResponseResult<?> sendSms (@Validated @RequestBody SendSmsDTO sendSmsDto) {

    String sendAccount = sendSmsDto.getSendAccount();
    String type = sendSmsDto.getType();
    Integer sendType = sendSmsDto.getSendType();
    if (!StrUtil.equalsAny(type, "1", "2")) {
      return ResponseResultGenerator.result(CommonRespCode.VALID_SERVICE_ILLEGAL_ARGUMENT);
    }

    if (sendType == null) {
      // 默认邮箱
      sendType = LoginTypeEnum.EMAIL.getType();
    }

    String key = RedisKeyEnum.SMS_LOCK.getKey(sendAccount);
    if (!RedissonUtils.tryLockNotWaitBoolean(key)) {
      return ResponseResultGenerator.result(CommonRespCode.REQUEST_NOT_SUPPORT);
    }
    try {

      String smsCodeCountLimitKey = RedisKeyEnum.SMS_CODE_COUNT_LIMIT.getKey(sendAccount, type);
      if (StringRedisUtils.exists(smsCodeCountLimitKey)) {
        return ResponseResultGenerator.result(CommonRespCode.REQUEST_FREQUENTLY);
      }

      RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
      String generate = randomGenerator.generate();

      if (sendType.equals(LoginTypeEnum.PHONE.getType()) && !PhoneUtil.isMobile(sendAccount)) {
        return ResponseResultGenerator.result(4001, "手机号格式错误");
      }

      if (sendType.equals(LoginTypeEnum.EMAIL.getType()) && !Validator.isEmail(sendAccount)) {
        return ResponseResultGenerator.result(4002, "邮箱格式错误");
      }

      commonHandler.sendCode(generate, sendAccount, type, LoginTypeEnum.getByEnum(sendType));
      return ResponseResultGenerator.success();
    } finally {
      RedissonUtils.unlock(key);
    }


  }

  @Operation(summary = "图片验证码")
  @GetMapping("/imageVerificationCode")
  public ResponseResult<?> imageVerificationCode (HttpServletResponse response, @RequestParam("phone") String phone) throws IOException {
    // 自定义纯数字的验证码（随机4位数字，可重复）
    RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
    LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
    lineCaptcha.setGenerator(randomGenerator);
    // 重新生成code
    lineCaptcha.createCode();
    ServletOutputStream outputStream = response.getOutputStream();
    lineCaptcha.write(outputStream);
    outputStream.flush();
    outputStream.close();
    StringRedisUtils.setForTimeMIN(RedisKeyEnum.SMS_IMAGE_CODE.getKey(phone), lineCaptcha.getCode(), 5);
    return ResponseResultGenerator.success();
  }


  @Operation(summary = "校验图片验证码")
  @PostMapping("/checkImageCode")
  public ResponseResult<?> checkImageCode(@RequestBody@Validated CheckImageCodeDTO checkImageCodeDTO){
    String sysImageCode = StringRedisUtils.get(RedisKeyEnum.SMS_IMAGE_CODE.getKey(checkImageCodeDTO.getAccountNum()));
    if(StrUtil.isBlank(sysImageCode)){
      return ResponseResultGenerator.result(CommonRespCode.IMAGE_CODE_EXPIRE);
    }
    if (!StrUtil.equals(sysImageCode,checkImageCodeDTO.getCode())) {
      return ResponseResultGenerator.result(CommonRespCode.VERIFICATION_CODE);
    }
    return ResponseResultGenerator.success();
  }

  @Operation(summary = "注册")
  @PutMapping("/smsSignUp")
  @DistributedLock(prefix = RedisKeyEnum.SMS_SIGN_UP_LOCK, key = "#smsSignUpDto.getAccountNum()", waitFor = 0)
  public ResponseResult<?> smsSignUp (@Validated @RequestBody SmsSignUpDTO smsSignUpDto) {

    String accountNum = smsSignUpDto.getAccountNum();
    String password = smsSignUpDto.getPassword();
    Integer type = smsSignUpDto.getType();
    if (type == null) {
      // 邮箱
      type = LoginTypeEnum.EMAIL.getType();
    }
    String sysImageCode = StringRedisUtils.get(RedisKeyEnum.SMS_IMAGE_CODE.getKey(accountNum));
    if(StrUtil.isBlank(sysImageCode)){
      return ResponseResultGenerator.result(CommonRespCode.IMAGE_CODE_EXPIRE);
    }
    RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
    if (!StrUtil.equals(smsSignUpDto.getImageVerificationCode(),sysImageCode)) {
      return ResponseResultGenerator.result(CommonRespCode.VERIFICATION_CODE);
    }

    String key = RedisKeyEnum.SMS_CODE.getKey(accountNum, 1);
    String smsCode = StringRedisUtils.get(key);
    if (StrUtil.isBlank(smsCode)) {
      return ResponseResultGenerator.result(CommonRespCode.SMS_CODE_EXPIRE);
    }
    if (!smsCode.equals(smsSignUpDto.getSmsCode())) {
      return ResponseResultGenerator.result(CommonRespCode.SMS_CODE_ERROR);
    }
    userInfoService.doSmsSignUp(accountNum, password, type);
    StringRedisUtils.delete(key);
    return ResponseResultGenerator.success();
  }

  @Operation(summary = "登录")
  @PostMapping("/login")
  @DistributedLock(prefix = RedisKeyEnum.SMS_SIGN_UP_LOCK, key = "#loginDto.getAccountNum()")
  public ResponseResult<String> login (@Validated @RequestBody LoginDTO loginDto) {
    Integer type = loginDto.getType();
    String accountNum = loginDto.getAccountNum();
    UserInfo userInfo;
    if (Objects.equals(type, LoginTypeEnum.EMAIL.getType())) {
      // 邮箱
      userInfo = userInfoService.getByEmail(accountNum);
    } else if (Objects.equals(type, LoginTypeEnum.PHONE.getType())) {
      userInfo = userInfoService.getByPhone(accountNum);
    } else {
      return ResponseResultGenerator.error();
    }

    if (userInfo == null) {
      return ResponseResultGenerator.result(ResponseEnum.USER_EXIST);
    }
    if (!userInfo.getUserPassword().equals(CommonUtils.md5Salt(loginDto.getPassword()))) {
      return ResponseResultGenerator.result(CommonRespCode.PASSWORD_ERROR);
    }
    Long userId = userInfo.getId();
    String token = StpUtil.createLoginSession(userId);
    inviteLogService.inviteHandler(loginDto.getInviteCode(), userId);
    return ResponseResultGenerator.success(token);
  }


  @Operation(summary = "修改密码")
  @PostMapping("/updatePassword")
  @DistributedLock(prefix = RedisKeyEnum.UPDATE_PASSWORD_LOCK, key = "#updatePasswordDto.getAccountNum()", waitFor = 0)
  public ResponseResult<?> updatePassword (@Validated @RequestBody UpdatePasswordDTO updatePasswordDto) {
    String password = updatePasswordDto.getPassword();
    String code = updatePasswordDto.getCode();
    String accountNum = updatePasswordDto.getAccountNum();
    Integer type = updatePasswordDto.getType();

    String key = RedisKeyEnum.SMS_CODE.getKey(accountNum, "2");
    String sysCode = StringRedisUtils.get(key);
    if (StrUtil.equals(sysCode, code)) {
      return ResponseResultGenerator.result(CommonRespCode.SMS_CODE_ERROR);
    }
    UserInfo userInfo;
    if (LoginTypeEnum.EMAIL.getType().equals(type)) {
      // 邮箱
      userInfo = userInfoService.getByEmail(accountNum);
    } else if (LoginTypeEnum.PHONE.getType().equals(type)) {
      // 手机号
      userInfo = userInfoService.getByPhone(accountNum);
    } else {
      return ResponseResultGenerator.error();
    }
    if(userInfo == null){
      return ResponseResultGenerator.result(ResponseEnum.USER_EXIST);
    }

    userInfo.setUserPassword(CommonUtils.md5Salt(password));
    userInfoService.updateById(userInfo);
    StringRedisUtils.delete(key);
    return ResponseResultGenerator.success();
  }

  @Operation(summary = "获取用户信息")
  @GetMapping("/userInfo")
  public ResponseResult<UserInfoVo> userInfo () {
    long userId = StpUtil.getLoginIdAsLong();

    UserInfo userInfo = userInfoService.getById(userId);
    UserInfoVo vo = new UserInfoVo();
    vo.setCreateTime(userInfo.getCreateTime());
    WechatUserInfo wechatUserInfo = wechatUserInfoService.getByUserId(userId);
    if (wechatUserInfo == null) {
      String phone = userInfo.getPhone();
      if (StrUtil.isNotBlank(phone)) {

        String nikeName = DesensitizedUtil.mobilePhone(phone);
        vo.setNikeName(nikeName);
      } else {
//        String nikeName = DesensitizedUtil.email(userInfo.getEmail());
        vo.setNikeName(userInfo.getEmail());
      }
      return ResponseResultGenerator.success(vo);
    }

    vo.setNikeName(wechatUserInfo.getNickName());
    vo.setHeadImgUrl(wechatUserInfo.getHeadImgUrl());
    return ResponseResultGenerator.success(vo);

  }

  @Operation(summary = "退出登录")
  @PostMapping("/logout")
  public ResponseResult<?> logout () {
    StpUtil.logout();
    return ResponseResultGenerator.success();

  }

}
