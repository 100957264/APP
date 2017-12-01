package com.android.fisewatchlauncher.exception;

/**
 * 封装thowable 添加 统一返回码
 * Created by mare on 2017/6/18 0018.
 */

public class CustomThrowable extends Exception {
    private int code;
    private String message;

    public CustomThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public CustomThrowable(Throwable throwable, int code, String message) {
        super(throwable);
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
