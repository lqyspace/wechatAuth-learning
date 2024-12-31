package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WxCpTpPermanentCodeInfo
 * @Description
 * @Author lengqy
 * @Date 2024年12月23日 00:07
 * @Version 1.0
 */
@Getter
@Setter
public class WxCpTpPermanentCodeInfo extends WxCpBaseResp{
    private static final long serialVersionUID = -2938695443990840150L;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private Long expiresIn;

    @SerializedName("permanent_code")
    private String permanentCode;
    /**
     * 授权企业信息
     */
    @SerializedName("auth_corp_info")
    private AuthCorpInfo authCorpInfo;

    /**
     * 代理服务商信息
     */
    @SerializedName("dealer_corp_info")
    private DealerCorpInfo dealerCorpInfo;

    /**
     * 授权信息。如果是通讯录应用，且没开启实体应用，是没有该项的。通讯录应用拥有企业通讯录的全部信息读写权限
     */
    @SerializedName("auth_info")
    private AuthInfo authInfo;

    /**
     * 授权用户信息
     */
    @SerializedName("auth_user_info")
    private AuthUserInfo authUserInfo;

    /**
     * 推广二维码安装相关信息
     */
    @SerializedName("register_code_info")
    private RegisterCodeInfo registerCodeInfo;

    /**
     * 企业当前生效的版本信息
     */
    @SerializedName("edition_info")
    private EditionInfo editionInfo;

    /**
     * 安装应用时，扫码或者授权链接中带的state值。详见state说明
     * state说明：
     * 目前会返回state包含以下几个场景。
     * （1）扫带参二维码授权代开发模版。
     */
    @SerializedName("state")
    private String state;

    /**
     * 代理服务商企业信息，应用被代理后才有该信息
     */
    @Getter
    @Setter
    public static class DealerCorpInfo implements Serializable {
        private static final long serialVersionUID = 5727013787375345340L;

        @SerializedName("corpid")
        private String corpId;

        @SerializedName("corp_name")
        private String corpName;
    }

    @Getter
    @Setter
    public static class AuthCorpInfo implements Serializable {
        private static final long serialVersionUID = -5561754467391962405L;

        @SerializedName("corpid")
        private String corpId;

        @SerializedName("corp_name")
        private String corpName;

        @SerializedName("corp_type")
        private String corpType;

        @SerializedName("corp_square_logo_url")
        private String corpSquareLogoUrl;

        @SerializedName("corp_round_logo_url")
        private String corpRoundLogoUrl;

        @SerializedName("corp_user_max")
        private String corpUserMax;

        @SerializedName("corp_agent_max")
        private String corpAgentMax;

        /**
         * 所绑定的企业微信主体名称(仅认证过的企业有)
         */
        @SerializedName("corp_full_name")
        private String corpFullName;

        /**
         * 认证到期时间
         */
        @SerializedName("verified_end_time")
        private Long verifiedEndTime;

        /**
         * 企业类型，1. 企业; 2. 政府以及事业单位; 3. 其他组织, 4.团队号
         */
        @SerializedName("subject_type")
        private Integer subjectType;

        /**
         * 授权企业在微工作台（原企业号）的二维码，可用于关注微工作台
         */
        @SerializedName("corp_wxqrcode")
        private String corpWxQrcode;

        @SerializedName("corp_scale")
        private String corpScale;

        @SerializedName("corp_industry")
        private String corpIndustry;

        @SerializedName("corp_sub_industry")
        private String corpSubIndustry;

        @SerializedName("location")
        private String location;
    }

    /**
     * 授权信息
     */
    @Getter
    @Setter
    public static class AuthInfo implements Serializable {
        private static final long serialVersionUID = -5028321625140879571L;

        /**
         * 授权的应用信息，注意是一个数组，但仅旧的多应用套件授权时会返回多个agent，对新的单应用授权，永远只返回一个agent
         */
        @SerializedName("agent")
        private List<Agent> agents;

    }

    /**
     * 企业当前生效的版本信息
     */
    @Getter
    @Setter
    public static class EditionInfo implements Serializable {
        private static final long serialVersionUID = -5028321625140879571L;

        /**
         * 授权的应用信息，注意是一个数组，但仅旧的多应用套件授权时会返回多个agent，对新的单应用授权，永远只返回一个agent
         */
        @SerializedName("agent")
        private List<Agent> agents;

    }

    /**
     * The type Agent.
     */
    @Getter
    @Setter
    public static class Agent implements Serializable {
        private static final long serialVersionUID = -5028321625140879571L;

        @SerializedName("agentid")
        private Integer agentId;

        @SerializedName("name")
        private String name;

        @SerializedName("round_logo_url")
        private String roundLogoUrl;

        @SerializedName("square_logo_url")
        private String squareLogoUrl;

