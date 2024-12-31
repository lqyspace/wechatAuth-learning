package com.bw.iot.tbc.wechatdemo.provider.service;

import com.bw.iot.tbc.wechatdemo.provider.bean.*;
import com.bw.iot.tbc.wechatdemo.provider.config.WxCpTpConfigStorageService;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.utils.http.RequestExecutor;

/**
 * @ClassName WxCpTpService
 * @Description
 * @Author lengqy
 * @Date 2024年12月08日 03:59
 * @Version 1.0
 */
public interface WxCpTpService {
    /**
     * 获得suite_ticket, 不强制刷新suite_ticket
     * @return the suite ticket
     * @throws WxErrorException the wx error exception
     * 由于无法主动刷新获取到suite_ticket，所以这里不提供强制刷新接口
     */
    String getSuiteTicket() throws WxErrorException;

    /**
     * 保存企业微信定时推送的suite_ticket（每隔10分钟）
     * 详情请见：https://work.weixin.qq.com/api/doc#90001/90143/90628
     *
     * 注意：微信不是固定10分钟推送suite_ticket，且suite_ticket的有效期为30分钟
     * https://work.weixin.qq.com/api/doc/10975#%E8%8E%B7%E5%8F%96%E7%AC%AC%E4%B8%89%E6%96%B9%E5%BA%94%E7%94%A8%E5%87%AD%E8%AF%81
     * @param suiteTicket the suite ticket
     */
    void setSuiteTicket(String suiteTicket);

    /**
     * 保存企业微信定时推送的suite_ticket（每隔10分钟）
     * 详情请见：https://work.weixin.qq.com/api/doc#90001/90143/90628
     *
     * 注意：微信不是固定10分钟推送suite_ticket，且suite_ticket的有效期为30分钟
     * https://work.weixin.qq.com/api/doc/10975#%E8%8E%B7%E5%8F%96%E7%AC%AC%E4%B8%89%E6%96%B9%E5%BA%94%E7%94%A8%E5%87%AD%E8%AF%81
     * @param suiteTicket the suite ticket
     */
    void setSuiteTicket(String suiteTicket, int expiresInSeconds);

    /**
     * 获得 suite_access_token，不强制刷新suite_access_token
     * @return the suite access token
     * @throws WxErrorException
     */
    String getSuiteAccessToken() throws WxErrorException;

    /**
     * 强制获取新的suite_access_token，本方法线程安全
     * 且在多线程同时刷新时只刷新一次，避免超出2000次/日的调用次数上限
     * 另：本service的所有方法都会在suite_access_token过期时调用此方法
     * 程序员在非必要情况下尽量不要调用此方法
     * 详情请见：https://work.weixin.qq.com/api/doc#90001/90143/90600
     *
     * @param forceRefresh 强制刷新
     * @return the suite access token
     * @throws WxErrorException
     */
    String getSuiteAccessToken(boolean forceRefresh) throws WxErrorException;

    /**
     * 获取suite_access_token 和 剩余过期时间，不强制刷新suite_access_token
     * @return
     * @throws WxErrorException
     */
    WxAccessToken getSuiteAccessTokenEntity() throws WxErrorException;

    /**
     * 获取suite_access_token和剩余过期时间, 支持强制刷新suite_access_token
     * @param forceRefresh 强制刷新
     * @return
     * @throws WxErrorException
     */
    WxAccessToken getSuiteAccessTokenEntity(boolean forceRefresh) throws WxErrorException;

    /**
     * 设置access_token
     * @param authCorpId        授权企业id
     * @param accessToken       access_token
     * @param expiresInSeconds
     */
    void setAccessToken(String authCorpId, String accessToken, int expiresInSeconds) throws WxErrorException;

    /**
     * 获取access_token
     * @param authCorpId  授权企业的corpId
     * @return
     * @throws WxErrorException
     */
    WxAccessToken getAccessToken(String authCorpId) throws WxErrorException;

    /**
     * 获取access_token, 支持强制刷新
     * @param forceRefresh 强制刷新
     * @param authCorpId   授权企业的corpId
     * @return
     * @throws WxErrorException
     */
    WxAccessToken getAccessToken(String authCorpId, boolean forceRefresh) throws WxErrorException;

    /**
     * 获取预授权链接
     * @param redirectUri 授权完成后的回调地址
     * @param state       a-zA-Z0-9的参数值（不超过128个字节），用于第三方自行校验session，防止跨域攻击
     * @return            pre_auth_url
     * @throws WxErrorException the wx error exception
     */
    String getPreAuthUrl(String redirectUri, String state) throws WxErrorException;

    /**
     * 获取预授权链接,测试环境下使用
     * @param redirectUri 授权完成后的回调地址
     * @param state a-zA-Z0-9的参数值（不超过128个字节），用于第三方自行校验session，防止跨域攻击
     * @param authType 授权类型，0 正式授权，1测试授权
     * @return pre_auth_url
     * @throws WxErrorException
     */
    String getPreAuthUrl(String redirectUri, String state, int authType) throws WxErrorException;

    /**
     * 获取企业永久授权码信息
     * @param authCode
     * @return
     * @throws WxErrorException
     */
    WxCpTpPermanentCodeInfo getPermanentCodeInfo(String authCode) throws WxErrorException;

