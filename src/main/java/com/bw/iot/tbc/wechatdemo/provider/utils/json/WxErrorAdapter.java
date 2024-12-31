package com.bw.iot.tbc.wechatdemo.provider.utils.json;

import com.bw.iot.tbc.wechatdemo.provider.constant.WxConsts;
import com.bw.iot.tbc.wechatdemo.provider.error.WxError;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @ClassName WxErrorAdapter
 * @Description
 * @Author lengqy
 * @Date 2024年12月10日 23:31
 * @Version 1.0
 */
public class WxErrorAdapter implements JsonDeserializer<WxError> {
    @Override
    public WxError deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        WxError.WxErrorBuilder builder = WxError.builder();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (jsonObject.get(WxConsts.ERR_CODE) != null && !jsonObject.get(WxConsts.ERR_CODE).isJsonNull()) {
            builder.errorCode(GsonHelper.getAsPrimitiveInt(jsonObject.get(WxConsts.ERR_CODE)));
        }
        if (jsonObject.get("errmsg") != null && !jsonObject.get("errmsg").isJsonNull()) {
            builder.errorMsg(GsonHelper.getAsString(jsonObject.get("errmsg")));
        }
        builder.json(jsonElement.toString());
        return builder.build();
    }
}
