package com.warape.aimechanician.domain;

import java.util.List;

import com.unfbx.chatgpt.entity.chat.ChatCompletion.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author wanmingyu
 * @create 2023/5/10 13:00
 */
@Data
@Schema(description = "ChatConfig")
public class ChatConfigEntity {

  @Schema(description = "apiKeys")
  private List<String> apiKeys;

  @Schema(description = "模型")
  public String model = Model.GPT_3_5_TURBO.getName();

  @Schema(description = "温度")
  private double temperature = 1;

  @Schema(description = "token")
  private Integer maxTokens = 2048;

  @Schema(description = "如果想要生成更有创造性和独特性的文本，可以使用presencePenalty参数")
  private double presencePenalty = 0;

  @Schema(description = "而如果想要生成更加细节丰富的、多样化的文本，可以使用frequencyPenalty参数。")
  private double frequencyPenalty = 0;

}
