package com.maunc.mvvmhabit.http.base;

public interface BaseNetDataCallBack<T> {

    void onSuccess(T t);

    void onError(String errorMsg);
}
