package com.dhanuka.morningparcel.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageLoadingUtils;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.retail.GRNDetail;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import com.dhanuka.morningparcel.utils.log;
import com.example.librarymain.DhanukaMain;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.beans.LoginBeanMain;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.database.itemmasterdao;
import com.dhanuka.morningparcel.events.OnImageDeleteListener;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import com.dhanuka.morningparcel.utils.log;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class OtpActivity extends AppCompatActivity implements OnOtpCompletionListener {
    SharedPreferences prefsL;
    SharedPreferences.Editor mEditorL;
    @BindView(R.id.otpll)
    LinearLayout otpll;
    @BindView(R.id.passwordll)
    LinearLayout passwordll;
    @BindView(R.id.proceedbtn)
    TextView proceedbtn;

    @BindView(R.id.passid)
    EditText passid;
    @BindView(R.id.confirmpassid)
    EditText confirmpassid;
    @BindView(R.id.nameid)
    EditText nameid;
    @Nullable
    @BindView(R.id.imgCamera)
    ImageView imgCamera;
    ArrayList<String> mListLastRows = new ArrayList<>();

    public static Preferencehelper prefs;
    @BindView(R.id.otp1)
    EditText otptxt1;

    @BindView(R.id.otp2)
    EditText otptxt2;
    @BindView(R.id.otp3)
    EditText otptxt3;
    @BindView(R.id.otp4)
    EditText otptxt4;
    String contactno;
    int isEnabled = 0;

    @BindView(R.id.otpcontbtn)
    TextView otpcontbtn;
    StringBuilder sb;
    @BindView(R.id.otp_view)
    OtpView otpView;
    @BindView(R.id.img_show1)
    ImageView passshow1;
    @BindView(R.id.img_show2)
    ImageView passhow2;


    @BindView(R.id.img_hide1)
    ImageView passhide1;


    @BindView(R.id.img_hide2)
    ImageView passhide2;

    @BindView(R.id.txt_forgot)
    TextView sentpassword;
    LinearLayout enterll;
    TextView invalidotptxt;
    @BindView(R.id.backbtn)
    ImageView backbtn;
    @BindView(R.id.resendpass)
    TextView resendpass;
    String mno;
    private final int REQUEST_CODE = 1010;
    private String PREFS_FILE_PATH = "capture_file_path";
    String mImagePathDataBase;
    String mImageNameDataBase;
    String mCurrentTimeDataBase;
    private boolean addedToMasterTable = false;
    int masterDataBaseId;
    private ImageLoadingUtils utils;

    Dialog CameraDialog;

    private String filePath;
    String date_of_installation, hoursstr;
    @Nullable
    @BindView(R.id.timeopen)
    EditText timeopen;
    @Nullable
    @BindView(R.id.timeclose)
    EditText timeclose;

    public void openCamera(View v, int mREQUEST_CODE) {
        if (!PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionModule permissionModule = new PermissionModule(OtpActivity.this);
            permissionModule.checkPermissions();

        } else {
            SharedPreferences prefs;
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            filePath = getOutputMediaFileUri(); // create a file to save the image
            prefs = PreferenceManager.getDefaultSharedPreferences(OtpActivity.this);
            prefs.edit().putString(PREFS_FILE_PATH, filePath).commit();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath))); // set the image file name
            log.e("file path in open camera==" + filePath);
            startActivityForResult(intent, mREQUEST_CODE);
            log.e("open camera is called");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Date currentTime = Calendar.getInstance().getTime();
        timeclose.setText(currentTime.toString());
        timeopen.setText(currentTime.toString());
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();

        if (!PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionModule permissionModule = new PermissionModule(OtpActivity.this);
            permissionModule.checkPermissions();

        }
        utils = new ImageLoadingUtils(this);

        CameraDialog = new Dialog(OtpActivity.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);
        timeopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(1);
            }
        });
        timeclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(2);
            }
        });

        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(OtpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OtpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(OtpActivity.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);

                    //   launchGalleryIntent();
                    /*          Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 66);
          */
                    CameraDialog.dismiss();
                }

            }
        });
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OtpActivity.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);

                //  openCamera();
                //     launchCameraIntent();
                CameraDialog.dismiss();
            }
        });

        resendpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpverify();
            }
        });
        otpView.setOtpCompletionListener(this);
        invalidotptxt = findViewById(R.id.invalidotptxt);
        invalidotptxt.setVisibility(View.GONE);
        enterll = findViewById(R.id.enterll);
      //  cityname.setText(getIntent().getStringExtra("cityname"));
        prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mEditorL = prefsL.edit();

        if (getIntent().getStringExtra("signup").equalsIgnoreCase("1")) {
            otpll.setVisibility(View.VISIBLE);
            passwordll.setVisibility(View.GONE);
            enterll.setVisibility(View.VISIBLE);


        } else {

            otpll.setVisibility(View.VISIBLE);
            passwordll.setVisibility(View.GONE);
            enterll.setVisibility(View.GONE);


        }
        mno = getIntent().getStringExtra("mno");
        sentpassword.setText("One Time Password Sent to " + mno);

        passhide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputTypeValue = passid.getInputType();
                passshow1.setVisibility(View.VISIBLE);
                passhide1.setVisibility(View.GONE);
                passid.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
        passshow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputTypeValue = passid.getInputType();
                passid.setTransformationMethod(null);
                passshow1.setVisibility(View.GONE);
                passhide1.setVisibility(View.VISIBLE);

            }
        });

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraDialog.show();
            }
        });

        passhide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputTypeValue = confirmpassid.getInputType();
                passhow2.setVisibility(View.VISIBLE);
                passhide2.setVisibility(View.GONE);
                confirmpassid.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        passhow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputTypeValue = confirmpassid.getInputType();
                confirmpassid.setTransformationMethod(null);
                passhow2.setVisibility(View.GONE);
                passhide2.setVisibility(View.VISIBLE);

            }
        });
        confirmpassid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (passid.getText().toString().equalsIgnoreCase(confirmpassid.getText().toString())) {
                    proceedbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    proceedbtn.setEnabled(true);


                } else {
                    proceedbtn.setEnabled(false);
                    proceedbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));

                }

            }
        });
        otpView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    otpcontbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    otpcontbtn.setEnabled(true);


                } else {
                    otpcontbtn.setEnabled(false);
                    otpcontbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));

                }


            }
        });

