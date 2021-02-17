package com.example.my_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_android_app.jsonreadandreturnlist.RecordTransactionList;
import com.example.my_android_app.restapiservices.RestApiCallBack;
import com.example.my_android_app.restapiservices.RestApiCallOnBrandName;
import com.example.my_android_app.restapiservices.RestApiCallOnScroll;
import com.example.my_android_app.transactionadapter.ButtonClickListener;
import com.example.my_android_app.transactionadapter.TransactionAdapter;
import com.example.my_android_app.transactionadapter.TransactionInfo;

import java.util.List;

public class TransactionList extends AppCompatActivity {
    private static final String TAG ="Transaction activity" ;
    private static String brandName = "BRAND";

    public static final String ACCOUNT_TYPE = "none";
    public static final String MOBILE_NUMBER ="none";
    public static final String ACCOUNT_ID = "0";
    public static final String EMAIL_ID ="none";
    public Boolean isLoaded = false;

    private RecyclerView rvView;
    private ProgressBar pbView;
    private RecordTransactionList rtList;
    private TransactionAdapter taView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_list);

        //rtList is instance of class RecordTransactionList, which record all the json data in list.
        this.rtList = new RecordTransactionList();
        this.rvView = findViewById(R.id.transactionRecyclerView);
        this.pbView = findViewById(R.id.onProgressList);

        initializeToolbar();
        initializeScrollEvent();
        initializeAdapterView();
        setApiCallOnBrandTypeApi(brandName);

    }
    
    //    /*-----------Initialize Toolbar--------------*/
    private void initializeToolbar(){

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Transaction List");

    }

    private void initializeScrollEvent(){

        rvView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(recyclerView.getChildAt(recyclerView.getChildCount() - 1) != null) {
                    //this logic ensure that scroll direction is in downward
                    if ((recyclerView.getScrollY() >= (recyclerView.getChildAt(recyclerView.getChildCount() - 1).getMeasuredHeight() - recyclerView.getMeasuredHeight()))) {
                        try{
                            int visibleItemCount = rvView.getLayoutManager().getChildCount();
                            int totalItemCount = rvView.getLayoutManager().getItemCount();
                            int pastVisibleItems = ((LinearLayoutManager) rvView.getLayoutManager()).findFirstVisibleItemPosition();

                            //'visibleItemCount + pastVisibleItems' calculate the total items present in view.
                            //After getting exceeded with total child items, then it call OnScrollApi function.
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                //Log.w(TAG, String.format("onScrollChange: load more..%d", totalItemCount));
                                if(isLoaded == false){
                                    OnScrollApi(brandName,totalItemCount);
                                }
                            }
                        }catch (NullPointerException e){
                            //e.printStackTrace();
                        }
                    }
                }

            }
        });

    }


    private void OnScrollApi(String name,int index) {

        showProgress();
        isLoaded = true;

        //RestApiCallOnScroll is a async task, which fetch data related to brand Name from assets folder json files.
        //After onPostExecute method, it trigger the interface 'RestApiCallBack'.
        RestApiCallOnScroll apiCallOnScroll = new RestApiCallOnScroll(this, new RestApiCallBack() {
            @Override
            public void onCompleteProcess(List<TransactionInfo> result) {
                if(result != null){
                    //After getting result, it will append data in Recycler view by calling TransactionAdapter.addAll function.
                    hideProgress();
                    taView.addAll(result);
                    isLoaded = false;
                    //rvView.suppressLayout(false);
                }else{
                    hideProgress();
                    isLoaded = true;
                    //rvView.suppressLayout(false);
                }
            }
        });

        //passed parameters are name='Brand Name' and index = total child elements present in current recycler view.
        apiCallOnScroll.execute(name,String.valueOf(index));

    }

    private void initializeAdapterView(){

        LinearLayoutManager llmView = new LinearLayoutManager(this);
        llmView.setOrientation(LinearLayoutManager.VERTICAL);
        rvView.setLayoutManager(llmView);
        rvView.setHasFixedSize(false);
        //rvView.setNestedScrollingEnabled(false);

        //taView stored the instance of adapter view with interface ButtonClickListener.
        taView = new TransactionAdapter( new ButtonClickListener() {

            @Override
            public void onPositionClicked(View v, int position) {
                // callback performed on click
                //After getting the position of card from where view button is clicked,
                //it will call RecordTransactionList.getTransactionInfo function.
                //then call startDashBoardActivityOnViewButtonRecyclerView with TransactionInfo object.
                TransactionInfo tiList ;
                tiList = rtList.getTransactionInfo(position);
                startDashBoardActivityOnViewButtonRecyclerView(tiList);
            }

            @Override
            public void onLongClicked(View v, int position) {
                // callback performed on click
            }
        });

        rvView.setAdapter(taView);
    }

    //This function will call when view button is click form recycler view child items
    private void startDashBoardActivityOnViewButtonRecyclerView(TransactionInfo tiList){
        Intent intent = new Intent(this, DashboardActivity.class);
        if(tiList.accountEmailId != null){
            intent.putExtra(EMAIL_ID,tiList.accountEmailId);
        }else{
            intent.putExtra(EMAIL_ID,"none");
        }
        intent.putExtra(ACCOUNT_TYPE, tiList.accountType);

        if(tiList.accountMobileNumber != null){
            intent.putExtra(MOBILE_NUMBER,tiList.accountMobileNumber);
        }else{
            intent.putExtra(MOBILE_NUMBER,"none");
        }
        if(tiList.accountId != 0){
            intent.putExtra(ACCOUNT_ID,String.valueOf(tiList.accountId));
        }else{
            intent.putExtra(ACCOUNT_ID,"none");
        }

        //intent.putExtra(MOBILE_NUMBER,tiList.accountMobileNumber);
        this.startActivity(intent);
    }


    private void showProgress(){
        pbView.setVisibility(View.VISIBLE);
    }

    private void hideProgress(){
        pbView.setVisibility(View.GONE);
    }

    /*-------------  Api call methods --------------*/
    private void setApiCallOnBrandTypeApi(String name){
        //RestApiCallOnBrandType is a async task, which fetch data related to brand Name from assets folder json files.
        //After onPostExecute method, it trigger the interface 'RestApiCallBack'.
        RestApiCallOnBrandName apiCallOnBrandType = new RestApiCallOnBrandName(this, new RestApiCallBack() {
            @Override
            public void onCompleteProcess(List<TransactionInfo> result) {
                if(result != null){
                    //taView.clear();
                    //After getting result, it will call TransactionAdapter.onBrandTypeBtnClickAddResult.
                    taView.onBrandTypeBtnClickAddResult(result);
                }

            }
        });

        apiCallOnBrandType.execute(name);
    }

}
