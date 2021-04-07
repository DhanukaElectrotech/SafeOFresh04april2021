package com.dhanuka.morningparcel.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageLoadingUtils;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.NetworkUtil;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.catcodemodel;
import com.dhanuka.morningparcel.MainActivity;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import com.dhanuka.morningparcel.utils.log;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static com.vincent.filepicker.activity.VideoPickActivity.IS_NEED_CAMERA;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.yalantis.ucrop.util.BitmapLoadUtils.calculateInSampleSize;
// to delete below reference

public class ComplaintReason extends MainActivity implements View.OnClickListener {
    String lastRowMaterTable;

    private TextView tvcomment, tvPhotoCount, toolbar_settxt;
    private ImageView ivcamera;
    private MaterialEditText etchequeno, etbillamount, ettds, etwhomtopay, etexpamount, ettaxamount, etDate, tvdueDate, etbankname;
    //private MaterialBetterSpinner spinnerBillable;
    private Spinner spnreason;
    private Button btnCancel, btnSubmit, btn_showreport;
    private Integer cameraphotoclicked = 0;
    Dialog Localdialog;

    AlertDialog.Builder builder;
    private String filePath, tripid, tripdid, type;
    private final int REQUEST_CODE = 1010;
    private String PREFS_FILE_PATH = "capture_file_path";
    String mImagePathDataBase;
    String mImageNameDataBase;
    Preferencehelper preferencehelper;

    String mCurrentTimeDataBase;
    private ImageLoadingUtils utils;
    private boolean addedToMasterTable = false;
    int masterDataBaseId;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    String serverid = "";
    private String woid, strclientname;
    ArrayList<String> mListLastRows = new ArrayList<>();


    public ArrayAdapter<String> myAdapter;
    ArrayList<String> businesstype1;

    String[] reason =
            {
                    "Select Reason",
                    "Vehicle Late",
                    "Rash Driving",
                    "Misbehaviour",
                    "Vehicle not Clean",
                    "Other",
            };

    Spinner spnr;
    ArrayAdapter<String> adapter;
    ListView listView;
    private int oneWayDay, oneWayMonth, oneWayYear;
    private String[] totalPermission;
    private static final int PERMISSION_ALL = 200;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Localdialog = new Dialog(ComplaintReason.this);
        builder = new AlertDialog.Builder(this);
        Localdialog.setContentView(R.layout.custom_dialogbox);
        Localdialog.setCancelable(false);
        Localdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textView = Localdialog.findViewById(R.id.custom_dialogtext);

        //  final View view = getLayoutInflater().inflate(R.layout.activity_complaint_reason, container_body);


        setContentView(R.layout.activity_complaint_reason);
        preferencehelper=new Preferencehelper(getApplicationContext());
        totalPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};
        getAllpermisiion();

        PermissionUtils permissionModule = new PermissionUtils(ComplaintReason.this);
        permissionModule.checkPermissions();
        findViews();
        // loadspinnerreason();
        settingTimeAndDate();
