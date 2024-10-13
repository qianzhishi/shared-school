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
    return service({
        ...config,
        method: 'post', 
        data: config.data 
    });
}

// 封装put请求
const put = (config: AxiosRequestConfig<any>) => {
    return service({
        ...config,
        method: 'put',
        data: config.data
    });
}

// 封装delete请求
const del = (config: AxiosRequestConfig<any>) => {
    return service({
        ...config,
        method: 'delete',
        data: config.data
    });
}

export default {get,post,put,del}