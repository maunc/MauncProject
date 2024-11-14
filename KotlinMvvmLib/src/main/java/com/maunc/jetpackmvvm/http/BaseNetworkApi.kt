package com.maunc.jetpackmvvm.http

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseNetworkApi {

    companion object {
        private const val DEFAULT_TIME = 20L
    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，Protocol
     */
    abstract fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder

    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(initOkHttp())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }

    private fun initOkHttp(): OkHttpClient {
        var builder = OkHttpClient().newBuilder()
        builder = setHttpClientBuilder(builder)
        builder.retryOnConnectionFailure(true) //设置出现错误进行重新连接。
            .addInterceptor(logInterceptor()) //添加打印拦截器
            .connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS) //超时时间 连接
            .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS) //超时时间 读
            .writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS) //超时时间 写
        return builder.build()
    }
}