import load from './request'

// 获取资源列表
const getFileListApi = () => {
  return load.get({
      url: '/api/frontend/upload/resourceList',
  })
}

// 根据含有的路径名获取资源列表
const getFileByPathApi = (data: any) => {
  return load.get({
      url: '/api/frontend/upload/getFileByPath',
      data
  })
}

// 下载资源文件
const downloadFileApi = (data: any) => {
  return load.get({
      url: '/api/frontend/upload/downloadFile',
      data,
      responseType: 'blob'
  })
}

// --------------------------------

// 上传资源
const uploadFileApi = (data: any) => {
  return load.post({
      url: 'api/frontend/upload/resource',
      data
  })
}

// 上传帖子
const postApi = (data: any) => {
  return load.post({
      url: '/api/frontend/upload/post',
      data
  })
}

// 上传图片
const uploadImageApi = (data: any) => {
  return load.post({
      url: '/api/frontend/upload/image',
      data
  })
}

// --------------------------------

// 删除图片
const removeImageApi = (data: any) => {
  return load.post({
      url: '/api/frontend/upload/removeImage',
      data
  })
}

// 删除资源
const delResourceApi = (data: any) => {
  return load.post({
      url: '/api/frontend/upload/delResource',
      data
  })
}

export default {
  getFileListApi,
  downloadFileApi,
  removeImageApi,
  getFileByPathApi,
  uploadFileApi,
  uploadImageApi,
  postApi,
  delResourceApi
}