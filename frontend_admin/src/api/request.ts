import type { AxiosRequestConfig } from 'axios';
import service from './config'

// 封装get请求
const get = (config: AxiosRequestConfig<any>) => {
    return service ({
        ...config,
        method: 'get',
        params: config.data
    });
}

// 封装post请求
const post = (config: AxiosRequestConfig<any>) => {
    const headers:any = {};
    if (config.data instanceof FormData) {
        // 如果是 FormData，则不设置 Content-Type
    } else {
        headers['Content-Type'] = 'application/x-www-form-urlencoded';
    }
    return service({
        ...config,
        method: 'post', 
        data: config.data ,
        headers:headers
    });
}

export default {get,post}