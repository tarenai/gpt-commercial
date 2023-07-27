/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-06-13 14:24:05
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-09 11:22:08
 * @Author: jinglin.gao
 */
/**
 * 网络请求配置
 */


import {
  SmileOutlined
} from '@ant-design/icons';
import {
  Button,
  notification
} from 'antd';

import axios from "axios";
import {
  getCookie
} from "@/utils";

// 提示状态
let tipsState = false; // 提示状态, 默认为false, 如果为true则显示提示消息并关闭提
const service = axios.create({
  baseURL: "", // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 50000, // request timeout
});

/**
 * http request 拦截器
 */
service.interceptors.request.use(
  (config) => {
    let tokenName = getCookie('tokenName');
    let tokenValue = getCookie('tokenValue');
    config.headers[tokenName] = `Bearer ${tokenValue}`
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * http response 拦截器
 */
service.interceptors.response.use(
  (response) => {
    console.log(response, 'response')
    if (response.data.code === 4002) {
      if(!tipsState){
        tipsState = true;
        const btn = (
          <Button type="primary" size="small" onClick={() => {
              tipsState = false;
              notification.close('noLoginTips')
              window.location.href = window.location.origin + '/#/login'
          }}>
            去登录
          </Button>
        );
        notification.open({
          message: '尊敬的用户您好:',
          description: '当前您暂未登录,或登录状态已经失效,点击按钮,跳转登录页。',
          icon: < SmileOutlined style = {
            {
              color: '#108ee9'
            }
          }
          />,
          btn,
          duration: 0,
          onClose: () => {
            tipsState = false;
          },
          key:"noLoginTips"

        });
      }

    }
    return response.data;
  },
  (error) => {
    console.log(error)
  }
);

export default service;