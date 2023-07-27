/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-08-18 16:42:46
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2022-08-18 17:09:00
 */
import request from "@/utils/request";

export function getList(params) {
  return request({
    url: "/vue-admin-template/table/list",
    method: "get",
    params,
  });
}
