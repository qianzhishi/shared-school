import content from './request'

// 获取推荐内容接口
const recommendApi = (data: any) => {
    return content.get({
        url: 'api/frontend/content/recommend',
        data
    })
}

// 根据帖子类型获取内容接口
const postListApi = (data: any) => {
  return content.get({
      url: '/api/frontend/content/list',
      data
  })
}

// 获取详细帖子内容接口
const detailApi = (data: any) => {
  return content.get({
      url: 'api/frontend/content/detail',
      data
  })
}

// 获取评论列表接口
const commentListApi = (data: any) => {
  return content.get({
      url: 'api/frontend/content/commentList',
      data
  })
}

// 获取动态内容接口
const likeNewsApi = (data: any) => {
  return content.get({
      url: '/api/frontend/content/likeNews',
      data
  })
}

// 获取搜索内容接口
const searchApi = (data: any) => {
  return content.get({
      url: '/api/frontend/content/search',
      data
  })
}

// --------------------------------

// 评论接口
const commentApi = (data: any) => {
  return content.post({
      url: '/api/frontend/content/comment',
      data
  })
}

// 点赞与取消点赞帖子接口
const likePostApi = (data: any) => {
  return content.post({
      url: 'api/frontend/content/likePost',
      data
  })
}

// 点赞与取消点赞评论接口
const likeCommentApi = (data: any) => {
  return content.post({
      url: 'api/frontend/content/likeComment',
      data
  })
}

// 上传图片
const uploadImageApi = (data: any) => {
  return content.post({
      url: '/api/frontend/upload/image',
      data
  })
}

// --------------------------------

// 修改帖子接口
const editPostApi = (data: any) => {
  return content.post({
      url: '/api/frontend/content/editPost',
      data
  })
}

// --------------------------------

// 删除帖子接口
const delPostApi = (data: any) => {
  return content.post({
      url: '/api/frontend/content/delPost',
      data
  })
}

// 删除用户的头像图片
const removeImageApi = (data: any) => {
  return content.post({
      url: '/api/frontend/upload/removeImage',
      data
  })
}

// 删除资源
const delResourceApi = (data: any) => {
  return content.post({
      url: '/api/frontend/upload/delResource',
      data
  })
}

export default {
  recommendApi,
  commentListApi,
  detailApi,
  postListApi,
  likeNewsApi,
  searchApi,
  commentApi,
  likePostApi,
  likeCommentApi,
  uploadImageApi,
  editPostApi,
  delPostApi,
  removeImageApi,
  delResourceApi
}