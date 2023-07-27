package com.warape.aimechanician.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanmingyu
 * @create 2023/5/5 16:37
 */
@Data
@Schema(description = "字典")
@AllArgsConstructor
@NoArgsConstructor
public class Dict<T> {

  private T key;
  private String value;

}
