package com.bw.iot.tbc.wechatdemo.provider.config.impl;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxAccessToken;
import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpProviderToken;
import com.bw.iot.tbc.wechatdemo.provider.config.QwAgentConfig;
import com.bw.iot.tbc.wechatdemo.provider.config.WxCpTpConfigStorageService;
import com.bw.iot.tbc.wechatdemo.provider.enums.WxCpErrorMsgEnum;
import com.bw.iot.tbc.wechatdemo.provider.error.WxError;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @ClassName WxCpTpConfigStorageServiceImpl
 * @Description 基于内存的企业微信配置provider，在实际生产中应该将这些配置持久化
 * @Author lengqy
 * @Date 2024年12月07日 21:29
 * @Version 1.0
 */
@Slf4j
@Component
public class WxCpTpConfigStorageServiceImpl implements WxCpTpConfigStorageService, Serializable {
    private static final long serialVersionUID = 7245408482252251011L;
    @Autowired
    private QwAgentConfig qwAgentConfig;

    private volatile String baseApiUrl;
    private volatile String suiteAccessToken;
    private volatile long suiteAccessTokenExpiresTime;
    private volatile String suiteTicket;
    private volatile long suiteTicketExpiresTime;
    private volatile String suiteId;
    private volatile String suiteSecret;
    private volatile String token;
    private volatile String aesKey;
    private volatile String corpId;
    private volatile String corpSecret;
    private volatile String providerSecret;
    private volatile String providerToken;
    private volatile long providerTokenExpiresTime;
    private final Map<String, String> authCorpAccessTokenMap = new HashMap<>();
    private final Map<String, Long> authCorpAccessTokenExpireTimeMap = new HashMap<>();



    // 构造方法，确保在依赖注入后再初始化字段
    @Autowired
    public WxCpTpConfigStorageServiceImpl(QwAgentConfig qwAgentConfig) {
        this.qwAgentConfig = qwAgentConfig;
        this.baseApiUrl = qwAgentConfig.getBaseUrl();
        this.suiteId = qwAgentConfig.getSuiteId();
        this.suiteSecret = qwAgentConfig.getSuiteSecret();
        this.token = qwAgentConfig.getToken();
        this.aesKey = qwAgentConfig.getEncodingAesKey();
        this.corpId = qwAgentConfig.getCorpId();
        this.providerSecret = qwAgentConfig.getProviderSecret();
    }


    @Override
    public void setBaseApiUrl(String baseUrl) {
        this.baseApiUrl = baseUrl;
    }

    @Override
    public String getApiUrl(String path) {
        if (baseApiUrl == null) {
            baseApiUrl = "https://qyapi.weixin.qq.com";
        }
        return baseApiUrl + path;
    }

    @Override
    public String getSuiteAccessToken() {
        return this.suiteAccessToken;
    }

    @Override
    public void setSuiteAccessToken(String suiteAccessToken) {
        this.suiteAccessToken = suiteAccessToken;
    }

    @Override
    public WxAccessToken getSuiteAccessTokenEntity() {
    	WxAccessToken accessToken = new WxAccessToken();
        int expiresIn = Math.toIntExact((this.suiteAccessTokenExpiresTime - System.currentTimeMillis()) / 1000L);
        accessToken.setExpiresIn(expiresIn);
        accessToken.setAccessToken(this.suiteAccessToken);
        return accessToken;
    }

    @Override
    public boolean isSuiteAccessTokenExpired() {
        return System.currentTimeMillis() > this.suiteAccessTokenExpiresTime;
    }

    @Override
    public void expireSuiteAccessToken() {
        this.suiteAccessTokenExpiresTime = 0L;
    }

    @Override
    public void updateSuiteAccessToken(String accessToken, int expiresInSeconds) {
        this.suiteAccessToken = accessToken;
        this.suiteAccessTokenExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("suiteAccessToken", this.suiteAccessToken);
            map.put("suiteAccessToken.suiteAccessTokenExpiresTime", this.suiteAccessTokenExpiresTime);
            updateProp(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateSuiteAccessToken 更新suiteAccessToken失败！", e);
        }
    }

    @Override
    public synchronized void updateSuiteAccessToken(WxAccessToken accessToken) {
        updateSuiteAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    }

