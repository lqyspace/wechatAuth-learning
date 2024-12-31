package com.bw.iot.tbc.wechatdemo.provider.service.impl;

import com.bw.iot.tbc.wechatdemo.provider.config.WxCpTpConfigStorageService;
import com.bw.iot.tbc.wechatdemo.provider.constant.WxCpApiPathConsts;
import com.bw.iot.tbc.wechatdemo.provider.enums.WxType;
import com.bw.iot.tbc.wechatdemo.provider.error.WxError;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.HttpType;
import com.bw.iot.tbc.wechatdemo.provider.utils.json.GsonParser;
import com.google.gson.JsonObject;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

/**
 * @ClassName WxCpTpServiceApacheHttpClientImpl
 * @Description
 * @Author lengqy
 * @Date 2024年12月19日 00:07
 * @Version 1.0
 */
@Service("wxCpTpServiceApacheHttpClientImpl")
public class WxCpTpServiceApacheHttpClientImpl extends WxCpTpServiceImpl<CloseableHttpClient, HttpHost>{
    private CloseableHttpClient httpClient;
    private HttpHost httpProxy;

    @Override
    public CloseableHttpClient getRequestHttpClient() {
        httpClient = HttpClients.createDefault();
        return httpClient;
    }

    @Override
    public HttpHost getRequestHttpProxy() {
        return httpProxy;
    }

    @Override
    public HttpType getRequestType() {
        return HttpType.APACHE_HTTP;
    }

    @Override
    public String getSuiteAccessToken(boolean forceRefresh) throws WxErrorException {
        if (!this.configStorage.isSuiteAccessTokenExpired() && !forceRefresh) {
            return this.configStorage.getSuiteAccessToken();
        }
        synchronized (this.globalSuiteAccessTokenRefreshLock) {
            try {
                HttpPost httpPost = new HttpPost(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_SUITE_TOKEN));
                if (this.httpProxy != null) {
                    RequestConfig config = RequestConfig.custom().
                            setProxy(this.httpProxy)
                            .build();
                    httpPost.setConfig(config);
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("suite_id", this.configStorage.getSuiteId());
                jsonObject.addProperty("suite_secret", this.configStorage.getSuiteSecret());
                jsonObject.addProperty("suite_ticket", this.configStorage.getSuiteTicket());
                StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
                httpPost.setEntity(entity);

                String resultContent = null;
                try(CloseableHttpClient client = getRequestHttpClient();
                    CloseableHttpResponse response = client.execute(httpPost)) {
                    resultContent = new BasicResponseHandler().handleResponse(response);
                } finally {
                    httpPost.releaseConnection();
                }
                WxError error = WxError.fromJson(resultContent, WxType.CP);
                if (error.getErrorCode() != 0) {
                    throw new WxErrorException(error);
                }
                jsonObject = GsonParser.parser(resultContent);
                String suiteAccessToken = jsonObject.get("suite_access_token").getAsString();
                Integer expiresIn = jsonObject.get("expires_in").getAsInt();
                this.configStorage.updateSuiteAccessToken(suiteAccessToken, expiresIn);
            } catch (Exception e) {
                throw new WxErrorException(e);
            }
        }
        return this.configStorage.getSuiteAccessToken();
    }

    @Override
    public WxCpTpConfigStorageService getConfigStorage() {
        return this.configStorage;
    }
}
