package com.bw.iot.tbc.wechatdemo.provider.error;

/**
 * @ClassName WxErrorException
 * @Description
 * @Author lengqy
 * @Date 2024年12月10日 23:59
 * @Version 1.0
 */
public class WxErrorException extends Exception{

    private static final long serialVersionUID = -2791952853099709352L;
    private final WxError error;
    private static final int DEFAULT_ERROR_CODE = -99;
    public WxErrorException(WxError error) {
        super(error.toString());
        this.error = error;
    }
    public WxErrorException(String errorMsg) {
        this(WxError.builder().errorCode(DEFAULT_ERROR_CODE).errorMsg(errorMsg).build());
    }

    public WxErrorException(WxError error, Throwable cause) {
        super(error.toString(), cause);
        this.error = error;
    }
    public WxErrorException(Throwable cause) {
        super(cause.getMessage(), cause);
        this.error = WxError.builder().errorCode(DEFAULT_ERROR_CODE).errorMsg(cause.getMessage()).build();
    }

    public WxError getError() {
        return this.error;
    }
}
