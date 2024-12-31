package com.bw.iot.tbc.wechatdemo.provider.service;

/**
 * @ClassName WxCpTpContactService
 * @Description
 * @Author lengqy
 * @Date 2024年12月29日 20:50
 * @Version 1.0
 */

import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpTpContactSearch;
import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpTpContactSearchResp;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;

/**
 * 通讯录的服务接口
 */
public interface WxCpTpContactService {

    /**
     * 通讯录单个搜索：https://work.weixin.qq.com/api/doc/90001/90143/91844
     *
     * @param search
     * @return
     * @throws WxErrorException
     */
    WxCpTpContactSearchResp contactSearch(WxCpTpContactSearch search) throws WxErrorException;
}
