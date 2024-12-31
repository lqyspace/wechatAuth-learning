package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName WxCpProviderToken
 * @Description
 * @Author lengqy
 * @Date 2024年12月29日 20:00
 * @Version 1.0
 */
@Data
public class WxCpProviderToken implements Serializable {
    private static final long serialVersionUID = 8742666348069518862L;

    /**
     * 服务商的access_token，最长为512字节
     */
    @SerializedName("provider_access_token")
    private String providerAccessToken;

    /**
     * provider_access_token的有效期，单位为秒
     */
    @SerializedName("expires_in")
    private Integer expiresIn;

    public static WxCpProviderToken fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpProviderToken.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }

}
