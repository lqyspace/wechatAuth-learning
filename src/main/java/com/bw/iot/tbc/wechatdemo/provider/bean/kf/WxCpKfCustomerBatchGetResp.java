package com.bw.iot.tbc.wechatdemo.provider.bean.kf;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxCpBaseResp;
import com.bw.iot.tbc.wechatdemo.provider.bean.external.contact.ExternalContact;
import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName WxCpKfCustomerBatchGetResp
 * @Description
 * @Author lengqy
 * @Date 2024年12月30日 21:17
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class WxCpKfCustomerBatchGetResp extends WxCpBaseResp {
    private static final long serialVersionUID = -3723131164933342479L;

    @SerializedName("customer_list")
    private List<ExternalContact> customerList;

    @SerializedName("invalid_external_userid")
    private List<String> invalidExternalUserId;

    /**
     * From json wx cp kf customer batch get resp.
     *
     * @param json the json
     * @return the wx cp kf customer batch get resp
     */
    public static WxCpKfCustomerBatchGetResp fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpKfCustomerBatchGetResp.class);
    }
}
