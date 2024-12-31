package com.bw.iot.tbc.wechatdemo.provider.bean.kf;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpBaseResp;
import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName WxCpKfAccountListResp
 * @Description
 * @Author lengqy
 * @Date 2024年12月30日 16:58
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class WxCpKfAccountListResp extends WxCpBaseResp {
    private static final long serialVersionUID = -9074981883586575238L;

    /**
     * 帐号信息列表
     */
    @SerializedName("account_list")
    private List<AccountListDTO> accountList;

    /**
     * The type Account list dto.
     */
    @NoArgsConstructor
    @Data
    public static class AccountListDTO {
        /**
         * 客服帐号ID
         */
        @SerializedName("open_kfid")
        private String openKfid;

        /**
         * 客服名称
         */
        @SerializedName("name")
        private String name;

        /**
         * 客服头像URL
         */
        @SerializedName("avatar")
        private String avatar;

        /**
         * 当前调用接口的应用身份，是否有该客服账号的管理权限（编辑客服账号信息、分配会话和收发消息）。组件应用不返回此字段
         */
        @SerializedName("manage_privilege")
        private Boolean hasManagePrivilege;
    }

    /**
     * From json wx cp kf account list resp.
     *
     * @param json the json
     * @return the wx cp kf account list resp
     */
    public static WxCpKfAccountListResp fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpKfAccountListResp.class);
    }
}
