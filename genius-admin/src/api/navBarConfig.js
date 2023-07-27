/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-05-06 21:36:59
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-06 22:03:32
 */
import request from "@/utils/request";

// 查询配置列表
export function topBarConfigList() {
  return request({
    url: "/api/manager/topBarConfig",
    method: "get",
  });
}

// 新增配置
export function topBarConfigSaveOrUpdate(data) {
  return request({
    url: "/api/manager/topBarConfigSaveOrUpdate",
    method: "post",
    data
  });
}


// 删除配置
export function topBarConfigDelete(id) {
  return request({
    url: `/api/manager/topBarConfig/${id}`,
    method: "delete",
  });
}
