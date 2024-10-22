import system from './request'

// 管理员登录登录接口
const loginApi = (data: any) => {
    return system.get({
        url: 'api/backend/login',
        data
    })
}

// 获取管理员列表接口
const adminListApi = (data: any) => {
  return system.get({
      url: 'api/backend/adminList',
      data
  })
}

// 获取用户列表接口
const userListApi = (data: any) => {
  return system.get({
      url: 'api/backend/userList',
      data
  })
}

// 获取用户接口
const getUserApi = (data: any) => {
  return system.get({
      url: 'api/frontend/user/info',
      data
  })
}

// 后台获取评论列表接口
const commentListApi = (data: any) => {
  return system.get({
      url: 'api/backend/commentList',
      data
  })
}

// 后台获取帖子列表接口
const postListApi = (data: any) => {
  return system.get({
      url: 'api/backend/cardList',
      data
  })
}

// 获取资源列表
const getFileListApi = () => {
  return system.get({
      url: '/api/frontend/upload/resourceList',
  })
}

// 根据含有的路径名获取资源列表
const getFileByPathApi = (data: any) => {
  return system.get({
      url: '/api/frontend/upload/getFileByPath',
      data
  })
}


// 更新管理员信息接口
const updateAdminApi = (data: any) => {
  return system.post({
      url: 'api/backend/updateAdmin',
      data
  })
}

// 删除管理员信息接口
const delAdminApi = (data: any) => {
  return system.post({
      url: 'api/backend/delAdmin',
      data
  })
}

// 删除资源
const delResourceApi = (data: any) => {
  return system.post({
      url: '/api/frontend/upload/delResource',
      data
  })
}

// 删除用户接口
const delUserApi = (data: any) => {
  return system.post({
      url: 'api/backend/delUser',
      data
  })
}

// 删除帖子接口
const delPostApi = (data: any) => {
  return system.post({
      url: 'api/frontend/content/delPost',
      data
  })
}

// 增加管理员接口
const addAdminApi = (data: any) => {
  return system.post({
      url: 'api/backend/addAdmin',
      data
  })
}

// 删除评论接口
const delCommentApi = (data: any) => {
  return system.post({
      url: 'api/frontend/content/delComment',
      data
  })
}


export default {
  loginApi,
  updateAdminApi,
  commentListApi,
  addAdminApi,
  adminListApi,
  userListApi,
  delAdminApi,
  delUserApi,
  delCommentApi,
  postListApi,
  delPostApi,
  getFileByPathApi,
  getFileListApi,
  delResourceApi,
  getUserApi
}
