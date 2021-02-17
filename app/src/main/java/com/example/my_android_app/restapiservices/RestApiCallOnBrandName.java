package com.example.my_android_app.restapiservices;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.my_android_app.jsonreadandreturnlist.ReadJsonAndReturnList;
import com.example.my_android_app.jsonreadandreturnlist.RecordTransactionList;
import com.example.my_android_app.transactionadapter.TransactionInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RestApiCallOnBrandName extends AsyncTask<String, Integer, String> {

    private static final String TAG = "MyApp";
    public InputStream in;
    private Activity activity;
    private ReadJsonAndReturnList jsData;
    private RecordTransactionList csList;
    private final RestApiCallBack callBack;
    private List<TransactionInfo> transactionList;


    /*---------------------RestApiCallOnBrandType----------------------*/
    //This class is extended with AsyncTask.
    //Constructor Parameters are Activity and interface RestApiCallBack.
    //Activity.getAssets() is used for fetching files from assets folder.

    public RestApiCallOnBrandName(Activity avtivity, RestApiCallBack callBack){

        this.activity = avtivity;
        this.jsData = new ReadJsonAndReturnList();
        this.csList = new RecordTransactionList();
        this.callBack = callBack;

    }

    @Override
    protected void onPreExecute() {

        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String... params) {

        JSONObject jsonObj;
        //publishProgress (10);

        //load the json file or by api call...
        String filename = getJsonFileName(params[0]);
        String jStr = loadJSONFromAsset(filename);


        //update the jsonObj
        try{
            jsonObj = new JSONObject(jStr);

            //List of transactionInfo are coming from ReadJsonAndReturnList.getTransactionListForRecyclerView
            transactionList = this.jsData.getTransactionListForRecyclerView(jsonObj);

            // Call RecordTransactionList.updateTransactionInfoList function to update the transactionList in RecordTransactionList class .
            csList.updateTransactionInfoList(transactionList);
        }
        catch(JSONException e){
            e.printStackTrace();
        }



        return "";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //Update the progress of current task
        //setProgressPercent(progress[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        //Show the result obtained from doInBackground

        //After completion of background process, RestApiCallBack.onCompleteProcess is called to get result in MainActivity class.
        callBack.onCompleteProcess(transactionList);

    }


    /*---------------- Read json from assets folder start----------------------*/
    private String loadJSONFromAsset(String filename) {
        String json;
        try {
            InputStream is = this.activity.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d(TAG, "loadJSONFromAsset: " + json);
        return json;
    }
    /*---------------- Read json from assets folder End----------------------*/



    private String getJsonFileName(String brandName){
        brandName = brandName+"_current_index_0"+".json";
        return brandName;
    }





}
