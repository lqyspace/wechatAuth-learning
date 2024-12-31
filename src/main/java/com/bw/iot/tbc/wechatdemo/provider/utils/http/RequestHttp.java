package com.bw.iot.tbc.wechatdemo.provider.utils.http;

/**
 * @ClassName RequestHttp
 * @Description
 * @Author lengqy
 * @Date 2024年12月19日 00:14
 * @Version 1.0
 */
public interface RequestHttp<H, P> {
    /**
     * 返回httpClient
     *
     * @return HttpClient
     */
    H getRequestHttpClient();

    /**
     * 返回httpProxy
     *
     * @return HttpHost
     */
    P getRequestHttpProxy();

    /**
     * 返回HttpType
     *
     * @return HttpType
     */
    HttpType getRequestType();
}
