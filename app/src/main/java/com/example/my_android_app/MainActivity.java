package com.example.my_android_app;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_android_app.jsonreadandreturnlist.RecordTransactionList;
import com.example.my_android_app.login.LoginActivity;
import com.example.my_android_app.restapiservices.RestApiCallBack;
import com.example.my_android_app.restapiservices.RestApiCallOnBrandType;
import com.example.my_android_app.restapiservices.RestApiCallOnLogin;
import com.example.my_android_app.restapiservices.RestApiCallOnScroll;
import com.example.my_android_app.transactionadapter.ButtonClickListener;
import com.example.my_android_app.transactionadapter.TransactionAdapter;
import com.example.my_android_app.transactionadapter.TransactionInfo;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import static android.view.View.*;

//String currentPhotoPath;
public class MainActivity extends AppCompatActivity {

    public static final String ACCOUNT_TYPE = "none";
    public static final String MOBILE_NUMBER ="none";
    public static final String ACCOUNT_ID = "0";
    public static final String EMAIL_ID ="none";
    public static final String TAG = "MyApp";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final String ALL ="ALL";
    private static final String PLDT ="PLDT";
    private static final String SMART ="SMART";

    private static String brandFilter = "none";
    private static String searchFilter = "ACCOUNT_TYPE";
    public static  Boolean isSearching = false;

    private Button bOptionALL;
    private Button bOptionSMART;
    private Button bOptionPLDT;
    private ProgressBar pbView;
    private ImageView pImageView;

    private TextView tvTextSearch;
    private SearchView svSearch;

    private DrawerLayout mDrawerLayout;
    private RecyclerView rvView;
    private RecordTransactionList rtList;
    private TransactionAdapter taView;


    private NavigationView navigationView;
    Intent transactionActivity,logOutAcitivity ;
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rtList is instance of class RecordTransactionList, which record all the json data in list.
        this.rtList = new RecordTransactionList();
//        pImageView = findViewById(R.id.profile_foreground_image);

        transactionActivity = new Intent(this, TransactionList.class);
        logOutAcitivity = new Intent(this, LoginActivity.class);

        initializeNavigationDrawerLayout();

        initializeToolbar();

        initializeNavigationViewListener();

        initializeStaticView();

        if(GlobalVariable.profilePicBitMap != null){
            this.pImageView.setImageBitmap(GlobalVariable.profilePicBitMap);
        }

        //initializeAdapterView();

        //initializeAllViewElements();

        //initializeScrollEvent();

        //initializeSearchBarListener();

