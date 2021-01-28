import axios from '@/libs/api.request'

//获取订单列表
export const getOrderList = (data) => {
  return axios.request({
    url: 'order/listForAdmin',
    method: 'post',
    data
  })
}


//取消订单
export const deleteOrder = (data) => {
  return axios.request({
    url: 'order/cancel',
    method: 'get',
    params:data
  })
}

//订单详情信息
export const detailOrder = (data) => {
  return axios.request({
    url: 'order/detail',
    method: 'get',
    data
  })
}

//联系客户取药email
export const SendEmailForOrder = (data) => {
  return axios.request({
    url: 'mail/SendEmailForOrder',
    method: 'get',
    params:data
  })
}





