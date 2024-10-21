import user from './request'

// 获取用户个人信息接口
const infoApi = (data: any) => {
    return user.get({
        url: 'api/frontend/user/info',
        data
    })
}

// 获取粉丝列表接口
const fansApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/fans',
        data
    })
}

// 获取关注列表接口
const subsApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/likes',
        data
    })
}


// 获取用户发布的帖子信息接口
const contentApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/content',
        data
    })
}

// 获取用户发布的评论列表接口
const commentListApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/commentList',
        data
    })
}

// --------------------------------

// 关注/取消关注接口
const subscribeApi = (data: any) => {
    return user.post({
        url: '/api/frontend/user/follow',
        data
    })
}

// 点赞/取消点赞评论
const likeCommentApi = (data: any) => {
    return user.post({
        url: 'api/frontend/content/likeComment',
        data
    })
}


// 上传图片
const uploadImageApi = (data: any) => {
    return user.post({
        url: '/api/frontend/upload/image',
        data
    })
}

// --------------------------------

// 编辑个人信息接口
const editApi = (data: any) => {
    return user.post({
        url: '/api/frontend/user/edit',
        data
    })
}

// 隐私设置接口
const setPrivacyApi = (data: any) => {
    return user.post({
        url: '/api/frontend/user/setPrivacy',
        data
    })
}

// 修改密码接口
const editPwdApi = (data: any) => {
    return user.post({
        url: '/api/frontend/user/editPwd',
        data
    })
}

// --------------------------------

// 删除用户上传的头像图片
const removeImageApi = (data: any) => {
    return user.post({
        url: '/api/frontend/upload/removeImage',
        data
    })
}

// 删除当前帖子接口
const delPostApi = (data: any) => {
    return user.post({
        url: '/api/frontend/content/delPost',
        data
    })
}

// 删除指定评论接口
const delCommentApi = (data: any) => {
    return user.post({
        url: '/api/frontend/content/delComment',
        data
    })
}



export default {
    infoApi,
    fansApi,
    subsApi,
    contentApi,
    commentListApi,
    subscribeApi,
    editApi,
    delPostApi,
    delCommentApi,
    setPrivacyApi,
    editPwdApi,
    likeCommentApi,
    uploadImageApi,
    removeImageApi,
}

