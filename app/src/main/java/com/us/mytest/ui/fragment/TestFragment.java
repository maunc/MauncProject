package com.us.mytest.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.goldze.mvvmhabit.base.BaseFragment;
import com.us.mytest.BR;
import com.us.mytest.R;
import com.us.mytest.databinding.FragmentTestBinding;

/**
 * ClsFunction：
 * CreateDate：2024/3/21
 * Author：TimeWillRememberUs
 */
public class TestFragment extends BaseFragment<FragmentTestBinding, TestFremgntVM> {

    private int num = 0;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_test;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        mBinding.testFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.numOI.set(++num);
                mBinding.testFragment.setText("TestFragment" + mViewModel.numOI.get());
            }
        });
    }
}
