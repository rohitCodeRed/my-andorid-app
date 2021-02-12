package com.example.my_android_app.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_android_app.R;
import com.example.my_android_app.login.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {
    private Button btnRegister, btnSignin, btnTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        btnSignin = findViewById(R.id.btnSignin);




        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });


    }
}
