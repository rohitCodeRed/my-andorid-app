package com.example.my_android_app.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.ActionBar;

import com.example.my_android_app.R;
import com.example.my_android_app.BaseActivity;
import com.example.my_android_app.login.LoginActivity;

public class SplashActivity extends BaseActivity {
   private static final int SPLASH_DELAY = 2000;
   private static final int UI_ANIMATION_DELAY = 300;
   private final Handler mHandler = new Handler(Looper.getMainLooper());
   private final Launcher mLauncher = new Launcher();
   private View mContentView, mControlView;

   private final Runnable mHidePartRunnable = new Runnable() {
       // Delayed removal of status and navigation bar

       @Override
       public void run() {
           getWindow().setDecorFitsSystemWindows(false);
       }
   };

   private final Runnable mShowPartRunnable = new Runnable() {
        // Delayed display of UI elements

        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlView.setVisibility(View.VISIBLE);
        }
   };

   private final Runnable mHideRunnable = new Runnable() {
       @Override
       public void run() {
           hide();
       }
   };

   @Override
   protected void onStart() {
       super.onStart();

       mHandler.postDelayed(mLauncher, SPLASH_DELAY);
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_splash);

       mControlView = findViewById(R.id.fullscreen_content_controls);
       mContentView = findViewById(R.id.fullscreen_content);
       getWindow().setDecorFitsSystemWindows(false);

       mHandler.removeCallbacks(mHidePartRunnable);
       mHandler.postDelayed(mShowPartRunnable, SPLASH_DELAY);
   }

   @Override
   protected void onPostCreate(Bundle savedInstanceState) {
       super.onPostCreate(savedInstanceState);

       // Trigger the initial hide() shortly after the activity has been created
       delayedHide(0);
   }

   @Override
   protected void onStop() {
       mHandler.removeCallbacks(mLauncher);
       super.onStop();
   }

   private void launch() {
       if (!isFinishing()) {
           startActivity(new Intent(this, LoginActivity.class));
           finish();
       }
   }

   private void hide() {
       ActionBar actionBar = getSupportActionBar();
       if (actionBar != null) {
           actionBar.hide();
       }
       mControlView.setVisibility(View.GONE);

       mHandler.removeCallbacks(mShowPartRunnable);
       mHandler.postDelayed(mHidePartRunnable, UI_ANIMATION_DELAY);
   }

   private void delayedHide(int delayMillis) {
       mHandler.removeCallbacks(mHideRunnable);
       mHandler.postDelayed(mHideRunnable, delayMillis);
   }

   private class Launcher implements Runnable {
       @Override
       public void run() {
           launch();
       }
   }
}
