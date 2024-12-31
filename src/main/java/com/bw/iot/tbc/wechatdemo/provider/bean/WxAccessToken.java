package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName WxAccessToken
 * @Description access token
 * @Author lengqy
 * @Date 2024年12月07日 22:25
 * @Version 1.0
 */
@Data
@Slf4j
public class WxAccessToken implements Serializable {
    private static final long serialVersionUID = -7605950324451993407L;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private Integer expiresIn = -1;

    public static WxAccessToken fromJson(String json) {
        return WxGsonBuilder.create().fromJson(json, WxAccessToken.class);
    }
}
