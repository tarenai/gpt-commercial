/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-06 09:30:54
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-17 17:18:06
 */
import request from "@/request/index.js";

// 邀请有礼
export function inviteGiftUrlInfo() {
    return request({
        url: "api/activity/inviteGiftUrlInfo",
        method: "get",
    });
}

// 获取用户信息

export function getUserInfo() {
    return request({
        url: "api/user/userInfo",
        method: "get",
    });
}

// 获取用户商品信息
export function getMemberUserInfo() {
    return request({
        url: "api/memberCard/memberUserInfo",
        method: "get",
    });
}

// 建议与反馈
export function sendFeedback(data) {
    return request({
        url: "api/feedback/send",
        method: "put",
        data
    });
}



// 用户信息
export function myAccount() {
    return request({
        url: "api/account/myAccount",
        method: "get",
    });

}


// 修改密码
export function updatePassword(data) {
    return request({
        url: "/api/user/updatePassword",
        method: "post",
        data
    });
}

// 查询导航列表
export function getNavList() {
    return request({
        url: "/api/topBarConfig/list",
        method: "get",
    });
}