package com.warape.aimechanician.config;

import java.util.List;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.function.KeyStrategy;
import com.warape.aimechanician.domain.ChatConfigEntity;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.utils.CommonUtils;
import com.warape.aimechanician.utils.StringRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wanmingyu
 * @create 2023/5/16 14:57
 */
@Slf4j
@Component
public class MyKeyStrategy implements KeyStrategy {

  @Override
  public String strategy () {
    return RandomUtil.randomEle(getKeys());
  }

  @Override
  public List<String> getKeys () {
    ChatConfigEntity chatConfigEntity = JSONUtil.toBean(StringRedisUtils.get(RedisKeyEnum.CHAT_CONFIG.getKey()), ChatConfigEntity.class);
    return chatConfigEntity.getApiKeys();
  }

  @Override
  public void keysWarring () {
    log.error("所有key都已失效~请更换");
    CommonUtils.sendEmail(null, "服务异常", "所有key都已失效~请及时更换");
  }

  @Override
  public void removeErrorKey (String key) {
    log.error("当前key:{} 已失效 自动删除中~", key);
    CommonUtils.sendEmail(null, "服务异常", StrUtil.format("当前key:{} 已失效 自动删除中~", key));
    ChatConfigEntity chatConfigEntity = JSONUtil.toBean(StringRedisUtils.get(RedisKeyEnum.CHAT_CONFIG.getKey()), ChatConfigEntity.class);
    chatConfigEntity.getApiKeys().remove(key);
    StringRedisUtils.set(RedisKeyEnum.CHAT_CONFIG.getKey(), JSONUtil.toJsonStr(chatConfigEntity));
  }
}
