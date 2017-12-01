package com.android.fisewatchlauncher.exception;

/**
 * 统一封装服务器返回回来的错误
 * Created by mare on 2017/6/18 0018.
 */

public class ServerException extends  RuntimeException{
    public int code;
    public String message;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
