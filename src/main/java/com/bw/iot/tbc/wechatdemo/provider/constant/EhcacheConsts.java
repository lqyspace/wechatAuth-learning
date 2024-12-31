package com.bw.iot.tbc.wechatdemo.provider.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * @ClassName EhcacheConsts
 * @Description
 * @Author lengqy
 * @Date 2024年12月08日 03:27
 * @Version 1.0
 */
public class EhcacheConsts {
    public final static String suiteAccessTokenKey = ":suiteAccessTokenKey:";
    public final static String suiteTicketKey = ":suiteTicketKey:";
    public final static String accessTokenKey = ":accessTokenKey:";
    public final static String authCorpJsApiTicketKey = ":authCorpJsApiTicketKey:";
    public final static String authSuiteJsApiTicketKey = ":authSuiteJsApiTicketKey:";
    public final static String providerTokenKey = ":providerTokenKey:";

    @Value("${qwConfig.ehcache.key-prefix:}")
    private static String keyPrefix;

    @Value("${qwagent.suite-id:}")
    private static String suiteId;

    public static String keyWithPrefix(String key) {
        return keyPrefix + key + suiteId;
    }
}
