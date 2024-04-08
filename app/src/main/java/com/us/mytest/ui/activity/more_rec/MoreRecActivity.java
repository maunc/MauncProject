package com.us.mytest.ui.activity.more_rec;

import android.os.Bundle;

import com.goldze.mvvmhabit.base.BaseActivity;
import com.us.mytest.BR;
import com.us.mytest.R;
import com.us.mytest.databinding.ActivityMoreRecBinding;

public class MoreRecActivity extends BaseActivity<ActivityMoreRecBinding, MoreRecVM> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_more_rec;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {

    }
}