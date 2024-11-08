package com.maunc.jetpackmvvm.http

object BaseNetCode {
    //请求成功, 正确的操作方式
    const val CODE_200: Int = 200

    //请求失败，不打印Message
    const val CODE_300: Int = 300

    //认证失败
    const val CODE_401: Int = 401

    const val CODE_403: Int = 403

    //未找到服务
    const val CODE_404: Int = 404

    const val CODE_408: Int = 408

    //服务器内部异常
    const val CODE_500: Int = 500

    //没有数据
    const val CODE_502: Int = 502

    //参数为空
    const val CODE_503: Int = 503

    const val CODE_504: Int = 504

    //请求的操作异常终止：未知的页面类型
    const val CODE_551: Int = 551
}