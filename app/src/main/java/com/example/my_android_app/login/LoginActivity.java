package com.example.my_android_app.login;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.my_android_app.GlobalVariable;
import com.example.my_android_app.R;
import com.example.my_android_app.MainActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "On Google Sign In";
    public static final String USER_NAME = "Default user";
    private Button btnSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    public GoogleSignInAccount account;
    public GoogleSignInOptions gso;
    private Intent loginActivity;
    private EditText userName,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = new Intent(new Intent(this, MainActivity.class));
        this.userName = findViewById(R.id.user_name);
        this.password = findViewById(R.id.password);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            mGoogleSignInClient.signOut();
        }

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGooglesignIn();
            }
        });

        btnSignIn = findViewById(R.id.btnSignin);
        //btnForgotPassword = findViewById(R.id.btnForgotPassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userName.getText().length() > 0 && password.getText().length() > 0){
                    loginActivity.putExtra(USER_NAME,userName.getText().toString());
                    GlobalVariable.userName = userName.getText().toString();
                    startActivity(loginActivity);
                    finish();
                }else{
                    //todo..
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Alert")
                            .setMessage("User name or password shouldn't be null")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });


   }

    public void onGooglesignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.w(TAG, String.valueOf(account.getDisplayName()));

            loginActivity.putExtra(USER_NAME,account.getDisplayName());
            GlobalVariable.userName = account.getDisplayName();
            startActivity(loginActivity);
            finish();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
            //todo...
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Login Alert")
                    .setMessage("signInResult:failed code=" + e.getStatusCode())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


}
