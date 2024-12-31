package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WxTpLoginInfo
 * @Description
 * @Author lengqy
 * @Date 2024年12月31日 00:54
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxTpLoginInfo extends WxCpBaseResp{
    private static final long serialVersionUID = -8525308252087025667L;

    @SerializedName("usertype")
    private Integer userType;
    @SerializedName("user_info")
    private UserInfo userInfo;
    @SerializedName("corp_info")
    private CorpInfoBean corpInfo;
    @SerializedName("auth_info")
    private AuthInfo authInfo;
    private List<Agent> agent;

    /**
     * From json wx tp login info.
     *
     * @param json the json
     * @return the wx tp login info
     */
    public static WxTpLoginInfo fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxTpLoginInfo.class);
    }

    /**
     * The type User info.
     */
    @Data
    public static class UserInfo implements Serializable {
        private static final long serialVersionUID = -4558358748587735192L;

        @SerializedName("userid")
        private String userId;
        @SerializedName("open_userid")
        private String openUserId;
        private String name;
        private String avatar;
    }

    /**
     * The type Corp info bean.
     */
    @Data
    public static class CorpInfoBean implements Serializable {
        private static final long serialVersionUID = -3160146744148144984L;

        @SerializedName("corpid")
        private String corpId;
    }

    /**
     * The type Auth info.
     */
    @Data
    public static class AuthInfo implements Serializable {
        private static final long serialVersionUID = -8697184659526210472L;

        private List<Department> department;

        /**
         * The type Department.
         */
        @Data
        public static class Department implements Serializable {
            private static final long serialVersionUID = -4389328276936557541L;

            private int id;
            private boolean writable;
        }
    }

    /**
     * The type Agent.
     */
    @Data
    public static class Agent implements Serializable {
        private static final long serialVersionUID = 1461544500964159037L;
        @SerializedName("agentid")
        private int agentId;
        @SerializedName("auth_type")
        private int authType;
    }
}
