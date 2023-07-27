/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-08-18 18:00:22
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-17 09:44:29
 */
import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
/* Layout */
import Layout from '@/layout'

export const constantRoutes = [{
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: {
        title: '仪表盘',
        icon: 'dashboard'
      }
    }]
  },


  {
    path: '/vipList',
    component: Layout,
    redirect: '/vipList',
    children: [{
      path: 'vipList',
      name: 'vipList',
      component: () => import('@/views/vipList/index'),
      meta: {
        title: '会员卡列表',
        icon: 'example'
      }
    }]
  },

  {
    path: '/vipStrategyList',
    component: Layout,
    redirect: '/vipStrategyList',
    children: [{
      path: 'vipStrategyList',
      name: 'vipStrategyList',
      component: () => import('@/views/vipStrategyList/index'),
      meta: {
        title: '会员权益列表',
        icon: 'form'
      }
    }]
  },

  {
    path: '/paymentOrderList',
    component: Layout,
    redirect: '/paymentOrderList',
    children: [{
      path: 'paymentOrderList',
      name: 'paymentOrderList',
      component: () => import('@/views/paymentOrderList/index'),
      meta: {
        title: '支付订单列表',
        icon: 'nested'
      }
    }]
  },

  {
    path: '/questionLogList',
    component: Layout,
    redirect: '/questionLogList',
    children: [{
      path: 'questionLogList',
      name: 'questionLogList',
      component: () => import('@/views/questionLogList/index'),
      meta: {
        title: '提问日志列表',
        icon: 'table'
      }
    }]
  },


  {
    path: '/userInfoList',
    component: Layout,
    redirect: '/userInfoList',
    children: [{
      path: 'userInfoList',
      name: 'userInfoList',
      component: () => import('@/views/userInfoList/index'),
      meta: {
        title: '用户信息列表',
        icon: 'user'
      }
    }]
  },

  {
    path: '/bannerConfig',
    component: Layout,
    redirect: '/bannerConfig',
    children: [{
      path: 'bannerConfig',
      name: 'bannerConfig',
      component: () => import('@/views/bannerConfig/index'),
      meta: {
        title: '广告管理',
        icon: 'nested'
      }
    }]
  },


  {
    path: '/navBarConfig',
    component: Layout,
    redirect: '/navBarConfig',
    children: [{
      path: 'navBarConfig',
      name: 'navBarConfig',
      component: () => import('@/views/navBarConfig/index'),
      meta: {
        title: '导航栏配置',
        icon: 'eye'
      }
    }]
  },
  {
    path: '/chatgptConfig',
    component: Layout,
    redirect: '/chatgptConfig',
    children: [{
      path: 'chatgptConfig',
      name: 'chatgptConfig',
      component: () => import('@/views/chatgptConfig/index'),
      meta: {
        title: 'chatgpt配置',
        icon: 'eye-open'
      }
    }]
  },
  {
    path: '/ststemConfig',
    component: Layout,
    redirect: '/ststemConfig',
    children: [{
      path: 'ststemConfig',
      name: 'ststemConfig',
      component: () => import('@/views/ststemConfig/index'),
      meta: {
        title: '系统配置',
        icon: 'tree'
      }
    }]
  },


  // 404 page must be placed at the end !!!
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
