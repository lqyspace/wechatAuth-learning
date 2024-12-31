package com.bw.iot.tbc.wechatdemo.provider.utils.http.okhttp;

import com.bw.iot.tbc.wechatdemo.provider.enums.WxType;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.RequestHttp;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.SimplePostRequestExecutor;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @ClassName OkHttpSimplePostRequestExecutor
 * @Description
 * @Author lengqy
 * @Date 2024年12月25日 01:06
 * @Version 1.0
 */
public class OkHttpSimplePostRequestExecutor extends SimplePostRequestExecutor<OkHttpClient, OkHttpProxyInfo> {
    public OkHttpSimplePostRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }

    @Override
    public String execute(String uri, String postEntity, WxType wxType) throws WxErrorException, IOException {
        RequestBody body = RequestBody.Companion.create(postEntity, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(uri).post(body).build();
        Response response = requestHttp.getRequestHttpClient().newCall(request).execute();
        return this.handleResponse(wxType, Objects.requireNonNull(response.body()).string());
    }
}
