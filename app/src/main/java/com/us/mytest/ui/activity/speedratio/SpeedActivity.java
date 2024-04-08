package com.us.mytest.ui.activity.speedratio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldze.mvvmhabit.base.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.us.mytest.BR;
import com.us.mytest.R;
import com.us.mytest.databinding.ActivitySpeedBinding;

public class SpeedActivity extends BaseActivity<ActivitySpeedBinding, SpeedVM> {

    private SpeedBgRecAdapter speedBgRecAdapter;
    private SpeedRecAdapter speedRecAdapter;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_speed;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        ImmersionBar.with(this).transparentBar().init();
        //背景rec
        SpeedRatioLayoutManager speedRatioLayoutManager = new SpeedRatioLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false, 0.45f);
        mBinding.speedBgRec.setLayoutManager(speedRatioLayoutManager);
        speedBgRecAdapter = new SpeedBgRecAdapter();
        mBinding.speedBgRec.setAdapter(speedBgRecAdapter);

        //前置rec
        mBinding.speedDataRec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        speedRecAdapter = new SpeedRecAdapter();
        mBinding.speedDataRec.setAdapter(speedRecAdapter);
        mBinding.speedDataRec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mBinding.speedBgRec.scrollBy(dx + mBinding.speedBgRec.getScrollX(), dy);
            }
        });
    }

    @Override
    public void createObserver() {
        super.createObserver();
        mViewModel.speedBgRecDataOF.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                speedBgRecAdapter.setList(mViewModel.speedBgRecDataOF.get());
            }
        });
        mViewModel.speedRecDataOF.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                speedRecAdapter.setList(mViewModel.speedRecDataOF.get());
            }
        });
    }
}