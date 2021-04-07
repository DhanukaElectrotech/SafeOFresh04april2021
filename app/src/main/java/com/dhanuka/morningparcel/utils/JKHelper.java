package com.dhanuka.morningparcel.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
 import com.dhanuka.morningparcel.Helper.DBImageUpload;
import com.dhanuka.morningparcel.Helper.ImageLoadingUtils;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.InterfacePackage.HttpCallBack;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.activity.LoginActivity;
import com.dhanuka.morningparcel.activity.Message;

import com.example.librarymain.DhanukaMain;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JKHelper extends Application {
    private static String filePath;
    private static final int REQUEST_CODE = 1010;
    public static final int REQUEST_CODE_GALLERY = 10;
    com.dhanuka.morningparcel.Helper.Preferencehelper prefs;
     public static final int REQUEST_CODE_PHONE = 100;
     private static String PREFS_FILE_PATH = "capture_file_path";
    static String mImagePathDataBase;
    static String mImageNameDataBase;
    static String mCurrentTimeDataBase;
    static String mdesc;
    static String mimagetype;
    public static ImageLoadingUtils utils;
     protected LocationSettingsRequest mLocationSettingsRequest;
    protected LocationRequest mLocationRequest;
    //PreferenceHelper prefs;

    private static boolean addedToMasterTable = false;
    static int masterDataBaseId;
    private Integer cameraphotoclicked = 0;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    String serverid = "";
    static String lastRowMaterTable;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    protected Location mCurrentLocation;
    String latitudeToServer, longitudeToServer;
    protected Boolean mRequestingLocationUpdates = false;
    ;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private double bearing = 0.0;
    private final float metersSec_in_KMPH = 3.6f;
    ProgressDialog fecthingYourLocation;

    public static final String TAG = "MmBizAppliction";

    /**
     * Show Log for this app.
     */
    public static void showLog(String text) {
        Log.e(TAG, text);
    }

    /**
     * Check EditText is empty or not.
     */

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();


        SimpleDateFormat mFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return mFormatter.format(new Date());
    }

    public static void runhttpcode(final Map<String, String> requestParams, String apinm, final Context ctx, final HttpCallBack httpCallBack) {


        StringRequest postRequest = new StringRequest(Request.Method.POST, apinm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        String res = response;

                        if (res.length() > 0) {
                            log.e("res ateendfsad " + res);
                            httpCallBack.serviceResponse(res);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Message.message(ctx, "Failed to Upload Status");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params = requestParams;
                return params;
            }
        };
        Volley.newRequestQueue(ctx).add(postRequest);


    }

    public static void pickImageFromGal(View v, Context context) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GALLERY);
    }

    public static void pickFilesFromPhone(View v, Context context, PackageManager p) {

        /*Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(".*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);*/

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", "*/*");
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;

        if (p.resolveActivity(sIntent, 0) != null) {
            // it is device with samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            ((Activity) context).startActivityForResult(chooserIntent, REQUEST_CODE_PHONE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void call(Context context, String phone) {
        //Intent callIntent = new Intent(Intent.ACTION_CALL);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phone));
        context.startActivity(callIntent);
    }

    public static String gettodaysdateonly() {
        SimpleDateFormat mFormatter = new SimpleDateFormat("MM/dd/yyyy");
        return mFormatter.format(new Date());
    }

    public static boolean isConnectedToNetwork(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            isConnected = networkInfo.isConnected();
        }

        return isConnected;
    }

    public static void runhttpcodenoprocessingbar(final Map<String, String> params, String apinm, final Context context, final HttpCallBack httpCallBack) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, apinm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        String res = response;

                        if (res.length() > 0) {
                            log.e("res ateendfsad " + res);
                            httpCallBack.serviceResponse(res);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Message.message(context, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1 = params;
                return params1;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(postRequest);
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];

        } else {
            account = null;
        }
        return account;
    }

    public static String getPhoneNumber(Context c) {
        TelephonyManager phoneManager = (TelephonyManager)
                c.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String phoneNumber = phoneManager.getLine1Number();


        return phoneNumber;
    }

    public static boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void updateserverphotoid(String serverid, String lastRowMaterTable, String tablenm, Context context) {
        ArrayList<DBImageUpload> list;
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        ImageUploadDAO dao = new ImageUploadDAO(database, context);
        list = dao.selectUploadPhotos();


//        ImageUploadDAO dao = new ImageUploadDAO(database, context);
        int count = dao.getlatesttypeidforupdate1(tablenm);
        lastRowMaterTable = String.valueOf(count);
        // change it for server exp id
        if (lastRowMaterTable != null) //&& !lastRowMaterTable.equalsIgnoreCase("0")
        {
            ImageUploadDAO pd = new ImageUploadDAO(database, context);
            pd.setWorkIdToTable(String.valueOf(serverid), lastRowMaterTable);
            ImageMasterDAO pds = new ImageMasterDAO(database, context);
            pds.setServerDetails(Integer.parseInt(lastRowMaterTable), Integer.valueOf(serverid), 0);
            ImageMasterDAO iDao = new ImageMasterDAO(database, context);
            String serverId = iDao.getServerIdById(1);
            DatabaseManager.getInstance().closeDatabase();
            log.e("in last row master table inserting  ");

            ArrayList<DBImageUpload> list1;
            list1 = dao.selectUploadPhotos();
        }


    }
static File mFile1;


    public static String getCurrentDateForAttendence() {
        SimpleDateFormat mFormatter = new SimpleDateFormat("MMM. dd,yyyy");
        return mFormatter.format(new Date());
    }

    public static String getCurrentTimeForAttendence() {

        SimpleDateFormat mFormatter = new SimpleDateFormat("hh:mm aaa");
        return mFormatter.format(new Date());
    }

    public static float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }

    public static String getFileCreatedDate(String filepath) {
        String date = null;

        File file = new File(filepath);
        Date lastModDate = new Date(file.lastModified());
        date = lastModDate.toString();


        return date;
    }


    public static void closeKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isServiceRunning(Context ctx, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static String getTime(double min) {
        String hourMinute = "0";
        if (min > 0) {
            int hourMin = 0;
            double remin = 0;
            hourMin = (int) min / 60;
            remin = min % 60;
            hourMinute = String.valueOf(hourMin) + " hours " + String.valueOf(remin) + " minutes ";
        }
        return hourMinute;
    }

    //todo remove all below <code></code>

    public static void textWithHtml(TextView mTextView, String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTextView.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
        } else {

            mTextView.setText(Html.fromHtml(msg));
        }
    }





    public  String  Encryptapi (String param  ,Context context)
    {
        String params ,token,userid;

        com.dhanuka.morningparcel.Helper.Preferencehelper prefs= new Preferencehelper(context);



        if (prefs.getTokenValue()==null)
        {

            token="";
        }
        else
        {
            token=prefs.getTokenValue();

        }

        if (prefs.getPrefsEmail()==null)
        {
            userid="";
        }
        else
        {
            userid=prefs.getPrefsEmail();


        }

        try {

            params= DhanukaMain.SafeOBuddyEncryptUtils(param,token,userid);
            return params;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public  String Decryptapi( String response, final Context context)
    {
        String responses;

        try
        {



            responses= DhanukaMain.SafeOBuddyDecryptUtils(response);
            JSONObject jsonObject= new JSONObject(responses);
            int success= jsonObject.getInt("success");
            if (success == 9999)
            {
                final android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(context);
                builder.setTitle("Session Out Please Login again in order to proceed further");
                builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("tokenval");
                        editor.commit();


                        context.startActivity(new Intent(context, LoginActivity.class));


                    }
                });
                builder.setCancelable(false);
                builder.show();

            }
            else
            {

                return DhanukaMain.SafeOBuddyDecryptUtils(response);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public static void openCommentDialog(final Context mContext, String param) {
        final String comments = param;
        Typeface font = Typeface.createFromAsset(
                mContext.getAssets(),
                "fonts/sansation-bold.ttf");
        final Dialog Localdialog = new Dialog(mContext);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        Localdialog.setContentView(R.layout.dialog_comment);
        Window window = Localdialog.getWindow();
        window.setWindowAnimations(R.style.DialogAnimation);
        final TextView txt_number = (TextView) Localdialog.findViewById(R.id.cmttext);
        final TextView icon_close = (TextView) Localdialog.findViewById(R.id.icon_close);
         txt_number.setTypeface(font);
        txt_number.setText(comments);
        //    Utility.textWithHtml(viewHolder.tvPrice, context.getString(R.string.prices));
//        String strTitle = mContext.getResources().getString(R.string.html_text);
//        textWithHtml(txt_title, strTitle.replace("VEHICALNO", strVehicalNo));
//        txt_number.setText(strMob);
//        txt_name.setText(strName);
//        imgCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent callIntent = new Intent(Intent.ACTION_CALL);
//                    callIntent.setData(Uri.parse("tel:" + txt_number.getText().toString()));
//
//                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        PermissionModule permissionModule = new PermissionModule(mContext);
//                        permissionModule.checkPermissions();
//
//                    } else {
//                        mContext.startActivity(callIntent);
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    FancyToast.makeText(mContext, "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
//                }
//
//            }
//        });
//        imgEditNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, UpdateAddressActivity.class).putExtra("id", strId)
//                        .putExtra("mob", strMob)
//                        .putExtra("name", strName)
//                        .putExtra("vehicleNo", strVehicalNo)
//                        .putExtra("roasterId", roasterId)
//                );
//
//                //new UpdateAddress(mContext,strId,strMob,strName,strVehicalNo).show();
//            }
//        });
        icon_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Localdialog.dismiss();
            }
        });
        Localdialog.show();
    }
//    public  boolean checkTabvsPhone(Context context)
//    {
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//
//        float yInches= metrics.heightPixels/metrics.ydpi;
//        float xInches= metrics.widthPixels/metrics.xdpi;
//        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
//        if (diagonalInches>=6.5){
//            return boolean 1;
//           // startActivity(new Intent(BaseActivity.this, OrderHistoryActvitylsp.class));
//        }else{
//            return boolean 1;
//            // smaller device
//            startActivity(new Intent(BaseActivity.this, OrderHistoryActivity.class));
//        }
//
//    }
}
