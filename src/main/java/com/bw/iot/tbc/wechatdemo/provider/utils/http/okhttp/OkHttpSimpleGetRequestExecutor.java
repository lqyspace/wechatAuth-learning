package com.bw.iot.tbc.wechatdemo.provider.utils.http.okhttp;

import com.bw.iot.tbc.wechatdemo.provider.enums.WxType;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.RequestHttp;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.SimpleGetRequestExecutor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @ClassName OkHttpSimpleGetRequestExecutor
 * @Description
 * @Author lengqy
 * @Date 2024年12月25日 00:09
 * @Version 1.0
 */public class OkHttpSimpleGetRequestExecutor extends SimpleGetRequestExecutor<OkHttpClient, OkHttpProxyInfo> {
    public OkHttpSimpleGetRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }
    @Override
    public String execute(String uri, String queryParam, WxType wxType) throws WxErrorException, IOException {
        if (queryParam != null) {
            if (uri.indexOf('?') == -1) {
                uri += '?';
            }
            uri += uri.endsWith("?") ? queryParam : '&' + queryParam;
        }

        //得到httpClient
        OkHttpClient client = requestHttp.getRequestHttpClient();
        Request request = new Request.Builder().url(uri).build();
        Response response = client.newCall(request).execute();
        return this.handleResponse(wxType, response.body().string());
    }
}
