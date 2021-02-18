package com.example.my_android_app.jsonreadandreturnlist;

import com.example.my_android_app.transactionadapter.TransactionInfo;
import java.util.List;



/*------------------RecordTransactionList--------------*/
//It keep the records of updated list in static variable transactionList and loginData.
//'loginData' have the login information for static login card.
//'transactionList' have the list of data related to brand name.

public class RecordTransactionList {
    public static List<TransactionInfo> transactionList;
    public static List<TransactionInfo> loginData;

    public void updateTransactionInfoList(List<TransactionInfo> list){
        transactionList = list;
    }

    public void updateLoginList(List<TransactionInfo> list){
        loginData = list;
    }

    public List<TransactionInfo> getOriginalTransactionList(){
        return transactionList;
    }

    public List<TransactionInfo> getLoginInfo(){ return loginData; }


    private Boolean isParameterValMatch(String queryVal, String listVal){
        return listVal.contains(queryVal);
    }

    public void appendList(List<TransactionInfo> appendlist){
        transactionList.addAll(appendlist);
    }

    public void appendSingleTransactionInfo(TransactionInfo r){
        transactionList.add(r);
    }

    public int getSizeOfList(){
        return transactionList.size();
    }

    public TransactionInfo getTransactionInfo(int index){
        return transactionList.get(index);
    }

}
