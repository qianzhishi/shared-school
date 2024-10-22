import axios from 'axios';
import { ElMessage } from 'element-plus'

// 创建axios实例
const service = axios.create({
    // 设置baseURL地址
    baseURL: 'http://localhost:8080',
    // 配置请求超时时间
    timeout: 10000,
});

// 请求拦截器
service.interceptors.request.use((config) => {
    // 在发送请求之前做些什么
    return config
}, (error) => {
    // 处理错误请求
    console.log(error);
    return Promise.reject(error);
});


// 响应拦截器
service.interceptors.response.use((res) => {
    // code是后端的状态码
	const code = res.data.code
    // 请求成功
	if (code == 200)
		return res.data
    // 请求失败
    else {
        ElMessage.error(res.data.message)
		return res.data
    }
},(error) => {
    console.log(error);
    ElMessage.error('糟糕，出错了，请稍后再试')
    return Promise.reject(error);
}
)

export default service

