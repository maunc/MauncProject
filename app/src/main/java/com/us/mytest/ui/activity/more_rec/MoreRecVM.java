package com.us.mytest.ui.activity.more_rec;

import android.app.Application;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.goldze.mvvmhabit.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ClsFunction：
 * CreateDate：2024/3/25
 * Author：TimeWillRememberUs
 */
public class MoreRecVM extends BaseViewModel {

    public MoreRecVM(@NonNull Application application) {
        super(application);
        //模拟数据
        List<MoreListBean> rootList = new ArrayList<>();
        rootList.add(new MoreListBean());
        rootList.add(new MoreListBean());

        //一级下的  一  个列表
        List<MoreListBean> root_one_list = new ArrayList<>();
        root_one_list.add(new MoreListBean("路飞"));
        root_one_list.add(new MoreListBean("柯南"));

        //一级下的  二  个列表
        List<MoreListBean> root_two_list = new ArrayList<>();
        root_two_list.add(new MoreListBean("小樱"));
        root_two_list.add(new MoreListBean("悟空"));

        MoreListBean root1 = new MoreListBean();
        root1.setName("一级列表Item1");
        root1.setChildrenList(root_one_list);

        MoreListBean root2 = new MoreListBean();
        root2.setName("一级列表Item2");
        root2.setChildrenList(root_two_list);



        List<BaseNode> oneList = new ArrayList<>();

    }
}
