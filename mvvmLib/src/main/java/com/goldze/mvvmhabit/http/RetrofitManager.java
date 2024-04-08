package com.goldze.mvvmhabit.http;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.goldze.mvvmhabit.BuildConfig;
import com.goldze.mvvmhabit.base.BaseApp;
import com.goldze.mvvmhabit.http.cookie.CookieJarImpl;
import com.goldze.mvvmhabit.http.cookie.store.PersistentCookieStore;
import com.goldze.mvvmhabit.http.interceptor.BaseInterceptor;
import com.goldze.mvvmhabit.http.interceptor.CacheInterceptor;
import com.goldze.mvvmhabit.http.interceptor.logging.Level;
import com.goldze.mvvmhabit.http.interceptor.logging.LoggingInterceptor;
import com.goldze.mvvmhabit.utils.LogUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goldze on 2017/5/10.
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitManager {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //缓存时间
    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
    //服务端根路径
    public static String baseUrl = "";

    private static Retrofit retrofit;

    private File httpCacheDirectory;
    private Cache cache;

    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitManager() {
        this(baseUrl, null);
    }

    private RetrofitManager(String url, Map<String, String> headers) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(BaseApp.getInstance().getCacheDir(), "goldze_cache");
        }
        try {
            cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
        } catch (Exception e) {
            LogUtils.e("Could not create http cache", e);
        }
        HttpsManager.SSLParams sslParams = HttpsManager.getSslSocketFactory();
        //构建者模式
        //是否开启日志打印
        //打印的等级
        // 打印类型
        // request的Tag
        // Response的Tag
        // 添加打印头, 注意 key 和 value 都不能是中文
        // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(BaseApp.getInstance())))
//                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CacheInterceptor(BaseApp.getInstance()))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggingInterceptor.Builder()
                        .loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    /**
     * create you ApiService
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    @Nullable
    public static <T> T execute(@NonNull Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return null;
    }
}
