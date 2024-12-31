package com.bw.iot.tbc.wechatdemo.provider.service.impl;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxAccessToken;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountLink;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountLinkResp;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountListResp;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfCustomerBatchGetResp;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpKfService;
import com.bw.iot.tbc.wechatdemo.provider.service.WxCpTpService;
import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static com.bw.iot.tbc.wechatdemo.provider.constant.WxCpApiPathConsts.GET_TOKEN;
import static com.bw.iot.tbc.wechatdemo.provider.constant.WxCpApiPathConsts.Kf.*;

/**
 * @ClassName WxCpKfServiceImpl
 * @Description  微信客服接口 —— 服务实现
 * @Author lengqy
 * @Date 2024年12月30日 17:07
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxCpKfServiceImpl implements WxCpKfService {

    private final WxCpTpService wxCpTpService;
    @Override
    public WxCpKfAccountListResp listAccount(Integer offset, Integer limit) throws WxErrorException {
        String url = wxCpTpService.getConfigStorage().getApiUrl(ACCOUNT_LIST) + "?access_token=" + wxCpTpService.getAccessToken(wxCpTpService.getConfigStorage().getCorpId()).getAccessToken();
        JsonObject jsonObject = new JsonObject();
        if (Objects.nonNull(offset)) {
            jsonObject.addProperty("offset", offset);
        }
        if (Objects.nonNull(limit)) {
            jsonObject.addProperty("limit", limit);
        }
        String resp = wxCpTpService.post(url, jsonObject.toString(), true);
        return Objects.isNull(resp)? null: WxCpKfAccountListResp.fromJson(resp);
    }

    @Override
    public WxCpKfAccountLinkResp getAccountLink(WxCpKfAccountLink link) throws WxErrorException {
        String url = wxCpTpService.getConfigStorage().getApiUrl(ADD_CONTACT_WAY) + "?access_token=" + wxCpTpService.getAccessToken(wxCpTpService.getConfigStorage().getCorpId()).getAccessToken();
        String resp = wxCpTpService.post(url, WxCpGsonBuilder.create().toJson(link), true);
        return Objects.isNull(resp)? null: WxCpKfAccountLinkResp.fromJson(resp);
    }

    @Override
    public WxCpKfCustomerBatchGetResp customerBatchGet(List<String> externalUserIdList, Integer needEnterSessionContext)
            throws WxErrorException{
        String url = wxCpTpService.getConfigStorage().getApiUrl(CUSTOMER_BATCH_GET) + "?access_token=" + wxCpTpService.getAccessToken(wxCpTpService.getConfigStorage().getCorpId()).getAccessToken();
        JsonArray jsonArray = new JsonArray();
        externalUserIdList.forEach(jsonArray::add);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("external_userid_list", jsonArray);
        jsonObject.addProperty("need_enter_session_context", needEnterSessionContext);
        String resp = wxCpTpService.post(url, jsonObject.toString(), true);
        return Objects.isNull(resp)? null: WxCpKfCustomerBatchGetResp.fromJson(resp);
    }

    private Properties getProp() {
        Properties prop = new Properties();
        String dataPath = System.getProperty("user.dir") + "/data/data.properties";
        File file = new File(dataPath);
        if (file.exists()) {
            try (FileReader fr = new FileReader(file)) {
                prop.load(fr);
                return prop;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("loadProp 加载配置文件失败！", e);
            }
        }
        return null;
    }
}
