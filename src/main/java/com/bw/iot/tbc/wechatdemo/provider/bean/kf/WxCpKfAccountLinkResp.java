package com.bw.iot.tbc.wechatdemo.provider.bean.kf;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpBaseResp;
import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName WxCpKfAccountLinkResp
 * @Description
 * @Author lengqy
 * @Date 2024年12月30日 17:04
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class WxCpKfAccountLinkResp extends WxCpBaseResp {
    private static final long serialVersionUID = -7778616554625404426L;

    /**
     * 客服链接，开发者可将该链接嵌入到H5页面中，用户点击链接即可向对应的微信客服帐号发起咨询。开发者也可根据该url自行生成需要的二维码图片
     */
    @SerializedName("url")
    private String url;

    /**
     * From json wx cp kf account link resp.
     *
     * @param json the json
     * @return the wx cp kf account link resp
     */
    public static WxCpKfAccountLinkResp fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpKfAccountLinkResp.class);
    }
}
