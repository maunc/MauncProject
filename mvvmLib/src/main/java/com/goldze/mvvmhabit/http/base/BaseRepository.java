package com.goldze.mvvmhabit.http.base;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import com.goldze.mvvmhabit.http.RetrofitManager;

public class BaseRepository {
    public <T> T create(Class<T> clazz) {
        return RetrofitManager.getInstance().create(clazz);
    }

    public DisposableObserver addSubscribe(@NonNull Observable<?> observable, DisposableObserver observer) {
        DisposableObserver baseObserver = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer);
        return baseObserver;
    }
}
