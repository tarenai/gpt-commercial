/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-10-12 15:26:42
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2022-10-12 15:26:54
 */
import request from "@/request/index.js";

export function login(data) {
  return request({
    url: "/api/login",
    method: "post",
    data,
  });
}
