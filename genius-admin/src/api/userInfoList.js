/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-30 18:04:32
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-21 16:52:04
 */
import request from "@/utils/request";

// 查看卡列表
export function userInfoList(params) {
  return request({
    url: "/api/manager/userInfoList",
    method: "get",
    params,
  })
}

// 会员充值
export function depositMember(data) {
  return request({
    url: "/api/manager/depositMember",
    method: "post",
    data,
  })
}

// 用户充值列表

export function memberExchangeFn(params) {
  return request({
    url: "/api/manager/memberExchange",
    method: "get",
    params,
  })
}