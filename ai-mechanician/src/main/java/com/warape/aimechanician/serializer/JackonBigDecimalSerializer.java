package com.warape.aimechanician.serializer;

import java.io.IOException;
import java.math.BigDecimal;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author apeto
 * @create 2023/4/4 17:20
 */
public class JackonBigDecimalSerializer extends JsonSerializer<BigDecimal> {


  @Override
  public void serialize (BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    if (bigDecimal == null) {
      jsonGenerator.writeNull();
    } else {
      jsonGenerator.writeString(NumberUtil.decimalFormat("0", bigDecimal));
    }
  }
}
