package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName WxCpTpAdmin
 * @Description
 * @Author lengqy
 * @Date 2024年12月29日 00:26
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxCpTpAdmin extends WxCpBaseResp{
    private static final long serialVersionUID = -4026690017948188709L;

    @SerializedName("admin")
    private List<Admin> admin;
    @Getter
    @Setter
    public static class Admin extends WxCpBaseResp {
        private static final long serialVersionUID = 641655689280903986L;

        @SerializedName("userid")
        private String userId;

        @SerializedName("auth_type")
        private Integer authType;

        public String toJson() {
            return WxCpGsonBuilder.create().toJson(this);
        }
    }

    public static WxCpTpAdmin fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpTpAdmin.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }
}
