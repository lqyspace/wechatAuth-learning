package com.bw.iot.tbc.wechatdemo.provider.config.impl;

import com.bw.iot.tbc.wechatdemo.provider.config.WxCpConfigStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName WxCpConfigStorageImpl
 * @Description
 * @Author lengqy
 * @Date 2024年12月31日 10:01
 * @Version 1.0
 */
@Slf4j
@Service
public class WxCpConfigStorageImpl implements WxCpConfigStorage, Serializable {
    private static final long serialVersionUID = 169958711264036771L;
    private volatile String agentJsapiTicket;
    private volatile String corpId;
    /**
     * The Agent id.
     */
    protected volatile Integer agentId;
    private volatile long agentJsapiTicketExpiresTime;
    /**
     * The Agent jsapi ticket lock.
     */
    protected transient Lock agentJsapiTicketLock = new ReentrantLock();
    @Override
    public void expireAgentJsapiTicket() {
        this.agentJsapiTicketExpiresTime = 0;
    }

    @Override
    public boolean isAgentJsapiTicketExpired() {
        if (this.agentJsapiTicketExpiresTime == 0) {
            Properties pro = loadProp();
            if (pro.contains("agentJsapiTicket.agentJsapiTicketExpiresTime"))
                this.agentJsapiTicketExpiresTime = Long.parseLong(pro.getProperty("agentJsapiTicket.agentJsapiTicketExpiresTime"));
        }
        return System.currentTimeMillis() > this.agentJsapiTicketExpiresTime;
    }

    @Override
    public void updateAgentJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        this.agentJsapiTicket = jsapiTicket;
        // 预留200秒的时间
        this.agentJsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
        Map<String, Object> map = new HashMap<>(2);
        map.put("agentJsapiTicket", this.agentJsapiTicket);
        map.put("agentJsapiTicket.agentJsapiTicketExpiresTime", this.agentJsapiTicketExpiresTime);
        saveProp(map);
    }

    @Override
    public String getAgentJsapiTicket() {
        if (this.agentJsapiTicket == null) {
            Properties pro = loadProp();
            if (pro.contains("agentJsapiTicket"))
                this.agentJsapiTicket = pro.getProperty("agentJsapiTicket");
        }
        return this.agentJsapiTicket;
    }

    @Override
    public String getCorpId() {
        if (this.corpId == null) {
            Properties properties = loadProp();
            if (properties.contains("corpid"))
                this.corpId = properties.getProperty("corpid");
        }
        return this.corpId;
    }

    @Override
    public Integer getAgentId() {
        if (this.agentId == null) {
            Properties properties = loadProp();
            if (properties.contains("agentid"))
                this.agentId = Integer.parseInt(properties.getProperty("agentid"));
        }
        return this.agentId;
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

    private void saveProp(Map<String, Object> map) {
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
}
