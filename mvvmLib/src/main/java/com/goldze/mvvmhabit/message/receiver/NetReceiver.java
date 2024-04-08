package com.goldze.mvvmhabit.message.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.goldze.mvvmhabit.message.bus.RxBus;
import com.goldze.mvvmhabit.message.status._NetStatus;
import com.goldze.mvvmhabit.utils.NetworkUtils;

/**
 * ClsFunction：是否有网
 * CreateDate：2024/3/20
 * Author：TimeWillRememberUs
 */
public class NetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RxBus.getDefault().post(new _NetStatus(NetworkUtils.isNetworkAvailable()));
    }
}