//        if (otpll.getVisibility() == View.VISIBLE) {
//            otpll.setVisibility(View.GONE);
//        } else if (passwordll.getVisibility() == View.GONE) {
//
//
//
//        }


        contactno = getIntent().getStringExtra("mno");
        prefs = new Preferencehelper(getApplicationContext());

        sb = new StringBuilder();
        proceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signupapi();
            }
        });
        proceedbtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (passid.getText().toString().length() > 4) {
                    proceedbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isEnabled = 1;
                } else {
                    proceedbtn.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                    isEnabled = 0;
                }

            }
        });


        otpcontbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prefs.getPrefsOtp().equalsIgnoreCase(otpView.getText().toString())) {

                    if (getIntent().getStringExtra("signup").equalsIgnoreCase("1")) {
                        LoginBeanMain loginBeanmain = (LoginBeanMain) getIntent().getSerializableExtra("mbeanlogin");
                        moveToNextWithPaymentMode(loginBeanmain);

                    } else {
                        passwordll.setVisibility(View.VISIBLE);
                        otpll.setVisibility(View.GONE);
                    }

                } else {

                    invalidotptxt.setVisibility(View.VISIBLE);


                    Toast.makeText(getApplicationContext(), "Otp is not match", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void Signupapi() {


        final ProgressDialog mProgressBar = new ProgressDialog(OtpActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.dismiss();
                        try {
                            String responses = DhanukaMain.SafeOBuddyDecryptUtils(response);
                            Log.d("responseSignup", responses);

                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String returnmessage = newjson.getString("returnmessage");
                                    serverid = returnmessage;
                                    updateserverphotoid1();
                                    updateserverphotoid();
                                    uploadimage();

                                    prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                            MODE_PRIVATE);
                                    mEditorL = prefsL.edit();
                                    prefs.setPREFS_firstname(nameid.getText().toString());
                                    if (prefs.getPrefsCountry().equalsIgnoreCase("USA")) {
                                        mEditorL.putString("cntry", "United States");
                                        mEditorL.commit();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class).putExtra("mno", getIntent().getStringExtra("mno")));
                                        prefs.clearAllPrefs();
                                    } else {
                                        mEditorL.putString("cntry", "India");
                                        mEditorL.commit();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class).putExtra("mno", getIntent().getStringExtra("mno")));
                                        prefs.clearAllPrefs();


                                    }


                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        try {

                            mProgressBar.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                            MODE_PRIVATE);
                    String cntry = prefs1.getString("cntry", "India");


                    String param = com.dhanuka.morningparcel.utils.AppUrls.strSignup + "&fname=" + nameid.getText().toString() + "&lname=" + "" + "&gender=" + "" + "&country=" + prefs.getPrefsCountry() + "&city=" + prefs.getPREFS_city() + "&username=" + contactno + "&password=" + passid.getText().toString()
                            + "&email=" + "" + "&bday=" + "01/01/1800" + "&mobileno=" + contactno + "&zip=" + "" + "&securityq=" + "" + "&securityp=" + "" + "&address=" + "" +
                            "&alternatephone=" + "" + "&alternatemail=" + "" + "&security2=" + "" + "&securitya2=" + "test" + "&type=" + "0" + "&state=" + "" + "&servicetype=" + "CONSUMER" + "&contactid=" + "0" +
                            "&flatno=" + "" + "&building=" + "" + "&society=" + "" + "&companyid=549"+    "&StoreOpenTime="+timeopen.getText().toString() +"&StoreCloseTime="+timeclose.getText().toString();;
                    Log.e("beforeenc", param);
                    JKHelper jkHelper = new JKHelper();
                    param = jkHelper.Encryptapi(param, OtpActivity.this);
                    params.put("val", param);
                    Log.e("afterenc", param);
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

        Volley.newRequestQueue(this).add(postRequest);


    }

    public void moveToNextWithPaymentMode(LoginBeanMain mLoginbeanmain) {

        if (mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1058")) {
            startActivity(new Intent(OtpActivity.this, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();

        } else if (!mLoginbeanmain.getmListLogin().get(0).getStrblatlong().equalsIgnoreCase("0")) {
            Intent intent = null;
            //  startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            mEditorL.putString("shopId", "");
            mEditorL.commit();

            if (prefsL.getString("cntry", "").isEmpty()) {

                intent = new Intent(OtpActivity.this, ChooseLocationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            } else {
                if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {

                    intent = new Intent(OtpActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    intent = new Intent(OtpActivity.this, OptionChooserActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                }
            }
            startActivity(intent);
        } else {
            startActivity(new Intent(OtpActivity.this, AddLatLong.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        }


    }


    @Override
    public void onOtpCompleted(String otp) {


    }

    public void otpverify() {


        final ProgressDialog mProgressBar = new ProgressDialog(OtpActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.dismiss();
                        try {
                            String responses = DhanukaMain.SafeOBuddyDecryptUtils(response);
                            Log.d("responseSignup", responses);

                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {
                                String otpvr = jsonObject.getString("OTP");
                                if (otpvr.equalsIgnoreCase("0")) {
                                    Toast.makeText(getApplicationContext(), "User Already Registered .Please Login", Toast.LENGTH_LONG).show();

                                  startActivity(new Intent(getApplicationContext(),LoginActivity.class));


                                } else {
                                    prefs.setPrefsOtp(otpvr);
                                }


                               //startActivity(new Intent(getApplicationContext(),OtpActivity.class).putExtra("mno",entrcntcnol.getText().toString()).putExtra("signup","1"));


                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        try {

                            mProgressBar.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                            MODE_PRIVATE);
                    String cntry = prefs1.getString("cntry", "India");


                    String param = com.dhanuka.morningparcel.utils.AppUrls.checkotp + "&phoneno=" + mno + "&country=" + prefs.getPrefsCountry();
                    Log.e("beforeenc", param);
                    JKHelper jkHelper = new JKHelper();
                    param = jkHelper.Encryptapi(param, OtpActivity.this);
                    params.put("val", param);
                    Log.e("afterenc", param);
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

        Volley.newRequestQueue(this).add(postRequest);


    }

    public String getOutputMediaFileUri() {
        return getOutputMediaFile().getAbsolutePath();
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Mmthinkbiz");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "Mtb_" + timeStamp + ".jpg");
        return mediaFile;
    }

    int asREQUEST_CODE = 1010;
    File mFile1;

    public void loadImage(String str) {
        Glide.with(OtpActivity.this)
                .load(str)
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(imgCamera);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<String> mImages = new ArrayList<>();
            if (requestCode == com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    mImages.add(path);
                    loadImage(path);
//                    mAdapter1.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }

            Bitmap bitmap = null;

            if (requestCode == com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    mImages.add(path);
//                    mAdapter1.addItems(mImages);
                    loadImage(path);
//                    //  if (!mBean.getStrrmasterid().isEmpty()) {

                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }
            if (requestCode == REQUEST_CODE) {
                asREQUEST_CODE = requestCode;
                log.e("request code true  ");
                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(OtpActivity.this);
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                log.e("file path in request code true==" + filePath);
                new ImageCompressionAsyncTask(true).execute(path);
            }

        }

    }

    String TAG = "GRM";


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

            options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
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
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "MmThinkBiz/Images");

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

                modle.setmDescription("user_Master");
                modle.setmImageType("user_Master");

                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, OtpActivity.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
// need to change this logic to use max number from database, instead of todocount -- Gunjan 11/04/2015
                    ImageMasterDAO dao = new ImageMasterDAO(database, OtpActivity.this);
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
            if (asREQUEST_CODE == 1010) {
                modle.setmDescription("Gr_detail");
                modle.setmImageType("Gr_detail");
            } else {
                modle.setmDescription("user_Master");
                modle.setmImageType("user_Master");
            }
            modle.setmImageId(masterDataBaseId);
            modle.setmImagePath(mImagePathDataBase);
            modle.setmImageName(mImageNameDataBase);
            arrayListUpload.add(modle);


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, OtpActivity.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            setPhotoCount();
        }
    }

    private void setPhotoCount() {
/*
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, OtpActivity.this);
                int count = dao.getCurrentWorkOrderImageCount(lastRowMaterTable);
                if (prefs.isDayStart()) {
                    tvPhotoCount.setText(count + " Photos");
                }
                log.e("photo inserted count==" + count);
            }
        });
*/

    }

    String lastRowMaterTable;

    private void updateserverphotoid() {
        // change it for server exp id
        if (lastRowMaterTable != null) {
            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            ImageUploadDAO pd = new ImageUploadDAO(database, OtpActivity.this);

            pd.setWorkIdToTable(String.valueOf(serverid), lastRowMaterTable);

            //           }
            ImageMasterDAO pds = new ImageMasterDAO(database, OtpActivity.this);
            pds.setServerDetails(Integer.parseInt(lastRowMaterTable), Integer.valueOf(serverid), 0);
            ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
            String serverId = iDao.getServerIdById(1);
            DatabaseManager.getInstance().closeDatabase();
            log.e("in last row master table inserting  ");
        }
    }


    private void updateserverphotoid1() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, OtpActivity.this);
                pd.setWorkIdToTable(String.valueOf(serverid), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, OtpActivity.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(serverid), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }

    String serverid = "";

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(OtpActivity.this) && !JKHelper.isServiceRunning(OtpActivity.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(OtpActivity.this, ImageUploadService.class));
        } else {
            stopService(new Intent(OtpActivity.this, ImageUploadService.class));
            startService(new Intent(OtpActivity.this, ImageUploadService.class));
        }
    }


    public void showHourPicker(int type) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hoursstr = Integer.toString(hourOfDay);


                String minutestr = Integer.toString(minute);
                if (view.isShown()) {

                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                }


                SimpleDateFormat format = new SimpleDateFormat("HH", Locale.US);
                String hour = format.format(new Date());


                Calendar calendar = Calendar.getInstance();
                int hourOfDayy = calendar.get(Calendar.HOUR_OF_DAY);

                if (hourOfDay < 10) {


                    hoursstr = "0" + hourOfDay;

                }
                if (minute < 10) {

                    minutestr = "0" + minutestr;
                }

                if (type == 1) {
                    timeopen.setText(hoursstr + ":" + minutestr);


                } else if (type == 2) {
                    timeclose.setText(hoursstr + ":" + minutestr);

                }


                //    Toast.makeText(getApplicationContext(),String.valueOf(hourOfDayy)+"more",Toast.LENGTH_LONG).show();

                //   timePicker1.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(OtpActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

}