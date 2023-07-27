/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-15 11:17:39
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-19 14:41:24
 */
import request from "@/utils/request";
// 用户注册趋势图
export function registerUserTrend(params) {
  return request({
    url: "/api/manager/registerUserTrend",
    method: "get",
    params,
  });
}


// 用户提问趋势图
export function qATrend(params) {
  return request({
    url: "/api/manager/qATrend",
    method: "get",
    params,
  });
}

// 用户支付趋势图
export function payTrend(params) {
  return request({
    url: "/api/manager/payTrend",
    method: "get",
    params,
  });
}

// 用户成功支付金额趋势图
export function payAmountTrend(params) {
  return request({
    url: "/api/manager/payAmountTrend",
    method: "get",
    params,
  });
}
