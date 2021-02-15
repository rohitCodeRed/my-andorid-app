package com.example.my_android_app;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class TransactionList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        initializeToolbar();
    }
    //    /*-----------Initialize Toolbar--------------*/
    private void initializeToolbar(){

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Transaction List");

    }



}