        /**
         * 旧的多应用套件中的对应应用id，新开发者请忽略
         */
        @SerializedName("appid")
        @Deprecated
        private String appid;

        /**
         * 授权模式，0为管理员授权；1为成员授权
         */
        @SerializedName("auth_mode")
        private Integer authMode;

        /**
         * 是否为代开发自建应用
         */
        @SerializedName("is_customized_app")
        private Boolean isCustomizedApp;

        /**
         * 应用权限
         */
        @SerializedName("privilege")
        private Privilege privilege;

        /**
         * 版本id
         */
        @SerializedName("edition_id")
        private String editionId;

        /**
         * 版本名称
         */
        @SerializedName("edition_name")
        private String editionName;

        /**
         * 付费状态
         * <br/>
         * <ul>
         *   <li>0-没有付费;</li>
         *   <li>1-限时试用;</li>
         *   <li>2-试用过期;</li>
         *   <li>3-购买期内;</li>
         *   <li>4-购买过期;</li>
         *   <li>5-不限时试用;</li>
         *   <li>6-购买期内，但是人数超标, 注意，超标后还可以用7天;</li>
         *   <li>7-购买期内，但是人数超标, 且已经超标试用7天</li>
         * </ul>
         */
        @SerializedName("app_status")
        private Integer appStatus;

        /**
         * 用户上限。
         * <p>特别注意, 以下情况该字段无意义，可以忽略：</p>
         * <ul>
         *   <li>1. 固定总价购买</li>
         *   <li>2. app_status = 限时试用/试用过期/不限时试用</li>
         *   <li>3. 在第2条“app_status=不限时试用”的情况下，如果该应用的配置为“小企业无使用限制”，user_limit有效，且为限制的人数</li>
         * </ul>
         */
        @SerializedName("user_limit")
        private Long userLimit;

        /**
         * 版本到期时间, 秒级时间戳, 根据需要自行乘以1000（根据购买版本，可能是试用到期时间或付费使用到期时间）。
         * <p>特别注意，以下情况该字段无意义，可以忽略：</p>
         * <ul>
         *   <li>1. app_status = 不限时试用</li>
         * </ul>
         */
        @SerializedName("expired_time")
        private Long expiredTime;

        /**
         * 是否虚拟版本
         */
        @SerializedName("is_virtual_version")
        private Boolean isVirtualVersion;

        /**
         * 是否由互联企业分享安装。详见 <a href='https://developer.work.weixin.qq.com/document/path/93360#24909'>企业互联</a>
         */
        @SerializedName("is_shared_from_other_corp")
        private Boolean isSharedFromOtherCorp;

    }

    /**
     * 授权人员信息
     */
    @Getter
    @Setter
    public static class AuthUserInfo implements Serializable {
        private static final long serialVersionUID = -5028321625140879571L;

        @SerializedName("userid")
        private String userId;

        @SerializedName("name")
        private String name;

        @SerializedName("avatar")
        private String avatar;

        /**
         * 授权管理员的open_userid，可能为空
         */
        @SerializedName("open_userid")
        private String openUserid;
    }

    /**
     * 推广二维码安装相关信息
     */
    @Getter
    @Setter
    public static class RegisterCodeInfo implements Serializable {
        private static final long serialVersionUID = -5028321625140879571L;

        /**
         * 注册码
         */
        @SerializedName("register_code")
        private String registerCode;

        /**
         * 推广包ID
         */
        @SerializedName("template_id")
        private String templateId;

        /**
         * 仅当获取注册码指定该字段时才返回
         */
        @SerializedName("state")
        private String state;

    }

    /**
     * 应用对应的权限
     */
    @Getter
    @Setter
    public static class Privilege implements Serializable {
        private static final long serialVersionUID = -5028321625140879571L;

        /**
         * 权限等级。
         * 1:通讯录基本信息只读
         * 2:通讯录全部信息只读
         * 3:通讯录全部信息读写
         * 4:单个基本信息只读
         * 5:通讯录全部信息只写
         */
        @SerializedName("level")
        private Integer level;

        @SerializedName("allow_party")
        private List<Integer> allowParties;

        @SerializedName("allow_user")
        private List<String> allowUsers;

        @SerializedName("allow_tag")
        private List<Integer> allowTags;

        @SerializedName("extra_party")
        private List<Integer> extraParties;

        @SerializedName("extra_user")
        private List<String> extraUsers;

        @SerializedName("extra_tag")
        private List<Integer> extraTags;


    }


    /**
     * From json wx cp tp permanent code info.
     *
     * @param json the json
     * @return the wx cp tp permanent code info
     */
    public static WxCpTpPermanentCodeInfo fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpTpPermanentCodeInfo.class);
    }

    @Override
    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }


}
