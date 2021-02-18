package com.example.my_android_app;


import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SelectedTransactionInfo extends AppCompatActivity {
    public static final String TAG = "MyApp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_transaction_layout);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Transaction Info");


        Intent intent = getIntent();
        String message =
                            "\nAccount Name: "+intent.getStringExtra(TransactionList.ACCOUNT_NAME)+
                            "\nTotal Amount: "+intent.getStringExtra(TransactionList.TOTAL_AMOUNT);
        TextView textView = findViewById(R.id.textViewDash);
        textView.setText(message);
    }


}