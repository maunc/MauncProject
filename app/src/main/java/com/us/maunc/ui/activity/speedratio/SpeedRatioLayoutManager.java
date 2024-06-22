package com.us.maunc.ui.activity.speedratio;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SpeedRatioLayoutManager extends LinearLayoutManager {
    //值越小阻力越大
    private double speedRatio;

    public SpeedRatioLayoutManager(Context context) {
        super(context);
    }

    public SpeedRatioLayoutManager(Context context, int orientation, boolean reverseLayout, double speedRatio) {
        super(context, orientation, reverseLayout);
        this.speedRatio = speedRatio;
    }

    public SpeedRatioLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int a = super.scrollHorizontallyBy((int) (speedRatio * dx), recycler, state);
        if (a == (int) (speedRatio * dx)) {
            return dx;
        }
        return a;
    }

    public void setSpeedRatio(double speedRatio) {
        this.speedRatio = speedRatio;
    }
}
