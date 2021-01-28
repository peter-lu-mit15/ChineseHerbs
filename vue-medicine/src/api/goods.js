import axios from '@/libs/api.request'

//获取药材列表
export const getProductList = (data) => {
  return axios.request({
    url: 'admin/product/list',
    method: 'post',
    data
  })
}

//添加药材信息
export const addProduct = (data) => {
  return axios.request({
    url: '/admin/product/add',
    method: 'post',
    data
  })
}

//后台批量上下架
export const batchUpdateSellStatus = (data) => {
  return axios.request({
    url: '/admin/product/batchUpdateSellStatus',
    method: 'post',
    data
  })
}

//删除药材信息
export const deleteProduct = (data) => {
  return axios.request({
    url: '/admin/product/delete',
    method: 'post',
    data
  })
}

//更新药材信息
export const updateProduct = (data) => {
  return axios.request({
    url: '/admin/product/update',
    method: 'post',
    data
  })
}

//添加药材图片
export const addFile = (data) => {
  return axios.request({
    url: '/admin/upload/file',
    method: 'post',
    data
  })
}

//获取处理类型列表
export const listByProduct = (data) => {
  return axios.request({
    url: '/admin/variant/listByProduct',
    method: 'get',
    params:data
  })
}

//添加药材处理类型
export const addVariant = (data) => {
  return axios.request({
    url: '/admin/variant/add',
    method: 'post',
    data
  })
}

//删除处理类型
export const deleteVariant = (data) => {
  return axios.request({
    url: '/admin/variant/delete',
    method: 'get',
    params:data
  })
}





// delete Materialtype
export const deleteMaterialtype = (ids) => {
  return axios.request({
    url: 'notice/notices/delete/' + ids,
    method: 'get'
  })
}

// batch command
export const batchCommand = (data) => {
  return axios.request({
    url: 'notice/notices/batch',
    method: 'get',
    params: data
  })
}


