package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.example.librarymain.DhanukaMain;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.beans.LoginBean;
import com.dhanuka.morningparcel.beans.LoginBeanMain;
import com.dhanuka.morningparcel.fcm.Config;
import com.dhanuka.morningparcel.utils.JKHelper;

public class EnterContactActivity extends AppCompatActivity {
    @BindView(R.id.entrcntcno)
    EditText entrcntcnol;
    @BindView(R.id.cityname)
    TextView cityname;
    @BindView(R.id.logmein)
    TextView logmein;
    int isEnabled = 0;
    String vercode = "";
    Preferencehelper prefs;
    LoginBeanMain mloginBean;
    ArrayList<LoginBean> mLoginList;
    Typeface face;
    SharedPreferences prefsL;
    SharedPreferences.Editor mEditorL;
    Dialog ErrorDailog;
    TextView errortext;
    Button buttonerror;
    TextView textView, forgot, VersionCodee;
    Dialog logindialog;
    TextView login,forgotpass;
    @BindView(R.id.backbtn)
    ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_contact);
        ButterKnife.bind(this);



        prefs = new Preferencehelper(this);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        String checksignup=getIntent().getStringExtra("signup");
        String city=getIntent().getStringExtra("cityname");
        cityname.setText(city);
        Log.d("Pringsignval",checksignup);
        logindialog = new Dialog(EnterContactActivity.this);
        logindialog.setContentView(R.layout.login_forgot_dialog);
        login = logindialog.findViewById(R.id.loginbtnn);
        forgotpass= logindialog.findViewById(R.id.passbtnn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                logindialog.dismiss();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
              logindialog.dismiss();
            }
        });

        logmein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checksignup.equalsIgnoreCase("1"))
                {
                    otpverify();



                }
                else
                {
                    LoginApi();

                }


            }
        });
        entrcntcnol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9 && entrcntcnol.getText().toString().length() > 5) {
                    logmein.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isEnabled = 1;
                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                } else {
                    logmein.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                    isEnabled = 0;
                }

            }
        });

    }

    public void otpverify() {


        final ProgressDialog mProgressBar = new ProgressDialog(EnterContactActivity.this);
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
                                if (otpvr.equalsIgnoreCase("0"))
                                {
                                    Toast.makeText(getApplicationContext(),"User Already Registered .Please Login",Toast.LENGTH_LONG).show();

                                   // startActivity(new Intent(getApplicationContext(),LoginActivity.class).putExtra("mno",entrcntcnol.getText().toString()).putExtra("cityname",cityname.getText().toString()));
                                    prefs.setPrefsOtp(otpvr);
                                    logindialog.show();
                                }
                                else
                                {
                                    startActivity(new Intent(getApplicationContext(),OtpActivity.class).putExtra("mno",entrcntcnol.getText().toString()).putExtra("signup","2").putExtra("cityname",cityname.getText().toString()));
                                    prefs.setPrefsOtp(otpvr);
                                }


                               // startActivity(new Intent(getApplicationContext(),OtpActivity.class).putExtra("mno",entrcntcnol.getText().toString()).putExtra("signup","1"));


                            }
                            else {
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


                    String param = com.dhanuka.morningparcel.utils.AppUrls.checkotp + "&phoneno=" + entrcntcnol.getText().toString() + "&country=" + prefs.getPrefsCountry();
                    Log.e("beforeenc", param);
                    JKHelper jkHelper = new JKHelper();
                    param = jkHelper.Encryptapi(param, EnterContactActivity.this);
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

    public void LoginApi() {
//        int vrCode = BuildConfig.VERSION_CODE;
//        vercode = BuildConfig.VERSION_NAME + "/" + String.valueOf(vrCode);

        mloginBean = new LoginBeanMain();

        final ProgressDialog mProgressBar = new ProgressDialog(EnterContactActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                MODE_PRIVATE);
                        mEditorL = prefsL.edit();

                        try {

                            prefs.setBannerPrefs("0");
                            // prefs.setPrefsPassword(Password.getText().toString());
                            prefs.setPrefsEmail(entrcntcnol.getText().toString());
                            String responses = DhanukaMain.SafeOBuddyDecryptUtils(response);
                            Log.e("Response1", responses);
                            // startService(new Intent(getApplicationContext(), UploadFCMActivity.class));

                            LoginBeanMain mLoginbeanmain = new Gson().fromJson(responses
                                    , LoginBeanMain.class);
                            mloginBean = mLoginbeanmain;

                            if (mLoginbeanmain.getStrSuccess() == 1) {
                                mLoginList = new ArrayList<>();
                                Log.e("HJSGJ", mloginBean.getmListLogin().size() + "");
                                prefs.setPrefsRunonce("0");
                                prefs.setTokenValue(mloginBean.getmListLogin().get(0).getStrtoken());
                                if (mLoginbeanmain.getmListLogin().get(0).getStruid().equalsIgnoreCase("0")) {
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
                                    prefs.setCID(mLoginbeanmain.getmListLogin().get(0).getStrval1());
                                    prefs.setPrefsOtp(mLoginbeanmain.getmListLogin().get(0).getOTP());
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


                                  startActivity(new Intent(getApplicationContext(),OtpActivity.class).putExtra("signup","1").putExtra("mbeanlogin",mLoginbeanmain));
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
                                } else if (mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1058") || mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1058")) {
                                    prefs.setPrefsLoginValue("1");
                                    prefs.setCID(mLoginbeanmain.getmListLogin().get(0).getStrval1());
                                    prefs.setPrefsContactId(mLoginbeanmain.getmListLogin().get(0).getStruid());
                                    prefs.setPrefsOtp(mLoginbeanmain.getmListLogin().get(0).getOTP());

                                    prefs.setPREFS_currentbal(mLoginbeanmain.getmListLogin().get(0).getStrcurrentbalance());
                                    prefs.setPrefsUsercategory(mLoginbeanmain.getmListLogin().get(0).getStrusercategory());
                                    //  startActivity(new Intent(LoginActivity.this, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    if (!mLoginbeanmain.getmListLogin().get(0).getStrblatlong().equalsIgnoreCase("0")) {
                                        startActivity(new Intent(EnterContactActivity.this, OtpActivity.class).putExtra("signup","1").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("mbeanlogin",mLoginbeanmain));
                                        finish();

                                    } else {
                                        startActivity(new Intent(EnterContactActivity.this, OtpActivity.class).putExtra("signup","1").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("mbeanlogin",mLoginbeanmain));
                                        finish();

                                    }
                                } else if (mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1060") || mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1060")) {
                                    prefs.setPrefsLoginValue("1");
                                    prefs.setCID(mLoginbeanmain.getmListLogin().get(0).getStrval1());
                                    prefs.setPrefsContactId(mLoginbeanmain.getmListLogin().get(0).getStruid());
                                    prefs.setPREFS_currentbal(mLoginbeanmain.getmListLogin().get(0).getStrcurrentbalance());
                                    prefs.setPrefsUsercategory(mLoginbeanmain.getmListLogin().get(0).getStrusercategory());
                                    //  startActivity(new Intent(LoginActivity.this, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    if (!mLoginbeanmain.getmListLogin().get(0).getStrblatlong().equalsIgnoreCase("0")) {
                                        startActivity(new Intent(EnterContactActivity.this, OtpActivity.class).putExtra("signup","1").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("mbeanlogin",mLoginbeanmain));
                                        finish();

                                    } else {
                                        startActivity(new Intent(EnterContactActivity.this, OtpActivity.class).putExtra("signup","1").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("mbeanlogin",mLoginbeanmain));
                                        finish();

                                    }
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
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EnterContactActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("tokenval");
                    editor.commit();
                    String param = getString(R.string.URL_OTP_LOGIN) + "&phoneno=" + entrcntcnol.getText().toString() ;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, EnterContactActivity.this);
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




}