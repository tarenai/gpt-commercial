package com.warape.aimechanician.config;

import com.warape.aimechanician.config.properties.WxMpProperties;
import com.warape.aimechanician.handler.wechat.LogHandler;
import com.warape.aimechanician.handler.wechat.ScanHandler;
import com.warape.aimechanician.handler.wechat.SubscribeHandler;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts.EventType;
import me.chanjar.weixin.common.redis.RedissonWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;


/**
 * wechat mp configuration
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfiguration {

  private final LogHandler logHandler;
  private final WxMpProperties properties;
  private final RedissonClient redissonClient;
  private final SubscribeHandler subscribeHandler;
  private final ScanHandler scanHandler;

  @Bean
  public WxMpService wxMpService () {
    WxMpService service = new WxMpServiceImpl();
    String appId = properties.getAppId();
    WxMpDefaultConfigImpl configStorage;
    if (this.properties.isUseRedis()) {
      configStorage = new WxMpRedisConfigImpl(new RedissonWxRedisOps(redissonClient), appId);
    } else {
      configStorage = new WxMpDefaultConfigImpl();
    }

    configStorage.setAppId(appId);
    configStorage.setSecret(properties.getSecret());
    configStorage.setToken(properties.getToken());
    configStorage.setAesKey(properties.getAesKey());
    service.setWxMpConfigStorage(configStorage);
    return service;
  }

  @Bean
  public WxMpMessageRouter messageRouter (WxMpService wxMpService) {
    final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

    // 记录所有事件的日志 （异步执行）
    newRouter.rule().async(false).handler(this.logHandler).next();

//        // 接收客服会话管理事件
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
//            .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
//            .handler(this.kfSessionHandler).end();
//        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
//            .handler(this.kfSessionHandler).end();

//        // 门店审核事件
//        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();
//
//        // 自定义菜单事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.CLICK).handler(this.menuHandler).end();
//
//        // 点击菜单连接事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.VIEW).handler(this.nullHandler).end();
//
//        // 关注事件
    newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();
//
//        // 取消关注事件
//        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();
//
//        // 上报地理位置事件
//        newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();
//
//        // 接收地理位置消息
//        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();
//
//        // 扫码事件
    newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();
//
//        // 默认
//        newRouter.rule().async(false).handler(this.msgHandler).end();

    return newRouter;
  }

}
