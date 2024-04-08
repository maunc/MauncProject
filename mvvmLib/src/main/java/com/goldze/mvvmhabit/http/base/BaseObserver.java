package com.goldze.mvvmhabit.http.base;

import com.goldze.mvvmhabit.http.exception.ApiException;
import com.goldze.mvvmhabit.http.exception.ExceptionHandle;
import com.goldze.mvvmhabit.http.exception.ResponseThrowable;
import com.goldze.mvvmhabit.http.exception.RetrofitException;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by goldze on 2017/5/10.
 * 统一的Code封装处理。该类仅供参考，实际业务逻辑, 根据需求来定义，
 */

public abstract class BaseObserver<T> extends DisposableObserver<BaseResponse<T>> {

    public BaseObserver() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(BaseResponse<T> baseResponse) {
        int errorCode = baseResponse.getCode();
        String errMsg = baseResponse.getMessage();
        onSuccess(baseResponse.getResult());
    }

    /**
     * 回调正常数据
     */
    protected abstract void onSuccess(T data);

    protected abstract void onError(int errCode, String errMsg);

    /**
     * 异常处理，包括两方面数据：
     * (1) 服务端没有没有返回数据，HttpException，如网络异常，连接超时;
     * (2) 服务端返回了数据，但 errcode!=20000,如 密码错误,App登陆超时,token失效
     */
    @Override
    public void onError(Throwable e) {
        RetrofitException.ResponeThrowable throwable = RetrofitException.retrofitException(e);
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            onError(apiException.getErrcode(), apiException.getErrmsg());
        } else {
            ResponseThrowable he = ExceptionHandle.handleException(e);
            onError(he.code, he.message);
        }
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
    }
}