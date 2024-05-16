package com.us.mytest.ui.activity.pushbox

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView

/**
 *ClsFunction：
 *CreateDate：2024/5/16
 *Author：TimeWillRememberUs
 */
class PushBoxScaleView @JvmOverloads constructor(context: Context, attrs: AttributeSet?) :
    AppCompatImageView(context, attrs) {

    private lateinit var animEnter: Animator
    private lateinit var animExit: Animator
    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var mX: Float = 0f
    private var mY: Float = 0f
    private val mHandler: Handler = Handler(Looper.myLooper()!!)
    private lateinit var mClickListener: PushBoxScaleOnClickListener

    init {
        val ofFloat1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.9f)
        val ofFloat2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.9f)
        animEnter = ObjectAnimator.ofPropertyValuesHolder(this, ofFloat1, ofFloat2)
        val ofFloat3 = PropertyValuesHolder.ofFloat("scaleX", 0.9f, 1f)
        val ofFloat4 = PropertyValuesHolder.ofFloat("scaleY", 0.9f, 1f)
        animExit = ObjectAnimator.ofPropertyValuesHolder(this, ofFloat3, ofFloat4)
        animEnter.duration = 200
        animExit.duration = 200
        animEnter.setInterpolator {
            LinearInterpolator().getInterpolation(it)
        }
        animExit.setInterpolator {
            LinearInterpolator().getInterpolation(it)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = height - paddingTop - paddingBottom
        mWidth = width - paddingLeft - paddingRight
        mX = x
        mY = y
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mHandler.post {
                    animExit.end()
                    animEnter.start()
                }
            }

            MotionEvent.ACTION_UP -> {
                mHandler.post {
                    animEnter.end()
                    animExit.start()
                }
                mClickListener.onClick()
            }
        }
        return true
    }

    fun setClickListener(listener: PushBoxScaleOnClickListener) {
        mClickListener = listener
    }

    interface PushBoxScaleOnClickListener {
        fun onClick()
    }
}