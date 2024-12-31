package com.bw.iot.tbc.wechatdemo.provider.bean;

import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxCpGsonBuilder;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WxCpTpContactSearchResp
 * @Description
 * @Author lengqy
 * @Date 2024年12月29日 21:00
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxCpTpContactSearchResp extends WxCpBaseResp{
    private static final long serialVersionUID = 2029580396197258173L;

    /**
     * 根据该字段判断是否为最后一页，若为false，开发者需要使用limit+cursor继续调用
     */
    @SerializedName("is_last")
    private boolean isLast;

    /**
     * 分页游标，在下次请求时填写以获取之后分页的记录，如果没有更多的数据则返回为空
     */
    @SerializedName("next_cursor")
    private String nextCursor;

    /**
     * 查询结果
     */
    @SerializedName("query_result")
    private QueryResult queryResult;

    @Getter
    @Setter
    public static class QueryResult implements Serializable {
        private static final long serialVersionUID = 7107892314951844113L;

        /**
         * 返回的在职用户信息（通过用户名称，拼音匹配）
         */
        @SerializedName("user")
        private User user;

        /**
         * 返回的部门信息 （通过部门名称，拼音匹配）
         */
        @SerializedName("party")
        private Party party;

        /**
         * 返回的离职用户信息（通过用户名称，拼音匹配）
         */
        @SerializedName("dismiss_user")
        private User dismissUser;

        @Getter
        @Setter
        public static class User implements Serializable {
            private static final long serialVersionUID = 1552139357787037974L;

            /**
             * 查询到的用户userid
             */
            @SerializedName("userid")
            private List<String> userId;

            /**
             * 查询到的用户open_userid
             */
            @SerializedName("open_userid")
            private List<String> openUserId;
        }

        @Getter
        @Setter
        public static class Party implements Serializable {
            private static final long serialVersionUID = -1564156490514229763L;

            /**
             * 返回的部门id
             */
            @SerializedName("department_id")
            private List<Integer> departmentId;
        }
    }

    public static WxCpTpContactSearchResp fromJson(String json) {
        return WxCpGsonBuilder.create().fromJson(json, WxCpTpContactSearchResp.class);
    }

    public String toJson() {
        return WxCpGsonBuilder.create().toJson(this);
    }
}
