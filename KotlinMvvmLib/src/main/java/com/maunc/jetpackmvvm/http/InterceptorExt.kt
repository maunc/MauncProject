package com.maunc.jetpackmvvm.http

import com.maunc.jetpackmvvm.BaseApp
import com.maunc.jetpackmvvm.ext.checkNetworkAvailable
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

fun logInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}

class HeadersInterceptor(private val headers: Map<String, String>?) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request()
            .newBuilder()
        if (!headers.isNullOrEmpty()) {
            val keys = headers.keys
            for (headerKey in keys) {
                headers[headerKey]?.let {
                    builder.addHeader(headerKey, it).build()
                }
            }
        }
        //请求信息
        return chain.proceed(builder.build())
    }
}

/**
 * 无网络状态下智能读取缓存的拦截器
 */
class CacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (BaseApp.instance.checkNetworkAvailable()) {
            val response = chain.proceed(request)
            // read from cache for 60 s
            val maxAge = 60
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        } else {
            //读取缓存信息
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
            val response = chain.proceed(request)
            //set cache times is 3 days
            val maxStale = 60 * 60 * 24 * 3
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
    }
}

//class MultDomainInterceptor : Interceptor {
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        //获取原始的originalRequest
//        val originalRequest: Request = chain.request()
//        //获取老的url
//        val oldUrl: HttpUrl = originalRequest.url()
//        //获取originalRequest的创建者builder
//        val builder: Request.Builder = originalRequest.newBuilder()
//        //获取头信息的集合如：manage,mdffx
//        val urlNameList: List<String> = originalRequest.headers("url_key")
//        if (urlNameList.isNotEmpty()) {
//            //删除原有配置中的值,就是namesAndValues集合里的值
//            builder.removeHeader("urlName")
//            //获取头信息中配置的value,如：manage或者mdffx
//            val urlName = urlNameList[0]
//            //根据头信息中配置的value,来匹配新的base_url地址
//            val baseURL: HttpUrl? = if (API.API_MODEL_1.equals(urlName)) {
//                HttpUrl.parse(Constant.PLATFORM_URL_HEADER1)?.newBuilder()?.build()
//            } else {
//                HttpUrl.parse(Constant.PLATFORM_URL_HEADER)?.newBuilder()?.build()
//            }
//            //重建新的HttpUrl，需要重新设置的url部分
//            val newHttpUrl: HttpUrl = oldUrl.newBuilder()
//                .scheme(baseURL!!.scheme()) //http协议如：http或者https
//                .host(baseURL.host()) //主机地址
//                .port(baseURL.port()) //端口
//                .build()
//            //获取处理后的新newRequest
//            val newRequest: Request = builder.url(newHttpUrl).build()
//            return chain.proceed(newRequest)
//        } else {
//            return chain.proceed(originalRequest)
//        }
//    }
//}