package com.us.maunc;

import android.app.Application;

import com.maunc.jetpackmvvm.BaseApp;

/**
 * ClsFunction：
 * CreateDate：2024/3/20
 * Author：TimeWillRememberUs
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Application getInstance() {
        return instance;
    }
}
