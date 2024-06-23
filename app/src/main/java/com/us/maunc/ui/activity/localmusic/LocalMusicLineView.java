package com.us.maunc.ui.activity.localmusic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LocalMusicLineView extends View {

    private Paint mPaint;//所需要的画笔
    private float mWidth; //画板的宽度
    private float mRectWidth;  //统一绘制宽度  不变
    private float mRectHeight;  //统一绘制高度  不断改变是×随机数改变
    private float mRectCount = 5; //需要的个数
    private int offsets = 1; //两个条之间的间距
    private LinearGradient mLinearGradient;
    private double mRandom;

    public LocalMusicLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public LocalMusicLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LocalMusicLineView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mRectHeight = getHeight();
        //mWidth 绘制宽度 占总宽的十分之六  (总宽/条数 = 每一个条的宽度)
        mRectWidth = (int) (mWidth * 0.6 / mRectCount);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mRectCount; i++) {
            mRandom = Math.random();
            float currentHeight = (float) (mRectHeight * mRandom);
            canvas.drawRect(
                    (float) (mRectWidth * i + offsets), //左
                    currentHeight,  //上
                    (float) (mRectWidth * (i + 1)),  //右
                    mRectHeight,  //下
                    mPaint);
        }
        postInvalidateDelayed(180);
    }
}
