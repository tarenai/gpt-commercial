/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-11 09:38:28
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-11 10:18:38
 */
import React from "react";
import "./index.css";
const CustomInput = ({
  type = "text",
  name = "",
  pattern = "",
  placeholder = "请输入内容",
  maxLength = 20,
}) => {
  return (
    <input
      name={name}
      className="custom_input"
      pattern={pattern}
      maxLength={maxLength}
      placeholder={placeholder}
    />
  );
};

export default CustomInput;
