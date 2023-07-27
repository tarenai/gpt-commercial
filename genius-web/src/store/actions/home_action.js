/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-11-06 14:48:47
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-09 13:20:03
 */

import {
  MAP_TOKEN,
  USER_INFO
} from "../constants/home_constant";



// 地图token
export const mapTokenAction = (data) => {
  return {
    type: MAP_TOKEN,
    data,
  };
};

export const userInfoAction = (data) => {
  return {
    type: USER_INFO,
    data,
  };
};