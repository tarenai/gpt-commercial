package com.warape.aimechanician.controller;

import java.io.InputStream;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apeto
 * @create 2023/4/9 5:14 下午
 */
@Slf4j
@Tag(name = "模板相关")
@Data
@RestController
@RequestMapping("/api/prompt")
public class PromptTemplateController {


  @Operation(summary = "模板列表")
  @GetMapping("/promptsTemplate")
  public ResponseResult<JSONArray> promptsTemplate () {
    try (InputStream stream = new ClassPathResource("prompts-zh.json").getStream()) {
      JSONArray objects = JSONUtil.parseArray(stream);
      return ResponseResultGenerator.success(objects);
    } catch (Exception e) {
      log.error("读取异常", e);
    }
    return ResponseResultGenerator.error();
  }

}
