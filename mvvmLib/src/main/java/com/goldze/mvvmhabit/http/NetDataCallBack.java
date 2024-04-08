package com.goldze.mvvmhabit.http;

public interface NetDataCallBack<T> {

    void onSuccess(T t);

    void onError(String errorMsg);
}
