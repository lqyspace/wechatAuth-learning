package com.bw.iot.tbc.wechatdemo.provider.bean.kf;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName WxCpKfAccountLink
 * @Description
 * @Author lengqy
 * @Date 2024年12月30日 17:05
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class WxCpKfAccountLink implements Serializable {
    private static final long serialVersionUID = 7110449118893313642L;

    /**
     * 客服帐号ID
     */
    @SerializedName("open_kfid")
    private String openKfid;

    /**
     * 场景值，字符串类型，由开发者自定义。
     * 不多于32字节
     * 字符串取值范围(正则表达式)：[0-9a-zA-Z_-]*
     * <p>
     * 1. 若scene非空，返回的客服链接开发者可拼接scene_param=SCENE_PARAM参数使用，用户进入会话事件会将SCENE_PARAM原样返回。
     * 其中SCENE_PARAM需要urlencode，且长度不能超过128字节。
     * 如 https://work.weixin.qq.com/kf/kfcbf8f8d07ac7215f?enc_scene=ENCGFSDF567DF&scene_param=a%3D1%26b%3D2
     * 2. 历史调用接口返回的客服链接（包含encScene=XXX参数），不支持scene_param参数。
     * 3. 返回的客服链接，不能修改或复制参数到其他链接使用。否则进入会话事件参数校验不通过，导致无法回调。
     */
    @SerializedName("scene")
    private String scene;
}
