package com.example.my_android_app.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.my_android_app.R;
import com.example.my_android_app.MainActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class LoginActivity extends AppCompatActivity {
    private Button btnSignIn, btnForgotPassword;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = findViewById(R.id.btnSignin);
        //btnForgotPassword = findViewById(R.id.btnForgotPassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

//        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
//            }
//        });

        // Facebook
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
        new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
                Profile profile = Profile.getCurrentProfile();
                // Log.d("FACEBOOK", "PROFILE" + profile.getFirstName());
                if (enableButtons && profile != null) {
                    // profilePictureView.setProfileId(profile.getId());
                    // greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
                } else {
                    // profilePictureView.setProfileId(null);
                    // greeting.setText(null);
                }
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK", "CANCELLED");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("FACEBOOK", "ERROR: " + exception.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
