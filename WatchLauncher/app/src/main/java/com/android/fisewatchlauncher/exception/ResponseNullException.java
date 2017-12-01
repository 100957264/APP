package com.android.fisewatchlauncher.exception;

/**
 * Created by blue on 2017/6/21 0021.
 */

public class ResponseNullException extends Exception {

    public int code;
    public String message;

    public ResponseNullException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
