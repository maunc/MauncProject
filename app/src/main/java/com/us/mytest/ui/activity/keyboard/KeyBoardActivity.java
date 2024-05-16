package com.us.mytest.ui.activity.keyboard;

import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import com.maunc.mvvmhabit.base.BaseActivity;
import com.us.mytest.BR;
import com.us.mytest.R;
import com.us.mytest.databinding.ActivityKeyBoardBinding;

/**
 * 键盘
 */
public class KeyBoardActivity extends BaseActivity<ActivityKeyBoardBinding, KeyBoardVM> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_key_board;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        ImmersionBar.with(this).statusBarColor(R.color.white)
                .statusBarDarkFont(true).init();
        mBinding.virtualKeyboardView.changeShowEditText(this, mBinding.textAmount);
        mBinding.textAmount.setOnClickListener(v -> {
            mBinding.virtualKeyboardView.showEditText();
        });
    }

    @Override
    public void onBackPressed() {
        if (mBinding.virtualKeyboardView.isShow()) {
            mBinding.virtualKeyboardView.closeEditText();
        } else {
            super.onBackPressed();
        }
    }
}