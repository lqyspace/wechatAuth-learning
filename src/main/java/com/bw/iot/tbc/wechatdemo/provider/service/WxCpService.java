package com.bw.iot.tbc.wechatdemo.provider.service;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpAgentJsapiSignature;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;

/**
 * @ClassName WxCpService
 * @Description
 * @Author lengqy
 * @Date 2024年12月31日 09:40
 * @Version 1.0
 */
public interface WxCpService {
    /**
     * 获得jsapi_ticket,不强制刷新jsapi_ticket
     * 应用的jsapi_ticket用于计算agentConfig（参见“通过agentConfig注入应用的权限”）的签名，签名计算方法与上述介绍的config的签名算法完全相同，但需要注意以下区别：
     * 签名的jsapi_ticket必须使用以下接口获取。且必须用wx.agentConfig中的agentid对应的应用secret去获取access_token。
     * 签名用的noncestr和timestamp必须与wx.agentConfig中的nonceStr和timestamp相同。
     *
     * @return
     * @throws WxErrorException
     */
    String getAgentJsapiTicket() throws WxErrorException;
    /**
     * 获取应用的jsapi_ticket
     * 应用的jsapi_ticket用于计算agentConfig（参见“通过agentConfig注入应用的权限”）的签名签名计算方法与上述介绍的config的签名算法相同，
     * 但需要注意以下区别：
     *  签名的jsapi_ticket必须使用以下接口获取。且必须使用wx.agentConfig中的agentid对应的应用secret去获取access_token。
     *  签名用的nonceStr和timestamp必须与wx.agentConfig中的nonceStr和timestamp相同
     *
     * 获得时会检查jsapiToken是否过期，如果过期了，那么刷新一下，否则就什么都不干
     *
     * 详情请见：https://work.weixin.qq.com/api/doc#10029/%E8%8E%B7%E5%8F%96%E5%BA%94%E7%94%A8%E7%9A%84jsapi_ticket
     * @param forceRefresh
     * @return
     * @throws WxErrorException
     */
    String getAgentJsapiTicket(boolean forceRefresh) throws WxErrorException;
    /**
     * 创建调用wx.agentConfig时所需要的签名
     * 详情请见：https://open.work.weixin.qq.com/api/doc/90000/90136/94313
     * @param url
     * @return
     * @throws WxErrorException
     */
    WxCpAgentJsapiSignature createAgentJsapiSignature(String url, Integer agentId) throws WxErrorException;
}
