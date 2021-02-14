package com.example.my_android_app.jsonreadandreturnlist;

import com.example.my_android_app.transactionadapter.TransactionInfo;

import java.util.ArrayList;
import java.util.List;



/*------------------RecordTransactionList--------------*/
//It keep the records of updated list in static variable transactionList and loginData.
//'loginData' have the login information for static login card.
//'transactionList' have the list of data related to brand name.

public class RecordTransactionList {
    private static List<TransactionInfo> transactionList;
    private static List<TransactionInfo> loginData;

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



    //Return the filtered data based upon filter "AccountType"

    public List<TransactionInfo> getFilteredTransactionList(String field, String value){
        int length = transactionList.size();
        List<TransactionInfo> filteredList = new ArrayList<>();

        for(int i=0;i< length;i++){
            Boolean isMatched = false;
            TransactionInfo info = transactionList.get(i);
            switch(field){
                case "ACCOUNT_TYPE":
                        isMatched = isParameterValMatch(value,info.accountType);
                    break;
                case "ACCOUNT_NAME":
                    isMatched = isParameterValMatch(value,info.accountType);
                    break;
            }

            if(isMatched){
                filteredList.add(info);
            }

        }


        return filteredList;


    }

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

    public TransactionInfo getLoginInfoObject(){
        return loginData.get(0);
    }
}
