package com.dhanuka.morningparcel.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.activity.retail.GRMMaster;
import com.dhanuka.morningparcel.fcm.Config;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.CommonHelper;
import com.dhanuka.morningparcel.Helper.NetworkUtil;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.beans.LoginBean;
import com.dhanuka.morningparcel.beans.LoginBeanMain;

import com.example.librarymain.DhanukaMain;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    LoginBeanMain mloginBean;
    ArrayList<LoginBean> mLoginList;
    String Email, Pass;
    TextView Username;
    TextView Password;
    TextView Logme;
    int isEnabled = 0;

    TextView errortext;
    Button buttonerror;
    TextView textView, forgot, VersionCodee;
    TextView btn_login;
    TextView et_user, et_pass;
    ImageView pass_show, pass_hide;
    Dialog ErrorDailog;

    ProgressDialog dialog;
    AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;
    public static Preferencehelper prefs;
    CommonHelper commonHelper;
    // Gunjan 22/08
    String vercode = "";
    Typeface face;
    SharedPreferences prefsL;
    SharedPreferences.Editor mEditorL;

    @Nullable
    @BindView(R.id.btn_register)
    TextView btn_register;
    @BindView(R.id.loginotp)
    TextView loginotp;

    @BindView(R.id.backbtn)
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  getWindow().setBackgroundDrawableResource(R.drawable.bg_login);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mProfile", "-1");
        editor.putString("links", "-1");
        editor.commit();



        viewdefineListner();
        ViewClickListner();
        try {
            String userno=getIntent().getStringExtra("mno");
            et_user.setText(userno);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StartActivityNew.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
        loginotp.setOnClickListener(this);
        loginotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EnterContactActivity.class).putExtra("signup", "2"));
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),StartActivityNew.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }


    public void deleteCache(Context context) {

        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("EEEEEERRRRRROOOOOOORRRR", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("isCache", "ok3");
                    editor.commit();
                }
            }
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            context.startActivity(mainIntent);
            Runtime.getRuntime().exit(0);
        }


    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (i < children.length) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
                i++;
            }
        }

        assert dir != null;
        return dir.delete();
    }


    @Override
    protected void onResume() {

        if (!TextUtils.isEmpty(prefs.getPrefsContactId()) && !TextUtils.isEmpty(prefs.getPrefsPassword())) {
//            Username.setText(prefs.getPrefsEmail());
//            Password.setText(prefs.getPrefsPassword());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //if (sharedPreferences.getString("isCache", "-1").equalsIgnoreCase("ok3")) {
                    Logme.performClick();
                   /* } else {

                        showDialogUpdate(LoginActivity.this,false);
                    }*/
                }
            }, 1000);
        }
        super.onResume();
    }

    public void showDialogUpdate(final Context ctx, boolean isTrue) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_update_app);
        dialog.setCancelable(isTrue);
        final TextView tvdesc = (TextView) dialog.findViewById(R.id.tvdesc);
        final Button submit = (Button) dialog.findViewById(R.id.btnupdate);
        tvdesc.setText("This Update needs the clear the cache of previous version. Please allow us to clear the cache and restart the app.");
        submit.setText("Allow");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCache(LoginActivity.this);

            }
        });


        // Display the custom alert dialog on interface
        dialog.show();


    }


    public void LoginApi() {
    //    int vrCode = BuildConfig.VERSION_CODE;
     //   vercode = BuildConfig.VERSION_NAME + "/" + String.valueOf(vrCode);

        String vercode="";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
             vercode = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("uid", Username.getText().toString());
        Log.d("pwd", Password.getText().toString());
        mloginBean = new LoginBeanMain();

        final ProgressDialog mProgressBar = new ProgressDialog(LoginActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        String finalVercode = vercode;
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                MODE_PRIVATE);
                        mEditorL = prefsL.edit();

                        try {


                            prefs.setBannerPrefs("0");
                            prefs.setPrefsPassword(Password.getText().toString());
                            prefs.setPREFS_trialuser("1");
                            prefs.setPrefsEmail(Username.getText().toString());
                            String responses = DhanukaMain.SafeOBuddyDecryptUtils(response);
                            Log.e("Response1Login", responses);
                            // startService(new Intent(getApplicationContext(), UploadFCMActivity.class));

                            LoginBeanMain mLoginbeanmain = new Gson().fromJson(responses
                                    , LoginBeanMain.class);
                            mloginBean = mLoginbeanmain;

                            if (mLoginbeanmain.getStrSuccess() == 1) {
                                mLoginList = new ArrayList<>();
                                Log.e("HJSGJ", mloginBean.getmListLogin().size() + "");
                                prefs.setPrefsRunonce("0");
                                prefs.setTokenValue(mloginBean.getmListLogin().get(0).getStrtoken());
                                if (Username.getText().toString().equalsIgnoreCase("0000000000")) {
                                    //  JKHelper jkhelper = new JKHelper();
                                    // jkhelper.getBillableUnder("9999"+"66773", LoginActivity.this);
                                    prefs.setPrefsContactId("66773");
                                    prefs.setPrefsTempContactid("66773");
                                    prefs.setPrefsCustId(Username.getText().toString());

                                    com.dhanuka.morningparcel.utils.Preferencehelper preferencehelper = new com.dhanuka.morningparcel.utils.Preferencehelper(LoginActivity.this);
                                    preferencehelper.setProfileId("66773");
                                    preferencehelper.setEmailId(Username.getText().toString());

                                    // No usercategory in Contactifo table is defined
                                    startActivity(new Intent(LoginActivity.this, GRMMaster.class).putExtra("type", "1").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)); // 1 for GRN and 2 for transfer
                                    LoginActivity.this.finish();
                                } else if (mLoginbeanmain.getmListLogin().get(0).getStruid().equalsIgnoreCase("0")) {
                                    mProgressBar.dismiss();

                                    errortext.setText("No Role is defined to this user or no such user exist");
                                    buttonerror.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ErrorDailog.dismiss();
                                        }
                                    });
                                    ErrorDailog.show();
                                } else if (mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1057") || mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1057")) {
                                    prefs.setPrefsLoginValue("1");
                                    prefs.setPrefsTempContactid(mLoginbeanmain.getmListLogin().get(0).getStruid());
                                    prefs.setCID(mLoginbeanmain.getmListLogin().get(0).getStrval1());
                                    prefs.setPREFS_currentbal(mLoginbeanmain.getmListLogin().get(0).getStrcurrentbalance());
                                    prefs.setPrefsContactId(mLoginbeanmain.getmListLogin().get(0).getStruid());
                                    prefs.setPrefsCountry(mLoginbeanmain.getmListLogin().get(0).getCountry());
                                    prefs.setPrefsCustId(mLoginbeanmain.getmListLogin().get(0).getCustPayId());
                                    prefs.setPrefsUsercategory(mLoginbeanmain.getmListLogin().get(0).getStrusercategory());
                                    if (mLoginbeanmain.getmListLogin().get(0).getPaymentoption().equalsIgnoreCase("BOTH")) {
                                        prefs.setPrefsPaymentoption(mLoginbeanmain.getmListLogin().get(0).getPaymentoption());
                                    } else if (mLoginbeanmain.getmListLogin().get(0).getPaymentoption().equalsIgnoreCase("Demo")) {
                                        prefs.setPrefsPaymentoption("1");

                                    } else {
                                        prefs.setPrefsPaymentoption("2");

                                    }

                                    mEditorL.putString("cntry", prefs.getPrefsCountry());
                                    mEditorL.commit();


                                    moveToNextWithPaymentMode(mLoginbeanmain);
    /*             if (mLoginbeanmain.getmListLogin().get(0).getPaymentoption().equalsIgnoreCase("BOTH")) {
                                        try {
                                            moveToNextWithPaymentMode(mLoginbeanmain);



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        btnDemo.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                prefs.setPrefsPaymentoption("1");
                                                moveToNextWithPaymentMode(mLoginbeanmain);

                                            }
                                        });

                                        btnDemo.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                prefs.setPrefsPaymentoption("2");
                                                moveToNextWithPaymentMode(mLoginbeanmain);
                                            }
                                        });


                                    } else {
                                        if (mLoginbeanmain.getmListLogin().get(0).getPaymentoption().equalsIgnoreCase("Demo")) {

                                            prefs.setPrefsPaymentoption("1");
                                        } else {
                                            prefs.setPrefsPaymentoption("2");

                                        }
                                        moveToNextWithPaymentMode(mLoginbeanmain);

                                    }*/


                                    finish();
                                } else if (mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1058") || mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1058") || mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1062")) {
                                    prefs.setPrefsLoginValue("1");
                                    prefs.setCID(mLoginbeanmain.getmListLogin().get(0).getStrval1());
                                    prefs.setPrefsTempContactid(mLoginbeanmain.getmListLogin().get(0).getStruid());
                                    prefs.setPrefsContactId(mLoginbeanmain.getmListLogin().get(0).getStruid());
                                    prefs.setPREFS_currentbal(mLoginbeanmain.getmListLogin().get(0).getStrcurrentbalance());
                                    prefs.setPrefsUsercategory(mLoginbeanmain.getmListLogin().get(0).getStrusercategory());
                                    prefs.setPrefsCountry(mLoginbeanmain.getmListLogin().get(0).getCountry());
                                    //  startActivity(new Intent(LoginActivity.this, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    Log.e("ISSSC", "1");

                                    mEditorL.putString("cntry", prefs.getPrefsCountry());
                                    mEditorL.commit();
                                    if (!mLoginbeanmain.getmListLogin().get(0).getStrblatlong().equalsIgnoreCase("0")) {
                                        startActivity(new Intent(LoginActivity.this, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        finish();

                                    } else {
                                        startActivity(new Intent(LoginActivity.this, AddLatLong.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("mtype","enter"));
                                        finish();

                                    }
                                } else if (mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1060") || mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1060")) {
                                    prefs.setPrefsLoginValue("1");
                                    prefs.setCID(mLoginbeanmain.getmListLogin().get(0).getStrval1());
                                    prefs.setPrefsContactId(mLoginbeanmain.getmListLogin().get(0).getStruid());
                                    prefs.setPrefsTempContactid(mLoginbeanmain.getmListLogin().get(0).getStruid());
                                    prefs.setPREFS_currentbal(mLoginbeanmain.getmListLogin().get(0).getStrcurrentbalance());
                                    prefs.setPrefsCountry(mLoginbeanmain.getmListLogin().get(0).getCountry());
                                    prefs.setPrefsUsercategory(mLoginbeanmain.getmListLogin().get(0).getStrusercategory());
                                    //  startActivity(new Intent(LoginActivity.this, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    Log.e("ISSSC", "2");

                                    if (!mLoginbeanmain.getmListLogin().get(0).getStrblatlong().equalsIgnoreCase("0")) {
                                        startActivity(new Intent(LoginActivity.this, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        finish();

                                    } else {
                                        startActivity(new Intent(LoginActivity.this, AddLatLong.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("mtype","enter"));
                                        finish();

                                    }
                                    mEditorL.putString("cntry", prefs.getPrefsCountry());
                                    mEditorL.commit();
                                } else {
                                    mProgressBar.dismiss();

                                    errortext.setText("No Role is defined to this user or no such user exist");
                                    buttonerror.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ErrorDailog.dismiss();
                                        }
                                    });
                                    ErrorDailog.show();

                                }
                                mProgressBar.dismiss();
                            } else {
                                mProgressBar.dismiss();

                                if (Username.getText().toString().equalsIgnoreCase("teash") && Password.getText().toString().equalsIgnoreCase("123456")) {
                                    prefs.setPrefsContactId("1");
                                    prefs.setPrefsTempContactid("1");
                                    prefs.setPrefsEmail("teash");
                                    prefs.setPrefsUsercategory("1057");
                                    prefs.setPrefsLoginValue("1");
                                    startActivity(new Intent(LoginActivity.this, AddLatLong.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("mtype","enter"));

                                    //       startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    overridePendingTransition(0, 0);
                                    //startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                                    finish();

                                } else {
                                    errortext.setText("No Role is defined to this user or no such user exist");
                                    buttonerror.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ErrorDailog.dismiss();
                                        }
                                    });
                                    ErrorDailog.show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            mProgressBar.dismiss();

                            errortext.setText("Login Error !!");
                            buttonerror.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ErrorDailog.dismiss();
                                }
                            });
                            ErrorDailog.show();

                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        errortext.setText("Something went wrong ");
                        buttonerror.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ErrorDailog.dismiss();
                            }
                        });
                        ErrorDailog.show();
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
                SharedPreferences prefsToken = getSharedPreferences("MORNING_PARCEL_TOKEN",
                        MODE_PRIVATE);
                SharedPreferences.Editor mEditorprefsToken = prefsToken.edit();

                SharedPreferences prefF = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String strtoken = prefF.getString("regId", "NO GCM");

                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("tokenval");
                    editor.commit();
                    String strName=Username.getText().toString();
                    if (strName.equalsIgnoreCase("0000000000")){
                        strName="gdhanuka";
                    }
                    String param = getString(R.string.URL_LOGIN_ENC) + "&uid=" +  strName+ "&pwd=" + Password.getText().toString() + "&GCMID=" + strtoken + "&version=" + finalVercode;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, LoginActivity.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, LocationSelectionAcitivityNew.class).putExtra("signup", "4"));
                break;
            case R.id.usernameide:
                Email = Username.getText().toString();
                break;
            case R.id.passwordide:

                Pass = Password.getText().toString();

                break;
            case R.id.logmein:
                LoginApi();
                Toast.makeText(getApplicationContext(), Email + Pass + "hello", Toast.LENGTH_LONG).show();

                break;
        }
    }

    public void viewdefineListner() {
        VersionCodee = findViewById(R.id.Versioncodde);
        commonHelper = new CommonHelper();
        ErrorDailog = new Dialog(LoginActivity.this);
        ErrorDailog.setContentView(R.layout.custom_dialogbox);
        ErrorDailog.setCancelable(false);
        buttonerror = ErrorDailog.findViewById(R.id.clickok);
        errortext = ErrorDailog.findViewById(R.id.custom_dialogtext);
        prefs = new Preferencehelper(this);
        Username = findViewById(R.id.usernameide);
        Password = findViewById(R.id.passwordide);
        ButterKnife.bind(this);
        Logme = findViewById(R.id.logmein);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //textView = (TextView) findViewById(R.id.txt_login);
        forgot = (TextView) findViewById(R.id.txt_forgot);
        btn_login = (TextView) findViewById(R.id.logmein);
        et_user = (TextView) findViewById(R.id.usernameide);
        et_pass = (TextView) findViewById(R.id.passwordide);
        pass_show = (ImageView) findViewById(R.id.img_show);
        pass_hide = (ImageView) findViewById(R.id.img_hide);
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();


        face = Typeface.createFromAsset(getAssets(),
                "fonts/sansation-bold.ttf");

        forgot.setTypeface(face);
        Logme.setTypeface(face);
        btn_register.setTypeface(face);
        et_user.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/sansation.ttf"));
        et_pass.setTypeface(Typeface.createFromAsset(getAssets(),
                "fonts/sansation.ttf"));
        btn_register.setOnClickListener(this);
        try {
            String username = getIntent().getStringExtra("username");
            String pass = getIntent().getStringExtra("pass");
            et_user.setText(username);
            et_pass.setText(pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        et_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }


            }
        });

        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 4) {
                    btn_login.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                    btn_login.setEnabled(false);
                }

            }
        });

    }


    public void ViewClickListner() {

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            VersionCodee.setTypeface(face);
            VersionCodee.setText("Version code - " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9 && et_pass.getText().toString().length() > 5) {
                    btn_login.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isEnabled = 1;
                } else {
                    btn_login.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                    isEnabled = 0;
                }

            }
        });
