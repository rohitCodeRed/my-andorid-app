package com.example.my_android_app.jsonreadandreturnlist;

import android.util.Log;

import com.example.my_android_app.transactionadapter.TransactionInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReadJsonAndReturnList {

    private static final String TAG = "MyApp";
    //Basically it return the list of TransactionInfo object for recycler view, which created from fetching json data.

    public List<TransactionInfo> getTransactionListForRecyclerView(JSONObject json){
        List<TransactionInfo> result = new ArrayList<>();



        try{
            JSONObject userAccount = json.getJSONObject("UserAccount");
            Log.d(TAG, "getTransactionListForRecyclerView: " + userAccount);
            //current Index
            int currentIndex = json.getInt("CurrentIndex");

            for(int i= currentIndex;i<(userAccount.length() + currentIndex);i++){
                TransactionInfo ci = new TransactionInfo();
                JSONObject userAccountChild = userAccount.getJSONObject(Integer.toString(i));
                Log.d(TAG, "getTransactionListForRecyclerView: json list............."+userAccountChild);

                ci.brandType = userAccountChild.getString("BrandType");
                ci.accountType= userAccountChild.getString("AccountType");
                ci.accountName = userAccountChild.getString("AccountName");

                String acctType = userAccountChild.getString("AccountType");

                if(Objects.equals(acctType,"Postpaid")){
                    ci.paymentDue = userAccountChild.getString("PaymentDue");
                    ci.totalAmount= userAccountChild.getInt("TotalAmount");
                }
                else if(Objects.equals(acctType, "Prepaid")){
                    ci.totalAmount= userAccountChild.getInt("TotalAmount");
                }
                //ci.ACCOUNT_ID = userAccountChild.getInt("AccountId");
                //ci.accountEmailId = userAccountChild.getString("EmailAddress");
                //ci.accountMobileNumber = userAccountChild.getString("MobileNumber");
               // userAccountChild = null;
                result.add(ci);

                //ci.accountMobileNumber = userAccountChild.getString("MobileNumber");
                //ci.accountEmailId = userAccountChild.getString("EmailAddress");
            }

        }catch(JSONException e){
            e.printStackTrace();


        }


        return result;
    }


    //Basically it return the list of TransactionInfo object for recycler view, which created from fetching json data.
    public List<TransactionInfo> getUpdatedListAtScroll(JSONObject json){
        List<TransactionInfo> result = new ArrayList<>();

        try{
            JSONObject userAccount = json.getJSONObject("UserAccount");
            Log.d(TAG, "getTransactionListForRecyclerView: " + userAccount);
            //current Index
            int currentIndex = json.getInt("CurrentIndex");

            for(int i= currentIndex;i<(userAccount.length() + currentIndex);i++){
                TransactionInfo ci = new TransactionInfo();
                JSONObject userAccountChild = userAccount.getJSONObject(Integer.toString(i));
                Log.d(TAG, "getTransactionListForRecyclerView: json list............."+userAccountChild);

                ci.brandType = userAccountChild.getString("BrandType");
                ci.accountType= userAccountChild.getString("AccountType");
                ci.accountName = userAccountChild.getString("AccountName");

                String acctType = userAccountChild.getString("AccountType");

                if(Objects.equals(acctType,"Postpaid")){
                    ci.paymentDue = userAccountChild.getString("PaymentDue");
                    ci.totalAmount= userAccountChild.getInt("TotalAmount");
                }
                else if(Objects.equals(acctType, "Prepaid")){
                    ci.totalAmount= userAccountChild.getInt("TotalAmount");
                }
                //ci.ACCOUNT_ID = userAccountChild.getInt("AccountId");
                //ci.accountEmailId = userAccountChild.getString("EmailAddress");
                //ci.accountMobileNumber = userAccountChild.getString("MobileNumber");
                // userAccountChild = null;
                result.add(ci);

                //ci.accountMobileNumber = userAccountChild.getString("MobileNumber");
                //ci.accountEmailId = userAccountChild.getString("EmailAddress");
            }

        }catch(JSONException e){
            e.printStackTrace();


        }

        return result;
    }

}
