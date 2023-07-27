import request from "@/utils/request";

// 权益列表
export function memberRightsList(params) {
  return request({
    url: "/api/manager/memberRightsList",
    method: "get",
    params,
  })
}

// 新建权益
export function memberRightsConfigSaveOrUpdate(data) {
  return request({
    url: "/api/manager/memberRightsConfigSaveOrUpdate",
    method: "post",
    data,
  })
}

// 删除权益
export function memberRightsDelete(id) {
  return request({
    url: `/api/manager/memberRights/${id}`,
    method: "delete",
  })
}
