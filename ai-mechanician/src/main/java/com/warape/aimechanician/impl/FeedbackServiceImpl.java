package com.warape.aimechanician.impl;

import com.warape.aimechanician.entity.Feedback;
import com.warape.aimechanician.mapper.FeedbackMapper;
import com.warape.aimechanician.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户反馈表 服务实现类
 * </p>
 *
 * @author warape
 * @since 2023-04-09 12:59:00
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

}
