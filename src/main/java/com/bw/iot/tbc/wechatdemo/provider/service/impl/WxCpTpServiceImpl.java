package com.bw.iot.tbc.wechatdemo.provider.service.impl;

import com.bw.iot.tbc.wechatdemo.provider.bean.*;
import com.bw.iot.tbc.wechatdemo.provider.config.WxCpTpConfigStorageService;
import com.bw.iot.tbc.wechatdemo.provider.constant.WxCpApiPathConsts;
import com.bw.iot.tbc.wechatdemo.provider.enums.WxCpErrorMsgEnum;
import com.bw.iot.tbc.wechatdemo.provider.enums.WxType;
import com.bw.iot.tbc.wechatdemo.provider.error.WxError;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.error.WxRuntimeException;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpTpService;
import com.bw.iot.tbc.wechatdemo.provider.utils.DataUtils;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.RequestExecutor;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.RequestHttp;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.SimpleGetRequestExecutor;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.SimplePostRequestExecutor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName WxCpTpServiceImpl
 * @Description
 * @Author lengqy
 * @Date 2024年12月09日 02:01
 * @Version 1.0
 */
@Slf4j
public abstract class WxCpTpServiceImpl<H, P> implements WxCpTpService, RequestHttp<H, P> {
    /**
     * The Config storage.
     */
    @Autowired
    protected WxCpTpConfigStorageService configStorage;

    /**
     * 全局刷新suite ticket的锁
     */
    protected final Object globalSuiteTicketRefreshLock = new Object();
    /**
     * 全局的是否正在刷新suite access token的锁
     */
    protected final Object globalSuiteAccessTokenRefreshLock = new Object();
    /**
     * The Global provider token refresh lock.
     */
    protected final Object globalProviderTokenRefreshLock = new Object();

    private int maxRetryTimes = 5;
    private int retrySleepMillis = 1000;

    @Override
    public String getSuiteTicket() throws WxErrorException {
        if (this.configStorage.isSuiteTicketExpired()) {
            WxError wxError = WxError.fromJson("{\"errcode\": 40085, \"errmsg\": \"invalid suite ticket\"}", WxType.CP);
            throw new WxErrorException(wxError);
        }
        return this.configStorage.getSuiteTicket();
    }

    @Override
    public void setSuiteTicket(String suiteTicket) {
        setSuiteTicket(suiteTicket, 28 * 60);
    }

    @Override
    public void setSuiteTicket(String suiteTicket, int expiresInSeconds) {
        synchronized (globalSuiteTicketRefreshLock) {
            this.configStorage.updateSuiteTicket(suiteTicket, expiresInSeconds);
        }
    }

    @Override
    public String getSuiteAccessToken() throws WxErrorException {
        return getSuiteAccessToken(false);
    }

    @Override
    public WxAccessToken getSuiteAccessTokenEntity(boolean forceRefresh) throws WxErrorException {
        getSuiteAccessToken(forceRefresh);
        return this.configStorage.getSuiteAccessTokenEntity();
    }

    @Override
    public WxAccessToken getSuiteAccessTokenEntity() throws WxErrorException {
        return this.getSuiteAccessTokenEntity(false);
    }

    @Override
    public void setAccessToken(String authCorpId, String accessToken, int expiresInSeconds) throws WxErrorException {
        this.configStorage.updateAccessToken(authCorpId, accessToken, expiresInSeconds);
    }

    @Override
    public WxAccessToken getAccessToken(String authCorpId, boolean forceRefresh) throws WxErrorException {
        if (!this.configStorage.isAccessTokenExpired(authCorpId) && !forceRefresh)
            return this.configStorage.getAccessTokenEntity(authCorpId);
        WxAccessToken corpToken = this.getCorpToken(authCorpId, this.configStorage.getPermanentCode(), forceRefresh);
        this.setAccessToken(authCorpId, corpToken.getAccessToken(), corpToken.getExpiresIn());
        return this.configStorage.getAccessTokenEntity(authCorpId);
    }

    @Override
    public WxAccessToken getAccessToken(String authCorpId) throws WxErrorException {
        return this.getAccessToken(authCorpId, false);
    }

