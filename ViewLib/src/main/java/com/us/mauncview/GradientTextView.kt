package com.us.mauncview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView

/**
 *ClsFunction：渐变TextView
 *CreateDate：2024/6/18
 *Author：TimeWillRememberUs
 */
@SuppressLint("Recycle", "ResourceAsColor")
class GradientTextView(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {

    companion object {
        private const val TAG = "GradientTextView"
    }

    @ColorInt
    private var defaultColor: Int = R.color.black

    @ColorInt
    private var startColor: Int = R.color.black

    @ColorInt
    private var endColor: Int = R.color.black

    private var directionType: Int
    private var gradientStartX = 0f
    private var gradientStartY = 0f
    private var gradientEndX = 0f
    private var gradientEndY = 0f

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.GradientTextView)
        startColor =
            typedArray.getColor(R.styleable.GradientTextView_gradient_startColor, defaultColor)
        endColor = typedArray.getColor(R.styleable.GradientTextView_gradient_endColor, defaultColor)
        directionType =
            typedArray.getInt(R.styleable.GradientTextView_gradient_direction, 0)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        when (directionType) {
            0 -> { //horizontal
                gradientEndX = paint.textSize * text.length
                gradientEndY = 0f
            }

            1 -> { //vertical
                gradientEndX = 0f
                gradientEndY = measuredHeight.toFloat()
            }
        }
        paint.shader = LinearGradient(
            gradientStartX,
            gradientStartY,
            gradientEndX,
            gradientEndY,
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )
    }
}