    /**
     * 获取企业授权信息
     * @param authCorpId      授权企业的corpId
     * @param permanentCode   授权企业的永久授权码
     * @return
     * @throws WxErrorException
     */
    WxCpTpPermanentCodeInfo getAuthInfo(String authCorpId, String permanentCode) throws WxErrorException;

    /**
     * 获取企业凭证
     * @return
     * @throws WxErrorException
     */
    WxAccessToken getCorpToken(String authCorpId, String permanentCode) throws WxErrorException;

    /**
     * 获取企业凭证, 支持强制刷新
     * @param authCorpId     授权企业id
     * @param permanentCode  永久授权码
     * @param forceRefresh   强制刷新
     * @return
     * @throws WxErrorException
     */
    WxAccessToken getCorpToken(String authCorpId, String permanentCode, boolean forceRefresh) throws WxErrorException;

    /**
     * 获取应用二维码
     * @return
     * @throws WxErrorException
     */
    WxCpTpAppQrcode getAppQrCode(String suiteId, String appId, String state, Integer style, Integer resultType) throws WxErrorException;

    /**
     * 获取应用管理员列表
     * @param accessToken  the access token
     * @param authCorpId   the auth_corp_id
     * @return
     * @throws WxErrorException
     */
    WxCpTpAdmin getAdminList(String accessToken, String authCorpId) throws WxErrorException;

    /**
     * 明文corpid转换为加密corpid，为更好地保护企业与用户的数据，第三方应用获取到的corpid不再是明文corpid，将升级为第三方服务商级别的加密corpid。
     * https://developer.work.weixin.qq.com/document/path/95327
     * 第三方可以将已有的明文corpid转换为加密corpid
     * @param corpId the corp id
     * @return
     * @throws WxErrorException
     */
    WxCpTpCorpId2OpenCorpId corpId2OpenCorpId(String corpId) throws WxErrorException;

    /**
     * 获取应用权限详情
     * @param accessToken
     * @return
     * @throws WxErrorException
     */
    WxCpTpAppPermission getAppPermission(String accessToken) throws WxErrorException;

    /**
     * 获取服务商providerToken
     *
     * @return
     * @throws WxErrorException
     */
    String getWxCpProviderToken() throws WxErrorException;

    /**
     * 获取服务商providerToken 和 剩余过期时间
     * @return
     * @throws WxErrorException
     */
    WxCpProviderToken getWxCpProviderTokenEntity() throws WxErrorException;

    /**
     * 获取服务商providerToken 和 剩余过期时间, 支持强制刷新
     * @param forceRefresh
     * @return
     * @throws WxErrorException
     */
    WxCpProviderToken getWxCpProviderTokenEntity(boolean forceRefresh) throws WxErrorException;

    /**
     * 获取访问用户身份
     * @param code
     * @return
     * @throws WxErrorException
     */
    WxCpTpUserInfo getUserInfo3rd(String code) throws WxErrorException;

    /**
     * 获取访问用户敏感信息
     * https://developer.work.weixin.qq.com/document/path/95833
     * @param userTicket
     * @return
     * @throws WxErrorException
     */
    WxCpTpUserDetail getUserDetail3rd(String userTicket) throws WxErrorException;

    /**
     * 获取登录用户身份
     * @param authCode
     * @return
     * @throws WxErrorException
     */
    WxTpLoginInfo getLoginInfo(String authCode) throws WxErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有微信API中的GET请求
     *
     * @param url 接口地址
     * @param queryParam 请求参数
     * @return the string
     * @throws WxErrorException the wx error exception
     */
    String get(String url, String queryParam) throws WxErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有微信API中的GET请求
     *
     * @param url 请求地址
     * @param queryParam 请求参数
     * @param withoutSuiteAccessToken 请求是否忽略suite_access_token，默认不忽略-false
     * @return the string
     * @throws WxErrorException
     */
    String get(String url, String queryParam, boolean withoutSuiteAccessToken) throws WxErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有微信API中的POST请求
     *
     * @param url       接口地址
     * @param postData  请求body字符串
     * @return the string
     * @throws WxErrorException the wx error exception
     */
    String post(String url, String postData) throws WxErrorException;

    /**
     * 当本Service没有实现某个API的时候，可以用这个，针对所有微信API中的POST请求
     *
     * @param url       接口地址
     * @param postData  请求body字符串
     * @param withoutSuiteAccessToken 请求是否忽略suite_access_token，默认不忽略-false
     * @return          the string
     * @throws WxErrorException  the wx error exception
     */
    String post(String url, String postData, boolean withoutSuiteAccessToken) throws WxErrorException;

    /**
     * Service没有实现某个API的时候，可以用这个
     * 比 #get 和 #post 方法更加灵活，可以自己构造RequestExecutor用来处理不同的参数和不同的返回类型
     *
     * @param executor 执行器
     * @param uri      请求地址
     * @param data     参数
     * @return
     * @param <T>      请求值类型
     * @param <E>      返回值类型
     * @throws WxErrorException
     */
    <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException;

    WxCpTpConfigStorageService getConfigStorage() throws WxErrorException;
}
