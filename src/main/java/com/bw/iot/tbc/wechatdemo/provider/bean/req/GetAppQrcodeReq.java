package com.bw.iot.tbc.wechatdemo.provider.bean.req;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName GetAppQrcodeReq
 * @Description
 * @Author lengqy
 * @Date 2024年12月28日 21:26
 * @Version 1.0
 */
@Getter
@Setter
public class GetAppQrcodeReq implements Serializable {
    private static final long serialVersionUID = -4132564531392058570L;

    /**
     * 第三方应用的suiteId，必须
     */
    private String suiteId;

    /**
     * 第三方应用的appId
     */
    private String appId;

    /**
     * state，用于区分不同的安装渠道，可以填写a-zA-Z0-9，长度不可超过32个字节，默认为空。
     * 扫应用带参二维码授权安装后，获取企业永久授权码接口会返回该state值
     */
    private String state;

    /**
     * 二维码样式，0：带说明外框的二维码，适用于实体物料；1：带说明外框的二维码，适用于屏幕类；2：不带说明外框（小尺寸）；
     * 3：不带说明外框（中尺寸）；4：不带说明外框（大尺寸）；
     */
    private Integer style;

    /**
     * 结果返回方式，默认为返回二维码图片buffer，1：返回二维码图片buffer；2：返回二维码图片url；
     */
    private Integer resultType;
}
