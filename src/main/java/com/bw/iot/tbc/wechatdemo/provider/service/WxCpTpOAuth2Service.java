package com.bw.iot.tbc.wechatdemo.provider.service;

/**
 * @ClassName WxCpTpOAuth2Service
 * @Description
 * @Author lengqy
 * @Date 2024年12月30日 09:18
 * @Version 1.0
 */

import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;

/**
 * 构造第三方应用oauth2链接接口
 * https://developer.work.weixin.qq.com/document/path/91120
 */
public interface WxCpTpOAuth2Service {
    /**
     * 构造第三方应用oauth2链接（静默授权）
     * @param redirectUri  授权后重定向的回调链接地址
     * @param state        重定向后state参数
     * @return             url链接
     * @throws WxErrorException
     */
    String buildAuthorizeUrl(String redirectUri, String state) throws WxErrorException;

    /**
     * 构造第三方应用oauth2链接
     * @param redirectUri 授权后重定向的回调链接地址
     * @param state       重定向后的state参数
     * @param scope       应用授权作用域
     * @return
     * @throws WxErrorException
     */
    String buildAuthorizeUrl(String redirectUri, String state, String scope) throws WxErrorException;
}
