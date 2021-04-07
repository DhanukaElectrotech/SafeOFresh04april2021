package com.dhanuka.morningparcel.customViews;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.activity.OptionChooserActivity;
import com.dhanuka.morningparcel.dialog.SelectShop;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class StoreView extends FrameLayout implements View.OnClickListener {

    Preferencehelper prefsOld;
    private FrameLayout tvCount;

    public StoreView(Context context) {
        super(context);
    }

    public StoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        prefsOld = new Preferencehelper(getContext());
        setOnClickListener(this);
        tvCount = (FrameLayout) findViewById(R.id.shpView);
        try {
            if (prefsOld.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                tvCount.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            tvCount.setVisibility(VISIBLE);

        }
    }

    public void setCount(int value) {

    }

    @Override
    public void onClick(View view) {
        SharedPreferences prefs = getContext().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getContext().MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
        if (prefs.getString("cntry", "India").equalsIgnoreCase("India")) {

            getAllShop();
        } else {
//            getAllShop1();
            getContext().startActivity(new Intent(getContext(),OptionChooserActivity.class));

        }
        //Utility.selectShop(getContext());

        //  getContext().startActivity(new Intent(getContext(), CartActivity.class));
    }


    public void getAllShop() {
        final ProgressDialog mProgressBar = new ProgressDialog(getContext());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getContext().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getContext().MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();


        StringRequest postRequest = new StringRequest(Request.Method.POST, getContext().getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        mProgressBar.dismiss();
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, getContext());
                        mEditor.putString("shopss", responses);
                        mEditor.commit();

                        // if (prefs.getString("shopId", "").isEmpty()) {
                        //  if (prefsOld.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                        new SelectShop(getContext(), 1, false).show();

                        //  Utility.selectShop(getContext(),1,false);
                        //}
                        //  }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    String contctId="";
                    if (prefsOld.getPrefsContactId().isEmpty()){
                        contctId="7777";
                    }else{
                        contctId=prefsOld.getPrefsContactId();
                    }

                    String param = AppUrls.URL_FILL_CONSIGNEE + "&contactid=" + contctId+ "&CId=" + "1";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getContext());
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
        Volley.newRequestQueue(getContext()).add(postRequest);
    }

    public void getAllShop1() {
        int storeType = 0;


        final ProgressDialog mProgressBar = new ProgressDialog(getContext());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getContext().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getContext().MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
        if (prefs.getString("typer", "com").equalsIgnoreCase("com")) {
            storeType = 0;
        } else {
            storeType = 1;
        }

        String contctId="";
        if (prefsOld.getPrefsContactId().isEmpty()){
            contctId="7777";
        }else{
            contctId=prefsOld.getPrefsContactId();
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST,"http://mmthinkbiz.com/MobileService.aspx/getds?method=Fillconsignee_Web&contactid="+contctId+"&CId=1&storetype="+storeType,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        mProgressBar.dismiss();
                        JKHelper jkHelper = new JKHelper();
                        Log.e("hjgfdfghj", response);
                        String responses = response/*jkHelper.Decryptapi(response,getContext())*/;
                        mEditor.putString("shopss", responses);
                        mEditor.commit();

                        // if (prefs.getString("shopId", "").isEmpty()) {
                        //   if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                        new SelectShop(getContext(), 1, true).show();

                        // Utility.selectShop(getContext(),1,true);
                        // }
                        //  }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                   /* String param =  AppUrls.URL_FILL_CONSIGNEE+ "&contactid=7777" +"&CId=" + "1&storetype="+storeType;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getContext());
             */
                    params.put("", "");
                    //    Log.d("afterencrption", finalparam);
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
        Volley.newRequestQueue(getContext()).add(postRequest);
    }


}
