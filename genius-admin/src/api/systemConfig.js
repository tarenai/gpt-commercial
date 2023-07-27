import request from '@/utils/request'


// 网站配置保存
export function getSetting() {
  return request({
    url: '/api/manager/getSetting',
    method: 'get',

  })
}


// 网站配置保存
export function saveSetting(data) {
  return request({
    url: '/api/manager/saveSetting',
    method: 'post',
    data
  })
}
