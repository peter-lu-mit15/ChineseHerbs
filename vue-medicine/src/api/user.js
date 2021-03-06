import axios from '@/libs/api.request'

export const login = ({ userName, password }) => {
  const data = {
    username:userName,
    password:password
  }
  return axios.request({
    url: 'login',
    data,
    method: 'post'
  })
}



//管理员登录
export const adminLogin = (data) => {
  return axios.request({
    url: '/adminLogin',
    method: 'post',
    data
  })
}
//退出登录
export const logout = (token) => {
  return axios.request({
    url: '/user/logout',
    method: 'post'
  })
}


export const getUserInfo = (token) => {
  return axios.request({
    url: 'get_info',
    params: {
      token
    },
    method: 'get'
  })
}


export const getUnreadCount = () => {
  return axios.request({
    url: 'message/count',
    method: 'get'
  })
}

export const getMessage = () => {
  return axios.request({
    url: 'message/init',
    method: 'get'
  })
}

export const getContentByMsgId = msg_id => {
  return axios.request({
    url: 'message/content',
    method: 'get',
    params: {
      msg_id
    }
  })
}

export const hasRead = msg_id => {
  return axios.request({
    url: 'message/has_read',
    method: 'post',
    data: {
      msg_id
    }
  })
}

export const removeReaded = msg_id => {
  return axios.request({
    url: 'message/remove_readed',
    method: 'post',
    data: {
      msg_id
    }
  })
}

export const restoreTrash = msg_id => {
  return axios.request({
    url: 'message/restore',
    method: 'post',
    data: {
      msg_id
    }
  })
}
