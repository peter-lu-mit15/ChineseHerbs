import Main from '@/components/main'
import parentView from '@/components/parent-view'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中，示例看QQ群路由配置
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 *  beforeCloseName: (-) 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数
 * }
 */

export default [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'Login - 登录',
      hideInMenu: true
    },
    component: () => import('@/view/admin/login/login.vue')
  },
  {
    path: '/customerLogin',
    name: 'customerLogin',
    meta: {
      icon: 'ios-stats',
      title: '用户登录',
      hideInMenu:true
    },
    component: () => import('@/view/customer/login/login.vue')
  },
  {
    path: '/customer_register',
    name: 'customer_register',
    meta: {
      hideInMenu:true,
      icon: 'ios-stats',
      title: '用户注册'
    },
    component: () => import('@/view/customer/login/register.vue')
  },
  {
    path: '/add_information',
    name: 'add_information',
    meta: {
      hideInMenu:true,
      icon: 'ios-stats',
      title: '完善信息'
    },
    component: () => import('@/view/customer/login/add_info.vue')
  },
  {
    path: '/confirmpage',
    name: 'confirmpage',
    meta: {
      hideInMenu:true,
      icon: 'ios-stats',
      title: '信息确认'
    },
    component: () => import('@/view/customer/login/confirm.vue')
  },
  {
    path: '/okay',
    name: 'okay',
    meta: {
      hideInMenu:true,
      icon: 'ios-stats',
      title: 'Okay'
    },
    component: () => import('@/view/customer/okay.vue')
  },
  {
    path: '/',
    name: 'startpage',
    meta: {
      hideInMenu:true,
      icon: 'ios-stats',
      title: 'startpage'
    },
    component: () => import('@/view/customer/startpage.vue')
  },
  {
    path: '/home',
    name: 'home',
    meta: {
      hideInMenu:true,
      icon: 'ios-stats',
      title: 'home'
    },
    component: () => import('@/view/medicine/index.vue')
  },
  // {
  //   path: '/sss',
  //   name: 'home',
  //   meta: {
  //     icon: 'ios-stats',
  //     title: 'home'
  //   },
  //   component: () => import('@/view/medicine/drugs.vue')
  // },
  // {
  //   path: '/admin',
  //   name: '_home',
  //   component: Main,
  //   meta: {
  //     hideInMenu: true,
  //     notCache: true
  //   },
  //   children: [
  //     {
  //       path: 'home',
  //       name: 'home',
  //       meta: {
  //         hideInMenu: true,
  //         title: '首页',
  //         notCache: true,
  //         icon: 'md-home'
  //       },
  //       component: () => import('@/view/single-page/home')
  //     }
  //   ]
  // },
  {
    path: '/goods',
    name: '药材管理',
    meta: {
      icon: 'logo-buffer',
      title: '药材管理'
    },
    redirect: "/goods/index",
    component: Main,
    children: [
      {
        path: 'index',
        name: '药材管理中心',
        meta: {
          icon: 'md-grid',
          title: '药材管理中心'
        },
        component: () => import('@/view/admin/goods/goodlist.vue')
      }
    ]
  },
  {
    path: '/orders',
    name: '订单管理',
    meta: {
      icon: 'logo-buffer',
      title: '订单管理'
    },
    component: Main,
    children: [
      {
        path: 'index',
        name: '订单管理中心',
        meta: {
          icon: 'md-grid',
          title: '订单管理中心'
        },
        component: () => import('@/view/admin/order/orderlist.vue')
      }
    ]
  },
  {
    path: '/users',
    name: '用户管理',
    meta: {
      icon: 'logo-buffer',
      title: '用户管理'
    },
    component: Main,
    children: [
      {
        path: 'index',
        name: '用户管理中心',
        meta: {
          icon: 'md-grid',
          title: '用户管理中心'
        },
        component: () => import('@/view/admin/user/userlist.vue')
      }
    ]
  },
  {
    path: '/boiled',
    name: '煮药管理中心',
    redirect: "/boiled/index",
    meta: {
      icon: 'logo-buffer',
      title: '煮药管理中心'
    },
    component: Main,
    children: [
      {
        path: 'index',
        name: '煮药价格管理',
        meta: {
          icon: 'md-grid',
          title: '煮药价格管理'
        },
        component: () => import('@/view/admin/boiled/index.vue')
      }
    ]
  },
  {
    path: '/system/seeting',
    name: '系统设置',
    redirect: "/system/seeting/index",
    meta: {
      icon: 'logo-buffer',
      title: '系统设置'
    },
    component: Main,
    children: [
      {
        path: 'index',
        name: '后台系统设置',
        meta: {
          icon: 'md-grid',
          title: '后台系统设置'
        },
        component: () => import('@/view/admin/system/index.vue')
      }
    ]
  },
  
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/401.vue')
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/500.vue')
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true
    },
    component: () => import('@/view/error-page/404.vue')
  }
]
