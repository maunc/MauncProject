package com.us.mytest.ui.activity.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.us.mytest.R

/**
 * 可设置图片大小的TextView
 *
 * @author ZengShaoHeng
 * @date 2019/5/29
 */
class DrawableTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var leftDrawableWidth: Int
    private var leftDrawableHeight: Int
    private var rightDrawableWidth: Int
    private var rightDrawableHeight: Int
    private var topDrawableWidth: Int
    private var topDrawableHeight: Int
    private var bottomDrawableWidth: Int
    private var bottomDrawableHeight: Int
    private var leftWidth = 0
    private var rightWidth = 0 //左右图片宽度 = 0
    private var addTail = false //是否对换行符的长字符串进行...替换
    private val DRAWABLE_LEFT = 0
    private val DRAWABLE_TOP = 1
    private val DRAWABLE_RIGHT = 2
    private val DRAWABLE_BOTTOM = 3
    private var drawableRightListener: DrawableListener.DrawableRightListener? = null
    private var drawableLeftListener: DrawableListener.DrawableLeftListener? = null
    private var drawableTopListener: DrawableListener.DrawableTopListener? = null
    private var drawableBottomListener: DrawableListener.DrawableBottomListener? = null

    /**
     * 设置顶部图片
     */
    fun setLeftDrawableWithRes(context: Context, res: Int) {
        val drawable = context.resources.getDrawable(res)
        setDrawableSize(drawable, DRAWABLE_LEFT)
        //        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(drawable, null, null, null)
    }
    /**
     * 设置顶部图片
     */
    fun setTopDrawableWithRes(context: Context, res: Int) {
        val drawable = context.resources.getDrawable(res)
        setDrawableSize(drawable, DRAWABLE_TOP)
        //        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(null, drawable, null, null)
    }

    /**
     * 设置顶部图片
     */
    fun setTopDrawableWithRes(context: Context, res: Int,width:Int,height:Int) {
        val drawable = context.resources.getDrawable(res)
        topDrawableWidth = width
        topDrawableHeight = height
        setDrawableSize(drawable, DRAWABLE_TOP)
        //        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(null, drawable, null, null)
    }
    /**
     * 设置顶部图片
     */
    fun setRightDrawableWithRes(context: Context, res: Int) {
        if(res == -1) {
            setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], null, compoundDrawables[3])
            return
        }
        val drawable = context.resources.getDrawable(res)
        setDrawableSize(drawable, DRAWABLE_RIGHT)
        //        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
    }
    //设置drawableRight 图片的点击监听
    fun setDrawableRightListener(drawableRightListener: DrawableListener.DrawableRightListener?) {
        this.drawableRightListener = drawableRightListener
    }

    fun setDrawableLeftListener(drawableLeftListener: DrawableListener.DrawableLeftListener?) {
        this.drawableLeftListener = drawableLeftListener
    }

    fun setDrawableTopListener(drawableTopListener: DrawableListener.DrawableTopListener?) {
        this.drawableTopListener = drawableTopListener
    }

    fun setDrawableBottomListener(drawableBottomListener: DrawableListener.DrawableBottomListener?) {
        this.drawableBottomListener = drawableBottomListener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (drawableRightListener != null) {
                    val drawableRight = compoundDrawables[DRAWABLE_RIGHT]
                    if (drawableRight != null && event.rawX >= right - drawableRight.bounds.width() && event.rawX < right) {
                        drawableRightListener!!.drawableRightListener(this)
                        return true
                    }
                }
                if (drawableLeftListener != null) {
                    val drawableLeft = compoundDrawables[DRAWABLE_LEFT]
                    if (drawableLeft != null && event.rawX <= left + drawableLeft.bounds.width() && event.rawX > left) {
                        drawableLeftListener!!.drawableLeftListener(this)
                        return true
                    }
                }
                if (drawableTopListener != null) {
                    val drawableTop = compoundDrawables[DRAWABLE_TOP]
                    if (drawableTop != null && event.rawY <= top + drawableTop.bounds.height() && event.rawY > top) {
                        drawableTopListener!!.drawableTopListener(this)
                        return true
                    }
                }
                if (drawableBottomListener != null) {
                    val drawableBottom = compoundDrawables[DRAWABLE_BOTTOM]
                    if (drawableBottom != null && event.rawY >= bottom - drawableBottom.bounds.height() && event.rawY < bottom) {
                        drawableBottomListener!!.drawableBottomListener(this)
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    //设置图片的高度和宽度
    private fun setDrawableSize(drawable: Drawable?, index: Int) {
        if (drawable == null) {
            return
        }
        //左上右下
        var width = 0
        var height = 0
        when (index) {
            DRAWABLE_LEFT -> {
                width = leftDrawableWidth
                height = leftDrawableHeight
            }
            DRAWABLE_TOP -> {
                width = topDrawableWidth
                height = topDrawableHeight
            }
            DRAWABLE_RIGHT -> {
                width = rightDrawableWidth
                height = rightDrawableHeight
            }
            DRAWABLE_BOTTOM -> {
                width = bottomDrawableWidth
                height = bottomDrawableHeight
            }
        }
        //如果没有设置图片的高度和宽度具使用默认的图片高度和宽度
        if (width < 0) {
            width = drawable.intrinsicWidth
        }
        if (height < 0) {
            height = drawable.intrinsicHeight
        }
        if (index == 0) {
            leftWidth = width
        } else if (index == 2) {
            rightWidth = width
        }
        drawable.setBounds(0, 0, width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //是否对包含有换行符的文字，以换行符分割的句子，超过textView最大宽度的时候以“...”结尾
        if (addTail) {
            val text = text.toString()
            var textWidth = width - paddingRight - paddingLeft.toFloat()
            if (leftWidth != 0) {
                textWidth = textWidth - leftWidth - compoundDrawablePadding
            }
            if (rightWidth != 0) {
                textWidth = textWidth - rightWidth - compoundDrawablePadding
            }
            setText(changeText(text, textWidth))
        }
    }

    /**
     * 以换行符\n来分离文本，每行超过最大长度的文本以...来替换 可以少写很多的textView
     *
     * @param text      文本内容
     * @param textWidth 文本最大长度
     * @return
     */
    private fun changeText(text: String, textWidth: Float): String {
        val contents = text.split("\\n").toTypedArray()
        var contentWidth: Float
        var content: String
        for (j in contents.indices) {
            content = contents[j]
            contentWidth = this.paint.measureText(content)
            if (contentWidth > textWidth) {
                var newContent: String
                var newContentWidth: Float
                for (i in content.length downTo 0) {
                    newContent = content.substring(0, i)
                    newContentWidth = this.paint.measureText("$newContent...")
                    if (newContentWidth <= textWidth) {
                        contents[j] = "$newContent..."
                        break
                    }
                }
            }
        }
        val stringBuilder = StringBuilder()
        for (k in contents.indices) {
            if (k < contents.size - 1) {
                stringBuilder.append(contents[k] + "\n")
            } else {
                stringBuilder.append(contents[k])
            }
        }
        return stringBuilder.toString()
    }

    /**
     * Drawable 监听回调接口
     *
     *
     * Created by yinw on 2016-12-19.
     */
    class DrawableListener {
        interface DrawableRightListener {
            fun drawableRightListener(view: View?)
        }

        interface DrawableLeftListener {
            fun drawableLeftListener(view: View?)
        }

        interface DrawableTopListener {
            fun drawableTopListener(view: View?)
        }

        interface DrawableBottomListener {
            fun drawableBottomListener(view: View?)
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView, defStyleAttr, 0)
        leftDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_leftDrawableHeight,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1f, resources.displayMetrics).toInt())
        leftDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_leftDrawableWidth,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1f, resources.displayMetrics).toInt())
        rightDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_rightDrawableHeight,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1f, resources.displayMetrics).toInt())
        rightDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_rightDrawableWidth,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1f, resources.displayMetrics).toInt())
        topDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_topDrawableHeight,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1f, resources.displayMetrics).toInt())
        topDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_topDrawableWidth,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1f, resources.displayMetrics).toInt())
        bottomDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_bottomDrawableHeight,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1f, resources.displayMetrics).toInt())
        bottomDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_bottomDrawableWidth,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -1f, resources.displayMetrics).toInt())
        addTail = typedArray.getBoolean(R.styleable.DrawableTextView_addTail, false)
        typedArray.recycle()
        val drawables = compoundDrawables
        for (i in drawables.indices) {
            setDrawableSize(drawables[i], i)
        }
        //放置图片
        setCompoundDrawables(drawables[DRAWABLE_LEFT], drawables[DRAWABLE_TOP], drawables[DRAWABLE_RIGHT], drawables[DRAWABLE_BOTTOM])
    }
}