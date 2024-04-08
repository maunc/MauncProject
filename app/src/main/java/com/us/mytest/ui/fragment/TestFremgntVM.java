package com.us.mytest.ui.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.goldze.mvvmhabit.base.BaseViewModel;

/**
 * ClsFunction：
 * CreateDate：2024/3/21
 * Author：TimeWillRememberUs
 */
public class TestFremgntVM extends BaseViewModel {

    public ObservableInt numOI = new ObservableInt(0);

    public TestFremgntVM(@NonNull Application application) {
        super(application);
    }
}
