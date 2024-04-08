package com.us.mytest.ui.activity.speedratio;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.goldze.mvvmhabit.base.BaseViewModel;
import com.us.mytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ClsFunction：
 * CreateDate：2024/3/24
 * Author：TimeWillRememberUs
 */
public class SpeedVM extends BaseViewModel {


    public ObservableField<List<Integer>> speedBgRecDataOF = new ObservableField<>();
    public ObservableField<List<Integer>> speedRecDataOF = new ObservableField<>();
    private Handler handler = new Handler();

    public SpeedVM(@NonNull Application application) {
        super(application);
        handler.post(() -> {
            List<Integer> bgData = new ArrayList<>();
            bgData.add(R.drawable.gewei);
            bgData.add(R.drawable.yangguanghao);
            bgData.add(R.drawable.longmao);
            speedBgRecDataOF.set(bgData);

            List<Integer> mainData = new ArrayList<>();
            mainData.add(R.drawable.s1);
            mainData.add(R.drawable.s2);
            mainData.add(R.drawable.s3);
            mainData.add(R.drawable.s4);
            mainData.add(R.drawable.s5);
            mainData.add(R.drawable.s6);
            mainData.add(R.drawable.s7);
            mainData.add(R.drawable.s8);
            mainData.add(R.drawable.s9);
            mainData.add(R.drawable.s10);
            mainData.add(R.drawable.s11);
            mainData.add(R.drawable.s12);
            mainData.add(R.drawable.s13);
            mainData.add(R.drawable.s14);
            mainData.add(R.drawable.s15);
            mainData.add(R.drawable.s16);

            speedRecDataOF.set(mainData);
        });
    }

}
