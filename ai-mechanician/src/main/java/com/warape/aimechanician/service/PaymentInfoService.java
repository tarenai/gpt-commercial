package com.warape.aimechanician.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warape.aimechanician.domain.Constants.PayStateEnum;
import com.warape.aimechanician.domain.vo.TrendVO;
import com.warape.aimechanician.entity.PaymentInfo;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

  PaymentInfo getByOrderNo (String orderNo);

  void callbackHandler (String outPaySn, String paySn, PayStateEnum sysPayStateEnum, Date payTime);

  List<TrendVO<Integer>> trend (Integer day, Integer payState);

  List<TrendVO<BigDecimal>> payAmountTrend (Integer day);
}
