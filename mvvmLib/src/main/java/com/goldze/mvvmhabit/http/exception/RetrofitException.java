package com.goldze.mvvmhabit.http.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;


public class RetrofitException {

    public static final int RESULT_CODE_SUCCESS = 0;
    public Throwable e;
    /*未经授权的*/
    private static final int UNAUTHORIZED = 401;
    /*被禁止的*/
    private static final int FORBIDDEN = 403;
    /*未发现*/
    private static final int NOT_FOUND = 404;
    /*请求超时*/
    private static final int REQUEST_TIMEOUT = 408;
    /*内部服务器错误*/
    private static final int INTERNAL_SERVER_ERROR = 500;
    /*错误网关*/
    private static final int BAD_GATEWAY = 502;
    /*服务不可用*/
    private static final int SERVICE_UNAVAILABLE = 503;
    /*网关超时*/
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponeThrowable retrofitException(Throwable e) {
        ResponeThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponeThrowable(e, ERROR.HTTP_ERROR);
            ex.addSuppressed(e);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络错误";
                    break;
            }
            return ex;
        } else if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            int errCode = apiException.getErrcode();
            handleServerException(errCode);
            ex = new ResponeThrowable(e, ERROR.HTTP_ERROR);
            ex.addSuppressed(e);
            return ex;
        } else if (e instanceof ServerException) {
            // 服务器下发的错误
            ServerException resultException = (ServerException) e;
            ex = new ResponeThrowable(resultException, resultException.code);
            ex.addSuppressed(e);
            ex.message = resultException.getMessage();
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponeThrowable(e, ERROR.PARSE_ERROR);
            ex.addSuppressed(e);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException
                || e instanceof SocketTimeoutException
                || e instanceof UnknownHostException) {
            ex = new ResponeThrowable(e, ERROR.NETWORD_ERROR);
            ex.addSuppressed(e);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof SSLHandshakeException) {
            ex = new ResponeThrowable(e, ERROR.SSL_ERROR);
            ex.addSuppressed(e);
            ex.message = "证书验证失败";
            return ex;
        } else {
            ex = new ResponeThrowable(e, ERROR.UNKNOWN);
            ex.addSuppressed(e);
            ex.message = "未知错误";
            return ex;
        }
    }

    /**
     * 约定异常
     */
    class ERROR {
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
    }

    public static class ResponeThrowable extends Exception {
        public int code;
        public String message;

        public ResponeThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }
    }

    /**
     * 根据业务逻辑处理异常信息
     */
    private static void handleServerException(int errcode) {
        switch (errcode) {
            case FORBIDDEN:
//                ToastUtils.showLong(R.string.base_lose_login_hint);
//                ARouter.getInstance().build(RouterActivityPath.Sign.PAGER_WITHPHONE).navigation();
                break;
            default:
                break;
        }
    }
}
