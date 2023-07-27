package com.warape.aimechanician.job;

import java.util.List;

import com.warape.aimechanician.domain.Constants.ExchangeCardStateEnum;
import com.warape.aimechanician.entity.ExchangeCardDetail;
import com.warape.aimechanician.service.ExchangeCardDetailService;
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
public class MemberJob {

  @Autowired
  private ExchangeCardDetailService exchangeCardDetailService;

  @Scheduled(cron = "5 * * * * ? ")
  public void expireMember () {
//    log.info("[开始执行]会员过期扫描定时器");
    List<ExchangeCardDetail> exchangeCardDetails = exchangeCardDetailService.selectExpire();
    for (ExchangeCardDetail exchangeCardDetail : exchangeCardDetails) {
      exchangeCardDetail.setExchangeState(ExchangeCardStateEnum.EXPIRES.getState());
    }
    exchangeCardDetailService.updateBatchById(exchangeCardDetails);

//    log.info("会员过期扫描定时器[执行完毕] 条数:{}", CollectionUtil.size(exchangeCardDetails));
  }

}
