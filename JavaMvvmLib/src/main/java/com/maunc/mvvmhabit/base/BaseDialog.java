package com.maunc.mvvmhabit.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseDialog<DB extends ViewDataBinding, VM extends BaseViewModel> extends DialogFragment {

    protected DB mBinding;
    protected VM mViewModel;
    protected AppCompatActivity mActivity;

    public abstract void initView(Bundle savedInstanceState);

    public void createObserver() {

    }

    /**
     * 初始化ViewModel的id  BR.viewModel
     */
    public abstract int initVariableId();

    public abstract int dialogLayout();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, dialogLayout(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int viewModelId = initVariableId();
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) createViewModel(mActivity, modelClass);
        }
        //关联ViewModel
        mBinding.setVariable(viewModelId, mViewModel);
        //支持LiveData绑定xml，数据改变，UI自动会更新
        mBinding.setLifecycleOwner(this);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(mViewModel);
        initView(savedInstanceState);
        createObserver();
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }

    @NonNull
    private <T extends ViewModel> T createViewModel(AppCompatActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }
}