        //initializeSearchCancelListener();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        menuItem.setIcon(R.drawable.ic_notifications_black_24dp);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    /*-----------Initialize Toolbar--------------*/
    private void initializeToolbar(){

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    /*-------Initialize drawer layout and navigation view-----*/
    private void initializeNavigationDrawerLayout(){
        this.mDrawerLayout = findViewById(R.id.drawer_layout);
        this.navigationView = findViewById(R.id.nav_view);
    }


    /*---------Initialize all events listeners----------------*/
    private void initializeNavigationViewListener(){
        //pImageView = (ImageView) navigationView.getHeaderView(1);
        View headerLayout =
                navigationView.inflateHeaderView(R.layout.drawable_header);
        this.pImageView = headerLayout.findViewById(R.id.profile_foreground_image);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);


                        switch (menuItem.getItemId()) {
                            case R.id.nav_upload_profile:
                                dispatchTakePictureIntent();
                                break;
                            case R.id.nav_transaction_list:
                                mDrawerLayout.closeDrawers();
                                startActivity(transactionActivity);

                                break;
                            case R.id.nav_animation_view:
                                mDrawerLayout.closeDrawers();
                                break;
                            case R.id.log_out:
                                mDrawerLayout.closeDrawers();
                                startActivity(logOutAcitivity);
                                finish();
                                break;
                            default:
                                //todo..

                        }
                        return true;
                    }
                });
    }

    //capture photo event
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.my_android_app.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } catch (ActivityNotFoundException e) {
//            // display error state to the user
//        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "profile_JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //GlobalVariable.profilePicPath = image.getAbsolutePath();
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = this.pImageView.getWidth();
        int targetH = this.pImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        Bitmap roundBitMap = getRoundedCornerBitmap(bitmap, 96);
        GlobalVariable.profilePicBitMap = roundBitMap;
        this.pImageView.setImageBitmap(roundBitMap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //galleryAddPic();
            setPic();
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            Bitmap roundBitMap = getRoundedCornerBitmap(imageBitmap, 96);
//            this.pImageView.setImageBitmap(roundBitMap);
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private void initializeStaticView(){
        //RestApiCallOnLogin is a async task, which fetch data related to login data from assets folder json file login_data.json.
        //After onPostExecute method, it trigger the interface 'RestApiCallBack'.
        RestApiCallOnLogin apiCallOnLogin = new RestApiCallOnLogin(this, new RestApiCallBack() {
            @Override
            public void onCompleteProcess(List<TransactionInfo> result) {
                if(result != null){
                    //After getting result, it will call initializeLoginCardInfo function with Transaction info object.
                    TransactionInfo info = result.get(0);
                    initializeLoginCardInfo(info);
                }
            }
        });
        apiCallOnLogin.execute();
    }


    /*--------------Start dashBoard Activity----------*/
    //This function will call when view button is click form login info card.
    private void startDashBoardActivityOnViewButtonLoginCard(TransactionInfo tiList){
        Intent intent = new Intent(this, DashboardActivity.class);
        if(tiList.userEmailId != null){
            intent.putExtra(EMAIL_ID,tiList.userEmailId);
        }else{
            intent.putExtra(EMAIL_ID,"none");
        }

        intent.putExtra(ACCOUNT_TYPE, tiList.accountType);

        if(tiList.userMobileNumber != null){
            intent.putExtra(MOBILE_NUMBER,tiList.userMobileNumber);
        }else{
            intent.putExtra(MOBILE_NUMBER,"none");
        }

        if(tiList.userAccountId != 0){
            intent.putExtra(ACCOUNT_ID,String.valueOf(tiList.userAccountId));
        }else{
            intent.putExtra(ACCOUNT_ID,"none");
        }
        this.startActivity(intent);
    }

    /*---------------Initialize  login card view--------------------*/

    private void initializeLoginCardInfo(final TransactionInfo result){
        TextView tvAccountType,tvUserAccountName,tvPaymentAmount,tvPaymentAmountText,tvPaymentDate,tvPaymentDateText;
        Button bViewAccount;
        View v = findViewById(R.id.transaction_view);
        String amount,brandText;
        Integer intInstance;

        tvAccountType =   v.findViewById(R.id.accountType);
        tvUserAccountName =  v.findViewById(R.id.userAccountName);
        tvPaymentAmount =   v.findViewById(R.id.paymentAmount);
        tvPaymentAmountText =  v.findViewById(R.id.paymentAmountText);
        tvPaymentDate =  v.findViewById(R.id.paymentDate);
        tvPaymentDateText =  v.findViewById(R.id.paymentDateText);
        bViewAccount = v.findViewById(R.id.viewAccount);

        brandText = result.brandType +" "+result.accountType;
        tvAccountType.setText(brandText);
        tvUserAccountName.setText(result.accountName);


        intInstance = result.totalAmount;
        amount = intInstance.toString();
        tvPaymentAmount.setText(amount);

        tvPaymentAmountText.setText(result.paymentAmountText);
        tvPaymentDate.setText(result.paymentDue);
        //Log.d(TAG, "onBindViewHolder: paymentDue"+ci.paymentDue );
        tvPaymentDateText.setText(result.paymentDueText);

        bViewAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startDashBoardActivityOnViewButtonLoginCard(result);
            }
        });

    }


    /*-------------Initialize All View elements-----------------------*/
//    private void initializeAllViewElements(){
//        this.svSearch =  findViewById(R.id.searchBar);
//        this.bOptionALL = findViewById(R.id.option_all);
//        this.bOptionPLDT = findViewById(R.id.option_pldt);
//        this.bOptionSMART = findViewById(R.id.option_smart);
//        this.tvTextSearch =  findViewById(R.id.textSearch);
//        this.pbView = findViewById(R.id.main_progress);
//    }


    /*-----------------------------Initialize AdapterView----------------------------------*/
    //TransactionAdapter is our adapter view class with extended RecyclerView.Adapter.

//    private void initializeAdapterView(){
//
//        rvView =  findViewById(R.id.cardList);
//        LinearLayoutManager llmView = new LinearLayoutManager(this);
//        llmView.setOrientation(LinearLayoutManager.VERTICAL);
//        rvView.setLayoutManager(llmView);
//        rvView.setHasFixedSize(false);
//        //rvView.setNestedScrollingEnabled(false);
//
//        //taView stored the instance of adapter view with interface ButtonClickListener.
//        taView = new TransactionAdapter( new ButtonClickListener() {
//
//            @Override
//            public void onPositionClicked(View v, int position) {
//                // callback performed on click
//                //After getting the position of card from where view button is clicked,
//                //it will call RecordTransactionList.getTransactionInfo function.
//                //then call startDashBoardActivityOnViewButtonRecyclerView with TransactionInfo object.
//                TransactionInfo tiList ;
//                tiList = rtList.getTransactionInfo(position);
//                startDashBoardActivityOnViewButtonRecyclerView(tiList);
//            }
//
//            @Override
//            public void onLongClicked(View v, int position) {
//                // callback performed on click
//            }
//        });
//
//        rvView.setAdapter(taView);
//    }









