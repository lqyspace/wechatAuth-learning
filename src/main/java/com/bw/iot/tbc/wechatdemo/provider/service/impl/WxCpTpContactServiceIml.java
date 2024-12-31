package com.bw.iot.tbc.wechatdemo.provider.service.impl;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpTpContactSearch;
import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpTpContactSearchResp;
import com.bw.iot.tbc.wechatdemo.provider.constant.WxCpApiPathConsts;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpTpContactService;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpTpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @ClassName WxCpTpContactServiceIml
 * @Description
 * @Author lengqy
 * @Date 2024年12月29日 21:18
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WxCpTpContactServiceIml implements WxCpTpContactService {


    private final WxCpTpService wxCpTpService;
    @Override
    public WxCpTpContactSearchResp contactSearch(WxCpTpContactSearch search) throws WxErrorException {
        String responseText = wxCpTpService.post(wxCpTpService.getConfigStorage().getApiUrl(WxCpApiPathConsts.Tp.CONTACT_SEARCH) + "?provider_access_token=" + wxCpTpService.getWxCpProviderToken(), search.toJson(), true);
        return Objects.isNull(responseText)? null: WxCpTpContactSearchResp.fromJson(responseText);
    }
}
