package com.example.my_android_app;


import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DashboardActivity extends AppCompatActivity {
    public static final String TAG = "MyApp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);


        Intent intent = getIntent();
        String message = "AccountType: "+intent.getStringExtra(MainActivity.ACCOUNT_TYPE)+
                            "\nAccountId: "+intent.getStringExtra(MainActivity.ACCOUNT_ID)+
                            "\nMobileNumber: "+intent.getStringExtra(MainActivity.MOBILE_NUMBER)+
                            "\nEmailId: "+intent.getStringExtra(MainActivity.EMAIL_ID);
        TextView textView = findViewById(R.id.textViewDash);
        textView.setText(message);
    }


}