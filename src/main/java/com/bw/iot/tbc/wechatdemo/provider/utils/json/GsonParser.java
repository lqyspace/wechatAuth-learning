package com.bw.iot.tbc.wechatdemo.provider.utils.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.Reader;

/**
 * @ClassName GsonParser
 * @Description
 * @Author lengqy
 * @Date 2024年12月21日 16:22
 * @Version 1.0
 */
public class GsonParser {
    private static final JsonParser JSON_PARSER = new JsonParser();

    public static JsonObject parser(String json) {
        return JSON_PARSER.parse(json).getAsJsonObject();
    }

    public static JsonObject parser(Reader reader) {
        return JSON_PARSER.parse(reader).getAsJsonObject();
    }

    public static JsonObject parser(JsonReader reader) {
        return JSON_PARSER.parse(reader).getAsJsonObject();
    }
}
