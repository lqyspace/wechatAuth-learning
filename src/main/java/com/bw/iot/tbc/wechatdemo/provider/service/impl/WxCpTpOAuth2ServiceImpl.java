package com.bw.iot.tbc.wechatdemo.provider.service.impl;

import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpTpOAuth2Service;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpTpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

import static com.bw.iot.tbc.wechatdemo.provider.constant.WxConsts.OAuth2Scope.SNSAPI_BASE;
import static com.bw.iot.tbc.wechatdemo.provider.constant.WxCpApiPathConsts.OAuth2.URL_OAUTH2_AUTHORIZE;

/**
 * @ClassName WxCpTpOAuth2ServiceImpl
 * @Description
 * @Author lengqy
 * @Date 2024年12月30日 09:32
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxCpTpOAuth2ServiceImpl implements WxCpTpOAuth2Service {

    private final WxCpTpService wxCpTpService;
    @Override
    public String buildAuthorizeUrl(String redirectUri, String state) throws WxErrorException {
        return this.buildAuthorizeUrl(redirectUri, state, SNSAPI_BASE);
    }

    @Override
    public String buildAuthorizeUrl(String redirectUri, String state, String scope) throws WxErrorException {
        try {
            StringBuilder url = new StringBuilder(URL_OAUTH2_AUTHORIZE);
            url.append("?appid=").append(this.wxCpTpService.getConfigStorage().getSuiteId());
            url.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
            url.append("&response_type=code");
            url.append("&scope=").append(scope);
            if (state != null) {
                url.append("&state=").append(state);
            }
            url.append("#wechat_redirect");
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("构建网页授权url失败！", e);
        }
        return null;
    }
}
