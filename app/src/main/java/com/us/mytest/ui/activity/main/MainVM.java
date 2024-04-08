package com.us.mytest.ui.activity.main;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.maunc.mvvmhabit.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ClsFunction：
 * CreateDate：2024/3/20
 * Author：TimeWillRememberUs
 */
public class MainVM extends BaseViewModel {

    public ObservableField<List<String>> mainRecDataOF = new ObservableField<>();

    public MainVM(@NonNull Application application) {
        super(application);
        new Handler().post(() -> {
            List<String> recData = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                recData.add("my_test" + i);
            }
            mainRecDataOF.set(recData);
        });
    }
}
