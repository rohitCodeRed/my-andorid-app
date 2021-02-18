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
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_android_app.jsonreadandreturnlist.RecordTransactionList;
import com.example.my_android_app.login.LoginActivity;

import com.example.my_android_app.transactionadapter.TransactionAdapter;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.view.View.*;

//String currentPhotoPath;
public class MainActivity extends AppCompatActivity {

    public static final String ACCOUNT_TYPE = "none";
    public static final String MOBILE_NUMBER ="none";
    public static final String ACCOUNT_ID = "0";
    public static final String EMAIL_ID ="none";
    public static final String TAG = "MyApp";
    public static String USER_NAME ="";
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
    //private RecordTransactionList rtList;
    private TransactionAdapter taView;


    private NavigationView navigationView;
    Intent transactionActivity,logOutAcitivity,animationViewActivity ;
    String currentPhotoPath;
    private TextView usernameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rtList is instance of class RecordTransactionList, which record all the json data in list.
        //this.rtList = new RecordTransactionList();
//        pImageView = findViewById(R.id.profile_foreground_image);

        transactionActivity = new Intent(this, TransactionList.class);
        logOutAcitivity = new Intent(this, LoginActivity.class);
        animationViewActivity = new Intent(this,DampingBallAnimationActivity.class);

        Intent intent = getIntent();
        USER_NAME = intent.getStringExtra(LoginActivity.USER_NAME);
        //GlobalVariable.userName = USER_NAME;

        usernameText = findViewById(R.id.imageUsername);
        usernameText.setText(GlobalVariable.userName+"!");

        initializeNavigationDrawerLayout();

        initializeToolbar();

        initializeNavigationViewListener();

        initializeLoginCardInfo();

        if(GlobalVariable.profilePicBitMap != null){
            this.pImageView.setImageBitmap(GlobalVariable.profilePicBitMap);
        }


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
                                startActivity(animationViewActivity);
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

    /*---------------Initialize  login card view--------------------*/

    private void initializeLoginCardInfo(){
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

        brandText = "Account Prepaid";//result.brandType +" "+result.accountType;
        tvAccountType.setText(brandText);
        tvUserAccountName.setText(GlobalVariable.userName+" Account");


        intInstance = 10000;
        amount = intInstance.toString();
        tvPaymentAmount.setText(amount);

        tvPaymentAmountText.setText("Total Amount Due");
        tvPaymentDate.setText("2021-01-30 00:00");
        //Log.d(TAG, "onBindViewHolder: paymentDue"+ci.paymentDue );
        tvPaymentDateText.setText("Payment due on");
        bViewAccount.setVisibility(GONE);

    }

}

