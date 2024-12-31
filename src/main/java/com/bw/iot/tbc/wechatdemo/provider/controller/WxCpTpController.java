package com.bw.iot.tbc.wechatdemo.provider.controller;

import com.alibaba.fastjson.JSON;
import com.bw.iot.tbc.wechatdemo.provider.aes.WXBizMsgCrypt;
import com.bw.iot.tbc.wechatdemo.provider.bean.*;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountLink;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountLinkResp;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountListResp;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfCustomerBatchGetResp;
import com.bw.iot.tbc.wechatdemo.provider.bean.req.GetAppQrcodeReq;
import com.bw.iot.tbc.wechatdemo.provider.config.QwAgentConfig;
import com.bw.iot.tbc.wechatdemo.provider.config.WxCpTpConfigStorageService;
import com.bw.iot.tbc.wechatdemo.provider.constant.WxCpApiPathConsts;
import com.bw.iot.tbc.wechatdemo.provider.enums.WxCpErrorMsgEnum;
import com.bw.iot.tbc.wechatdemo.provider.error.WxError;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import com.bw.iot.tbc.wechatdemo.provider.service.*;
import com.bw.iot.tbc.wechatdemo.provider.service.impl.WxCpTpServiceApacheHttpClientImpl;
import com.bw.iot.tbc.wechatdemo.provider.service.impl.WxCpTpServiceImpl;
import com.bw.iot.tbc.wechatdemo.provider.utils.ehcache.EhcacheUtil;
import com.bw.iot.tbc.wechatdemo.provider.utils.xml.XmlToMapUtil;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @ClassName WxCpTpController
 * @Description
 * @Author lengqy
 * @Date 2024年12月23日 11:19
 * @Version 1.0
 */
@RestController
@RequestMapping("qw/h5/qwagent")
@Slf4j
public class WxCpTpController {
    @Autowired
    private QwAgentConfig qwAgentConfig;

    @Autowired
    @Qualifier("wxCpTpServiceApacheHttpClientImpl")
    private WxCpTpService wxCpTpService;

    @Autowired
    private WxCpTpContactService wxCpTpContactService;

    @Autowired
    private WxCpTpConfigStorageService configStorageService;

    @Autowired
    private WxCpKfService wxCpKfService;

    @Autowired
    private WxCpService wxCpService;

    @Autowired
    private WxCpTpOAuth2Service wxCpTpOAuth2Service;

    private static volatile Properties prop = new Properties();
    @GetMapping("/test")
    public String test(){
        return "测试";
    }

    @PostMapping("/test2")
    public String test2(@RequestBody String data){
        return String.format("这是一个: %s", data);
    }

