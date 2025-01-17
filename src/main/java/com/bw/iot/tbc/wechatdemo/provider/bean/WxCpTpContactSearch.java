package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName WxCpTpContactSearch
 * @Description
 * @Author lengqy
 * @Date 2024年12月29日 20:51
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class WxCpTpContactSearch implements Serializable {
    private static final long serialVersionUID = -8964752289494408920L;

    /**
     * 查询的企业corpid
     */
    @SerializedName("auth_corpid")
    private String authCorpId;

    /**
     * 搜索关键词。当查询用户时应为用户名称、名称拼音或者英文名；当查询部门时应为部门名称或者部门名称拼音
     */
    @SerializedName("query_word")
    private String queryWord;

    /**
     * 查询类型 1：查询用户，返回用户userid列表 2：查询部门，返回部门id列表。 不填该字段或者填0代表同时查询部门跟用户
     */
    @SerializedName("query_type")
    private Integer type;

    /**
     * 查询范围，仅查询类型包含用户时有效。 0：只查询在职用户 1：同时查询在职和离职用户（离职用户仅当离职前有激活企业微信才可以被搜到）
     */
    @SerializedName("query_range")
    private Integer range;

    /**
     * 应用id，若非0则只返回应用可见范围内的用户或者部门信息
     */
    @SerializedName("agentid")
    private Integer agentId;

    /**
     * 查询返回的最大数量，默认为50，最多为200，查询返回的数量可能小于limit指定的值。limit会分别控制在职数据和离职数据的数量。
     */
    @SerializedName("limit")
    private Integer limit;

    /**
     * 如果需要精确匹配用户名称或者部门名称或者英文名，不填则默认为模糊匹配；1：匹配用户名称或者部门名称 2：匹配用户英文名
     */
    @SerializedName("full_match_field")
    private Integer fullMatchField;

    /**
     * 用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填
     */
    @SerializedName("cursor")
    private String cursor;

    /**
     * To json string.
     *
     * @return the string
     */
    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }

    public static WxCpTpContactSearch fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpTpContactSearch.class);
    }
}
