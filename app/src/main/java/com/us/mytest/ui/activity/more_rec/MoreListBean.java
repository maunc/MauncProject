package com.us.mytest.ui.activity.more_rec;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import java.util.List;

/**
 * ClsFunction：
 * CreateDate：2024/3/25
 * Author：TimeWillRememberUs
 */
public class MoreListBean {

    private String name;
    private List<MoreListBean> childrenList;

    public MoreListBean() {
    }

    public MoreListBean(String name) {
        this.name = name;
    }

    public MoreListBean(String name, List<MoreListBean> childrenList) {
        this.name = name;
        this.childrenList = childrenList;
    }

    public List<MoreListBean> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<MoreListBean> childrenList) {
        this.childrenList = childrenList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
