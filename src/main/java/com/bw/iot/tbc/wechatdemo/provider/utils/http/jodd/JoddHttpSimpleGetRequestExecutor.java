package com.bw.iot.tbc.wechatdemo.provider.utils.http.jodd;

import com.bw.iot.tbc.wechatdemo.provider.enums.WxType;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.RequestHttp;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.SimpleGetRequestExecutor;
import jodd.http.HttpConnectionProvider;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.ProxyInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName JoddHttpSimpleGetRequestExecutor
 * @Description
 * @Author lengqy
 * @Date 2024年12月24日 23:58
 * @Version 1.0
 */
public class JoddHttpSimpleGetRequestExecutor extends SimpleGetRequestExecutor<HttpConnectionProvider, ProxyInfo> {

    public JoddHttpSimpleGetRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }
    @Override
    public String execute(String uri, String queryParam, WxType wxType) throws WxErrorException, IOException {
        if (queryParam != null) {
            if (uri.indexOf('?') == -1) {
                uri += '?';
            }
            uri += uri.endsWith("?")? queryParam: '&' + queryParam;
        }

        HttpRequest httpRequest = HttpRequest.get(uri);
        if (requestHttp.getRequestHttpProxy() != null) {
            requestHttp.getRequestHttpClient().useProxy(requestHttp.getRequestHttpProxy());
        }
        httpRequest.withConnectionProvider(requestHttp.getRequestHttpClient());
        HttpResponse response = httpRequest.send();
        response.charset(StandardCharsets.UTF_8.name());

        return handleResponse(wxType, response.bodyText());
    }
}
