package com.android.fisewatchlauncher.exception;

/**
 * 服务器返回数据格式异常导致的解析数据失败
 * Created by mare on 2017/6/18 0018.
 */

public class FormatException extends RuntimeException{
    public int code = -200;
    public String message = "服务端返回数据格式异常";

    public FormatException() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
