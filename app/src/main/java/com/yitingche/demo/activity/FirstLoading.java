package com.yitingche.demo.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.yitingche.demo.R;

public class FirstLoading extends MyBaseActivity {

    private static final String TAG = "FirstLoading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_loading);

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                setCharLayout();
            }
        });

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(FirstLoading.this,MainActivity.class));
                finish();
            }
        },1000);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setCharLayout() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float widthPixels = metrics.widthPixels;
        float heightPixels = metrics.heightPixels;
        ViewGroup view = (ViewGroup) findViewById(R.id.id_r_layout_char_p_container);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.topMargin = (int) (heightPixels * (380/1280f));

        params.height = (int) (heightPixels * (168f / 1280f));

        View imageView = findViewById(R.id.id_imageView_char_p);
        imageView.getLayoutParams().width = (int) (widthPixels * (118f / 720f));
        imageView.getLayoutParams().height = (int) (heightPixels * (168f / 1280f));
    }
}
