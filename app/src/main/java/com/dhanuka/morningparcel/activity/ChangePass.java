package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

public class ChangePass extends AppCompatActivity {
    EditText oldpass,confirmpass,newpass;
    Preferencehelper prefs;
    Button changefinalpass;
    @BindView(R.id.btn_back)
    TextView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind(this);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        oldpass=findViewById(R.id.oldpass);
        changefinalpass=findViewById(R.id.changefinalpass);
        confirmpass=findViewById(R.id.confrmpass);

        prefs= new Preferencehelper(getApplicationContext());
        changefinalpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    requestForchangepassword();



            }
        });

    }
    private void requestForchangepassword() {





        final ProgressDialog prgDialog = new ProgressDialog(ChangePass.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST,"http://mmthinkbiz.com/MobileService.aspx/NewRegistration?method=changepasswordfirtstime",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Responsepasschange", response);
                        String res = response;
                        prgDialog.dismiss();

//                        if (res.length() > 0) {
//                            log.e("res login==" + res);
//
//                        }
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            int success = jsonObject.getInt("success");

                            if (success == 1) {

                                JSONArray loginvalidationArray = jsonObject.getJSONArray("loginvalidation");

                                if (loginvalidationArray.length() > 0) {
                                    JSONObject returnmessageObj = loginvalidationArray.getJSONObject(0);
                                    String returnmessage = returnmessageObj.getString("returnmessage");

                                    if (returnmessage.equalsIgnoreCase("0"))
                                    {
                                        FancyToast.makeText(getApplicationContext(), "Password Update Failed", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

                                    }
                                    else {
                                        FancyToast.makeText(getApplicationContext(), "Password Updated Successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();


                                    }
                                }

                            } else if (success == 0)
                            {

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


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", prefs.getPrefsContactId());
                params.put("pwd",confirmpass.getText().toString());
                params.put("OldPassword",oldpass.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(ChangePass.this).add(postRequest);

    }
}
