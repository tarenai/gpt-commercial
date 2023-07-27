/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-02 10:33:59
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-06 23:25:15
 */
import request from "@/request/index.js";
export function authorizationUrl(params) {
    return request({
        url: "/api/wechat/authorizationUrl",
        method: "get",
        params
    });
}

export function wechatLogin(data) {
    return request({
        url: "/api/wechat/wechatLogin",
        method: "post",
        data
    });
}
export function wechatTestLogin(data) {
    return request({
        url: "/api/test/wechatLogin",
        method: "post",
        data
    });
}

// 退出登录
export function logout() {
    return request({
        url: "/api/user/logout",
        method: "post",
    });
}
// 校验token是否过期
export function checkToken() {
    return request({
        url: "/api/user/checkToken",
        method: "get",
    });
}

// 图片验证码
export function imageVerificationCode(params) {
    return request({
        url: "/api/user/imageVerificationCode",
        method: "get",
        params,
        responseType: 'blob',
    });
}


// 发送短信
export function sendSms(data) {
    return request({
        url: "/api/user/sendSms",
        method: "put",
        data,
    });
}

// 用户注册

export function smsSignUp(data) {
    return request({
        url: "/api/user/smsSignUp",
        method: "put",
        data,
    });
}

// 账号密码登录
export function userLogin(data) {
    return request({
        url: "/api/user/login",
        method: "post",
        data,
    });
}
// 系统配置
export function systemConfig() {
    return request({
        url: "/api/web/setting",
        method: "get",
    });
}


// wx轮询查询登录状态
export function wxLoginState(params) {
    return request({
        url: "/api/wechat/loginState",
        method: "get",
        params
    });
}

// 图片验证码
export function checkImageCode(data) {
    return request({
        url: "/api/user/checkImageCode",
        method: "post",
        data,
    });
}