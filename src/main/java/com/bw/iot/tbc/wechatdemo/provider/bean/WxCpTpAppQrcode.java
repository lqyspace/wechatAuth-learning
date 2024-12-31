package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName WxCpTpAppQrcode
 * @Description
 * @Author lengqy
 * @Date 2024年12月28日 20:08
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxCpTpAppQrcode extends WxCpBaseResp{
    private static final long serialVersionUID = -108244782065704345L;

    @SerializedName("qrcode")
    private String qrcode;

    public static WxCpTpAppQrcode fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpTpAppQrcode.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }
}
