package com.us.mytest.ui.activity.more_rec.bean;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.List;

/**
 * ClsFunction：
 * CreateDate：2024/3/27
 * Author：TimeWillRememberUs
 */
public class MoreOneBean extends BaseExpandNode {

    private String name;
    private List<BaseNode> nodeList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return nodeList;
    }
}
