/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-03-29 17:36:11
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-05 15:28:26
 */
import request from "@/request/index.js";

export function questions(params) {
    return request({
        url: "/api/chat/questions",
        method: "get",
        params,
    });
}
// 查询会话
export function sessionRecordSidebar() {
    return request({
        url: "/api/session/sessionRecordSidebar",
        method: "get",

    });
}

// 新建会话

export function createSession() {
    return request({
        url: "/api/session/createSession",
        method: "post",
    });
}

// 查询会话当前内容
export function getSessionDetail(params) {
    return request({
        url: "/api/session/sessionDetail",
        method: "get",
        params
    });
}

// 删除会话
export function removeSession(params) {
    return request({
        url: "/api/session/removeSession",
        method: "delete",
        params
    });
}

// 获取广告列表
export function getAdvertiseList() {
    return request({
        url: "/api/advertise/list",
        method: "get",
    });
}