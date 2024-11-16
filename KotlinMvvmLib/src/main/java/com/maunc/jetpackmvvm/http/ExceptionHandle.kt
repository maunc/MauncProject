package com.maunc.jetpackmvvm.http

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

enum class Error(private val code: Int, private val err: String) {
    /**
     * 未知错误
     */
    UNKNOWN(1000, "请求失败，请稍后再试"),

    /**
     * 解析错误
     */
    PARSE_ERROR(1001, "解析错误，请稍后再试"),

    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, "网络连接错误，请稍后重试"),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, "证书出错，请稍后再试"),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, "网络连接超时，请稍后重试");

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }
}

object ExceptionHandle {

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = AppException(Error.NETWORK_ERROR, e)
                    return ex
                }

                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(Error.PARSE_ERROR, e)
                    return ex
                }

                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR, e)
                    return ex
                }

                is javax.net.ssl.SSLException -> {
                    ex = AppException(Error.SSL_ERROR, e)
                    return ex
                }

                is ConnectTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }

                is java.net.SocketTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }

                is java.net.UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                    return ex
                }

                is AppException -> return it

                else -> {
                    ex = AppException(Error.UNKNOWN, e)
                    return ex
                }
            }
        }
        ex = AppException(Error.UNKNOWN, e)
        return ex
    }
}

/**
 * 错误结构体
 */
class AppException : Exception {

    private var errorMsg: String //错误消息
    private var errCode: Int = 0 //错误码
    private var errorLog: String? //错误日志
    private var throwable: Throwable? = null

    constructor(
        errCode: Int?,
        error: String?,
        errorLog: String? = "",
        throwable: Throwable? = null,
    ) : super(error) {
        this.errorMsg = error ?: "请求失败，请稍后再试"
        this.errCode = errCode ?: 0
        this.errorLog = errorLog ?: this.errorMsg
        this.throwable = throwable
    }

    constructor(
        error: Error,
        e: Throwable?
    ) {
        errCode = error.getKey()
        errorMsg = error.getValue()
        errorLog = e?.message
        throwable = e
    }
}

object BaseNetCode {
    //请求成功, 正确的操作方式
    const val CODE_200: Int = 200

    //请求失败，不打印Message
    const val CODE_300: Int = 300

    const val CODE_302: Int = 302

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