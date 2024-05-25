package com.maunc.jetpackmvvm.ext

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field

/**
 * 设置view显示
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * 设置view占位隐藏
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 设置view隐藏
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrGone(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}

/**
 * 降级ViewPager2灵敏度
 */
fun ViewPager2.desensitization() {
    try {
        val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView
        val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * 4) //6 is empirical value
    } catch (ignore: Exception) {
        ignore.printStackTrace()
    }
}

/**
 * 优化输入框
 */
fun EditText.afterTextChange(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}

/**
 * 获取文本
 */
fun EditText.textString(): String {
    return this.text.toString()
}

/**
 * 获取去除空字符串的文本
 */
fun EditText.textStringTrim(): String {
    return this.textString().trim()
}

/**
 * 文本是否为空
 */
fun EditText.isEmpty(): Boolean {
    return this.textString().isEmpty()
}

/**
 * 去空字符串后文本是否为空
 */
fun EditText.isTrimEmpty(): Boolean {
    return this.textStringTrim().isEmpty()
}

fun FragmentManager.showFragment(id: Int, fragment: Fragment) {
    if (fragment.isAdded && !fragment.isHidden) {
        Log.d("FragmentExt", "changeFragment: 目标fragment已处于展示状态")
        return
    }
    val transaction = beginTransaction()
    for (item in fragments) {
        if (!item.isHidden) {
            Log.d("FragmentExt", "changeFragment: ${item.javaClass.simpleName}进行隐藏")
            transaction.hide(item)
        }
    }
    val tag = fragment.javaClass.simpleName
    val toFragment = findFragmentByTag(tag) ?: fragment
    if (toFragment.isAdded) {
        Log.d("FragmentExt", "changeFragment: 目标fragment展示")
        transaction.show(toFragment)
    } else {
        Log.d("FragmentExt", "changeFragment: 目标fragment添加并展示")
        transaction.add(id, toFragment, tag)
    }
    transaction.commit()
}
