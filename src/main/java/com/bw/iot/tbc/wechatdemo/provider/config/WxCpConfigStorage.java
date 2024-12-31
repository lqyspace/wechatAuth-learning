package com.bw.iot.tbc.wechatdemo.provider.config;

/**
 * @ClassName WxCpConfigStorage
 * @Description
 * @Author lengqy
 * @Date 2024年12月31日 10:00
 * @Version 1.0
 */
public interface WxCpConfigStorage {
    /**
     * 强制将jsapi ticket过期掉.
     */
    void expireAgentJsapiTicket();

    /**
     * Is agent jsapi ticket expired boolean.
     *
     * @return the boolean
     */
    boolean isAgentJsapiTicketExpired();

    /**
     * 应该是线程安全的.
     *
     * @param jsapiTicket      the jsapi ticket
     * @param expiresInSeconds the expires in seconds
     */
    void updateAgentJsapiTicket(String jsapiTicket, int expiresInSeconds);
    /**
     * Gets agent jsapi ticket.
     *
     * @return the agent jsapi ticket
     */
    String getAgentJsapiTicket();

    /**
     * Gets corp id.
     *
     * @return the corp id
     */
    String getCorpId();
    /**
     * Gets agent id.
     *
     * @return the agent id
     */
    Integer getAgentId();
}
