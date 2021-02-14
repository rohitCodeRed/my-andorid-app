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

public class RestApiCallOnLogin extends AsyncTask<String, Integer, String> {

    private static final String TAG = "MyApp";
    public InputStream in;

    private Activity activity;


    private ReadJsonAndReturnList jsData;
    private RecordTransactionList csList;
    private final RestApiCallBack callBack;
    private List<TransactionInfo> transactionList;

    /*---------------------RestApiCallOnLogin----------------------*/
    //This class is extended with AsyncTask.
    //Constructor Parameters are Activity and interface RestApiCallBack.
    //Activity.getAssets() is used for fetching files from assets folder.

    public RestApiCallOnLogin(Activity activity, RestApiCallBack callBack){
        this.activity = activity;
        this.callBack = callBack;
        this.jsData = new ReadJsonAndReturnList();
        this.csList = new RecordTransactionList();
    }



    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }

    @Override
    protected String doInBackground(String... params) {
        JSONObject jsonObj;
        //publishProgress (1);

        //load the json file or by api call...
        String jStr = loadJSONFromAsset();

        //update the jsonObj
        try{
            jsonObj = new JSONObject(jStr);

            //List of transactionInfo are coming from ReadJsonAndReturnList.getTransactionListLoggedInAccount.
            transactionList = this.jsData.getTransactionListLoggedInAccount(jsonObj);

            // Call RecordTransactionList.updateLoginList function to update the loginInfo in RecordTransactionList class.
            csList.updateLoginList(transactionList);
        }
        catch(JSONException e){
            e.printStackTrace();
        }



        return "";
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        //Update the progress of current task
    }

    @Override
    protected void onPostExecute(String s) {
        //Show the result obtained from doInBackground

        //After completion of background process, RestApiCallBack.onCompleteProcess is called to get result in MainActivity class.
        callBack.onCompleteProcess(transactionList);

    }


    /*---------------- Read json from assets folder start----------------------*/
    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = this.activity.getAssets().open("login_data.json");
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





}
