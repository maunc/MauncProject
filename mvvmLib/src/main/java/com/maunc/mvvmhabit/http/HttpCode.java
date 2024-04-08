package com.maunc.mvvmhabit.http;

public final class HttpCode {

    //请求成功, 正确的操作方式
    public static final int CODE_200 = 200;
    //请求失败，不打印Message
    public static final int CODE_300 = 300;
    //认证失败
    public static final int CODE_401 =401;

    public static final int CODE_403 =403;
    //未找到服务
    public static final int CODE_404 =404;

    public static final int CODE_408 =408;
    //服务器内部异常
    public static final int CODE_500 = 500;
    //没有数据
    public static final int CODE_502 = 502;
    //参数为空
    public static final int CODE_503 = 503;

    public static final int CODE_504 = 504;
    //请求的操作异常终止：未知的页面类型
    public static final int CODE_551 = 551;
}
