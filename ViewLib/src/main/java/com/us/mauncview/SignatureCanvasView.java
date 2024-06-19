package com.us.mauncview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ClsFunction：
 * CreateDate：2024/6/19
 * Author：TimeWillRememberUs
 */
public class SignatureCanvasView extends View {

    private int widthSize;
    private int heightSize;
    private Bitmap bitmap;//整个画板显示的位图
    private final Paint paint = new Paint();//画板的画笔
    private final Paint eraserPaint = new Paint();
    private final Canvas canvas = new Canvas();//画板的画布
    private float xTouch = 0;
    private float yTouch = 0;
    private boolean isPen = true;

    public SignatureCanvasView(Context context) {
        super(context);
        initData();
    }

    public SignatureCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public SignatureCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        //设置为可点击才能获取到MotionEvent.ACTION_MOVE
        setClickable(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(7);
        //设置是否使用抗锯齿功能，抗锯齿功能会消耗较大资源，绘制图形的速度会减慢
        paint.setAntiAlias(true);
        //设置是否使用图像抖动处理，会使图像颜色更加平滑饱满，更加清晰
        paint.setDither(true);

        eraserPaint.setColor(Color.rgb(220, 220, 220));
        eraserPaint.setStyle(Paint.Style.STROKE);
        eraserPaint.setStrokeWidth(40);
        eraserPaint.setAntiAlias(true);
        eraserPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("SignatureCanvasView", "onMeasure widthMeasureSpec=" + widthMeasureSpec +
                " heightMeasureSpec=" + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.d("SignatureCanvasView", "onMeasure widthSize=" + widthSize +
                " heightSize=" + heightSize +
                ",widthMode=" + widthMode +
                ",heightMode=" + heightMode);
        initBitmap();
    }

    private void initBitmap() {
        if (null != bitmap) {
            bitmap.recycle();
        }
        bitmap = Bitmap.createBitmap(widthSize, heightSize, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(220, 220, 220));
        canvas.setBitmap(bitmap);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xTouch = event.getX();
                yTouch = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                canvas.drawLine(xTouch, yTouch, event.getX(), event.getY(), isPen ? paint : eraserPaint);
                xTouch = event.getX();
                yTouch = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取画好的电子签名
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * 清除电子签名
     */
    public void clear() {
        initBitmap();
        invalidate();
    }

    public void setPenMode() {
        isPen = true;
    }

    public void setEraserMode() {
        isPen = false;
    }
}
