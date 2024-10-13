import user from './request'


//get请求

// 获取用户个人信息接口
const infoApi = (data: any) => {
    return user.get({
        url: 'api/frontend/user/info?apipost_id=cc8a45cf9e038',
        data
    })
}

// 获取粉丝列表接口
const fansApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/fans?apipost_id=ccde48779e05c',
        data
    })
}

// 获取关注列表接口
const subsApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/subs?apipost_id=cd089ccb9e064',
        data
    })
}

// 获取隐私设置接口
const settingsApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/settings?apipost_id=cccf47df9e04b',
        data
    })
}

// 获取用户发布的帖子信息接口
const contentApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/content?apipost_id=cd11b3139e068',
        data
    })
}

// 获取评论列表接口
const commentApi = (data: any) => {
    return user.get({
        url: '/api/frontend/user/comment?apipost_id=cd6ac3cf9e08a',
        data
    })
}

// --------------------------------

// 关注/取消关注接口
const subscribeApi = (data: any) => {
    return user.post({
        url: '/api/frontend/user/subscribe?apipost_id=cda987039e0a4',
        data
    })
}

// 点赞/取消点赞评论
const likeApi = (data: any) => {
    return user.post({
        url: 'api/frontend/content/like?apipost_id=de4d3e939e0fe',
        data
    })
}


// 上传图片
const loadPictureApi = (data: any) => {
    return user.post({
        url: '/api/frontend/upload/loadPicture?apipost_id=10248b3a39e08d',
        data
    })
}

// --------------------------------

// 编辑个人信息接口
const editApi = (data: any) => {
    return user.put({
        url: '/api/frontend/user/edit?apipost_id=ccbcc6b39e041',
        data
    })
}

// 隐私设置接口
const setApi = (data: any) => {
    return user.put({
        url: '/api/frontend/user/set?apipost_id=cd986c4f9e099',
        data
    })
}

// 修改密码接口
const editPwdApi = (data: any) => {
    return user.put({
        url: '/api/frontend/user/editpwd?apipost_id=cd8ce32b9e093',
        data
    })
}

// --------------------------------

// 移除图片
const removePictureApi = (data: any) => {
    return user.del({
        url: '/api/frontend/upload/removePicture?apipost_id=1023828e79e086',
        data
    })
}

// 删除当前帖子接口
const deleteApi = (data: any) => {
    return user.del({
        url: '/api/frontend/content/delete?apipost_id=de7b75c79e12b',
        data
    })
}

// 删除指定评论接口
const delcommentApi = (data: any) => {
    return user.del({
        url: '/api/frontend/content/delcomment?apipost_id=de4522bf9e0f6',
        data
    })
}



export default {
    infoApi,
    fansApi,
    subsApi,
    settingsApi,
    contentApi,
    commentApi,
    subscribeApi,
    editApi,
    deleteApi,
    delcommentApi,
    setApi,
    editPwdApi,
    likeApi,
    loadPictureApi,
    removePictureApi,
}

