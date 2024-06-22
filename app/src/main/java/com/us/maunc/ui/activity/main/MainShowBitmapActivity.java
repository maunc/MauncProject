package com.us.maunc.ui.activity.main;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.us.mauncview.ZoomImageView;
import com.us.maunc.R;

public class MainShowBitmapActivity extends AppCompatActivity {

    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_show_bitmap);
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR)
                .transparentBar().init();
        ZoomImageView imageView = findViewById(R.id.test_image);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        imageView.setOnClickListener(view -> finishTest());
    }

    private void finishTest() {
        finish();
        overridePendingTransition(R.anim.main_enter, R.anim.bitmap_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishTest();
    }
}