//    private void initializeScrollEvent(){
//        NestedScrollView nsView;
//        //To overcome conflicts between scrolls of Recycler view and Nested scrollable view, we use NestedScrollable event
//        //This event will trigger when scrolling of nestedScrollingView.. is happening.
//        nsView = findViewById(R.id.scrollList);
//        nsView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if(v.getChildAt(v.getChildCount() - 1) != null) {
//
//                    //this logic ensure that scroll direction is in downward
//                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
//                            scrollY > oldScrollY) {
//
//                        try{
//                            int visibleItemCount = rvView.getLayoutManager().getChildCount();
//                            int totalItemCount = rvView.getLayoutManager().getItemCount();
//                            int pastVisibleItems = ((LinearLayoutManager) rvView.getLayoutManager()).findFirstVisibleItemPosition();
//
//                            //'visibleItemCount + pastVisibleItems' calculate the total items present in view.
//                            //After getting exceeded with total child items, then it call OnScrollApi function.
//                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                                Log.d(TAG, "onScrollChange: load more..");
//
//                                if((!isSearching) &&(!Objects.equals(brandFilter, "none"))){
//
//                                    OnScrollApi(brandFilter,totalItemCount);
//                                }
//                            }
//                        }catch (NullPointerException e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//            }
//        });
//    }
//
//
//
//    //Search event will trigger, when user click on submit button
//    private void initializeSearchBarListener(){
//
//        this.svSearch.setOnSearchClickListener(new SearchView.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                searchTextHide();
//            }
//        });
//
//        this.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                //After submit it will call TransactionAdapter.filtered function.
//                //List of filtered data will come from RecordTransaction.getFilteredTransactionList.
//                //For now searchFilter is on the basis of 'ACCOUNT_TYPE'.
//                taView.filteredList(rtList.getFilteredTransactionList(searchFilter,query));
//                isSearching = true;
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }
//
//    //Search event will trigger on cancel event.
//    private void initializeSearchCancelListener(){
//        this.svSearch.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//
//                //it will clear the Recycler view adapter and add the list of original Transaction List by RecordTransaction.getOriginalTransactionList.
//                taView.onCancelSearch(rtList.getOriginalTransactionList());
//                searchTextShow();
//
//                isSearching = false;
//                return false;
//            }
//        });
//    }


    /*-------------  Api call methods --------------*/
//    private void setApiCallOnBrandTypeApi(String name){
//        //RestApiCallOnBrandType is a async task, which fetch data related to brand Name from assets folder json files.
//        //After onPostExecute method, it trigger the interface 'RestApiCallBack'.
//        RestApiCallOnBrandType apiCallOnBrandType = new RestApiCallOnBrandType(this, new RestApiCallBack() {
//            @Override
//            public void onCompleteProcess(List<TransactionInfo> result) {
//                if(result != null){
//                    //taView.clear();
//                    //After getting result, it will call TransactionAdapter.onBrandTypeBtnClickAddResult.
//                    taView.onBrandTypeBtnClickAddResult(result);
//                }
//
//            }
//        });
//
//        apiCallOnBrandType.execute(name);
//    }



//    private void OnScrollApi(String name,int index){
//
//        showProgress();
//        //RestApiCallOnScroll is a async task, which fetch data related to brand Name from assets folder json files.
//        //After onPostExecute method, it trigger the interface 'RestApiCallBack'.
//        RestApiCallOnScroll apiCallOnScroll = new RestApiCallOnScroll(this, new RestApiCallBack() {
//            @Override
//            public void onCompleteProcess(List<TransactionInfo> result) {
//                if(result != null){
//                    //After getting result, it will append data in Recycler view by calling TransactionAdapter.addAll function.
//                    hideProgress();
//                    taView.addAll(result);
//                }else{
//                    hideProgress();
//                }
//            }
//        });
//
//        //passed parameters are name='Brand Name' and index = total child elements present in current recycler view.
//        apiCallOnScroll.execute(name,String.valueOf(index));
//    }



    /*---------------------On brand Type button click methods ----------------------*/

