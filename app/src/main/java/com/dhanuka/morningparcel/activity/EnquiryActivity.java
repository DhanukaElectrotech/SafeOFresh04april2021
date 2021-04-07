package com.dhanuka.morningparcel.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.beans.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;


public class EnquiryActivity extends AppCompatActivity {

    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.spinnerSubject)
    Spinner spinnerSubject;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtQty)
    EditText edtQty;
    @BindView(R.id.edtMsg)
    EditText edtMsg;
    TextView textView;
    Button clickok;

    public void makeBackClick(View view) {
        onBackPressed();
    }

    String type = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enquiry);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        Localdialog = new Dialog(EnquiryActivity.this);
        builder = new AlertDialog.Builder(this);
        Localdialog.setContentView(R.layout.custom_dialogbox);
        Localdialog.setCancelable(false);
        Localdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textView = Localdialog.findViewById(R.id.custom_dialogtext);
        clickok = Localdialog.findViewById(R.id.clickok);
        clickok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Localdialog.dismiss();

onBackPressed();            }
        });
        getItemList(EnquiryActivity.this);

    }

    public void makeEnquiry(View view) {
        if (edtName.getText().toString().isEmpty()) {
            FancyToast.makeText(EnquiryActivity.this, "Please Enter Name", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            edtName.requestFocus();

        } else if (edtPhone.getText().toString().isEmpty()) {
            FancyToast.makeText(EnquiryActivity.this, "Please Enter Phone Number", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            edtName.requestFocus();

        } else if (edtPhone.getText().toString().length() > 10 || edtPhone.getText().toString().length() < 10) {
            FancyToast.makeText(EnquiryActivity.this, "Please Enter Valid Phone Number", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            edtName.requestFocus();

        } else if (edtQty.getText().toString().isEmpty()) {
            FancyToast.makeText(EnquiryActivity.this, "Please Enter Quantity", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            edtName.requestFocus();

        } else {
            submitEnquiry(EnquiryActivity.this);
        }
    }

    ArrayList<String> mListCategory = new ArrayList<>();
    Dialog Localdialog;
    AlertDialog.Builder builder;

    public void getItemList(Context ctx) {
        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        mListCategory = new ArrayList<String>();
        //  mListCategory.add("Select Item");
        //  societylist.add("Add New Society");

        ArrayList<CatcodeHelper> catlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.GET_REASON_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        mProgressBar.dismiss();
                        String res = response;


                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String catcodedes = newjson.getString("CodeDescription");
                                    mListCategory.add(catcodedes);
                                }


                            } else {
                                //  societylist.add("Select Society");
                                //  societylist.add("Add New Society");
                                mListCategory = new ArrayList<String>();
                                mListCategory.add("Select Item");
                            }
                            ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, mListCategory);
                            spinnerSubject.setAdapter(societyadaopter);
                            spinnerSubject.setSelection(Integer.parseInt(type));

                        } catch (Exception e) {
                            mListCategory = new ArrayList<String>();
                            mListCategory.add("Select Item");
                            ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, mListCategory);
                            spinnerSubject.setAdapter(societyadaopter);
                            spinnerSubject.setSelection(Integer.parseInt(type));

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, mListCategory);
                        spinnerSubject.setAdapter(societyadaopter);
                        spinnerSubject.setSelection(Integer.parseInt(type));

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("contactid", "0");
                params.put("type", "111");


                return params;


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ctx).add(postRequest);
    }


    public void submitEnquiry(Context ctx) {
        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        mListCategory = new ArrayList<String>();
        //  mListCategory.add("Select Item");
        //  societylist.add("Add New Society");

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://www.mmthinkbiz.com/Mobileservice.aspx?method=insertitemenquiry_Web",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        mProgressBar.dismiss();
                        String res = response;


                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnmessage");
                            if (jsonObject.getInt("success") == 1) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String msg = jsonObject1.getString("returnmsg");
                                textView.setText(msg);
                                Localdialog.show();
                            } else {
                                FancyToast.makeText(EnquiryActivity.this, "Somehting went wrong, please try again later.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                            }

                        } catch (Exception e) {

                            FancyToast.makeText(EnquiryActivity.this, "Somehting went wrong, please try again later.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        FancyToast.makeText(EnquiryActivity.this, "Somehting went wrong, please try again later.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", edtName.getText().toString());
                params.put("PhoneNo", edtPhone.getText().toString());
                params.put("Subject", spinnerSubject.getSelectedItem().toString());
                params.put("Qty", edtQty.getText().toString());
                params.put("Message", edtMsg.getText().toString());


                return params;


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ctx).add(postRequest);
    }


}