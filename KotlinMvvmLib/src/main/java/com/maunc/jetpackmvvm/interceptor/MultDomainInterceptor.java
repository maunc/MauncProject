package com.maunc.jetpackmvvm.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class MultDomainInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
//        //获取原始的originalRequest
//        Request originalRequest = chain.request();
//        //获取老的url
//        HttpUrl oldUrl = originalRequest.url();
//        //获取originalRequest的创建者builder
//        Request.Builder builder = originalRequest.newBuilder();
//        //获取头信息的集合如：manage,mdffx
//        List<String> urlnameList = originalRequest.headers("redirect_url");
//        if (urlnameList != null && urlnameList.size() > 0) {
//            //删除原有配置中的值,就是namesAndValues集合里的值
//            builder.removeHeader("urlname");
//            //获取头信息中配置的value,如：manage或者mdffx
//            String urlname = urlnameList.get(0);
//
//            HttpUrl baseURL = null;
//            //根据头信息中配置的value,来匹配新的base_url地址
//            if (API.API_MODEL_1.equals(urlname)) {
//                baseURL = HttpUrl.parse(Constant.PLATFORM_URL_HEADER1)
//                        .newBuilder()
//                        .build();
//            } else {
//                baseURL = HttpUrl.parse(Constant.PLATFORM_URL_HEADER)
//                        .newBuilder()
//                        .build();
//            }
//
//            //重建新的HttpUrl，需要重新设置的url部分
//            HttpUrl newHttpUrl = oldUrl.newBuilder()
//                    .scheme(baseURL.scheme())//http协议如：http或者https
//                    .host(baseURL.host())//主机地址
//                    .port(baseURL.port())//端口
//                    .build();
//            //获取处理后的新newRequest
//            Request newRequest = builder.url(newHttpUrl).build();
//            return chain.proceed(newRequest);
//        } else {
//            return chain.proceed(originalRequest);
//        }
        return null;
    }
}
