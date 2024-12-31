package com.bw.iot.tbc.wechatdemo.provider.utils.json;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxAccessToken;
import com.bw.iot.tbc.wechatdemo.provider.error.WxError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

/**
 * @ClassName WxGsonBuilder
 * @Description
 * @Author lengqy
 * @Date 2024年12月23日 11:05
 * @Version 1.0
 */
public class WxGsonBuilder {
    // 单例模式
    private static final GsonBuilder INSTANCE = new GsonBuilder();
    private static volatile Gson GSON_INSTANCE;

    static {
        // 避免HTML转义
        INSTANCE.disableHtmlEscaping();
        // 注册自定义适配器
        INSTANCE.registerTypeAdapter(WxAccessToken.class, new WxAccessTokenAdapter());
        INSTANCE.registerTypeAdapter(WxError.class, new WxErrorAdapter());
    }

    // 单例模式
    public static Gson create() {
        // 如果不是空的，就没必要去争夺锁进行创建
        if (Objects.isNull(GSON_INSTANCE)) {
            synchronized (INSTANCE) {
                // 双重检查，防止多次创建，因为创建过程可能被其他线程抢先执行了
                if (Objects.isNull(GSON_INSTANCE)) {
                    GSON_INSTANCE = INSTANCE.create();
                }
            }
        }
        return GSON_INSTANCE;
    }
}
