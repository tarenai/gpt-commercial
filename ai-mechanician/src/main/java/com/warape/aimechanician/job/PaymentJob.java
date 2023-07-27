package com.warape.aimechanician.job;

import java.util.List;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.warape.aimechanician.domain.Constants.AliPayStateEnum;
import com.warape.aimechanician.domain.Constants.PayStateEnum;
import com.warape.aimechanician.entity.PaymentInfo;
import com.warape.aimechanician.service.PaymentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author apeto
 * @create 2023/4/9 5:41 下午
 */
@Slf4j
@Component
public class PaymentJob {

  @Autowired
  private PaymentInfoService paymentInfoService;
  @Autowired(required = false)
  private AlipayClient alipayClient;

  @Scheduled(cron = "5 * * * * ? ")
  public void payQuery () {
    List<PaymentInfo> list =
        paymentInfoService.lambdaQuery().eq(PaymentInfo::getPayState, PayStateEnum.PAYING.getState()).list();
    for (PaymentInfo paymentInfo : list) {
      AlipayTradeQueryModel model = new AlipayTradeQueryModel();
      model.setOutTradeNo(paymentInfo.getPaySn());
      model.setTradeNo(paymentInfo.getOutPaySn());
      AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
      request.setBizModel(model);
      try {
        AlipayTradeQueryResponse execute = alipayClient.execute(request);
        if (!execute.isSuccess()) {
          paymentInfo.setPayState(PayStateEnum.ERROR.getState());
          paymentInfoService.updateById(paymentInfo);
          continue;
        }
        PayStateEnum payStateEnum = AliPayStateEnum.getByPayStateEnum(execute.getTradeStatus());
        paymentInfoService.callbackHandler(execute.getTradeNo(), execute.getOutTradeNo(), payStateEnum, execute.getSendPayDate());
      } catch (AlipayApiException e) {
        log.error("阿里支付查询失败", e);
      }
    }
  }

}
