/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-30 18:04:32
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-30 18:11:51
 */
import request from "@/utils/request";

// 查看卡列表
export function managerQlogs(params) {
  return request({
    url: "/api/manager/qLogs",
    method: "get",
    params,
  })
}
