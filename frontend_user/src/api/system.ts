import system from './request'

// 用户登录接口
const loginApi = (data: any) => {
    return system.get({
        url: '/frontend/system/login?apipost_id=cc19811b9e023',
        data
    })
}

// --------------------------------

// 用户注册接口
const registerApi = (data: any) => {
  return system.post({
      url: '/frontend/system/register?apipost_id=cb20aef79e00d',
      data
  })
}

export default {
  loginApi,
  registerApi,
}
