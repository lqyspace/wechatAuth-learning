package com.bw.iot.tbc.wechatdemo.provider.utils.http.apache;

import com.bw.iot.tbc.wechatdemo.provider.enums.WxType;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.RequestHttp;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.SimplePostRequestExecutor;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * @ClassName ApacheSimplePostRequestExecutor
 * @Description
 * @Author lengqy
 * @Date 2024年12月25日 00:57
 * @Version 1.0
 */
public class ApacheSimplePostRequestExecutor extends SimplePostRequestExecutor<CloseableHttpClient, HttpHost> {
    public ApacheSimplePostRequestExecutor(RequestHttp requestHttp) {
        super(requestHttp);
    }
    @Override
    public String execute(String uri, String postEntity, WxType wxType) throws WxErrorException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        if (requestHttp.getRequestHttpProxy() != null) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setProxy(requestHttp.getRequestHttpProxy()).build();
            httpPost.setConfig(requestConfig);
        }

        if (postEntity != null) {
            StringEntity stringEntity = new StringEntity(postEntity, "UTF-8");
            stringEntity.setContentType("application/json; charset=utf-8");
            httpPost.setEntity(stringEntity);
        }

        try(CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            return this.handleResponse(wxType, responseContent);
        } finally {
            httpPost.releaseConnection();
        }
    }
}
