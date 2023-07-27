package com.warape.aimechanician.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.warape.aimechanician.config.properties.AliPayProperties;
import com.warape.aimechanician.domain.Constants.AliPayStateEnum;
import com.warape.aimechanician.service.PaymentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apeto
 * @create 2023/4/13 19:17
 */
@Tag(name = "支付宝网关")
@Slf4j
@RestController
@RequestMapping("/api/ali")
public class AliGatewayController {

  @Autowired
  private PaymentInfoService paymentInfoService;
  @Autowired
  private AliPayProperties aliPayProperties;

  @PostMapping("/gateway")
  public Object gateway (HttpServletRequest request) {

    Map<String, String> map = convertToMap(request);
    log.info("支付宝网关回调参数:{}", JSONUtil.toJsonStr(map));
    return "SUCCESS";
  }

  @Operation(description = "支付回调")
  @RequestMapping("/precreatePayCallback")
  public Object precreatePayCallback (HttpServletRequest request) {
    Map<String, String> map = convertToMap(request);
    log.info("支付宝扫码付支付回调结果:{}", JSONUtil.toJsonStr(map));
    JSONObject jsonObject = JSONUtil.parseObj(map);
    BigDecimal payAmount = jsonObject.getBigDecimal("buyer_pay_amount");
    String tradeNo = jsonObject.getStr("trade_no");
    String outTradeNo = jsonObject.getStr("out_trade_no");
    Date payTime = jsonObject.getDate("gmt_payment");
    AliPayStateEnum aliPayStateEnum = jsonObject.getEnum(AliPayStateEnum.class, "trade_status");
    try {
      if (!AlipaySignature.rsaCheckV1(map, aliPayProperties.getAliPublicKey(), "UTF-8", "RSA2")) {
        return "failure";
      }
    } catch (AlipayApiException e) {
      log.error("支付宝扫码支付回调验签异常", e);
      return "failure";
    }
    try {
      paymentInfoService.callbackHandler(tradeNo, outTradeNo, aliPayStateEnum.getSysPayStateEnum(), payTime);
      return "success";
    } catch (Exception e) {
      return "failure";
    }

  }

  public static Map<String, String> convertToMap (HttpServletRequest request) {
    Map<String, String> params = new HashMap<>(32);
    Map<String, String[]> requestParams = request.getParameterMap();
    for (String name : requestParams.keySet()) {
      String[] values = requestParams.get(name);
      String valueStr = "";
      for (int i = 0; i < values.length; i++) {
        valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
      }
      params.put(name, valueStr);
    }
    return params;
  }
}
