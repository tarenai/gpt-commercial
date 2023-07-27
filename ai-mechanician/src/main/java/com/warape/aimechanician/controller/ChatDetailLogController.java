package com.warape.aimechanician.controller;

import java.util.List;
import java.util.stream.Collectors;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.unfbx.chatgpt.entity.chat.Message.Role;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.vo.SessionRecordVo;
import com.warape.aimechanician.entity.ChatDetailLog;
import com.warape.aimechanician.service.ChatDetailLogService;
import com.warape.aimechanician.utils.StringRedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 问答记录表 前端控制器
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
@Tag(name = "会话记录相关")
@RestController
@RequestMapping("/api/session")
public class ChatDetailLogController {

  @Autowired
  private ChatDetailLogService chatDetailLogService;


  @Operation(summary = "创建会话")
  @PostMapping("/createSession")
  public ResponseResult<String> createSession () {
    return ResponseResultGenerator.success(IdUtil.fastSimpleUUID());
  }


  @Operation(summary = "会话记录(侧栏)")
  @GetMapping("/sessionRecordSidebar")
  public ResponseResult<List<SessionRecordVo>> sessionRecordSidebar () {
    Long userId = StpUtil.getLoginIdAsLong();
    List<SessionRecordVo> result = chatDetailLogService.selectLastQuestion(userId, Role.USER.getName());
    return ResponseResultGenerator.success(result);
  }

  @Operation(summary = "会话记录详情")
  @GetMapping("/sessionDetail")
  public ResponseResult<List<SessionRecordVo>> sessionDetail (@RequestParam("reqId") String reqId) {
    Long userId = StpUtil.getLoginIdAsLong();
    List<ChatDetailLog> chatDetailLogs = chatDetailLogService.selectByUserIdAndReqId(userId, reqId);
    List<SessionRecordVo> result = chatDetailLogs.stream().map(chatDetailLog -> {
      SessionRecordVo sessionRecordVo = new SessionRecordVo();
      sessionRecordVo.setReqId(reqId);
      sessionRecordVo.setContent(chatDetailLog.getContent());
      sessionRecordVo.setChatRole(chatDetailLog.getChatRole());
      sessionRecordVo.setCreateTime(chatDetailLog.getCreateTime());
      return sessionRecordVo;
    }).collect(Collectors.toList());

    return ResponseResultGenerator.success(result);
  }

  @Operation(summary = "会话记录消息数量")
  @GetMapping("/sessionCount")
  public ResponseResult<Long> sessionCount (@RequestParam("reqId") String reqId) {
    return ResponseResultGenerator.success(chatDetailLogService.selectCountByUserIdAndReqId(StpUtil.getLoginIdAsLong(), reqId));
  }

  @Operation(summary = "删除会话")
  @DeleteMapping("/removeSession")
  public ResponseResult<String> removeSession (@RequestParam("reqId") String reqId) {
    Long userId = StpUtil.getLoginIdAsLong();
    String redisKey = RedisKeyEnum.QUESTIONS.getKey(userId.toString(), reqId);
    StringRedisUtils.delete(redisKey);
    chatDetailLogService.removeByReqId(userId, reqId);
    return ResponseResultGenerator.success();
  }

}
