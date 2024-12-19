package com.us.maunc.ui.activity.xiecheng

import com.maunc.jetpackmvvm.http.BaseNetworkApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit


val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {

//    http://localhost:8080/hello
    NetworkApi.INSTANCE.getApi(ApiService::class.java, "http://192.168.120.175:8080/")
}

class NetworkApi : BaseNetworkApi() {

    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }


    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder
//            .followRedirects(false)//禁止Okhttp的重定向操作，我们自己处理
//            .followSslRedirects(false)//https的重定向也自己处理
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder
    }
}