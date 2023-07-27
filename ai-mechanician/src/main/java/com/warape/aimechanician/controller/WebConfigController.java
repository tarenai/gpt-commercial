package com.warape.aimechanician.controller;

import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import com.warape.aimechanician.domain.Constants.LoginTypeEnum;
import com.warape.aimechanician.domain.ResponseResult;
import com.warape.aimechanician.domain.ResponseResultGenerator;
import com.warape.aimechanician.domain.SystemConstants.RedisKeyEnum;
import com.warape.aimechanician.domain.dto.SaveSettingDTO;
import com.warape.aimechanician.utils.CommonUtils;
import com.warape.aimechanician.utils.StringRedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanmingyu
 * @create 2023/5/5 14:03
 */
@Tag(name = "站点信息")
@Slf4j
@RestController
@RequestMapping("/api/web")
public class WebConfigController {

  @GetMapping("/setting")
  @Operation(description = "获取网站设置")
  public ResponseResult<SaveSettingDTO> setting () {
    return ResponseResultGenerator.success(CommonUtils.getPackageWebConfig());
  }

}
