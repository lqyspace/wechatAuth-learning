package com.bw.iot.tbc.wechatdemo.provider.utils.http.apache;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @ClassName Utf8ResponseHandler
 * @Description
 * @Author lengqy
 * @Date 2024年12月24日 18:15
 * @Version 1.0
 */
public class Utf8ResponseHandler implements ResponseHandler<String> {
    public static final ResponseHandler<String> INSTANCE = new Utf8ResponseHandler();
    @Override
    public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        final StatusLine statusLine = httpResponse.getStatusLine();
        final HttpEntity entity = httpResponse.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.toString());
        }
        return entity == null ? null: EntityUtils.toString(entity, Consts.UTF_8);
    }
}