    @Override
    @SneakyThrows
    public String getPreAuthUrl(String redirectUri, String state) throws WxErrorException {
        String result = get(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_PREAUTH_CODE), null);
        WxCpTpPreauthCode preAuthCode = WxCpTpPreauthCode.fromJson(result);
        String preAuthUrl = "https://open.work.weixin.qq.com/3rdapp/install?suite_id=" + this.configStorage.getSuiteId() +
                "&pre_auth_code=" + preAuthCode.getPreAuthCode() + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8");
        if (StringUtils.isNotEmpty(state)) {
            preAuthUrl += "&state=" + state;
        }
        return preAuthUrl;
    }
    @Override
    @SneakyThrows
    public String getPreAuthUrl(String redirectUri, String state, int authType) throws WxErrorException {
        String result = get(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_PREAUTH_CODE), null);
        WxCpTpPreauthCode preAuthCode = WxCpTpPreauthCode.fromJson(result);
        String setSessionUrl = "https://qyapi.weixin.qq.com/cgi-bin/service/set_session_info";

        Map<String, Object> sessionInfo = new HashMap<>(1);
        sessionInfo.put("auth_type", authType);
        Map<String, Object> param = new HashMap<>(2);
        param.put("pre_auth_code", preAuthCode.getPreAuthCode());
        param.put("session_info", sessionInfo);
        String postData = new Gson().toJson(param);

        post(setSessionUrl, postData);

        String preAuthUrl = "https://open.work.weixin.qq.com/3rdapp/install?suite_id=" + this.configStorage.getSuiteId() +
                "&pre_auth_code=" + preAuthCode.getPreAuthCode() + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8");
        if (StringUtils.isNotEmpty(state)) {
            preAuthUrl += "&state=" + state;
        }
        return preAuthUrl;
    }

    @Override
    public WxCpTpPermanentCodeInfo getPermanentCodeInfo(String authCode) throws WxErrorException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_code", authCode);

        String result = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_PERMANENT_CODE), jsonObject.toString());
        return WxCpTpPermanentCodeInfo.fromJson(result);
    }

    @Override
    public WxCpTpPermanentCodeInfo getAuthInfo(String authCorpId, String permanentCode) throws WxErrorException {
        if (Objects.isNull(authCorpId)) {
            throw new WxErrorException(new WxError(84054, WxCpErrorMsgEnum.findMsgByCode(84054)));
        }
        if (Objects.isNull(permanentCode)) {
            throw new WxErrorException(new WxError(41025, WxCpErrorMsgEnum.findMsgByCode(41025)));
        }
        JsonObject json = new JsonObject();
        json.addProperty("auth_corpid", authCorpId);
        json.addProperty("permanent_code", permanentCode);
        String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_AUTH_INFO), json.toString());
        return Objects.isNull(resp)? null: WxCpTpPermanentCodeInfo.fromJson(resp);
    }

    @Override
    public WxAccessToken getCorpToken(String authCorpId, String permanentCode) throws WxErrorException {
        JsonObject json = new JsonObject();
        json.addProperty("auth_corpid", authCorpId);
        json.addProperty("permanent_code", permanentCode);
        String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_CORP_TOKEN), json.toString());
        return Objects.isNull(resp)? null: WxAccessToken.fromJson(resp);
    }

    @Override
    public WxAccessToken getCorpToken(String authCorpId, String permanentCode, boolean forceRefresh) throws WxErrorException {
        if (this.configStorage.isAccessTokenExpired(authCorpId) || forceRefresh) {
            WxAccessToken corpToken = this.getCorpToken(authCorpId, permanentCode);
            this.configStorage.updateAccessToken(authCorpId, corpToken.getAccessToken(), corpToken.getExpiresIn());
        }
        return this.configStorage.getAccessTokenEntity(authCorpId);
    }

    @Override
    public WxCpTpAppQrcode getAppQrCode(String suiteId, String appId, String state, Integer style, Integer resultType) throws WxErrorException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("suite_id", suiteId);
        jsonObject.addProperty("appid", appId);
        jsonObject.addProperty("state", state);
        jsonObject.addProperty("style", style);
        jsonObject.addProperty("result_type", resultType);
        String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_APP_QRCODE), jsonObject.toString());
        return Objects.isNull(resp)? null: WxCpTpAppQrcode.fromJson(resp);
    }

    @Override
    public WxCpTpAdmin getAdminList(String accessToken, String authCorpId) throws WxErrorException {
        String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_ADMIN_LIST_AGENT) + "?access_token=" + accessToken, null, true);
        return Objects.isNull(resp)? null: WxCpTpAdmin.fromJson(resp);
    }

    @Override
    public WxCpTpCorpId2OpenCorpId corpId2OpenCorpId(String corpId) throws WxErrorException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("corpid", corpId);
        String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.CORPID_TO_OPENCORPID) + "?provider_access_token=" + getWxCpProviderToken(), corpId, true);
        return Objects.isNull(resp)? null: WxCpTpCorpId2OpenCorpId.fromJson(resp);
    }

    @Override
    public WxCpTpAppPermission getAppPermission(String accessToken) throws WxErrorException {
        String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_APP_PERMISSION) + "?access_token=" + accessToken, null, true);
        return Objects.isNull(resp)? null: WxCpTpAppPermission.fromJson(resp);
    }

    @Override
    public String getWxCpProviderToken() throws WxErrorException {
        if (this.configStorage.isProviderTokenExpired()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("corpid", this.configStorage.getCorpId());
            jsonObject.addProperty("provider_secret", this.configStorage.getProviderSecret());
            String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_PROVIDER_TOKEN), jsonObject.toString(), true);
            WxCpProviderToken providerToken = WxCpProviderToken.fromJson(resp);
            String providerAccessToken = providerToken.getProviderAccessToken();
            Integer expiresIn = providerToken.getExpiresIn();

            synchronized(globalProviderTokenRefreshLock) {
                this.configStorage.updateProviderToken(providerAccessToken, expiresIn);
            }
        }
        return this.configStorage.getProviderToken();
    }

    @Override
    public WxCpProviderToken getWxCpProviderTokenEntity() throws WxErrorException {
        return this.getWxCpProviderTokenEntity(false);
    }

    @Override
    public WxCpProviderToken getWxCpProviderTokenEntity(boolean forceRefresh) throws WxErrorException {
        if (forceRefresh) {
            this.configStorage.expireProviderToken();
        }
        this.getWxCpProviderToken();
        return this.configStorage.getProviderTokenEntity();
    }

    @Override
    public WxCpTpUserInfo getUserInfo3rd(String code) throws WxErrorException {
        String url = this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_USERINFO3RD) + "?access_token=" + getSuiteAccessToken() + "&code=" + code;
        String resp = get(url, null, true);
        return Objects.isNull(resp)? null: WxCpTpUserInfo.fromJson(resp);
    }

    @Override
    public WxCpTpUserDetail getUserDetail3rd(String userTicket) throws WxErrorException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_ticket", userTicket);
        String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_USERDETAIL3RD) + "?access_token=" + getSuiteAccessToken(), jsonObject.toString(), true);
        return Objects.isNull(resp)? null: WxCpTpUserDetail.fromJson(resp);
    }

    @Override
    public WxTpLoginInfo getLoginInfo(String authCode) throws WxErrorException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_code", authCode);
        String resp = post(this.configStorage.getApiUrl(WxCpApiPathConsts.Tp.GET_LOGIN_INFO) + "?access_token=" + getWxCpProviderToken(), jsonObject.toString(), true);
        return Objects.isNull(resp)? null: WxTpLoginInfo.fromJson(resp);
    }

    @Override
    public String get(String url, String queryParam) throws WxErrorException {
        return execute(SimpleGetRequestExecutor.create(this), url, queryParam);
    }

    @Override
    public String get(String url, String queryParam, boolean withoutSuiteAccessToken) throws WxErrorException {
        return execute(SimpleGetRequestExecutor.create(this), url, queryParam, withoutSuiteAccessToken);
    }

    @Override
    public String post(String url, String postData) throws WxErrorException {
        // 这个this代表的是WxCpTpServiceImpl的子类，需要注意WxCpTpServiceImpl是抽象类，抽象类的实例化是其子类的实例化
        return execute(SimplePostRequestExecutor.create(this), url, postData);
    }

    @Override
    public String post(String url, String postData, boolean withoutSuiteAccessToken) throws WxErrorException {
        return execute(SimplePostRequestExecutor.create(this), url, postData, withoutSuiteAccessToken);
    }

    @Override
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
        return execute(executor, uri, data, false);
    }
    /**
     * Execute t
     * @param executor         the executor
     * @param uri              the uri
     * @param data             the data
     * @param withoutSuiteAccessToken  the without suite access token
     * @return                 the t
     * @param <T>              the type parameter
     * @param <E>              the type parameter
     * @throws WxErrorException the wx error exception
     */
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data, boolean withoutSuiteAccessToken) throws WxErrorException{
        int retryTimes = 0;
        do {
            try {
                return this.executeInternal(executor, uri, data, withoutSuiteAccessToken);
            } catch (WxErrorException e) {
                if (retryTimes + 1 > this.maxRetryTimes) {
                    log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
                    //最后一次重试失败后，直接抛出异常，不再等待
                    throw new WxRuntimeException("微信服务端异常，超出重试次数");
                }

                WxError error = e.getError();
                /**
                 * -1 系统繁忙，1000ms后自动重试
                 */
                if (error.getErrorCode() == -1) {
                    int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
                    try {
                        log.debug("微信系统繁忙，{} ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                    }
                }else {
                    throw e;
                }
            }
        } while(retryTimes++ < this.maxRetryTimes);

        log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
        throw new WxRuntimeException("微信服务端异常，超出重试次数");
    }

    /**
     * Execute internal t.
     * @param executor
     * @param uri
     * @param data
     * @return
     * @param <T>
     * @param <E>
     * @throws WxErrorException
     */
    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
        return executeInternal(executor, uri, data, false);
    }

    /**
     * Execute internal t.
     * @param executor
     * @param uri
     * @param data
     * @param withoutSuiteAccessToken
     * @return
     * @param <T>
     * @param <E>
     * @throws WxErrorException
     */
    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data,
                                       boolean withoutSuiteAccessToken) throws WxErrorException {
        E dataForLog = DataUtils.handleDataWithSecret(data);

        if (uri.contains("suite_access_token=")) {
            throw new IllegalArgumentException("uri参数中不允许有suite_access_token: " + uri);
        }
        // 确保suite_access_token一定是由内部拼接而成，而不是从外部传进来的
        String uriWithAccessToken;
        if (!withoutSuiteAccessToken) {
            String suiteAccessToken = getSuiteAccessToken(false);
            uriWithAccessToken = uri + (uri.contains("?")? "&": "?") + "suite_access_token=" + suiteAccessToken;
        } else {
            uriWithAccessToken = uri;
        }

        try {
            T result = executor.execute(uriWithAccessToken, data, WxType.CP);
            log.debug("\n 【请求地址】：{}\n 【请求参数】：{}\n 【响应数据】：{}", uriWithAccessToken, dataForLog, result);
            return result;
        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            /**
             * 发生以下情况时尝试重新刷新suite_access_token
             * 42009 suite_access_token已过期
             */
            if (wxError.getErrorCode() == WxCpErrorMsgEnum.CODE_42009.getCode()) {
                // 强制设置wxCpTpConfigStorage它的suite_access_token过期，这样在下一次请求里就会刷新suite access token
                this.configStorage.expireSuiteAccessToken();
                return this.execute(executor, uri, data);
            }

            if (wxError.getErrorCode() != 0) {
                log.error("\n【请求地址】: {}\n【请求参数】：{}\n【错误信息】：{}", uriWithAccessToken, dataForLog, wxError);
                throw new WxErrorException(wxError, e);
            }
            return null;
        } catch (IOException e) {
            log.error("\n【请求地址】: {}\n【请求参数】：{}\n【异常信息】：{}", uriWithAccessToken, dataForLog, e.getMessage());
            throw new WxRuntimeException(e);
        }
    }
}