    /**
     * Sets suite access token expires time.
     *
     * @param suiteAccessTokenExpiresTime the suite access token expires time
     */
    @Deprecated
    public void setSuiteAccessTokenExpiresTime(long suiteAccessTokenExpiresTime) {
        this.suiteAccessTokenExpiresTime = suiteAccessTokenExpiresTime;
    }

    @Override
    public String getSuiteTicket() {
        try {
            if (this.suiteTicket == null) {
                Properties prop = new Properties();
                String dataPath = System.getProperty("user.dir") + "/data/data.properties";
                File file = new File(dataPath);
                if (file.exists()) {
                    try (FileReader fr = new FileReader(file)) {
                        prop.load(fr);
                        Long expiresIn = Long.parseLong(prop.getProperty("suiteTicket.suiteTicketExpiresTime"));
                        if (expiresIn <= System.currentTimeMillis())
                            throw new WxErrorException(new WxError(40085, WxCpErrorMsgEnum.findMsgByCode(40085)));
                        this.suiteTicket = prop.getProperty("suiteTicket");
                        this.suiteTicketExpiresTime = expiresIn;
                    }
                }else {
                    throw new WxErrorException(new WxError(40085, WxCpErrorMsgEnum.findMsgByCode(40085)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getSuiteTicket 获取suiteTicket失败！", e);
        }
		return this.suiteTicket;
	}

    /**
     * Sets suite ticket.
     *
     * @param suiteTicket the suite ticket
     */
    @Override
    public void setSuiteTicket(String suiteTicket) {
        this.suiteTicketExpiresTime = System.currentTimeMillis() + (28 * 60 * 1000);
        this.suiteTicket = suiteTicket;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("suiteTicket", this.suiteTicket);
            map.put("suiteTicket.suiteTicketExpiresTime", this.suiteTicketExpiresTime);
            updateProp(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("saveSuiteTicket 保存suiteTicket失败！", e);
        }
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

    private Properties loadProp() {
        Properties prop = new Properties();
        String dataPath = System.getProperty("user.dir") + "/data/data.properties";
        File file = new File(dataPath);
        if (file.exists()) {
            try (FileReader fr = new FileReader(file)) {
                prop.load(fr);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("loadProp 加载配置文件失败！", e);
            }
        }
        return prop;
    }

    @Override
    public boolean isSuiteTicketExpired() {
        return this.suiteTicket == null ||
                System.currentTimeMillis() > this.suiteTicketExpiresTime;
    }

    @Override
    public void expireSuiteTicket() {
    	this.suiteTicketExpiresTime = 0L;
    }

    @Override
    public synchronized void updateSuiteTicket(String suiteTicket, int expiresInSeconds) {
        this.suiteTicket = suiteTicket;
        // 预留200秒的时间
        this.suiteTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("suiteTicket", this.suiteTicket);
            map.put("suiteTicket.suiteTicketExpiresTime", this.suiteTicketExpiresTime);
            updateProp(map);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("saveSuiteTicket 更新suiteTicket失败！", e);
        }
    }

    /**
     * Gets suite ticket expires time.
     *
     * @return the suite ticket expires time
     */
    @Deprecated
    public long getSuiteTicketExpiresTime() {
        return this.suiteTicketExpiresTime;
    }

    /**
     * Sets suite ticket expires time.
     *
     * @param suiteTicketExpiresTime the suite ticket expires time
     */
    @Deprecated
    public void setSuiteTicketExpiresTime(long suiteTicketExpiresTime) {
        this.suiteTicketExpiresTime = suiteTicketExpiresTime;
    }

    @Override
    public String getSuiteId() {
		return this.suiteId;
	}

    /**
     * Sets suite id.
     *
     * @param corpId the corp id
     */
    @Deprecated
    public void setSuiteId(String corpId) {
        this.suiteId = corpId;
    }

    @Override
    public String getSuiteSecret() {
		return this.suiteSecret;
	}

    /**
     * Sets suite secret.
     *
     * @param corpSecret the corp secret
     */
    @Deprecated
    public void setSuiteSecret(String corpSecret) {
        this.suiteSecret = corpSecret;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getPermanentCode() {
        try {
            Properties prop = new Properties();
            String dataPath = System.getProperty("user.dir") + "/data/data.properties";
            File file = new File(dataPath);
            if (file.exists()) {
                try (FileReader fr = new FileReader(file)) {
                    prop.load(fr);
                    if (Objects.isNull(prop.getProperty("permanentCode"))) {
                        throw new WxErrorException(new WxError(41025, WxCpErrorMsgEnum.findMsgByCode(41025)));
                    }
                    return prop.getProperty("permanentCode");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    @Deprecated
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getAesKey() {
        return this.aesKey;
    }

    /**
     * Sets aes key.
     *
     * @param aesKey the aes key
     */
    @Deprecated
    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }


    @Override
    public String getCorpId() {
        return this.corpId;
    }

    /**
     * Sets corp id.
     *
     * @param corpId the corp id
     */
    @Deprecated
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Override
    public String getCorpSecret() {
        return this.corpSecret;
    }

    /**
     * Sets corp secret.
     *
     * @param corpSecret the corp secret
     */
    @Deprecated
    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    @Deprecated
    public void setProviderSecret(String providerSecret) {
        this.providerSecret = providerSecret;
    }

    @Override
    public String getProviderSecret() {
        return this.providerSecret;
    }

    @Override
    public String getAccessToken(String authCorpId) {
        return this.authCorpAccessTokenMap.get(authCorpId);
    }

    @Override
    public WxAccessToken getAccessTokenEntity(String authCorpId) {
        String accessToken = this.authCorpAccessTokenMap.getOrDefault(authCorpId, StringUtils.EMPTY);
        Long expire = authCorpAccessTokenExpireTimeMap.getOrDefault(authCorpId, 0L);
        WxAccessToken accessTokenEntity = new WxAccessToken();
        accessTokenEntity.setAccessToken(accessToken);
        accessTokenEntity.setExpiresIn((int) ((expire - System.currentTimeMillis()) / 1000 + 200));
        return accessTokenEntity;
    }

    @Override
    public boolean isAccessTokenExpired(String authCorpId) {
        // 不存在或者过期
        return authCorpAccessTokenExpireTimeMap.get(authCorpId) == null
        || System.currentTimeMillis() > authCorpAccessTokenExpireTimeMap.get(authCorpId);
    }

    @Override
    public void expireAccessToken(String authCorpId) {
        authCorpAccessTokenMap.remove(authCorpId);
        authCorpAccessTokenExpireTimeMap.remove(authCorpId);
    }

    @Override
    public void updateAccessToken(String authCorpId, String accessToken, int expiresInSeconds) {
        authCorpAccessTokenMap.put(authCorpId, accessToken);
        // 预留200秒的时间
        authCorpAccessTokenExpireTimeMap.put(authCorpId, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L);
    }

    @Override
    public boolean isProviderTokenExpired() {
        Properties prop = loadProp();
        this.providerTokenExpiresTime = Long.parseLong(prop.getProperty("providerToken.providerTokenExpiresTime"));
        return System.currentTimeMillis() > this.providerTokenExpiresTime;
    }

    @Override
    public void updateProviderToken(String providerToken, int expiresInSeconds) {
        this.providerToken = providerToken;
        this.providerTokenExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
        try {
            Map<String, Object> map = new HashMap<>(2);
            map.put("providerToken", this.providerToken);
            map.put("providerToken.providerTokenExpiresTime", this.providerTokenExpiresTime);
            updateProp(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("updateProviderToken error", e);
        }
    }

    @Override
    public String getProviderToken() {
        Properties prop = loadProp();
        this.providerToken = prop.getProperty("providerToken");
        return this.providerToken;
    }

    @Override
    public WxCpProviderToken getProviderTokenEntity() {
    	WxCpProviderToken providerTokenEntity = new WxCpProviderToken();
        Properties prop = loadProp();
    	providerTokenEntity.setProviderAccessToken(prop.getProperty("providerToken"));
    	this.providerTokenExpiresTime = Long.parseLong(prop.getProperty("providerToken.providerTokenExpiresTime"));
        return providerTokenEntity;
    }

    @Override
    public void expireProviderToken() {
        this.providerTokenExpiresTime = 0L;
        try {
            Map<String, Object> map = new HashMap<>(1);
            map.put("providerToken.providerTokenExpiresTime", this.providerTokenExpiresTime);
            updateProp(map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("expireProviderToken error", e);
        }
    }

}
