package com.us.maunc.ui.activity.speedratio;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.us.maunc.R;

/**
 * ClsFunction：
 * CreateDate：2024/3/24
 * Author：TimeWillRememberUs
 */
public class SpeedBgRecAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public SpeedBgRecAdapter() {
        super(R.layout.item_speed_bg);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Integer integer) {
        baseViewHolder.setImageResource(R.id.item_speed_bgImg, integer);
    }
}
