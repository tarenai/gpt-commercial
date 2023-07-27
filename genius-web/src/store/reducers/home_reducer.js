/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-11-06 13:52:22
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-03-15 09:06:24
 */


import moment from "moment";
import {
  MAP_TOKEN,
  USER_INFO
} from "../constants/home_constant";

const defaultVal = {
  // 地图token
  mapToken: "测试数据",
  userInfo: null
};
// 进行默认值赋值
const home_reducer = (state = defaultVal, action) => {
  const {
    type,
    data
  } = action;
  switch (type) {
    case MAP_TOKEN:
      return {
        ...state,
        mapToken: data,
      };
    case USER_INFO:
      return {
        ...state,
        userInfo: data,
      };
    default:
      // 当我们第一次初始化时需要将默认值返回出去
      return state;
  }
};

export default home_reducer;