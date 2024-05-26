package com.maunc.jetpackmvvm.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.maunc.jetpackmvvm.BaseApp

/**
 *ClsFunction：
 *CreateDate：2024/5/26
 *Author：TimeWillRememberUs
 */
object AppUtils {

    fun getDimens(@DimenRes dimens: Int): Int {
        return try {
            BaseApp.instance.resources.getDimensionPixelOffset(dimens)
        } catch (e: Resources.NotFoundException) {
            0
        }
    }

    fun getDrawable(@DrawableRes drawable: Int): Drawable? {
        return try {
            ResourcesCompat.getDrawable(BaseApp.instance.resources, drawable, null)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }

    fun getString(@StringRes string: Int): String {
        return try {
            BaseApp.instance.getString(string)
        } catch (e: Resources.NotFoundException) {
            ""
        }
    }
}