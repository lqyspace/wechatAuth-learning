package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName WxCpAgentJsapiSignature
 * @Description
 * @Author lengqy
 * @Date 2024年12月31日 09:42
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxCpAgentJsapiSignature implements Serializable {
    private static final long serialVersionUID = 3916565251030089270L;
    private String url;

    @SerializedName("corpid")
    private String corpid;

    @SerializedName("agentid")
    private Integer agentid;

    private long timestamp;

    private String nonceStr;

    private String signature;
}
