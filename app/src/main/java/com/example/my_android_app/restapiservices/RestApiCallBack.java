package com.example.my_android_app.restapiservices;

import com.example.my_android_app.transactionadapter.TransactionInfo;

import java.util.List;

public interface RestApiCallBack {

    //Call when background process is completed.
    void onCompleteProcess(List<TransactionInfo> result);

}
