package com.us.mytest.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.us.mytest.R;
import com.us.mytest.ui.activity.main.MainActivity;
import com.us.mytest.ui.activity.speedratio.SpeedActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViewById(R.id.start_main).setOnClickListener(view -> startActivity(
                new Intent(WelcomeActivity.this, MainActivity.class)));

        findViewById(R.id.start_speed).setOnClickListener(view -> startActivity(
                new Intent(WelcomeActivity.this, SpeedActivity.class)));
    }
}