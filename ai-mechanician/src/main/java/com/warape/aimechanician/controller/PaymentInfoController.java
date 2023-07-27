package com.warape.aimechanician.controller;

import java.math.BigDecimal;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.warape.aimechanician.annotations.DistributedLock;
import com.warape.aimechanician.config.properties.AliPayProperties;
import com.warape.aimechanician.domain.Constants.PayStateEnum;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.dto.BuyMember;
import com.warape.aimechanician.domain.vo.BuyMemberVo;
import com.warape.aimechanician.entity.MemberCard;
import com.warape.aimechanician.entity.PaymentInfo;
import com.warape.aimechanician.exception.ServiceException;
import com.warape.aimechanician.service.MemberCardService;
import com.warape.aimechanician.service.PaymentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author warape
 * @since 2023-03-29 08:14:15
 */
@Slf4j
@Tag(name = "支付相关")
@RestController
@RequestMapping("/api/paymentInfo")
public class PaymentInfoController {

  @Autowired(required = false)
  private AlipayClient alipayClient;
  @Autowired
  private AliPayProperties aliPayProperties;
  @Autowired
  private MemberCardService memberCardService;
  @Autowired
  private PaymentInfoService paymentInfoService;


  @Operation(description = "查询购买结果")
  @GetMapping("/paymentQuery")
  public ResponseResult<?> paymentQuery (@RequestParam("orderNo") String orderNo) {
    StpUtil.checkLogin();
    PaymentInfo paymentInfo = paymentInfoService.getByOrderNo(orderNo);
    if (paymentInfo == null) {
      return ResponseResultGenerator.error();
    }

    return ResponseResultGenerator.success(paymentInfo.getPayState());
  }

  @Operation(description = "购买会员")
  @PostMapping("/buyMember")
  @DistributedLock(prefix = RedisKeyEnum.BUY_MEMBER_LOCK, key = "#buyMember.getMemberId()", waitFor = 0, isReqUserId = true)
  public ResponseResult<BuyMemberVo> buyMember (@RequestBody @Validated BuyMember buyMember) {

    long userId = StpUtil.getLoginIdAsLong();
    String orderNo = IdUtil.fastSimpleUUID();
    MemberCard memberCard = memberCardService.getById(buyMember.getMemberId());
    BigDecimal amount = memberCard.getAmount();
    PaymentInfo paymentInfo = new PaymentInfo();
    paymentInfo.setUserId(userId);
    paymentInfo.setPaySn(orderNo);
    paymentInfo.setOutPaySn("");
    paymentInfo.setGoodsCode(memberCard.getCardCode());
    paymentInfo.setPayAmount(amount);
    paymentInfo.setPayState(PayStateEnum.PAYING.getState());
    paymentInfo.setPayType(buyMember.getPayType());
    paymentInfo.setPayMerchant("");
    if (!paymentInfoService.save(paymentInfo)) {
      return ResponseResultGenerator.error();
    }

    AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
    model.setOutTradeNo(orderNo);

    model.setTotalAmount(amount.toPlainString());
    model.setSubject("商品");
    AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
    request.setBizModel(model);
    request.setNotifyUrl(aliPayProperties.getPayCallbackUrl());
    try {
      AlipayTradePrecreateResponse execute = alipayClient.execute(request);
      if (!execute.isSuccess()) {
        return ResponseResultGenerator.error();
      }
      BuyMemberVo vo = new BuyMemberVo();
      vo.setQrCode(execute.getQrCode());
      vo.setOrderNo(orderNo);
      return ResponseResultGenerator.success(vo);
    } catch (AlipayApiException e) {
      throw new ServiceException(500, e.getMessage());
    }

  }


}
