package com.bw.iot.tbc.wechatdemo.provider.config;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxAccessToken;
import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpProviderToken;

/**
 * @ClassName WxCpTpConfigStorageService
 * @Description 微信客户端（第三方应用）配置存储服务
 * @Author lengqy
 * @Date 2024年12月07日 21:26
 * @Version 1.0
 */
public interface WxCpTpConfigStorageService {
    /**
     * 设置企业微信服务器 baseUrl
     * 默认值是 https://qyapi.weixin.qq.com ，如果使用默认值，则不需要调用 setBaseApiUrl
     *
     * @param baseUrl 企业微信服务器 Url
     */
    void setBaseApiUrl(String baseUrl);

    /**
     * 读取企业微信 API Url
     * 支持私有化企业微信服务器
     *
     * @param path the path
     * @return the api url
     */
    String getApiUrl(String path);

    /**
     * 第三方应用的suite access token
     * @return the suite access token
     */
    String getSuiteAccessToken();

    /**
     * 第三方应用的suite access token
     * @param suiteAccessToken
     */
    void setSuiteAccessToken(String suiteAccessToken);

    /**
     * 获取suite access token 和 剩余过期时间
     * @return the suite access token and the remaining expiration time
     */
    WxAccessToken getSuiteAccessTokenEntity();

    /**
     * Is suite access token expired boolean
     * @return the boolean
     */
    boolean isSuiteAccessTokenExpired();

    /**
     * Expire suite access token.
     * 强制 suite access token 失效
     */
    void expireSuiteAccessToken();

    /**
     * Update suite access token.
     *
     * @param accessToken the access token
     */
    void updateSuiteAccessToken(WxAccessToken accessToken);

    /**
     * Update suite access token.
     *
     * @param accessToken the access token
     * @param expiresInSeconds the expires in seconds
     */
    void updateSuiteAccessToken(String accessToken, int expiresInSeconds);

    /**
     * 第三方应用的 suite ticket相关
     * @return the suite
     */
    String getSuiteTicket();

    /**
     * set suite ticket
     *
     * @param suiteTicket the suite ticket
     */
    void setSuiteTicket(String suiteTicket);

    /**
     * Is suite ticket expired boolean.
     *
     * @return the boolean
     */
    boolean isSuiteTicketExpired();

    /**
     * Expire suite ticket.
     * 强制将suite ticket过期掉
     */
    void expireSuiteTicket();

    /**
     * Update suite ticket.
     *
     * @param suiteTicket      the suite ticket
     * @param expiresInSeconds the expires in seconds
     */
    void updateSuiteTicket(String suiteTicket, int expiresInSeconds);

    /**
     * 第三方应用的其他配置，来自于企微配置
     *
     * @return the suite id
     */
    String getSuiteId();

    /**
     * Gets suite secret.
     *
     * @return the suite secret
     */
    String getSuiteSecret();

    /**
     * 第三方应用token，用来检查应用的签名
     * get token
     *
     * @return the token
     */
    String getToken();

    /**
     * 授权企业的永久授权码
     * @return
     */
    String getPermanentCode();

    /**
     * 密钥，用于加解密消息
     *
     * @return the aes key
     */
    String getAesKey();

    /**
     * 企业id
     * @return the corp id
     */
    String getCorpId();

    /**
     * 密钥，用于第三方应用获取用户信息
     * Get Corp Secret
     * @return the corp secret
     */
    String getCorpSecret();

    /**
     * 服务商secret
     *
     * @return the provider secret
     */
    String getProviderSecret();

    /**
     * 授权企业的access token相关
     *
     * @param authCorpId the auth corp id
     * @return the access token
     */
    String getAccessToken(String authCorpId);

    /**
     * Get access token entity.
     *
     * @param authCorpId the auth corp id
     * @return the access token entity
     */
    WxAccessToken getAccessTokenEntity(String authCorpId);

    /**
     * Is access token expired boolean.
     *
     * @param authCorpId the auth corp id
     * @return the boolean
     */
    boolean isAccessTokenExpired(String authCorpId);

    /**
     * 强制将access token过期掉
     *
     * @param authCorpId the auth corp id
     */
    void expireAccessToken(String authCorpId);

    /**
     * Update access token.
     *
     * @param authCorpId the auth corp id
     * @param accessToken the access token
     * @param expiresInSeconds the expires in seconds
     */
    void updateAccessToken(String authCorpId, String accessToken, int expiresInSeconds);

    /**
     * Is provider token expired boolean.
     * @return
     */
    boolean isProviderTokenExpired();

    /**
     * Update provider token.
     * @param providerToken
     * @param expiresInSeconds
     */
    void updateProviderToken(String providerToken, int expiresInSeconds);

    /**
     * Get provider token.
     * @return
     */
    String getProviderToken();

    /**
     * Get provider token entity.
     * @return
     */
    WxCpProviderToken getProviderTokenEntity();

    /**
     * 强制将provider token过期掉
     */
    void expireProviderToken();
}
