package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName WxCpTpAppPermission
 * @Description
 * @Author lengqy
 * @Date 2024年12月29日 20:29
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxCpTpAppPermission extends WxCpBaseResp{
    private static final long serialVersionUID = 2330896146207610171L;

    @SerializedName("app_permissions")
    private List<String> appPermissions;

    public static WxCpTpAppPermission fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpTpAppPermission.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }
}
