import load from './request'

// 获取资源路径
const pathApi = () => {
  return load.get({
      url: '/api/frontend/upload/path?apipost_id=115b2bb9b9e030',
  })
}

// 下载资源文件
const filesApi = () => {
  return load.get({
      url: '/api/frontend/download/files?apipost_id=11c8680339e05c',
  })
}

// 获取资源路径
const filedataApi = (data: any) => {
  return load.get({
      url: '/api/frontend/download/filedata?apipost_id=1160ffbd39e046',
      data
  })
}

// --------------------------------

// 上传资源
const resourceApi = (data: any) => {
  return load.post({
      url: 'api/frontend/upload/resource?apipost_id=115f570079e039',
      data
  })
}

// 上传帖子
const postApi = (data: any) => {
  return load.post({
      url: '/api/frontend/upload/post?apipost_id=1136fd0139e027',
      data
  })
}

// 上传图片
const loadPictureApi = (data: any) => {
  return load.post({
      url: '/api/frontend/upload/loadPicture?apipost_id=10248b3a39e08d',
      data
  })
}

// --------------------------------

// 移除图片
const removePictureApi = (data: any) => {
  return load.del({
      url: '/api/frontend/upload/removePicture?apipost_id=1023828e79e086',
      data
  })
}

export default {
  pathApi,
  filesApi,
  filedataApi,
  removePictureApi,
  resourceApi,
  loadPictureApi,
  postApi,
}