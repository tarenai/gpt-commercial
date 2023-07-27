package com.warape.aimechanician.controller;

import java.util.ArrayList;
import java.util.List;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.chat.Message.Role;
import com.warape.aimechanician.annotations.MethodInfo;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.dto.WechatLoginReq;
import com.warape.aimechanician.domain.vo.TokenInfoVo;
import com.warape.aimechanician.listener.OpenAISSEEventSourceListener;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.service.InviteLogService;
import com.warape.aimechanician.service.MemberCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author apeto
 * @create 2023/3/27 14:17
 */
@Profile("!prod")
@Tag(name = "测试接口")
@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

  @Autowired
  private OpenAiStreamClient openAiStreamClient;
  @Autowired
  private InviteLogService inviteLogService;
  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;
  @Autowired
  private MemberCardService memberCardService;

  @CrossOrigin
  @GetMapping("/questions")
  public SseEmitter questions (String prompt) {
    SseEmitter sseEmitter = new SseEmitter(0l);
    List<Message> messages = new ArrayList<>();
    messages.add(Message.builder().role(Role.USER).content(prompt).build());
    OpenAISSEEventSourceListener openAIEventSourceListener = new OpenAISSEEventSourceListener(sseEmitter);
    openAIEventSourceListener.setOnComplate(msg -> {
      log.info("最终结果:{}", msg);
    });
    openAiStreamClient.streamChatCompletion(messages, openAIEventSourceListener);
    return sseEmitter;
  }

  @MethodInfo
  @Operation(summary = "测试微信注册登录")
  @PostMapping("/wechatLogin")
  public ResponseResult<TokenInfoVo> wechatLogin (@RequestBody WechatLoginReq wechatLoginReq) {
    long start = System.currentTimeMillis();
    StpUtil.login(RandomUtil.randomInt(1, 15));
    log.info("登录耗时:{}", System.currentTimeMillis() - start);

    long tokenTime = System.currentTimeMillis();
    SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
    log.info("获取token好使:{}", System.currentTimeMillis() - tokenTime);
    TokenInfoVo tokenInfo = new TokenInfoVo();
    tokenInfo.setTokenName(saTokenInfo.getTokenName());
    tokenInfo.setTokenValue(saTokenInfo.getTokenValue());
    long userIdLong = StpUtil.getLoginIdAsLong();
    inviteLogService.inviteHandler(wechatLoginReq.getInviteCode(), userIdLong);
    return ResponseResultGenerator.success(tokenInfo);
  }

  @Operation(summary = "测试充值")
  @GetMapping("/testDeposit")
  public ResponseResult<?> testDeposit (Long userId, Integer productId) {

    exchangeCardDetailService.exchange(userId, memberCardService.getById(productId));
    return ResponseResultGenerator.success();
  }

}
