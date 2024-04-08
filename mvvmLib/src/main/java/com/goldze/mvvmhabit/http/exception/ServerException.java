package com.goldze.mvvmhabit.http.exception;

/**
 * -----------------------------------------------------------------------------------------
 *
 * @项目名称: 9dianzheng
 * @包名: com.goldze.base.network
 * @作者: SAM
 * @创建时间: 2019 年 06 月 11 日  11 小时 48 分 星期二
 * 页面：
 * -----------------------------------------------------------------------------------------
 **/

public class ServerException extends RuntimeException {

    public int code;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
    }


    /**
     * 约定异常
     */
    public static class CODE{
        /* 统一默认错误码 */
        public static final int FAILURE = 0;
        /* Token 失效 */
        public static final int TOKEN_FAILURE = 1300;
        /* Token 被其他设备登录 */
        public static final int TOKEN_OTHER_ONE_LOGGED_IN = 1;
    }
}