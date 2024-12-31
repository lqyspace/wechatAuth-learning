package com.bw.iot.tbc.wechatdemo.provider.utils;

/**
 * @ClassName RandomUtils
 * @Description
 * @Author lengqy
 * @Date 2024年12月31日 10:25
 * @Version 1.0
 */
public class RandomUtils {
    private static final String RANDOM_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final java.util.Random RANDOM = new java.util.Random();

    public static String getRandomStr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(RANDOM_STR.charAt(RANDOM.nextInt(RANDOM_STR.length())));
        }
        return sb.toString();
    }
}
