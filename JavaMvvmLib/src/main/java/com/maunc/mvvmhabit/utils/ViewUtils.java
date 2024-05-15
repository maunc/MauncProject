package com.maunc.mvvmhabit.utils;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.maunc.mvvmhabit.BaseApp;

/**
 * ClsFunction：
 * CreateDate：2024/5/15
 * Author：TimeWillRememberUs
 */
public class ViewUtils {

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void setViewBackgroundAll(@DrawableRes int drawableRes, View... views) {
        if (views == null) {
            return;
        }
        try {
            for (View view : views) {
                view.setBackground(BaseApp.getInstance().getResources().getDrawable(drawableRes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
