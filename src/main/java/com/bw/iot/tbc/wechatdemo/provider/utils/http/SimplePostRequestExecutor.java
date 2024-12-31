package com.bw.iot.tbc.wechatdemo.provider.utils.http;

import com.bw.iot.tbc.wechatdemo.provider.enums.WxType;
import com.bw.iot.tbc.wechatdemo.provider.error.WxError;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.apache.ApacheSimplePostRequestExecutor;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.jodd.JoddHttpSimplePostRequestExecutor;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.okhttp.OkHttpSimplePostRequestExecutor;

import java.io.IOException;

/**
 * @ClassName SimplePostRequestExecutor
 * @Description
 * @Author lengqy
 * @Date 2024年12月25日 00:53
 * @Version 1.0
 */
public abstract class SimplePostRequestExecutor<H, P> implements RequestExecutor<String, String> {
    protected RequestHttp<H, P> requestHttp;

    public SimplePostRequestExecutor(RequestHttp<H, P> requestHttp)
    {
        this.requestHttp = requestHttp;
    }

    @Override
    public void execute(String uri, String data, ResponseHandler<String> handler, WxType wxType)
            throws WxErrorException, IOException {
        handler.handle(this.execute(uri, data, wxType));
    }

    public static RequestExecutor<String, String> create(RequestHttp requestHttp) {
        switch (requestHttp.getRequestType()) {
            case APACHE_HTTP:
                return new ApacheSimplePostRequestExecutor(requestHttp);
            case JODD_HTTP:
                return new JoddHttpSimplePostRequestExecutor(requestHttp);
            case OK_HTTP:
                return new OkHttpSimplePostRequestExecutor(requestHttp);
            default:
                throw new IllegalArgumentException("非法请求参数");
        }
    }

    public String handleResponse(WxType wxType, String responseContent) throws WxErrorException {
        if (responseContent.isEmpty()) {
            throw new WxErrorException("无响应内容");
        }

        if (responseContent.startsWith("<xml>")) {
            // xml格式数据直接输出
            return responseContent;
        }

        WxError error = WxError.fromJson(responseContent, wxType);
        if (error.getErrorCode() != 0) {
            throw new WxErrorException(error);
        }
        return responseContent;
    }
}
