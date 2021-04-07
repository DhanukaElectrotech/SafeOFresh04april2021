package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.Helper.NetworkUtil;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.utils.AppUrls;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.example.librarymain.DhanukaMain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnSubmit;
    private TextView tvSuccess;
    private TextView tvTitle;
    private Toolbar mToolbar;
    TextView mobileno;
    JKHelper jkHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        intialization();
        mobileno= findViewById(R.id.et_email_address);
        mobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>9)
                {
                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                }

            }
        });
        listners();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public EditText getEtEmailAddress() {
        return (EditText) findViewById(R.id.et_email_address);
    }

    public EditText getPhone() {
        return (EditText) findViewById(R.id.et_Phone);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:

                if (NetworkUtil.isConnectedToNetwork(ForgotPasswordActivity.this)) {
                    upadtePassword();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();

                }


        }


    }

    public void listners() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordActivity.this.onBackPressed();
            }
        });
    }

    public void intialization() {
        jkHelper= new JKHelper();
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        tvTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText("Forgot Password");
        TextView ibBack = (TextView) mToolbar.findViewById(R.id.btn_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        tvSuccess = (TextView) findViewById(R.id.tv_success);
        btnSubmit.setOnClickListener(this);
        tvSuccess.setVisibility(View.GONE);
    }

    private void upadtePassword() {

        final String emailId = getEtEmailAddress().getText().toString();
        final String phone = getPhone().getText().toString();
        if (TextUtils.isEmpty(String.valueOf(getEtEmailAddress())) && TextUtils.isEmpty(String.valueOf(getPhone()))) {
            getEtEmailAddress().setError("... information is required.");
            return;

        }


        final ProgressDialog prgDialog = new ProgressDialog(ForgotPasswordActivity.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST,getString(R.string.URL_BASE_URL) ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response


                        prgDialog.dismiss();

                        try {
                            String responses= DhanukaMain.SafeOBuddyDecryptUtils(response);
                            Log.e("forgotpassresponse", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            Log.v("success", "" + success);
                            if (success == 1) {
                                //Message.message(ForgotPasswordActivity.this,"UID and PWD is sent to Registered Phone Number");
                                FancyToast.makeText(ForgotPasswordActivity.this, "Link is sent to registered Mail ID or Phone No", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            } else if (success == 0) {
                                FancyToast.makeText(ForgotPasswordActivity.this, "UserID/Phone number is not Found", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        prgDialog.dismiss();
                        FancyToast.makeText(ForgotPasswordActivity.this, "Failed To Submit EmailId", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {

            //7042741404
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("contactid", "0");
                String vals = "",emailIdstr="";

                if (emailId.length() > 0) {
                    vals = "0";
                    emailIdstr =emailId;
                } else {
                    emailIdstr="0";
                    vals = phone;
                }

                ;



                try {

                    String param= AppUrls.URL_UPDATE_FORGOT_PASSWORD+"&phoneno="+ emailId  ;
                    Log.e("beforeenc",param);
                    param= jkHelper.Encryptapi(param, ForgotPasswordActivity.this);
                    params.put("val", param);
                    Log.e("afterenc",param);
                    return params;

                }
                catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }

            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(ForgotPasswordActivity.this).add(postRequest);
    }
}
