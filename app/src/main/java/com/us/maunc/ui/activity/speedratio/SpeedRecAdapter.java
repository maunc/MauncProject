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
public class SpeedRecAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public SpeedRecAdapter() {
        super(R.layout.item_speed);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Integer integer) {
        baseViewHolder.setImageResource(R.id.item_speed_dataImg, integer);
    }
}
