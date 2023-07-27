package com.warape.aimechanician.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.warape.aimechanician.annotations.DistributedLock;
import com.warape.aimechanician.domain.CommonRespCode;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.dto.SendFeedback;
import com.warape.aimechanician.entity.Feedback;
import com.warape.aimechanician.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户反馈表 前端控制器
 * </p>
 *
 * @author warape
 * @since 2023-04-09 12:59:00
 */
@Tag(name = "投诉与建议")
@Slf4j
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

  @Autowired
  private FeedbackService feedbackService;

  @Operation(summary = "发送建议")
  @PutMapping("/send")
  @DistributedLock(prefix = RedisKeyEnum.FEEDBACK_SEND_LOCK, waitFor = 0, isReqUserId = true)
  public ResponseResult<?> send (@Validated @RequestBody SendFeedback sendFeedback) {

    long userId = StpUtil.getLoginIdAsLong();

    String desc = sendFeedback.getDesc();
    if (StrUtil.length(desc) > 300) {
      return ResponseResultGenerator.result(CommonRespCode.VALID_SERVICE_ILLEGAL_ARGUMENT);
    }

    LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Feedback::getUserId, userId);
    long count = feedbackService.count(queryWrapper);
    if (count > 50) {
      return ResponseResultGenerator.result(CommonRespCode.REQUEST_FREQUENTLY);
    }
    Feedback feedback = new Feedback();
    feedback.setUserId(userId);
    feedback.setProposals(desc);
    feedbackService.save(feedback);
    return ResponseResultGenerator.success();
  }
}
