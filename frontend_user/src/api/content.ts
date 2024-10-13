import content from './request'

// 获取推荐内容接口
const recommendApi = (data: any) => {
    return content.get({
        url: 'api//frontend/content/recommend?apipost_id=de7fefd79e12f',
        data
    })
}

// 根据帖子类型获取内容接口
const listApi = (data: any) => {
  return content.get({
      url: '/api/frontend/content/list?apipost_id=cde057639e0c4',
      data
  })
}

// 获取详细帖子内容接口
const detailApi = (data: any) => {
  return content.get({
      url: 'api/frontend/content/detail?apipost_id=cde2c2579e0c7',
      data
  })
}

// 获取子评论列表接口
const replylistApi = (data: any) => {
  return content.get({
      url: 'api/frontend/content/replylist?apipost_id=dd466f7f9e0da',
      data
  })
}

// 获取动态内容接口
const newsApi = (data: any) => {
  return content.get({
      url: '/api/frontend/content/news?apipost_id=de8d7bdb9e13d',
      data
  })
}

// 获取搜索内容接口
const searchApi = (data: any) => {
  return content.get({
      url: '/api/frontend/content/search?apipost_id=de60898f9e10c',
      data
  })
}

// --------------------------------

// 评论接口
const commentApi = (data: any) => {
  return content.post({
      url: '/api/frontend/content/comment?apipost_id=ddfa962f9e0ea',
      data
  })
}

// 点赞与取消点赞接口
const likeApi = (data: any) => {
  return content.post({
      url: 'api/frontend/content/like?apipost_id=de4d3e939e0fe',
      data
  })
}

// 上传图片
const loadPictureApi = (data: any) => {
  return content.post({
      url: '/api/frontend/upload/loadPicture?apipost_id=10248b3a39e08d',
      data
  })
}

// --------------------------------

// 修改帖子接口
const editApi = (data: any) => {
  return content.put({
      url: '/api/frontend/content/edit?apipost_id=de6edbf39e11f',
      data
  })
}

// --------------------------------

// 删除帖子接口
const deleteApi = (data: any) => {
  return content.del({
      url: '/api/frontend/content/delete?apipost_id=de7b75c79e12b	',
      data
  })
}

// 移除图片
const removePictureApi = (data: any) => {
  return content.del({
      url: '/api/frontend/upload/removePicture?apipost_id=1023828e79e086',
      data
  })
}

export default {
  recommendApi,
  replylistApi,
  detailApi,
  listApi,
  newsApi,
  searchApi,
  commentApi,
  likeApi,
  loadPictureApi,
  editApi,
  deleteApi,
  removePictureApi,
}