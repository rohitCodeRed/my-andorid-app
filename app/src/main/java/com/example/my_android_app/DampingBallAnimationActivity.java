package com.example.my_android_app;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_android_app.animationView.DampingBallAnimation;

public class DampingBallAnimationActivity extends AppCompatActivity {
    private static final String TAG="my aap";
    private DampingBallAnimation dpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damping_ball);
        dpView = findViewById(R.id.rotatingView);
        DisplayMetrics metrics = new DisplayMetrics();
        int dpi = getResources().getDisplayMetrics().densityDpi;

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d(TAG, "onCreate: "+(metrics.heightPixels*160)/dpi+":"+(metrics.widthPixels*160)/dpi);
        dpView.updateMaxScreenCoordinates(getWindowManager().getDefaultDisplay(),getResources().getDisplayMetrics().density + 0.5f);

        dpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpView.startAnimation();
            }
        });

    }
}
