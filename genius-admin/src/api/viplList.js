import request from "@/utils/request";

// 查看卡列表
export function memberList(params) {
  return request({
    url: "/api/manager/memberList",
    method: "get",
    params,
  })
}

// 新建会员卡
export function memberConfigSaveOrUpdate(data) {
  return request({
    url: "/api/manager/memberConfigSaveOrUpdate",
    method: "post",
    data,
  })
}

// 删除会员卡
export function memberConfigDelete(id) {
  return request({
    url: `/api/manager/memberConfig/${id}`,
    method: "delete",
  })
}
