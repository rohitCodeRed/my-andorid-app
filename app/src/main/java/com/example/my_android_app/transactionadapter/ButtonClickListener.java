package com.example.my_android_app.transactionadapter;

import android.view.View;

public interface ButtonClickListener {

    //Tell the position and which view element is clicked frmon recycler View child items.
    void onPositionClicked(View v, int position);

    void onLongClicked(View v, int position);
}
