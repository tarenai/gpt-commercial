/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-08-18 16:42:46
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-19 10:28:33
 */
import axios from 'axios'
import {
  MessageBox,
  Message
} from 'element-ui'
import store from '@/store'
import {
  getToken
} from '@/utils/auth'
// 提示状态
let tipsState = false; // 提示状态, 默认为false, 如果为true则显示提示消息并关闭提
// create an axios instance
const service = axios.create({
  baseURL: "", // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    let token = getToken()
    if (token) {
      config.headers['satoken'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(

  response => {
    const res = response.data;
    if (res.code === 4002) {
      if (!tipsState) {
        tipsState = true; // 显示提示消息, 并关闭提示消息 不要忘记保存该值并回到
        MessageBox('当前身份信息已过期，请重新登陆!', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          window.location.href = window.location.origin + '/#/login'
          tipsState = false
        }).catch(() => {
          tipsState = false
        });
      }

    } else {
      return res;
    }

  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.msg,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
