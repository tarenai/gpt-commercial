package com.warape.aimechanician.handler.wechat;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@Component
public class ScanHandler extends AbstractHandler {

  @Override
  public WxMpXmlOutMessage handle (WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
      WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
    // 扫码事件处理
      log.info("扫码事件");
    return null;
  }
}
