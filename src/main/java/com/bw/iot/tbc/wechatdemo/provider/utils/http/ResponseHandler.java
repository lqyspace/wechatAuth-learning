package com.bw.iot.tbc.wechatdemo.provider.utils.http;

/**
 * @ClassName ResponseHandler
 * @Description
 * @Author lengqy
 * @Date 2024年12月23日 15:14
 * @Version 1.0
 */

/**
 * http请求响应回调处理接口
 * @param <T> 返回值类型
 */
public interface ResponseHandler<T> {

    /**
     * 响应结果处理
     *
     * @param t 要处理的对象
     */
    void handle(T t);
}
