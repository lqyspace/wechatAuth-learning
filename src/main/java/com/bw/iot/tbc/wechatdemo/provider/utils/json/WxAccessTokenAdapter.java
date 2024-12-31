package com.bw.iot.tbc.wechatdemo.provider.utils.json;

import com.bw.iot.tbc.wechatdemo.provider.bean.WxAccessToken;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @ClassName WxAccessTokenAdapter
 * @Description
 * @Author lengqy
 * @Date 2024年12月07日 23:31
 * @Version 1.0
 */
public class WxAccessTokenAdapter implements JsonDeserializer<WxAccessToken> {

    @Override
    public WxAccessToken deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        WxAccessToken accessToken = new WxAccessToken();
        JsonObject accessTokenJsonObject = jsonElement.getAsJsonObject();

        if (accessTokenJsonObject.get("access_token") != null && !accessTokenJsonObject.get("access_token").isJsonNull()) {
            accessToken.setAccessToken(GsonHelper.getAsString(accessTokenJsonObject.get("access_token")));
        }
        if (accessTokenJsonObject.get("expires_in") != null && !accessTokenJsonObject.get("expires_in").isJsonNull()) {
            accessToken.setExpiresIn(GsonHelper.getAsPrimitiveInt(accessTokenJsonObject.get("expires_in")));
        }
        return accessToken;
    }
}
