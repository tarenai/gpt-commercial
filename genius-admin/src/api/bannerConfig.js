/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-06 12:28:04
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-06 12:38:20
 */

import request from "@/utils/request";

// 查询广告列表
export function advertiseList() {
  return request({
    url: "/api/advertise/list",
    method: "get",
  });
}

// 新建广告
export function advertiseConfigSaveOrUpdate(data) {
  return request({
    url: '/api/manager/advertiseConfigSaveOrUpdate',
    method: "post",
    data
  });
}



// 删除广告
export function deleteAdvertise(id) {
  return request({
    url: `/api/manager/advertiseConfig/${id}`,
    method: "delete",
  });
}
