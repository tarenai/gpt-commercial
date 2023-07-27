package com.warape.aimechanician.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unfbx.chatgpt.entity.chat.Message;
import com.warape.aimechanician.annotations.MethodInfo;
import com.warape.aimechanician.domain.vo.SessionRecordVo;
import com.warape.aimechanician.domain.vo.TrendVO;
import com.warape.aimechanician.entity.ChatDetailLog;

/**
 * <p>
 * 问答记录表 服务类
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
public interface ChatDetailLogService extends IService<ChatDetailLog> {

  List<ChatDetailLog> selectByUserId (Long userId);


  List<SessionRecordVo> selectLastQuestion (Long userId, String chatRole);

  List<ChatDetailLog> selectByUserIdAndReqId (Long userId, String reqId);

  List<ChatDetailLog> selectByUserIdAndReqIdDesc (Long userId, String reqId);

  Long selectCountByUserIdAndReqId (Long userId, String reqId);

  void removeByReqId (Long userId,String reqId);

  void questionsCompleted (String reqId, Long userId, String prompt, String msg);

  @MethodInfo
  List<Message> getMessage (Long userId, String reqId, String prompt);

  List<TrendVO> trend (Integer day);
}
