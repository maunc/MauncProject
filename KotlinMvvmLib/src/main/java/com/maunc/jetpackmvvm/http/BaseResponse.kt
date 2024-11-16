package com.maunc.jetpackmvvm.http

/**
 * 描述　: 服务器返回数据的基类
 * 如果需要框架帮你做脱壳处理请继承它！！请注意：
 * 根据自己的业务判断返回请求结果是否成功
 */
abstract class BaseResponse<T> {

    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

}

/**
 * 调用方参考代码
 */
data class ApiResponse<T>(
    val code: Int,
    val msg: String,
    val data: T
) : BaseResponse<T>() {

    override fun isSuccess() = code == 200

    override fun getResponseCode() = code

    override fun getResponseData() = data

    override fun getResponseMsg() = msg

}