    @GetMapping({"/dataCallback", "instructionCallback"})
    @ResponseBody
    public String callbackUrl(HttpServletRequest request) {
        try {
            String mas_signature = request.getParameter("msg_signature");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            String timestamp = request.getParameter("timestamp");
            log.info("服务商模式 get msg_signature: {}, nonce: {}, timestamp: {}, echostr: {}", mas_signature, nonce, timestamp, echostr);
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(qwAgentConfig.getToken(), qwAgentConfig.getEncodingAesKey(), qwAgentConfig.getCorpId());
            // 验证url
            String str = wxBizMsgCrypt.VerifyURL(mas_signature, timestamp, nonce, echostr);
            log.info("验证str：{}", str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("dataCallback、instructionCallback 回调失败！");
        }
        return null;
    }

    @PostMapping("/dataCallback")
    @ResponseBody
    public String dataCallback(HttpServletRequest request, @RequestBody String dataBody) {
        String msg_signature = request.getParameter("msg_signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        log.info("服务商模式 数据推送 post msg_signature:" + msg_signature + " nonce:" + nonce +" timestamp:" + timestamp + " dataBody:" + dataBody);
        String suiteId = null;
        try {
            Map<String, Object> requestMap = XmlToMapUtil.parseXmlToMap(dataBody);
            suiteId = (String) requestMap.get("ToUserName");
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(qwAgentConfig.getToken(), qwAgentConfig.getEncodingAesKey(), suiteId);
            String sMsg = wxBizMsgCrypt.DecryptMsg(msg_signature, timestamp, nonce, dataBody);
            log.info("服务商模式 数据推送 解密后 sMsg：{}, resultMap: {}", sMsg, requestMap);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            log.info("dataCallback 回调失败！");
        }
        return null;
    }

    @PostMapping("/instructionCallback")
    @ResponseBody
    public String instructionCallback(HttpServletRequest request, @RequestBody String dataBody) {
        String msg_signature = request.getParameter("msg_signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        log.info("服务商模式 指令推送 post: \nmsg_signature:" + msg_signature + " nonce:" + nonce +" timestamp:" + timestamp + "\ndataBody:" + dataBody);

        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(qwAgentConfig.getToken(), qwAgentConfig.getEncodingAesKey(), qwAgentConfig.getSuiteId());
            String sMsg = wxBizMsgCrypt.DecryptMsg(msg_signature, timestamp, nonce, dataBody);
            Map<String, Object> requestMap = XmlToMapUtil.parseXmlToMap(sMsg);
            // 保存suite_ticket
            if (Objects.nonNull(requestMap.get("SuiteTicket"))) {
                wxCpTpService.setSuiteTicket(String.valueOf(requestMap.get("SuiteTicket")));
            }
            if (Objects.isNull(wxCpTpService.getSuiteTicket()))
                throw new WxErrorException(new WxError(40085, WxCpErrorMsgEnum.findMsgByCode(40085)));
            // 保存suite_access_token
            String suiteAccessToken = wxCpTpService.getSuiteAccessToken();
            configStorageService.setSuiteAccessToken(suiteAccessToken);
            EhcacheUtil.put("suite_access_token", suiteAccessToken);

            log.info("服务商模式 指令推送 解密后\n sMsg：{}\n requestMap: {}\n suiteAccessToken: {}", sMsg, requestMap, suiteAccessToken);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            log.info("instructionCallback 回调失败！");
        }
        return null;
    }

    @GetMapping("/listAccount")
    @SneakyThrows
    public WxCpKfAccountListResp listAccount() {
        return wxCpKfService.listAccount(0, 100);
    }

    @GetMapping("/getAccountLink")
    @SneakyThrows
    public WxCpKfAccountLinkResp getAccountLink() {
        WxCpKfAccountLink link = new WxCpKfAccountLink();
        link.setOpenKfid("wk-UPpQAAAndLXokWXRCCF__sknbQSOA");
        link.setScene("customerId"); // 不能多于32字节
        return wxCpKfService.getAccountLink(link);
    }

    /**
     * 获取客户基础信息
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/customerBatchGet")
    public WxCpKfCustomerBatchGetResp customerBatchGet() throws WxErrorException {
        List<String> externalUserIdList = new ArrayList<>();
        externalUserIdList.add("wx423444414");
        return wxCpKfService.customerBatchGet(externalUserIdList, 1);
    }

    @GetMapping("/getAuthInfo")
    public WxCpTpPermanentCodeInfo getAuthInfo(){
        WxCpTpPermanentCodeInfo wxCpTpPermanentCodeInfo = null;
        try {
            wxCpTpPermanentCodeInfo = wxCpTpService.getAuthInfo(this.configStorageService.getCorpId(), this.configStorageService.getPermanentCode());
            Map<String, Object> map = new HashMap<>(2);
            if (Objects.nonNull(wxCpTpPermanentCodeInfo) && wxCpTpPermanentCodeInfo.getAuthCorpInfo()!=null && wxCpTpPermanentCodeInfo.getAuthCorpInfo().getCorpId()!=null) {
                map.put("corpid", wxCpTpPermanentCodeInfo.getAuthCorpInfo().getCorpId());
            }
            if (Objects.nonNull(wxCpTpPermanentCodeInfo) && wxCpTpPermanentCodeInfo.getAuthInfo()!=null && wxCpTpPermanentCodeInfo.getAuthInfo().getAgents()!=null){
                map.put("agentid", wxCpTpPermanentCodeInfo.getAuthInfo().getAgents().get(0).getAgentId());
            }
            updateProp(map);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.info("getAuthInfo 回调失败！");
        }
        return wxCpTpPermanentCodeInfo;
    }

    @GetMapping("/getCorpToken")
    public WxAccessToken getAccessToken() {
        WxAccessToken accessToken = null;
        try {
            accessToken = wxCpTpService.getCorpToken(this.configStorageService.getCorpId(), this.configStorageService.getPermanentCode());
            if (Objects.nonNull(accessToken)) {
                wxCpTpService.setAccessToken(this.configStorageService.getCorpId(), accessToken.getAccessToken(), accessToken.getExpiresIn());
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.info("getAccessToken 回调失败！");
        }
        return accessToken;
    }

    @PostMapping("/getAppQrCode")
    public WxCpTpAppQrcode getAppQrCode(@RequestBody GetAppQrcodeReq req) throws WxErrorException {
        if (Objects.nonNull(req)) {
            WxCpTpAppQrcode result = wxCpTpService.getAppQrCode(req.getSuiteId(), req.getAppId(), req.getState(), req.getStyle(), req.getResultType());
            log.info("服务商模式 getAppQrCode result: {}", result);
            return result;
        }
        return null;
    }

    @GetMapping("/getAdminList")
    public WxCpTpAdmin getAdminList() throws WxErrorException {
        WxCpTpAdmin wxCpTpAdmin =
                wxCpTpService.getAdminList(wxCpTpService.getAccessToken(this.configStorageService.getCorpId()).getAccessToken(),
                        this.configStorageService.getCorpId());
        log.info("服务商模式 getAdminList wxCpTpAdmin: {}", wxCpTpAdmin);
        return wxCpTpAdmin;
    }

    @PostMapping("/contactSearch")
    public WxCpTpContactSearchResp contactSearch(@RequestBody WxCpTpContactSearch search) throws WxErrorException {
        log.info("服务商模式 contactSearch req: {}", JSON.toJSONString(search));
        WxCpTpContactSearchResp wxCpTpContactSearchResp = wxCpTpContactService.contactSearch(search);
        log.info("服务商模式 contactSearch wxCpTpContactSearchResp: {}", wxCpTpContactSearchResp);
        return wxCpTpContactSearchResp;
    }

    @SneakyThrows
    @GetMapping("/getPreAuthUrl")
    public void getPreAuthUrl(HttpServletRequest request, HttpServletResponse response) {
        String preAuthUrl = wxCpTpService.getPreAuthUrl("http://47.96.42.105:8081/qw/h5/qwagent/urlRedirect", null, 1);
        log.info("服务商模式 getPreAuthUrl preAuthUrl: {}", preAuthUrl);
        response.sendRedirect(preAuthUrl);
    }

    @SneakyThrows
    @GetMapping("/getAppPermission")
    public WxCpTpAppPermission getAppPermission() {
        return wxCpTpService.getAppPermission(wxCpTpService.getAccessToken(this.configStorageService.getCorpId()).getAccessToken());
    }

    @GetMapping("/getProviderToken")
    public WxCpProviderToken getProviderToken() throws WxErrorException {
        WxCpProviderToken providerToken = wxCpTpService.getWxCpProviderTokenEntity(true);
        log.info("服务商模式 getProviderToken providerToken: {}", providerToken);
        return providerToken;
    }

    @GetMapping("/urlRedirect")
    public String urlRedirect(HttpServletRequest request) {
        try {
            String authCode = request.getParameter("auth_code");
            int expiresIn = Integer.parseInt(request.getParameter("expires_in"));
            String state = request.getParameter("state");
            log.info("服务商模式 urlRedirect authCode: {}, expiresIn: {}, state: {}", authCode, expiresIn, state);
            // 获取永久授权码
            WxCpTpPermanentCodeInfo wxCpTpPermanentCodeInfo = wxCpTpService.getPermanentCodeInfo(authCode);
            log.info("服务商模式 urlRedirect 获取永久授权码 wxCpTpPermanentCodeInfo: {}", JSON.toJSONString(wxCpTpPermanentCodeInfo));
            // todo 保存
            EhcacheUtil.put("wxCpTpPermanentCodeInfo", JSON.toJSONString(wxCpTpPermanentCodeInfo));
            // 保存永久授权码
            savePermanentCodeInfo(wxCpTpPermanentCodeInfo);
        } catch (Exception e){
            log.error("服务商模式 urlRedirect 回调失败！");
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * @api {GET} /qw/h5/qwagent/createAgentJsapiSignature createAgentJsapiSignature()
     * @apiVersion 1.0.0
     * @apiGroup WxCpTpController
     * @apiName createAgentJsapiSignature()
     * @apiParam (请求参数) {String} url
     * @apiParam (请求参数) {Number} agentId
     * @apiParamExample 请求参数示例
     * agentId=391&url=WSEKZ5QkzN
     * @apiSuccess (响应结果) {String} url
     * @apiSuccess (响应结果) {String} corpid
     * @apiSuccess (响应结果) {Number} agentid
     * @apiSuccess (响应结果) {Number} timestamp
     * @apiSuccess (响应结果) {String} nonceStr
     * @apiSuccess (响应结果) {String} signature
     * @apiSuccessExample 响应结果示例
     * {"agentid":9589,"corpid":"9","signature":"xaPOfRR","url":"bL8","nonceStr":"fy3nYdKEr0","timestamp":511}
     */
    @GetMapping("/createAgentJsapiSignature")
    public WxCpAgentJsapiSignature createAgentJsapiSignature(@RequestParam("url") String url, @RequestParam("agentId") Integer agentId) throws WxErrorException {
        WxCpAgentJsapiSignature wxCpAgentJsapiSignature = wxCpService.createAgentJsapiSignature(url, agentId);
        log.info("服务商模式 createAgentJsapiSignature wxCpAgentJsapiSignature: {}", wxCpAgentJsapiSignature);
        return wxCpAgentJsapiSignature;
    }

    private void savePermanentCodeInfo(WxCpTpPermanentCodeInfo wxCpTpPermanentCodeInfo) throws IOException {
        try {
            String dataPath = System.getProperty("user.dir") + "/data/data.properties";
            File file = new File(dataPath);
            // 确保父目录一定存在
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    log.error("无法创建父目录: {}", parentFile.getAbsolutePath());
                }
            }
            // 创建文件
            if (!file.exists() && !file.createNewFile()) {
                log.error("无法创建文件: {}", dataPath);
            }

            try(FileReader fr = new FileReader(file)) {
                prop.load(fr);
            }
            try (FileWriter fw = new FileWriter(file)) {
                prop.setProperty("accessToken", wxCpTpPermanentCodeInfo.getAccessToken());
                prop.setProperty("permanentCode", wxCpTpPermanentCodeInfo.getPermanentCode());
                prop.setProperty("accessToken.expiresIn", String.valueOf(wxCpTpPermanentCodeInfo.getExpiresIn()));
                prop.store(fw, "savePermanentCodeInfo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("savePermanentCodeInfo 保存永久授权码失败！", e);
        }
    }

    private Properties loadProp() {
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
        return prop;
    }
    private void updateProp(Map<String, Object> map) {
        Properties prop = new Properties();
        String dataPath = System.getProperty("user.dir") + "/data/data.properties";
        File file = new File(dataPath);
        try {
            // 确保父目录存在
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                if (!parentFile.mkdirs()){
                    log.error("无法创建父目录: {}", parentFile.getAbsolutePath());
                }
            }
            // 创建文件
            if (!file.exists() && !file.createNewFile()) {
                log.error("无法创建文件: {}", dataPath);
            }
            log.info("操作配置文件 {} 成功！", dataPath);

            try(FileReader fr = new FileReader(file)) {
                prop.load(fr);
            }
            try(FileWriter fw = new FileWriter(file)) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    prop.setProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
                prop.store(fw, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("操作配置文件失败！", e);
        }
    }

    @GetMapping("/loginAuth")
    @SneakyThrows
    public void loginAuth(HttpServletResponse response) {
        String url = wxCpTpOAuth2Service.buildAuthorizeUrl("http://47.96.42.105:8081/qw/h5/qwagent/urlRedirectLoginAuth", "state");
        log.info("服务商模式 loginAuth redirect url: {}", url);
        response.sendRedirect(url);
    }

    @GetMapping("/urlRedirectLoginAuth")
    public String urlRedirectLoginAuth(HttpServletRequest request){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        log.info("服务商模式 urlRedirectLoginAuth code: {}, state: {}", code, state);
        try {
            WxCpTpUserInfo userInfo = wxCpTpService.getUserInfo3rd(code);
            log.info("服务商模式 urlRedirectLoginAuth userInfo: {}", JSON.toJSONString(userInfo));
            if (userInfo != null) {
                WxCpTpUserDetail userDetail = wxCpTpService.getUserDetail3rd(userInfo.getUserTicket());
                log.info("服务商模式 urlRedirectLoginAuth userDetail: {}", JSON.toJSONString(userDetail));
            }
            WxTpLoginInfo loginInfo = wxCpTpService.getLoginInfo(code);
            log.info("服务商模式 urlRedirectLoginAuth loginInfo: {}", JSON.toJSONString(loginInfo));
        } catch (WxErrorException e) {
            log.error("服务商模式 urlRedirectLoginAuth 获取用户信息失败！", e);
        }
        return "ok";
    }

}
