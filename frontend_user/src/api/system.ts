import system from './request'

// 用户登录接口
const loginApi = (data: any) => {
    return system.get({
        url: 'api/frontend/system/login',
        data
    })
}

// --------------------------------

// 用户注册接口
const registerApi = (data: any) => {
  return system.post({
      url: 'api/frontend/system/register',
      data
  })
}

export default {
  loginApi,
  registerApi,
}
