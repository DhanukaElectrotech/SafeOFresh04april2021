package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.StoreWiseAdapter;
import com.dhanuka.morningparcel.beans.StorewiseBean;
import com.dhanuka.morningparcel.beans.FancyToast;

public class StoreWiseStockActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView lvProducts;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue;
    List<StorewiseBean.storeinnerbean> mListReport = new ArrayList<>();
    String timeinterval = "", mDate, mTime;
    @BindView(R.id.backbtnicon12)
    ImageView backbtnicon;
    HashMap<String, String> branchhash = new HashMap<>();
    ArrayList<String> branchlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_wise_stock);

        ButterKnife.bind(this);
        lvProducts = findViewById(R.id.lvProducts);
        linearContinue = (LinearLayout) findViewById(R.id.linearContinue);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        lvProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//        timeinterval = getIntent().getStringExtra("tinterval");
//        mDate = getIntent().getStringExtra("mDate");
//        mTime = getIntent().getStringExtra("mTime");

        // Log.d("ShowmymTime1",getIntent().getStringExtra("mTime"));
        backbtnicon.setOnClickListener(this);

        getReports();
    }


    public void getReports() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());
        Preferencehelper prefs;
        prefs = new Preferencehelper(getApplicationContext());

        pbLoading.setVisibility(View.VISIBLE);
        Log.d("paramstoclwise", "http://mmthinkbiz.com/MobileService.aspx?method=StoreWiseCurrentStock" + "&CId=" + prefs.getPrefsContactId());
        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://mmthinkbiz.com/MobileService.aspx?method=StoreWiseCurrentStock_Web" + "&CId=" + "66738",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responseWEEK", response);
                            // JKHelper jkHelper = new JKHelper();
                            //  String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("responseWEEKNew", response);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                StorewiseBean storewiseBean = new Gson().fromJson(response, StorewiseBean.class);
                                mListReport = storewiseBean.getstoredatalist();
                                pbLoading.setVisibility(View.GONE);
                                linearContinue.setVisibility(View.GONE);
                                lvProducts.setVisibility(View.VISIBLE);
                                for (int i = 0; i < mListReport.size(); i++) {
                                    branchhash.put(storewiseBean.getstoredatalist().get(i).getStoreName(), storewiseBean.getstoredatalist().get(i).getBranchID());

                                    branchlist.add(storewiseBean.getstoredatalist().get(i).getStoreName());
                                }
                                lvProducts.setAdapter(new StoreWiseAdapter(getApplicationContext(), mListReport, mDate, mTime, branchhash, branchlist));


                            } else {
                                linearContinue.setVisibility(View.VISIBLE);
                                //    com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            linearContinue.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        linearContinue.setVisibility(View.VISIBLE);


                    }
                }
        );

//        {
//
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//
//                try {
//                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//
////                    String param = getString(R.string.STORE_WISE_REPORTS)  ;
////                    Log.d("Beforeencrptionpastodr", param);
////                    JKHelper jkHelper = new JKHelper();
////                    String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
////                    params.put("val", finalparam);
//                    params.put("", "");
////                    Log.d("afterencrptionpastodr", finalparam);
//                    return params;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return params;
//                }
//
//
//            }
//        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbtnicon12:
                super.onBackPressed();
                break;
        }

    }
}