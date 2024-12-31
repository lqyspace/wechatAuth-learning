package com.bw.iot.tbc.wechatdemo.provider.service;

/**
 * @ClassName WxCpKfService
 * @Description
 * @Author lengqy
 * @Date 2024年12月30日 16:31
 * @Version 1.0
 */

import com.bw.iot.tbc.wechatdemo.provider.bean.WxAccessToken;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountLink;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountLinkResp;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfAccountListResp;
import com.bw.iot.tbc.wechatdemo.provider.bean.kf.WxCpKfCustomerBatchGetResp;
import com.bw.iot.tbc.wechatdemo.provider.error.WxErrorException;

import java.util.List;

/**
 * 微信客服接口
 * 用户可以发起咨询，企业可以进行回复
 * 企业可在微信客服官网使用企业微信扫码开通微信客服，开通后即可使用
 */
public interface WxCpKfService {

    /**
     * 获取客服帐号列表，包括所有的客服帐号的客服ID、名称和头像。
     *
     * @param offset   分页，偏移量，默认为0
     * @param limit    分页，预期请求的数据量，默认为100，取值范围1 ~ 100
     * @return         客服账号列表
     * @throws WxErrorException
     */
    WxCpKfAccountListResp listAccount(Integer offset, Integer limit) throws WxErrorException;

    /**
     * 企业可通过此接口获取带有不同参数的客服链接，不同客服帐号对应不同的客服链接。获取后，企业可将链接嵌入到网页等场景中，
     * 微信用户点击链接即可向对应的客服帐号发起咨询。企业可依据参数来识别用户的咨询来源等
     *
     * @param link 参数
     * @return 链接 account link
     * @throws WxErrorException 异常
     */
    WxCpKfAccountLinkResp getAccountLink(WxCpKfAccountLink link) throws WxErrorException;

    /**
     * 获取客户基础信息
     *
     * @param externalUserIdList the external user id list
     * @return wx cp kf customer batch get resp
     * @throws WxErrorException the wx error exception
     */
    WxCpKfCustomerBatchGetResp customerBatchGet(List<String> externalUserIdList, Integer needEnterSessionContext)
            throws WxErrorException;
}
