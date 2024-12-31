package com.bw.iot.tbc.wechatdemo.provider.utils.http.jodd;

import com.bw.iot.tbc.wechatdemo.provider.enums.WxType;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.RequestHttp;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.SimplePostRequestExecutor;
import jodd.http.HttpConnectionProvider;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.ProxyInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName JoddHttpSimplePostRequestExecutor
 * @Description
 * @Author lengqy
 * @Date 2024年12月25日 01:05
 * @Version 1.0
 */
public class JoddHttpSimplePostRequestExecutor extends SimplePostRequestExecutor<HttpConnectionProvider, ProxyInfo> {
    public JoddHttpSimplePostRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String uri, String postEntity, WxType wxType) throws WxErrorException, IOException {
        HttpConnectionProvider provider = requestHttp.getRequestHttpClient();
        ProxyInfo proxyInfo = requestHttp.getRequestHttpProxy();

        HttpRequest request = HttpRequest.post(uri);
        if (proxyInfo != null) {
            provider.useProxy(proxyInfo);
        }
        request.withConnectionProvider(provider);
        if (postEntity != null) {
            request.contentType("application/json", "utf-8");
            request.bodyText(postEntity);
        }
        HttpResponse response = request.send();
        response.charset(StandardCharsets.UTF_8.name());

        return this.handleResponse(wxType, response.bodyText());
    }
}
