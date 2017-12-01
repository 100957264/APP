package com.android.fisewatchlauncher.exception;

import android.net.ParseException;

import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.StringUtils;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLPeerUnverifiedException;

import retrofit2.HttpException;

/**
 * 用CustomeException去包装自定义的CustomThrowable
 * Created by mare on 2017/6/18 0018.
 */

public class CustomException {

    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;
    public static final int ACCESS_DENIED = 302;
    public static final int HANDEL_ERRROR = 417;

    public static CustomThrowable handleException(Throwable e) {

        LogUtils.e( e.getMessage());
        LogUtils.e(StringUtils.getCauseStr(e));
        CustomThrowable customThrowable;
        if (!(e instanceof ServerException) && e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            customThrowable = new CustomThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    customThrowable.setMessage("未授权的请求");
                case FORBIDDEN:
                    customThrowable.setMessage("禁止访问");
                case NOT_FOUND:
                    customThrowable.setMessage("服务器地址未找到");
                case REQUEST_TIMEOUT:
                    customThrowable.setMessage("请求超时");
                case GATEWAY_TIMEOUT:
                    customThrowable.setMessage("网关响应超时");
                case INTERNAL_SERVER_ERROR:
                    customThrowable.setMessage("服务器出错");
                case BAD_GATEWAY:
                    customThrowable.setMessage("无效的请求");
                case SERVICE_UNAVAILABLE:
                    customThrowable.setMessage("服务器不可用");
                case ACCESS_DENIED:
                    customThrowable.setMessage("网络错误");
                case HANDEL_ERRROR:
                    customThrowable.setMessage("接口处理失败");

                default:
                    if (e.getMessage() != null ) {
                        customThrowable.setMessage(e.getMessage());
                        break;
                    }

                    if (e.getLocalizedMessage() != null) {
                        customThrowable.setMessage(e.getLocalizedMessage());
                        break;
                    }
                    customThrowable.setMessage("未知错误");
                    break;
            }
            customThrowable.setCode(httpException.code());
            return customThrowable;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            customThrowable = new CustomThrowable(resultException, resultException.code);
            customThrowable.setMessage(resultException.getMessage());
            return customThrowable;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            customThrowable = new CustomThrowable(e, ERROR.PARSE_ERROR);
            customThrowable.setMessage("解析错误");
            return customThrowable;
        } else if (e instanceof ConnectException) {
            customThrowable = new CustomThrowable(e, ERROR.NETWORD_ERROR);
            customThrowable.setMessage("连接失败");
            return customThrowable;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            customThrowable = new CustomThrowable(e, ERROR.SSL_ERROR);
            customThrowable.setMessage("证书验证失败");
            return customThrowable;
        } else if (e instanceof java.security.cert.CertPathValidatorException) {
            LogUtils.e( e.getMessage());
            customThrowable = new CustomThrowable(e, ERROR.SSL_NOT_FOUND);
            customThrowable.setMessage("证书路径没找到");

            return customThrowable;
        } else if (e instanceof SSLPeerUnverifiedException) {
            LogUtils.e( e.getMessage());
            customThrowable = new CustomThrowable(e, ERROR.SSL_NOT_FOUND);
            customThrowable.setMessage("无有效的SSL证书");
            return customThrowable;

        } else if (e instanceof ConnectTimeoutException){
            customThrowable = new CustomThrowable(e, ERROR.TIMEOUT_ERROR);
            customThrowable.setMessage("连接超时");
            return customThrowable;
        } else if (e instanceof java.net.SocketTimeoutException) {
            customThrowable = new CustomThrowable(e, ERROR.TIMEOUT_ERROR);
            customThrowable.setMessage("连接超时");
            return customThrowable;
        } else if (e instanceof ClassCastException) {
            customThrowable = new CustomThrowable(e, ERROR.FORMAT_ERROR);
            customThrowable.setMessage("类型转换出错");
            return customThrowable;
        } else if (e instanceof NullPointerException) {
            customThrowable = new CustomThrowable(e, ERROR.NULL);
            customThrowable.setMessage("数据有空");
            return customThrowable;
        } else if (e instanceof FormatException) {
            FormatException resultException = (FormatException) e;
            customThrowable = new CustomThrowable(e, resultException.code);
            customThrowable.setMessage(resultException.message);
            return customThrowable;
        } else if (e instanceof UnknownHostException){
            LogUtils.e( e.getMessage());
            customThrowable = new CustomThrowable(e, NOT_FOUND);
            customThrowable.setMessage("服务器地址未找到,请检查网络或Url");
            return customThrowable;
        }else if (e instanceof ResponseNullException){
            LogUtils.e( e.getMessage());
            customThrowable = new CustomThrowable(e, ERROR.RESPONSE_NULL);
            customThrowable.setMessage(RESPONSE_NULL_MSG);
            return customThrowable;
        } else {
            LogUtils.e( e.getMessage());
            customThrowable = new CustomThrowable(e, ERROR.UNKNOWN);
            customThrowable.setMessage(e.getMessage());
            return customThrowable;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        /**
         * 证书未找到
         */
        public static final int SSL_NOT_FOUND = 1007;

        /**
         * 出现空值
         */
        public static final int NULL = -100;

        /**
         * 格式错误
         */
        public static final int FORMAT_ERROR = 1008;

        /**
         * 响应体为空
         */
        public static final int RESPONSE_NULL = 1009;
    }

    public static final String RESPONSE_NULL_MSG = "返回的响应体为空";

}