//    public void brandButtonOnClick(View v){
//        switch (v.getId()){
//            case R.id.option_all:
//                //rvView.getLayoutManager().setMeasuredDimension(100,500);
//                showSearchBar();
//                searchTextShow();
//                //hideLoginText();
//
//                setBorder(bOptionALL);
//
//                //check for toggle...
//                if(Objects.equals(brandFilter, ALL) ){
//                    toggleBtn(bOptionALL);
//                }else {
//                    //unset remaining buttons border..
//                    unSetAllButtonBorder(ALL);
//
//                    //set account name filter..
//                    setBrandNameFilter(ALL);
//
//
//                    //call restApiCallOnAccountName
//                    setApiCallOnBrandTypeApi(brandFilter);
//                }
//                break;
//            case R.id.option_smart:
//                showSearchBar();
//                searchTextShow();
//                //hideLoginText();
//
//                setBorder(bOptionSMART);
//
//                //check for toggle...
//                if(Objects.equals(brandFilter, SMART) ){
//                    toggleBtn(bOptionSMART);
//                }else{
//                    //unset remaining buttons border..
//                    unSetAllButtonBorder(SMART);
//
//                    //set account name filter..
//                    setBrandNameFilter(SMART);
//
//                    //call restApiCallOnAccountName
//                    setApiCallOnBrandTypeApi(brandFilter);
//                }
//
//                break;
//            case R.id.option_pldt:
//                showSearchBar();
//                searchTextShow();
//                //hideLoginText();
//                setBorder(bOptionPLDT);
//
//                //check for toggle...
//                if(Objects.equals(brandFilter, PLDT)){
//                    toggleBtn(bOptionPLDT);
//                }else{
//                    //unset remaining border..
//                    unSetAllButtonBorder(PLDT);
//
//                    //set account name filter..
//                    setBrandNameFilter(PLDT);
//
//                    //call restApiCallOnAccountName
//                    setApiCallOnBrandTypeApi(brandFilter);
//                }
//                break;
//        }
//    }




    // Use to unset the bottom border color from blue to grey.
//    private void unSetAllButtonBorder(String filter){
//
//        if(!Objects.equals(brandFilter, filter) ){
//            Log.d(TAG, "unSetAllButtonBorder: accName "+filter);
//            switch(brandFilter){
//                case "ALL":
//                    unsetSingleButtonBorder(bOptionALL);
//                    break;
//                case "PLDT":
//                    unsetSingleButtonBorder(bOptionPLDT);
//                    break;
//                case "SMART":
//                    unsetSingleButtonBorder(bOptionSMART);
//                    break;
//
//            }
//        }
//
//    }
//
//
//    private void toggleBtn(Button btn){
//        btn.setBackgroundResource(R.drawable.button_border_unset);
//
//        hideSearchBar();
//        searchTextHide();
//        //showLoginText();
//
//        //reset accountName Filter....
//        setBrandNameFilter("none");
//
//        //On login api call....
//        taView.clear();
//
//    }
//
//    // Use to set the bottom border color from grey to blue.
//    private void setBorder(Button btn){
//        btn.setTextColor(getResources().getColor(R.color.dark_black));
//        btn.setBackgroundResource(R.drawable.button_border_set);
//    }
//
//    private void unsetSingleButtonBorder(Button btn){
//        btn.setBackgroundResource(R.drawable.button_border_unset);
//    }
//
//    private void setBrandNameFilter(String name){
//        brandFilter = name;
//    }
//    private void hideSearchBar(){
//        this.svSearch.setVisibility(INVISIBLE);
//
//    }
//
//    private void showSearchBar(){
//        this.svSearch.setVisibility(VISIBLE);
//        //this.svSearch.setIconified(false);
//        this.svSearch.setSubmitButtonEnabled(true);
//    }
//
//
//
//    private void searchTextHide(){
//        tvTextSearch.setVisibility(INVISIBLE);
//    }
//    private void searchTextShow(){
//        tvTextSearch.setVisibility(VISIBLE);
//    }
//
//    private void showProgress(){
//        pbView.setVisibility(View.VISIBLE);
//    }
//
//    private void hideProgress(){
//        pbView.setVisibility(View.GONE);
//    }





//    //This function will call when view button is click form recycler view child items
//    private void startDashBoardActivityOnViewButtonRecyclerView(TransactionInfo tiList){
//        Intent intent = new Intent(this, DashboardActivity.class);
//        if(tiList.accountEmailId != null){
//            intent.putExtra(EMAIL_ID,tiList.accountEmailId);
//        }else{
//            intent.putExtra(EMAIL_ID,"none");
//        }
//        intent.putExtra(ACCOUNT_TYPE, tiList.accountType);
//
//        if(tiList.accountMobileNumber != null){
//            intent.putExtra(MOBILE_NUMBER,tiList.accountMobileNumber);
//        }else{
//            intent.putExtra(MOBILE_NUMBER,"none");
//        }
//        if(tiList.accountId != 0){
//            intent.putExtra(ACCOUNT_ID,String.valueOf(tiList.accountId));
//        }else{
//            intent.putExtra(ACCOUNT_ID,"none");
//        }
//
//        //intent.putExtra(MOBILE_NUMBER,tiList.accountMobileNumber);
//        this.startActivity(intent);
//    }




}

