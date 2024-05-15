package com.us.mytest.ui.activity.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.maunc.mvvmhabit.base.BaseActivity;
import com.maunc.mvvmhabit.utils.AppUtils;
import com.maunc.mvvmhabit.utils.ConvertUtils;
import com.maunc.mvvmhabit.utils.DeviceUtils;
import com.maunc.mvvmhabit.utils.LogUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gyf.immersionbar.ImmersionBar;
import com.us.mytest.BR;
import com.us.mytest.R;
import com.us.mytest.databinding.ActivityMainBinding;

/**
 * 滑动示例
 */
public class MainActivity extends BaseActivity<ActivityMainBinding, MainVM> {

    private MainRecAdapter mainRecAdapter;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @SuppressLint("CheckResult")
    @Override
    public void initData() {

        LogUtils.e("X:" + AppUtils.getDimens(R.dimen.x50));
        LogUtils.e("Y:" + AppUtils.getDimens(R.dimen.y50));
        initView();
        ImmersionBar.with(this).statusBarDarkFont(true)
                .transparentBar().init();
        BottomSheetBehavior<NestedScrollView> sheetBehavior =
                BottomSheetBehavior.from(mBinding.testNestedView);
        //是否可以完全隐藏
        sheetBehavior.setHideable(false);
        //设置为协调状态
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //缩到最短露头的高度
        sheetBehavior.setPeekHeight(360);
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //如果不设置模式带上状态栏的高度
                //减去的这个值越小 bottomSheet的高度就越高，要控制高度合法
                int targetBottomHeight = DeviceUtils.getDeviceHeight(false) - 50;
                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams.height != targetBottomHeight) {
                    layoutParams.height = targetBottomHeight;
                    bottomSheet.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                LogUtils.e("slideOffset:", slideOffset);
                mBinding.backMainRl.setAlpha(Math.max(-(slideOffset - 1), 0.66f));
                mBinding.backMainRl.setTranslationY(-(450 * slideOffset));
            }
        });
    }

    private void initView() {
        mBinding.myRec.setLayoutManager(new LinearLayoutManager(this));
        mainRecAdapter = new MainRecAdapter();
        mBinding.myRec.setAdapter(mainRecAdapter);
        mBinding.mybgShow.setOnClickListener(view -> {
            MainShowBitmapActivity.bitmap = ConvertUtils.drawable2Bitmap(mBinding.mybgShow.getDrawable());
            startActivity(new Intent(MainActivity.this, MainShowBitmapActivity.class));
            overridePendingTransition(R.anim.bitmap_enter, R.anim.main_exit);
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void createObserver() {
        super.createObserver();
        mViewModel.mainRecDataOF.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mainRecAdapter.setList(mViewModel.mainRecDataOF.get());
            }
        });
    }
}