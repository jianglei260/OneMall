package com.myworld.onemall.splash;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.myworld.onemall.R;
import com.myworld.onemall.base.Config;
import com.myworld.onemall.main.MainActivity;
import com.myworld.onemall.upgrade.AppUpdateTool;

public class SplashActivity extends AppCompatActivity {
    public static final int DELAY_TIME = 1000;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textView = (TextView) findViewById(R.id.text);
        ObjectAnimator up = ObjectAnimator.ofFloat(textView, "translationY", 360, 0).setDuration(DELAY_TIME);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(textView, "alpha", 0, 1).setDuration(DELAY_TIME);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alpha).with(up);
        animatorSet.start();
        checkPermission();
    }

    public void start() {
        if (isAuto())
            checkUpdate();
        realStart();
    }

    public void realStart() {

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME + 500);
    }

    public boolean isAuto() {
        SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean auto = preferences.getBoolean("auto_upgrade", true);
        return auto;
    }

    public void checkUpdate() {
        AppUpdateTool updateTool = new AppUpdateTool.Builder(this).setAutoUpdate(true).setRequestUrl(Config.UPDATE_URL).build();
        updateTool.checkUpdate();
    }

    public void checkPermission() {
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storagePermission == PackageManager.PERMISSION_GRANTED) {
            start();
        } else {
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        checkPermission();
    }

}
