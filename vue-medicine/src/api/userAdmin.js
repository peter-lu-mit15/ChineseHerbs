import axios from '@/libs/api.request'

//获取用户列表
export const getAdminUserList = (data) => {
  return axios.request({
    url: 'admin/user/list',
    method: 'post',
    data
  })
}


//删除用户信息
export const deleteUser = (data) => {
  return axios.request({
    url: '/admin/user/delete',
    method: 'post',
    data
  })
}

//更新用户信息
export const updateUser = (data) => {
  return axios.request({
    url: '/admin/user/update',
    method: 'post',
    data
  })
}

//获取系统参数
export const getSystemInfo = () => {
  return axios.request({
    url: '/admin/system/topOne',
    method: 'get',
  })
}
//更新系统设置
export const updateSystem = (data) => {
  return axios.request({
    url: '/admin/system/update',
    method: 'post',
    data
  })
}

//添加抓药师邮箱
export const emailAdd = (data) => {
  return axios.request({
    url: '/mail/add',
    method: 'post',
    data
  })
}

//删除抓药师邮箱
export const emailDelete = (data) => {
  return axios.request({
    url: '/mail/delete',
    method: 'get',
    params:data
  })
}

//修改抓药师邮箱
export const emailUpdate = (data) => {
  return axios.request({
    url: '/mail/update',
    method: 'post',
    data
  })
}

//抓药师邮箱列表
export const emailList = (data) => {
  return axios.request({
    url: '/mail/list',
    method: 'get',
    params:data
  })
}



