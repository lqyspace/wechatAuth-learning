package com.bw.iot.tbc.wechatdemo.provider.utils.json;

import com.bw.iot.tbc.wechatdemo.provider.error.WxError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

/**
 * @ClassName WxCpGsonBuilder
 * @Description
 * @Author lengqy
 * @Date 2024年12月23日 00:01
 * @Version 1.0
 */
public class WxCpGsonBuilder {
    private static final GsonBuilder INSTANCE = new GsonBuilder();
    private static volatile Gson GSON_INSTANCE;

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.registerTypeAdapter(WxError.class, new WxErrorAdapter());
    }

    public static Gson create() {
        if (Objects.isNull(GSON_INSTANCE)) {
            synchronized (INSTANCE) {
                if (Objects.isNull(GSON_INSTANCE)) {
                    GSON_INSTANCE = INSTANCE.create();
                }
            }
        }
        return GSON_INSTANCE;
    }
}
