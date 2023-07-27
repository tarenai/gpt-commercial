import request from "@/utils/request";

// 查询模型
export function chatModelList() {
  return request({
    url: "/api/manager/chatModel",
    method: "get",
  });
}
// 保存配置
export function saveOrUpdateChatConfig(data) {
  return request({
    url: "/api/manager/saveOrUpdateChatConfig",
    method: "post",
    data
  });
}

// 获取已经保存的配置
export function getChatConfig() {
  return request({
    url: "/api/manager/chatConfig",
    method: "get"
  });
}
