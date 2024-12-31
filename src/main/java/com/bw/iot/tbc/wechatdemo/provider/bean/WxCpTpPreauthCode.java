package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName WxCpTpPreauthCode
 * @Description
 * @Author lengqy
 * @Date 2024年12月23日 14:39
 * @Version 1.0
 */
@Getter
@Setter
public class WxCpTpPreauthCode extends WxCpBaseResp{
    private static final long serialVersionUID = -1543104173371159223L;

    @SerializedName("pre_auth_code")
    protected String preAuthCode;

    @SerializedName("expires_in")
    protected Long expiresIn;

    public static WxCpTpPreauthCode fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpTpPreauthCode.class);
    }
}
