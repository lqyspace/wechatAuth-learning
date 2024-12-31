package com.bw.iot.tbc.wechatdemo.provider.service.impl;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpAgentJsapiSignature;
import com.bw.iot.tbc.wechatdemo.provider.config.WxCpConfigStorage;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpService;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpTpService;
import com.bw.iot.tbc.wechatdemo.provider.utils.RandomUtils;
import com.bw.iot.tbc.wechatdemo.provider.utils.crypto.SHA1;
import com.bw.iot.tbc.wechatdemo.provider.utils.json.GsonParser;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bw.iot.tbc.wechatdemo.provider.constant.WxCpApiPathConsts.GET_AGENT_CONFIG_TICKET;

/**
 * @ClassName WxCpServiceImpl
 * @Description
 * @Author lengqy
 * @Date 2024年12月31日 09:57
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WxCpServiceImpl implements WxCpService {

    private final WxCpTpService wxCpTpService;
    @Autowired
    private WxCpConfigStorage configStorage;
    /**
     * 全局的是否正在刷新agent的jsapi_ticket的锁.
     */
    protected final Object globalAgentJsapiTicketRefreshLock = new Object();
    @Override
    public String getAgentJsapiTicket() throws WxErrorException {
        return this.getAgentJsapiTicket(false);
    }

    @Override
    public String getAgentJsapiTicket(boolean forceRefresh) throws WxErrorException {
        if (forceRefresh) {
            this.configStorage.expireAgentJsapiTicket();
        }
        if (this.configStorage.isAgentJsapiTicketExpired()) {
            synchronized (this.globalAgentJsapiTicketRefreshLock) {
                if (this.configStorage.isAgentJsapiTicketExpired()) {
                    String response = this.wxCpTpService.get(this.wxCpTpService.getConfigStorage().getApiUrl(GET_AGENT_CONFIG_TICKET), null);
                    JsonObject jsonObject = GsonParser.parser(response);
                    this.configStorage.updateAgentJsapiTicket(jsonObject.get("ticket").getAsString(),
                            jsonObject.get("expires_in").getAsInt());
                }
            }
        }
        return this.configStorage.getAgentJsapiTicket();
    }

    @Override
    public WxCpAgentJsapiSignature createAgentJsapiSignature(String url, Integer agentId) throws WxErrorException {
        long timestamp = System.currentTimeMillis() / 1000;
        String noncestr = RandomUtils.getRandomStr();
        String jsapiTicket = getAgentJsapiTicket(false);
        String signature = SHA1.genWithAmple(
                "jsapi_ticket=" + jsapiTicket,
                "noncestr=" + noncestr,
                "timestamp=" + timestamp,
                "url=" + url
        );

        WxCpAgentJsapiSignature jsapiSignature = new WxCpAgentJsapiSignature();
        jsapiSignature.setTimestamp(timestamp);
        jsapiSignature.setNonceStr(noncestr);
        jsapiSignature.setUrl(url);
        jsapiSignature.setSignature(signature);

        jsapiSignature.setCorpid(this.configStorage.getCorpId());
        jsapiSignature.setAgentid(this.configStorage.getAgentId());

        return jsapiSignature;
    }
}
