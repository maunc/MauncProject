package com.maunc.jetpackmvvm.ext

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

private const val TAG = "FragmentExt"

fun FragmentManager.showFragment(id: Int, fragment: Fragment) {
    if (fragment.isAdded && !fragment.isHidden) {
        Log.d(TAG, "changeFragment: 目标fragment已处于展示状态")
        return
    }
    val transaction = beginTransaction()
    for (item in fragments) {
        if (!item.isHidden) {
            Log.d(TAG, "changeFragment: ${item.javaClass.simpleName}进行隐藏")
            transaction.hide(item)
        }
    }
    val tag = fragment.javaClass.simpleName
    val toFragment = findFragmentByTag(tag) ?: fragment
    if (toFragment.isAdded) {
        Log.d(TAG, "changeFragment: 目标fragment展示")
        transaction.show(toFragment)
    } else {
        Log.d(TAG, "changeFragment: 目标fragment添加并展示")
        transaction.add(id, toFragment, tag)
    }
    transaction.commit()
}