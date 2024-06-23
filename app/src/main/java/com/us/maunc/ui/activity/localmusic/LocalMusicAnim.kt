package com.us.maunc.ui.activity.localmusic

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 *ClsFunction：
 *CreateDate：2024/6/23
 *Author：TimeWillRememberUs
 */
object LocalMusicAnim {

    private var rotationAnim: ValueAnimator? = null

    fun initRotateAnim(view: View, duration: Long = 5000) {
        val rotationHolder = PropertyValuesHolder.ofFloat("rotation", 0f, 360f)
        rotationAnim = ObjectAnimator.ofPropertyValuesHolder(view, rotationHolder)
        rotationAnim?.duration = duration
        rotationAnim?.repeatCount = ObjectAnimator.INFINITE
        rotationAnim?.repeatMode = ObjectAnimator.RESTART
        rotationAnim?.interpolator = LinearInterpolator()
    }

    fun refreshRotateAnim(flag: Boolean) {
        if (flag) {
            if (rotationAnim!!.isPaused) {
                rotationAnim?.resume()
            } else {
                rotationAnim?.start()
            }
        } else {
            if (rotationAnim?.isRunning == true) {
                rotationAnim?.pause()
            }
        }
    }

    fun forceRotateAnim() {
        rotationAnim?.cancel()
    }
}