/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-03 09:16:53
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-04 09:35:52
 */
import jsCookie from "js-cookie";
import copy from 'copy-to-clipboard';
import {
    message
} from 'antd';
export const getCookie = (name) => {
    return jsCookie.get(name);
}


export const setCookie = (name, value) => {
    jsCookie.set(name, value, {
        expires: 30,
        path: "/",
    });
}

export const removeCookie = (name) => {
    jsCookie.remove(name);
}
/**
 * @description: 异常提醒
 * @param {*} type 类型
 * @param {*} content 提示内容
 * @return {*}
 * @author: jinglin.gao
 */
export const messageFn = ({
    type,
    content
}) => {
    switch (type) {
        case "success":
            message.success(content);
            break;
        case "error":
            message.error(content);
            break;
        case "info":
            message.error(content);
            break;
        default:
            message.success(content);
            break;
    }
}

/**
 * @description: 复制内容到剪切板
 * @return {*}
 * @author: jinglin.gao
 */

export const copyToClipboardFn = (data, successInfo) => {
    copy(data)
    messageFn({
        type: "success",
        content: successInfo
    })
}

/**
 * @description: 获取url中的参数
 * @return {*}
 * @author: jinglin.gao
 */
export const queryString = (target) => {
    let url = window.location.href;

    if (url.indexOf("?") === -1) return false;
    let urlList = url.split("?");
    let paramsStr = urlList[1];
    let paramsList = paramsStr.split("&");
    let queryStringObj = {};
    paramsList.forEach((item) => {
        let newArr = item.split("=");
        queryStringObj[newArr[0]] = decodeURIComponent(newArr[1]);
    });

    return queryStringObj[target];
};


// 设置session
export const setSessionStorage = (name, value) => {
    window.sessionStorage.setItem(name, value);
}

// 读取session
export const getSessionStorage = (name) => {
    return window.sessionStorage.getItem(name);
}