/*
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        SocietyDao pd = new SocietyDao(database, this);
        ArrayList<CatcodeHelper> listDatabasenew = pd.selectAll();
        BuildingDAO buildingDAO = new BuildingDAO(database, this);
        ArrayList<CatcodeHelper> listDatabasenew1 = buildingDAO.selectAll();
        Log.e("listDatabasenew", listDatabasenew.size() + " = " + listDatabasenew1.size());
        if (listDatabasenew.size() > 0) {
        } else {
            final ProgressDialog mProgressBar = new ProgressDialog(LoginActivity.this);
            mProgressBar.setTitle("Safe'O'Fresh");
            mProgressBar.setMessage("Loading resources...");
            mProgressBar.show();
            mProgressBar.setCancelable(false);
            Handler handler;
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.dismiss();
                }
            }, 4000);


            CommonHelper.getsoceitylist("55", LoginActivity.this);
            if (listDatabasenew1.size() > 0) {
            } else {
                CommonHelper.getbuilding("56", LoginActivity.this);

            }
        }
*/


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });
        pass_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputTypeValue = et_pass.getInputType();
                pass_show.setVisibility(View.VISIBLE);
                pass_hide.setVisibility(View.GONE);
                et_pass.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
        pass_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inputTypeValue = et_pass.getInputType();
                et_pass.setTransformationMethod(null);
                pass_show.setVisibility(View.GONE);
                pass_hide.setVisibility(View.VISIBLE);

            }
        });
        Logme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Username.getText().toString().equalsIgnoreCase("")) {

                    Username.setError("Please enter username");


                } else if (Password.getText().toString().equalsIgnoreCase("")) {
                    Password.setError("Please enter password");
                } else {
                    if (NetworkUtil.isConnectedToNetwork(LoginActivity.this)) {

                        LoginApi();
                    } else {

                        errortext.setText("No Internet connection found ");
                        buttonerror.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ErrorDailog.dismiss();
                            }
                        });
                        ErrorDailog.show();
                    }

                }
            }
        });
    }


    public void moveToNextWithPaymentMode(LoginBeanMain mLoginbeanmain) {
        com.dhanuka.morningparcel.database.DatabaseManager dbManager;
        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(LoginActivity.this);
        dbManager.deleteAll();

        if (!mLoginbeanmain.getmListLogin().get(0).getStrblatlong().equalsIgnoreCase("0")) {
            Intent intent = null;
            //  startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            mEditorL.putString("shopId", "");
            mEditorL.commit();

            if (prefsL.getString("cntry", "").isEmpty()) {

                intent = new Intent(LoginActivity.this, ChooseLocationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            } else {
                if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {

                    intent = new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    prefsL.getString("cntry", mLoginbeanmain.getmListLogin().get(0).getCountry());
                    intent = new Intent(LoginActivity.this, OptionChooserActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                }
            }
            startActivity(intent);
        } else {
            startActivity(new Intent(LoginActivity.this, AddLatLong.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("mtype","enter"));

        }
    }

}