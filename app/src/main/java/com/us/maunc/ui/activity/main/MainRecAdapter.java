package com.us.maunc.ui.activity.main;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.us.maunc.R;

/**
 * ClsFunction：
 * CreateDate：2024/3/24
 * Author：TimeWillRememberUs
 */
public class MainRecAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MainRecAdapter() {
        super(R.layout.item_tv);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setGone(R.id.item_toolbar, getItemPosition(s) != 0);
    }
}
