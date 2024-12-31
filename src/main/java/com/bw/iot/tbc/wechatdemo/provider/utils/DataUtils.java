package com.bw.iot.tbc.wechatdemo.provider.utils;

/**
 * @ClassName DataUtils
 * @Description
 * @Author lengqy
 * @Date 2024年12月24日 09:13
 * @Version 1.0
 */

import org.apache.commons.lang3.StringUtils;

/**
 * 数据处理类
 */
public class DataUtils {
    public static <E> E handleDataWithSecret(E data) {
        E dataForLog = data;
        if (data instanceof String && StringUtils.contains((String)data, "&secret=")) {
            dataForLog = (E) StringUtils.replaceAll((String) data, "&secret=\\w+&", "&secret=*******&");
        }
        return dataForLog;
    }
}
