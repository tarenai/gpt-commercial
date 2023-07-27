/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-30 11:03:41
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-30 15:20:57
 */

import request from "@/utils/request";

export function login(params) {
  return request({
    url: "/api/manager/login",
    method: "get",
    params,
  });
}
// 退出登录
export function logout() {
  return request({
    url: "/api/manager/logout",
    method: "get",

  });
}
