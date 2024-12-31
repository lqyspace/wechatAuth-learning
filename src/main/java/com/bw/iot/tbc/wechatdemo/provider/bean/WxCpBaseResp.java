package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName WxCpBaseResp
 * @Description
 * @Author lengqy
 * @Date 2024年12月22日 23:30
 * @Version 1.0
 */
@Getter
@Setter
public class WxCpBaseResp implements Serializable {
    private static final long serialVersionUID = 5491968010459128085L;

    @SerializedName("errcode")
    protected Long errCode;

    @SerializedName("errmsg")
    protected String errMsg;

    public boolean success() {
        return getErrCode() == 0;
    }

    public static WxCpBaseResp fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpBaseResp.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }
}
