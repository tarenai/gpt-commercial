package com.warape.aimechanician.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.chat.Message.Role;
import com.warape.aimechanician.annotations.MethodInfo;
import com.warape.aimechanician.domain.Constants;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.vo.SessionRecordVo;
import com.warape.aimechanician.domain.vo.TrendVO;
import com.warape.aimechanician.entity.ChatDetailLog;
import com.warape.aimechanician.mapper.ChatDetailLogMapper;
import com.warape.aimechanician.service.ChatDetailLogService;
import com.warape.aimechanician.service.ExchangeCardDetailService;
import com.warape.aimechanician.utils.StringRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.warape.aimechanician.domain.Constants.CONTEXT_SPLIT;

/**
 * <p>
 * 问答记录表 服务实现类
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
@Slf4j
@Service
public class ChatDetailLogServiceImpl extends ServiceImpl<ChatDetailLogMapper, ChatDetailLog> implements ChatDetailLogService {

  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;
  @Autowired
  private SaTokenConfig saTokenConfig;

  @Override
  public List<ChatDetailLog> selectByUserId (Long userId) {
    LambdaQueryWrapper<ChatDetailLog> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatDetailLog::getUserId, userId);
    queryWrapper.orderByDesc(ChatDetailLog::getId);
    queryWrapper.last("limit 5");
    return baseMapper.selectList(queryWrapper);
  }

  @Override
  public List<SessionRecordVo> selectLastQuestion (Long userId, String chatRole) {

    return baseMapper.selectLastQuestion(userId, chatRole);
  }

  @Override
  public List<ChatDetailLog> selectByUserIdAndReqId (Long userId, String reqId) {
    LambdaQueryWrapper<ChatDetailLog> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatDetailLog::getUserId, userId);
    queryWrapper.eq(ChatDetailLog::getRequestId, reqId);
    queryWrapper.orderByAsc(ChatDetailLog::getId);
    return baseMapper.selectList(queryWrapper);
  }

  @Override
  public List<ChatDetailLog> selectByUserIdAndReqIdDesc (Long userId, String reqId) {
    LambdaQueryWrapper<ChatDetailLog> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatDetailLog::getUserId, userId);
    queryWrapper.eq(ChatDetailLog::getRequestId, reqId);
    queryWrapper.orderByDesc(ChatDetailLog::getId);
    queryWrapper.last("limit 10");
    return baseMapper.selectList(queryWrapper);
  }

  @Override
  public Long selectCountByUserIdAndReqId (Long userId, String reqId) {
    LambdaQueryWrapper<ChatDetailLog> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatDetailLog::getUserId, userId);
    queryWrapper.eq(ChatDetailLog::getRequestId, reqId);
    return baseMapper.selectCount(queryWrapper);
  }

  @Override
  public void removeByReqId (Long userId, String reqId) {
    LambdaQueryWrapper<ChatDetailLog> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatDetailLog::getRequestId, reqId);
    queryWrapper.eq(ChatDetailLog::getUserId, userId);
    remove(queryWrapper);
  }

  @Async("chat")
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void questionsCompleted (String reqId, Long userId, String prompt, String msg) {

    String redisKey = RedisKeyEnum.QUESTIONS.getKey(userId.toString(), reqId);

    ChatDetailLog askUserEntity = new ChatDetailLog();
    askUserEntity.setUserId(userId);
    askUserEntity.setRequestId(reqId);
    askUserEntity.setChatRole(Role.USER.getName());
    askUserEntity.setContent(prompt);
    if (!save(askUserEntity)) {
      log.error("chatDetailLogService save is user fail userId:{} reqId:{}", userId, reqId);
    }

    // 提问放入缓存
    for (String splitStr : StrUtil.split(prompt, Constants.CONTEXT_SPLIT)) {
      StringRedisUtils.add(redisKey, JSONUtil.toJsonStr(new Message(Role.USER.getName(), splitStr, userId.toString())), askUserEntity.getId());
    }

    ChatDetailLog answerEntity = new ChatDetailLog();
    answerEntity.setUserId(userId);
    answerEntity.setRequestId(reqId);
    answerEntity.setChatRole(Role.ASSISTANT.getName());
    answerEntity.setContent(msg);
    if (!save(answerEntity)) {
      log.error("chatDetailLogService save assistant is fail userId:{} reqId:{}", userId, reqId);
    }
    // 回答放入缓存
    for (String splitStr : StrUtil.split(msg, Constants.CONTEXT_SPLIT)) {

      StringRedisUtils.add(redisKey, JSONUtil.toJsonStr(new Message(Role.ASSISTANT.getName(), splitStr, userId.toString())), answerEntity.getId());
    }

    Integer consumeCount = 1;
    // 消费卡
    exchangeCardDetailService.consume(userId, consumeCount);
    // 设置过期时间
    StringRedisUtils.expireSeconds(redisKey, saTokenConfig.getTimeout() / 2);
  }

  @MethodInfo
  @Override
  public List<Message> getMessage (Long userId, String reqId, String prompt) {

    String redisKey = RedisKeyEnum.QUESTIONS.getKey(userId.toString(), reqId);

    int limit = Constants.CONTEXT_LIMIT;

    List<Message> messages = new ArrayList<>();

    Set<String> strings = StringRedisUtils.reverseRangeByScore(redisKey, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, limit);
    if (CollectionUtil.isNotEmpty(strings)) {
      for (String str : strings) {
        messages.add(0, JSONUtil.toBean(str, Message.class));
      }
    } else {
      List<ChatDetailLog> chatDetailLogs = selectByUserIdAndReqIdDesc(userId, reqId);
      for (ChatDetailLog chatDetailLog : chatDetailLogs) {
        String content = chatDetailLog.getContent();
        String chatRole = chatDetailLog.getChatRole();
        for (String splitContent : StrUtil.split(content, CONTEXT_SPLIT)) {
          Message message = new Message(chatRole, splitContent, userId.toString());
          if (messages.size() >= limit) {
            break;
          }
          messages.add(0, message);
          StringRedisUtils.add(redisKey, JSONUtil.toJsonStr(message), chatDetailLog.getId());
        }
      }
    }

    messages.add(Message.builder().role(Role.USER).content(prompt).build());
    return messages;
  }

  @Override
  public List<TrendVO> trend (Integer day) {
    Date end = new Date();
    DateTime start = DateUtil.offsetDay(end, -day);
    if (day == 0) {
      start = DateUtil.beginOfDay(end);
      end = DateUtil.endOfDay(end);
    }
    return baseMapper.trend(start,end);
  }
}
