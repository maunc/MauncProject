package com.us.mytest.ui.activity.more_rec;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.List;

/**
 * ClsFunction：
 * CreateDate：2024/3/25
 * Author：TimeWillRememberUs
 */
public class MoreRecAdapter extends BaseNodeAdapter {

    public MoreRecAdapter() {
        addNodeProvider(new MoreOneProvider());
        addNodeProvider(new MoreTwoProvider());
    }

    @Override
    protected int getItemType(@NonNull List<? extends BaseNode> list, int i) {
        return 0;
    }
}
