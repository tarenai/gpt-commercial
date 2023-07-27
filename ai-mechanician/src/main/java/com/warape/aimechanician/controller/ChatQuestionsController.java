package com.warape.aimechanician.controller;

import java.io.IOException;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.exception.BaseException;
import com.warape.aimechanician.annotations.DistributedLock;
import com.warape.aimechanician.domain.ChatConfigEntity;
import com.warape.aimechanician.domain.CommonRespCode;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.exception.SseEmitterException;
import com.warape.aimechanician.listener.OpenAISSEEventSourceListener;
import com.warape.aimechanician.service.ChatDetailLogService;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.service.UserInfoService;
import com.warape.aimechanician.utils.StringRedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.warape.aimechanician.domain.Constants.ResponseEnum.MEMBER_LIMIT_COUNT;
import static com.warape.aimechanician.domain.Constants.ResponseEnum.NOT_LOGIN;

/**
 * @author apeto
 * @create 2023/3/27 11:23
 */
@Slf4j
@RestController
@Tag(name = "chat相关")
@RequestMapping("/api/chat")
public class ChatQuestionsController {

  @Autowired
  private ChatDetailLogService chatDetailLogService;
  @Autowired
  private OpenAiStreamClient openAiStreamClient;
  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;

  @CrossOrigin
  @Operation(summary = "问答")
  @GetMapping("/questions")
  @DistributedLock(prefix = RedisKeyEnum.QUESTIONS_LOCK, key = "#reqId", waitFor = 0, isReqUserId = true)
  public SseEmitter questions (@RequestParam("prompt") String prompt, @RequestParam("reqId") String reqId) throws IOException {

    SseEmitter sseEmitter = new SseEmitter(0L);
    try {

      if (StrUtil.isBlank(prompt)) {
        throw new SseEmitterException(CommonRespCode.VALID_SERVICE_ILLEGAL_ARGUMENT);
      }
      Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
      if (loginIdDefaultNull == null) {
        throw new SseEmitterException(NOT_LOGIN);
      }
      Long userId = Long.valueOf(loginIdDefaultNull.toString());
//      UserInfo userInfo = userInfoService.getById(userId);

//      if (userInfo.getMemberValidTime() == null || new Date().compareTo(userInfo.getMemberValidTime()) > 0) {
//        throw new SseEmitterException(MEMBER_EXP);
//      }

      if (exchangeCardDetailService.getSurplusCount(userId) == 0) {
        throw new SseEmitterException(MEMBER_LIMIT_COUNT);
      }
      OpenAISSEEventSourceListener openAIEventSourceListener = new OpenAISSEEventSourceListener(sseEmitter);
      openAIEventSourceListener.setOnComplate(msg -> chatDetailLogService.questionsCompleted(reqId, userId, prompt, msg));

      ChatConfigEntity chatConfigEntity = JSONUtil.toBean(StringRedisUtils.get(RedisKeyEnum.CHAT_CONFIG.getKey()), ChatConfigEntity.class);
      ChatCompletion completion = ChatCompletion
          .builder()
          .messages(chatDetailLogService.getMessage(userId, reqId, prompt))
          .temperature(chatConfigEntity.getTemperature())
          .maxTokens(chatConfigEntity.getMaxTokens())
          .frequencyPenalty(chatConfigEntity.getFrequencyPenalty())
          .presencePenalty(chatConfigEntity.getPresencePenalty())
          .model(chatConfigEntity.getModel())
//          .model(Model.GPT_3_5_TURBO.getName())
          .build();
      openAiStreamClient.streamChatCompletion(completion, openAIEventSourceListener);
    } catch (SseEmitterException e) {
      log.error("questions SseEmitterException", e);
      sseEmitter.send(SseEmitter.event()
          .data(e.toString())
          .reconnectTime(3000));
    } catch (BaseException e) {
      log.error("questions BaseException", e);
      sseEmitter.send(SseEmitter.event()
          .data(JSONUtil.toJsonStr(ResponseResultGenerator.result(e.getCode(), e.getMessage())))
          .reconnectTime(3000));
    } catch (Exception e) {
      log.error("questions Exception", e);
      sseEmitter.send(SseEmitter.event()
          .data(CommonRespCode.ERROR.toJsonStr())
          .reconnectTime(3000));
    }

    return sseEmitter;
  }

//
//  @Operation(description = "停止提问")
//  @GetMapping("/complete")
//  public ResponseResult<?> complete (@RequestParam("reqId") String reqId) {
//    long userId = StpUtil.getLoginIdAsLong();
//
//    SseEmitter sseEmitter = redisTemplate.opsForValue().get(RedisKeyEnum.SEE_KEY.getKey(userId, reqId));
//    if (sseEmitter != null) {
//      sseEmitter.complete();
//    }
//    return ResponseResultGenerator.success();
//  }

}
