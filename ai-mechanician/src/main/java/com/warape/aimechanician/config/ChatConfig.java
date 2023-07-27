package com.warape.aimechanician.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.OpenAiStreamClient.Builder;
import com.unfbx.chatgpt.interceptor.DynamicKeyOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.warape.aimechanician.config.properties.ChatConfigProperties;
import com.warape.aimechanician.domain.ChatConfigEntity;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.utils.StringRedisUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author apeto
 * @create 2023/3/31 9:11 下午
 */
@Configuration
public class ChatConfig {

  @Autowired
  private ChatConfigProperties chatConfigProperties;
  @Autowired
  private MyKeyStrategy myKeyStrategy;

  @Bean
  public OpenAiStreamClient openAiStreamClient () {
    List<String> apiKeys = chatConfigProperties.getApiKeys();
    List<String> keys = myKeyStrategy.getKeys();
    if (CollUtil.isEmpty(keys)) {
      ChatConfigEntity chatConfigEntity = new ChatConfigEntity();
      chatConfigEntity.setApiKeys(apiKeys);
      StringRedisUtils.set(RedisKeyEnum.CHAT_CONFIG.getKey(), JSONUtil.toJsonStr(chatConfigEntity));
    }

    String apiHost = chatConfigProperties.getApiHost();
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(600, TimeUnit.SECONDS)
        .readTimeout(600, TimeUnit.SECONDS);

    Builder builder = OpenAiStreamClient.builder().apiHost(apiHost).keyStrategy(myKeyStrategy).authInterceptor(new DynamicKeyOpenAiAuthInterceptor(myKeyStrategy));
    if (SpringUtil.getProperty("spring.profiles.active").equals("local")) {
      // 代理
      Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
      okHttpClientBuilder.proxy(proxy);
    }
    builder.okHttpClient(okHttpClientBuilder.build());
    return builder.build();
  }


}
