package com.warape.aimechanician.listener;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.chat.Message.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 描述：OpenAIEventSourceListener
 *
 * @author warape
 * @date 2023-02-22
 */
@Slf4j
public class OpenAISSEEventSourceListener extends EventSourceListener {

  private long tokens;

  private final SseEmitter sseEmitter;

  protected String lastMessage = "";


  /**
   * Called when all new message are received.
   *
   * @param message the new message
   */
  @Setter
  @Getter
  protected Consumer<String> onComplate = msg -> {

  };

  public OpenAISSEEventSourceListener (SseEmitter sseEmitter) {
    this.sseEmitter = sseEmitter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onOpen (EventSource eventSource, Response response) {
    log.info("OpenAI建立sse连接...");
  }

  /**
   * {@inheritDoc}
   */
  @SneakyThrows
  @Override
  public void onEvent (EventSource eventSource, String id, String type, String data) {

    try {
      tokens += 1;

      if (data.equals("[DONE]")) {
        onComplate.accept(lastMessage);
        sseEmitter.send(SseEmitter.event()
            .id(id)
            .data("DONE")
            .reconnectTime(3000));
        // 传输完成后自动关闭sse
        sseEmitter.complete();
        return;
      }
      ObjectMapper mapper = new ObjectMapper();
      ChatCompletionResponse completionResponse = mapper.readValue(data, ChatCompletionResponse.class); // 读取Json
      List<ChatChoice> choices = completionResponse.getChoices();
      Message delta = choices.get(0).getDelta();
      if (CollectionUtil.isEmpty(choices) || delta == null) {
        return;
      }
      String content = delta.getContent();
      if (StrUtil.isBlank(content)) {
        return;
      }
      delta.setRole(Role.ASSISTANT.getName());
      lastMessage += content;
      sseEmitter.send(SseEmitter.event()
          .id(completionResponse.getId())
          .data(delta)
          .reconnectTime(3000));
    } catch (Exception e) {
      eventSource.cancel();
      throw e;
    }

  }


  @Override
  public void onClosed (EventSource eventSource) {
    log.info("流式输出返回值总共{}tokens", tokens() - 2);
    log.info("OpenAI关闭sse连接...");
  }


  @SneakyThrows
  @Override
  public void onFailure (EventSource eventSource, Throwable t, Response response) {
    try {

      if (Objects.isNull(response)) {
        return;
      }
      ResponseBody body = response.body();
      if (Objects.nonNull(body)) {
        log.error("OpenAI  sse连接异常data：{}，异常：{}", body, t);
      } else {
        log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
      }
    } finally {
      if (StrUtil.isNotBlank(lastMessage)) {
        onComplate.accept(lastMessage);
      }
      eventSource.cancel();
    }


  }

  /**
   * tokens
   *
   * @return
   */
  public long tokens () {
    return tokens;
  }
}
