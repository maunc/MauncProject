package com.goldze.mvvmhabit.message.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.goldze.mvvmhabit.message.bus.RxBus;
import com.goldze.mvvmhabit.message.status._ScreenStatus;

/**
 * ClsFunction：是否息屏
 * CreateDate：2024/3/20
 * Author：TimeWillRememberUs
 */
public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            RxBus.getDefault().post(new _ScreenStatus(true));
        } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
            RxBus.getDefault().post(new _ScreenStatus(false));
        }
    }
}
