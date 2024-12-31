package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName WxCpTpCorpId2OpenCorpId
 * @Description
 * @Author lengqy
 * @Date 2024年12月29日 18:42
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxCpTpCorpId2OpenCorpId extends WxCpBaseResp{
    private static final long serialVersionUID = -2318387658708107805L;

    @SerializedName("open_corpid")
    private String openCorpId;

    public static WxCpTpCorpId2OpenCorpId fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpTpCorpId2OpenCorpId.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }
}
