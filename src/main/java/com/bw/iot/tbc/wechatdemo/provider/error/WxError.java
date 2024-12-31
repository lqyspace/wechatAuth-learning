package com.bw.iot.tbc.wechatdemo.provider.error;

import com.bw.iot.tbc.wechatdemo.provider.enums.*;
import com.bw.iot.tbc.wechatdemo.provider.utils.json.WxGsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @ClassName WxError
 * @Description
 * @Author lengqy
 * @Date 2024年12月23日 11:08
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxError implements Serializable {
    private static final long serialVersionUID = 109371498254772273L;

    /**
     * 微信错误代码
     */
    private int errorCode;

    /**
     * 微信错误信息
     * （如果可以翻译成中文，就为中文）
     */
    private String errorMsg;

    /**
     * 微信接口返回的错误原始信息（英文）
     */
    private String errorMsgEn;

    private String json;

    public WxError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static WxError fromJson(String json) {
        return fromJson(json, null);
    }

    public static WxError fromJson(String json, WxType type) {
        final WxError wxError = WxGsonBuilder.create().fromJson(json, WxError.class);
        if (wxError.getErrorCode() == 0 || type == null) {
            return wxError;
        }

        if (StringUtils.isNotEmpty(wxError.getErrorMsg())) {
            wxError.setErrorMsgEn(wxError.getErrorMsg());
        }

        switch (type) {
            case MP: {
                final String msg = WxMpErrorMsgEnum.findMsgByCode(wxError.getErrorCode());
                if (msg != null) {
                    wxError.setErrorMsg(msg);
                }
                break;
            }
            case CP: {
                final String msg = WxCpErrorMsgEnum.findMsgByCode(wxError.getErrorCode());
                if (msg != null) {
                    wxError.setErrorMsg(msg);
                }
                break;
            }
            case MiniApp: {
                final String msg = WxMaErrorMsgEnum.findMsgByCode(wxError.getErrorCode());
                if (msg != null) {
                    wxError.setErrorMsg(msg);
                }
                break;
            }
            case Open: {
                final String msg = WxOpenErrorMsgEnum.findMsgByCode(wxError.getErrorCode());
                if (msg != null) {
                    wxError.setErrorMsg(msg);
                }
                break;
            }
            case Channel: {
                final String msg = WxChannelErrorMsgEnum.findMsgByCode(wxError.getErrorCode());
                if (msg != null) {
                    wxError.setErrorMsg(msg);
                }
                break;
            }
            default:
                return wxError;
        }
        return wxError;
    }

    @Override
    public String toString() {
        if (this.json == null) {
            return "错误代码：" + this.errorCode + ", 错误信息：" + this.errorMsg;
        }
        return "错误代码：" + this.errorCode + ", 错误信息：" + this.errorMsg + ", 原始JSON数据：" + this.json;
    }
}
