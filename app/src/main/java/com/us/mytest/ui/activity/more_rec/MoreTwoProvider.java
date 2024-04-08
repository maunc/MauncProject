package com.us.mytest.ui.activity.more_rec;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.us.mytest.R;

/**
 * ClsFunction：
 * CreateDate：2024/3/25
 * Author：TimeWillRememberUs
 */
public class MoreTwoProvider extends BaseNodeProvider {
    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_provider_two;
    }

    @Override
    public void convert(@NonNull BaseViewHolder baseViewHolder, BaseNode baseNode) {

    }
}
