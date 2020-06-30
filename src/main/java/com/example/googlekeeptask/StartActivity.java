package com.example.googlekeeptask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private ImageView mAppIcon;
    private TextView mAppName;
    private static int START_SCREEN = 3000;

    Animation appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAppIcon = findViewById(R.id.iv_app_icon);
        mAppName = findViewById(R.id.tv_app_name);

        appName = AnimationUtils.loadAnimation(this,R.anim.app_name);
        mAppName.setAnimation(appName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this,MainActivity.class));
                finish();
            }
        },START_SCREEN);

    }
}