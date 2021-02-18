package com.example.my_android_app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_android_app.animationView.PageIndicatorView;

public class CustomViewActivity extends AppCompatActivity {


    private PageIndicatorView pgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        pgView = findViewById(R.id.rotatingView);
        pgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgView.startAnimation();
            }
        });

    }
}
