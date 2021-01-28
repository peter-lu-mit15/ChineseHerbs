import axios from '@/libs/api.request'

//获取列表
export const getBoiledList = (data) => {
  return axios.request({
    url: '/boiled/list',
    method: 'get',
    params:data
  })
}


//修改
export const updateBoiled = (data) => {
  return axios.request({
    url: '/boiled/update',
    method: 'post',
    data
  })
}

//添加
export const addBoiled = (data) => {
  return axios.request({
    url: '/boiled/add',
    method: 'post',
    data
  })
}

//删除
export const deleteBoiled = (data) => {
  return axios.request({
    url: '/boiled/delete',
    method: 'get',
    params:data
  })
}



