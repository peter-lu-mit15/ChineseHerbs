import axios from '@/libs/api.request'

//获取药材列表
export const getProductList = (data) => {
  return axios.request({
    url: '/productByLogin/list',
    method: 'post',
    data
  })
}


//根据字母返回汉字
export const getCharacterByPyin = (data) => {
  return axios.request({
    url: '/product/getCharacterByPyin',
    method: 'post',
    data
  })
}

// 添加药材列表，生成订单
export const addCartList = (data) => {
  return axios.request({
    url: '/cart/addList',
    method: 'post',
    data
  })
}


//用户登录
export const customerLogin = (data) => {
  return axios.request({
    url: '/login',
    method: 'post',
    data
  })
}

//用户注册
export const customerRegister = (data) => {
  return axios.request({
    url: '/register',
    method: 'post',
    data
  })
}

//用户完善信息
export const customerUpdateInfo = (data) => {
  return axios.request({
    url: '/user/update',
    method: 'post',
    data
  })
}


//获取煮药价格
export const boiledCount = () => {
  return axios.request({
    url: '/admin/system/boiledCount',
    method: 'get',
  })
}

//订单确认邮件通知
export const sendEmailForOrder = (data) => {
  return axios.request({
    url: '/mail/sendEmail',
    method: 'get',
    params:data
  })
}

//获取煮药价格列表
export const boiledList = (data) => {
  return axios.request({
    url: '/boiled/list',
    method: 'get',
    params:data
  })
}

