package com.warape.aimechanician.config;

import java.util.Objects;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 填充器
 * 使用属性值不是数据库真实字段
 * insertFill 在首次保存数据时填充值，更新数据时不改变；
 * updateFill 在更新数据时被设置的字段更新值；
 *
 * @author duanwj
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill (MetaObject metaObject) {
    if(Objects.isNull(getFieldValByName("createBy", metaObject))){
      setFieldValByName("createBy","system",metaObject);
    }
    if(Objects.isNull(getFieldValByName("updateBy", metaObject))){
      setFieldValByName("updateBy","system",metaObject);
    }
    if(Objects.isNull(getFieldValByName("createTime", metaObject))){
      setFieldValByName("createTime", DateUtil.date(), metaObject);
    }
    if(Objects.isNull(getFieldValByName("updateTime", metaObject))){
      setFieldValByName("updateTime", DateUtil.date(), metaObject);
    }
    setFieldValByName("yn", 1, metaObject);
  }

  @Override
  public void updateFill (MetaObject metaObject) {
    if(Objects.isNull(getFieldValByName("updateBy", metaObject))){
      setFieldValByName("updateBy","system",metaObject);
    }
    setFieldValByName("updateTime", DateUtil.date(), metaObject);
  }
}
