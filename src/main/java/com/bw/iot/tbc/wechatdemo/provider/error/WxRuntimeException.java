package com.bw.iot.tbc.wechatdemo.provider.error;

/**
 * @ClassName WxRunTimeException
 * @Description
 * @Author lengqy
 * @Date 2024年12月07日 23:38
 * @Version 1.0
 */
public class WxRuntimeException extends RuntimeException{
    private static final long serialVersionUID = 8506628584115217986L;
    public WxRuntimeException(Throwable e) {
        super(e);
    }
    public WxRuntimeException(String message) {
        super(message);
    }
    public WxRuntimeException(String message, Throwable e) {
        super(message, e);
    }
}