//        toolbar_settxt = findViewById(R.id.txt_action);
//        toolbar_settxt.setText("Submit Complaint");
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("Submit Complaint");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        preferencehelper = new Preferencehelper(ComplaintReason.this);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            type = b.getString("type");

            tripid = b.getString("tripid");
            tripdid = b.getString("tripdid");
        }

        if (NetworkUtil.isConnectedToNetwork(ComplaintReason.this)) {
            //requestForLogin();
            getCategoryCodes(preferencehelper.getPrefsContactId(), "41");
        } else {
            Crouton.showText(ComplaintReason.this, "Please Connect To Internet", Style.ALERT);
        }

        tvPhotoCount.setText("0 Photos");
        utils = new ImageLoadingUtils(this);
    }


    private void getAllpermisiion() {
        if (!PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, totalPermission, PERMISSION_ALL);
        }
    }


    public void getCategoryCodes(final String contactid, final String types) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, ComplaintReason.this);
                        Log.e("Response", responses);

                        try {
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {

                                log.e("in success one ");
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                final ArrayList<catcodemodel> dataList = new ArrayList<catcodemodel>();
                                businesstype1 = new ArrayList<>();
                                businesstype1.add("SELECT REASON");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject loopObjects = jsonArray.getJSONObject(i);
                                    String mCatCodeID = loopObjects.getString("CatCodeID");
                                    String mCompanyID = loopObjects.getString("CompanyID");
                                    String mCodeDescription = loopObjects.getString("CodeDescription");
                                    String mCodeID = loopObjects.getString("CodeID");
                                    String mComment = loopObjects.getString("Comment");
                                    String mVal1 = loopObjects.getString("Val1");
                                    String mVal2 = loopObjects.getString("Val2");
                                    String mVal3 = loopObjects.getString("Val3");
                                    String mAssignedcodeID = loopObjects.getString("AssignedcodeID");
                                    String mAutoSMS = loopObjects.getString("AutoSMS");
                                    String mAutomail = loopObjects.getString("Automail");
                                    String mBranchID = loopObjects.getString("BranchID");
                                    businesstype1.add(mCodeDescription);

                                    catcodemodel model = new catcodemodel();
                                    model.setmCatCodeID(mCatCodeID);
                                    model.setmCompanyID(mCompanyID);
                                    model.setmCodeDescription(mCodeDescription);
                                    model.setmCodeID(mCodeID);
                                    model.setmComment(mComment);
                                    model.setmVal1(mVal1);
                                    model.setmVal2(mVal2);
                                    model.setmVal3(mVal3);
                                    model.setmAssignedcodeID(mAssignedcodeID);
                                    model.setmAutoSMS(mAutoSMS);
                                    model.setmAutomail(mAutomail);
                                    model.setmBranchID(mBranchID);
                                    dataList.add(model);
                                }

/*
                                adapter = new ArrayAdapter<String>(ComplaintReason.this, android.R.layout.simple_spinner_item, reason);
                                spnreason.setAdapter(adapter);
*/

                                myAdapter = new ArrayAdapter<String>(ComplaintReason.this, R.layout.simple_list_item_single_choice1, businesstype1);
                                spnreason.setAdapter(myAdapter);
                            } else if (success == 0) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Message.message(ComplaintReason.this, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + contactid + "&type=" + types;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ComplaintReason.this);
                    params.put("val", finalparam);
                    Log.d("afterencrption", finalparam);
                    return params;
                } catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ComplaintReason.this).add(postRequest);


    }


    private void settingTimeAndDate() {
        Calendar mcurrentTime = Calendar.getInstance();
        //mcurrentTime.add(Calendar.HOUR_OF_DAY, 1);
        oneWayDay = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        oneWayMonth = mcurrentTime.get(Calendar.MONTH) + 1;
        oneWayYear = mcurrentTime.get(Calendar.YEAR);
        //Calendar forEndTime = Calendar.getInstance();
        //forEndTime.add(Calendar.HOUR_OF_DAY, 2);

        String displayDay = String.format("%02d", oneWayDay);
        String displayMonth = String.format("%02d", oneWayMonth);
        etDate.setText(displayMonth + "/" + displayDay + "/" + oneWayYear);

    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            String vals = spinner.getItemAtPosition(i).toString();
            if (vals.equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void showOneWayDatePicker() {
        DatePickerDialog mDatePicker1;
        mDatePicker1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int selectedYear, int selectedMonth, int selectedDay) {
                oneWayYear = selectedYear;
                oneWayMonth = selectedMonth + 1;
                oneWayDay = selectedDay;
                String displayDay = String.format("%02d", oneWayDay);
                String displayMonth = String.format("%02d", oneWayMonth);
                etDate.setText(displayMonth + "/" + displayDay + "/" + oneWayYear);
            }
        }, oneWayYear, oneWayMonth - 1, oneWayDay);
        mDatePicker1.setTitle("Select Date");
        mDatePicker1.show();
    }

    private void findViews() {

        toolbar_settxt = findViewById(R.id.txt_action);
        tvcomment = (TextView) findViewById(R.id.et_comment);
        tvPhotoCount = (TextView) findViewById(R.id.tv_photo_count);
        etDate = (MaterialEditText) findViewById(R.id.et_date);
        spnreason = (Spinner) findViewById(R.id.spinner_rsn);
        ivcamera = (ImageView) findViewById(R.id.iv_camera);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btn_showreport = (Button) findViewById(R.id.btn_showreport);
        etDate.setOnClickListener(this);
        tvPhotoCount.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btn_showreport.setOnClickListener(this);
        ivcamera.setOnClickListener(this);
    }


    Map<String, String> params;


    private void buttonclickservercall(String status, String tripid, String tripdid, String comment) {

        params = new HashMap<>();


        String urlid = getString(R.string.URL_BASE_URL);

        if (tripdid.equalsIgnoreCase("")) {
            tripdid = "0";
        }
        if (tripid.equalsIgnoreCase("")) {
            tripid = "0";
        }
//        params.put("c", tripid);
//        params.put("tripdid", tripdid);
        String address = ""/*globalVariable.getaddress()*/;
        String latestDate = "";
        Date latestdate = new Date()/*globalVariable.getgdate()*/;

        String complaintdate = etDate.getText().toString();
        String todaysdate = JKHelper.gettodaysdateonly();
        DateFormat dfdb = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat dfoutput = new SimpleDateFormat("MM/dd/yyyy");
        Date startdate = null;
        Date enddts = null;
        try {
            startdate = dfdb.parse(complaintdate);
            enddts = dfdb.parse(todaysdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date tomorrow = calendar.getTime();

        if (enddts.getTime() < startdate.getTime()) {
            Message.message(ComplaintReason.this, "Complaint can not be raised for future date. Please select prior date.");
            etDate.requestFocus();
        } else {


            if (address != null) {
                address = address;
            } else {
                address = "";
            }
            if (latestdate != null) {
                latestDate = String.valueOf(latestdate);
            } else {
                latestDate = String.valueOf(latestdate);
            }

            try {
                String param = getString(R.string.URL_UPLOAD_COMPLAINT_TABLE) + "&type=" + status + "&Date=" + etDate.getText().toString()
                        + "&tripid=" + tripid + "&tripdid=" + tripdid + "&Lat=" + "" + "&Long=" + "" + "&comment=" + comment + "&reason=" + spnreason.getSelectedItem().toString() + "&Address=" + address + "&coordinatetime=" + latestDate + "&contactid=" + preferencehelper.getPrefsContactId();

                Log.d("Beforeencrptioncomplaint", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, ComplaintReason.this);
                params.put("val", finalparam);
                Log.d("afterencrptioncomplaint", finalparam);

            } catch (Exception e) {
                e.printStackTrace();

            }

            final ProgressDialog prgDialog = new ProgressDialog(ComplaintReason.this);
            prgDialog.setMessage("Please Wait.....");
            prgDialog.setCancelable(false);
            prgDialog.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, urlid,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ComplaintReason.this);
                            log.e("successcomplaint" + responses);
                            prgDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(responses);
                                int success = jsonObject.getInt("success");

                                if (success == 1) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("returnmessage");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject loopObjects = jsonArray.getJSONObject(i);
                                        String returnmessage = loopObjects.getString("status");
                                        serverid = loopObjects.getString("ids");
                                        updateserverphotoid();
                                        uploadimage();
                                        textView.setText("Complaint has been registered successfully");
                                        Localdialog.findViewById(R.id.clickok).setOnClickListener(new View.OnClickListener() {
                                            @SuppressLint("ResourceAsColor")
                                            public void onClick(View v) {


                                                Localdialog.dismiss();
                                                finish();
                                            }
                                        });

                                        Localdialog.show();
                                        //  Message.message(ComplaintReason.this, returnmessage);
                                        Crouton.makeText(ComplaintReason.this, returnmessage, Style.CONFIRM);

                                    }
                                } else if (success == 0) {
                                    Message.message(ComplaintReason.this, "Unsuccessful");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            prgDialog.dismiss();

                            Message.message(ComplaintReason.this, "Failed to Upload Status");
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

            Volley.newRequestQueue(this).add(postRequest);


        }
    }

    public void loadspinnerreason() {
        adapter = new ArrayAdapter<String>(ComplaintReason.this, android.R.layout.simple_spinner_item, reason);
        spnreason.setAdapter(adapter);
        spnreason.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
/*
                        int position = spnreason.getSelectedItemPosition();
                        String val = spnreason.getSelectedItem().toString();
                        worktypechangeevent(val);
*/
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
    }

    public void clearContols() {
onBackPressed();
        etDate.setText("");
        tvcomment.setText("");
        tvPhotoCount.setText("0 Photos");
        lastRowMaterTable = null;
        spnreason.requestFocus();
    }

    public void onClick(View v) {
        if (v == etDate) {
            showOneWayDatePicker();
        } else if (v == btnCancel) {
            clearContols();
        } else if (v == tvPhotoCount) {

            if (lastRowMaterTable == null) {
                Toast.makeText(ComplaintReason.this, "No Photos Clicked Yet", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(ComplaintReason.this, PhotosScreen.class).putExtra("photo_count", tvPhotoCount.getText().toString()).putExtra("id", lastRowMaterTable));
        } else if (v == btn_showreport) {
            startActivity(new Intent(ComplaintReason.this, complaintreport.class).putExtra("type", "3"));  // Done
        } else if (v == btnSubmit) {
            type = "3";
            {
                if (NetworkUtil.isConnectedToNetwork(ComplaintReason.this)) {

                    if (spnreason.getSelectedItem().toString().equalsIgnoreCase("SELECT REASON")) {
                        textView.setText("Please select reason");
                        Localdialog.findViewById(R.id.clickok).setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            public void onClick(View v) {


                                Localdialog.dismiss();

                            }
                        });

                        Localdialog.show();
                    } else if (type.equalsIgnoreCase("3")) {
                        buttonclickservercall("3", "0", "0", tvcomment.getText().toString());
                    } else {
                        // uploaddata();
                    }
                } else {
                    Crouton.showText(ComplaintReason.this, "Please Connect To Internet", Style.ALERT);
                }
                JKHelper.closeKeyboard(ComplaintReason.this, v);
            }
            log.e("api day attendence called====");
        } else if (v == ivcamera) {
      /*      if (!PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)||!PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)||!PermissionUtils.hasPermission(this, Manifest.permission.CAMERA)||!PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ComplaintReason.this);
                alert.setTitle(getString(R.string.app_name));
                alert.setMessage("Permission Denied to Access The Storage and Camera, Please Provide the Permission.");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(ComplaintReason.this, totalPermission, PERMISSION_ALL);
*//*
                        String [] mPermissions = new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

                        PermissionUtils.requestPermissions(ComplaintReason.this,mPermissions,PERMISSION_ALL);
     *//*               }
                });
                alert.show();
            }else{
                */

            //  openCamera(v);

            Intent intent1 = new Intent(ComplaintReason.this, ImagePickActivity.class);
            intent1.putExtra(IS_NEED_CAMERA, true);
            intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
            intent1.putExtra(IS_NEED_FOLDER_LIST, false);
            startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);
            JKHelper.closeKeyboard(ComplaintReason.this, v);

            //  }

            if (PermissionUtils.hasPermission(ComplaintReason.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {

            }
        }
    }

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(ComplaintReason.this) && !JKHelper.isServiceRunning(ComplaintReason.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(ComplaintReason.this, ImageUploadService.class));
        } else {
            stopService(new Intent(ComplaintReason.this, ImageUploadService.class));
            startService(new Intent(ComplaintReason.this, ImageUploadService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }





    File mFile1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
//                    mImages.add(path);
//                    mAdapter.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }

            }



 /*        if (requestCode == REQUEST_CODE) {
                log.e("request code true  ");
                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(ComplaintReason.this);
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                log.e("file path in request code true==" + filePath);
                new ImageCompressionAsyncTask(true).execute(path);
            }*/
            else if (requestCode == 1) {
                String message = data.getStringExtra("result");
                etchequeno.setText(message);
            }
        }

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //Log.i(TAG, "User agreed to make required location settings changes.");
                        // prefs.set(true);
                        break;
                    case Activity.RESULT_CANCELED:
                        // Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery) {
            this.fromGallery = fromGallery;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {
                filePath = compressImage(params[0]);
            } catch (Exception e) {
                Log.e("exception", e.getMessage());

            }

            return filePath;
        }

        public String compressImage(String imageUri) {

            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filename = getFilename(imageUri);
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filename;

        }

        private String getRealPathFromURI(String contentURI) {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }

        public String getFilename(String imageUri) {
          //  File file = new File(Environment.getExternalStorageDirectory().getPath(), "MmThinkBiz/Images");

            File file =getApplicationContext().getExternalFilesDir("MmThinkBiz/Images");

            if (!file.exists()) {
                file.mkdirs();
            }


            String filename = imageUri.substring(imageUri.lastIndexOf("/") + 1);

            log.e("file name in compress image== " + filename);

            String uriSting = (file.getAbsolutePath() + "/" + filename);
            log.e("uri string compress image ==" + uriSting);

            mImagePathDataBase = uriSting;
            mImageNameDataBase = filename;
            mCurrentTimeDataBase = JKHelper.getCurrentDate();

            log.e("mimage path database==" + mImagePathDataBase);
            log.e("image name==" + mImageNameDataBase);
            log.e("current image type==" + mCurrentTimeDataBase);

            return uriSting;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            log.e("post excute  ");
            addToDatabase();
        }

        private void addToDatabase() {

            if (!addedToMasterTable) {
                final ArrayList<DbImageMaster> arrayList = new ArrayList<>();
                DbImageMaster modle = new DbImageMaster();
                modle.setmDate(JKHelper.getCurrentDate());
                modle.setmUploadStatus(0);
                modle.setmDescription("Contact_Complaint");
                modle.setmImageType("Contact_Complaint");
                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, ComplaintReason.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageMasterDAO dao = new ImageMasterDAO(database, ComplaintReason.this);
                    masterDataBaseId = dao.getlatestinsertedid();
                    lastRowMaterTable = String.valueOf(masterDataBaseId);
                    mListLastRows.add(lastRowMaterTable);
                    log.e("string id ============== " + masterDataBaseId);
                }
            });


            final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
            DbImageUpload modle = new DbImageUpload();
            modle.setmDate(mCurrentTimeDataBase);
            modle.setmImageUploadStatus(0);
            modle.setmDescription("Contact_Complaint");
            modle.setmImageType("Contact_Complaint");
            modle.setmImageId(masterDataBaseId);
            modle.setmImagePath(mImagePathDataBase);
            modle.setmImageName(mImageNameDataBase);
            arrayListUpload.add(modle);


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, ComplaintReason.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            //    setPhotoCount();


        }
    }


    private void updateserverphotoid() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, ComplaintReason.this);
                pd.setWorkIdToTable(String.valueOf(serverid), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, ComplaintReason.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(serverid), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }


}

