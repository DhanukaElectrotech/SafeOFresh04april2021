package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.BranchWiseAdapter;
import com.dhanuka.morningparcel.beans.BranchSalesBeanNew;
import com.dhanuka.morningparcel.beans.Dynamicbranchbean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.utils.JKHelper;

public class BranchWiseACtivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView lvProducts;
    private ProgressBar pbLoading;
    SharedPreferences.Editor mEditor;
    ArrayList<Dynamicbranchbean.beanchinnerbean> mListReportbranch = new ArrayList<>();
    ArrayList<String> branchlist = new ArrayList<>();
    HashMap<String, String> branchhash = new HashMap<>();
    private LinearLayout linearContinue;
    SharedPreferences prefs2;
    List<BranchSalesBeanNew.beanchinnerbean> mListReport = new ArrayList<>();
    String timeinterval = "", mDate, mTime;
    @BindView(R.id.backbtnicon12)
    ImageView backbtnicon;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_wise_a_ctivity);
        ButterKnife.bind(this);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        lvProducts = findViewById(R.id.lvProducts);
        linearContinue = (LinearLayout) findViewById(R.id.linearContinue);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        lvProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        timeinterval = getIntent().getStringExtra("tinterval");
        mDate = getIntent().getStringExtra("mDate");
        mTime = getIntent().getStringExtra("mTime");

        Log.d("ShowmymTime1", getIntent().getStringExtra("mTime"));
        backbtnicon.setOnClickListener(this);

        getReports();

    }


    public void getBranch() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());
        Preferencehelper prefs;
        prefs = new Preferencehelper(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responseWEEK", response);
                            // JKHelper jkHelper = new JKHelper();
                            //  String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.d("responseWEEKNew", response);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                Dynamicbranchbean branchSalesBean = new Gson().fromJson(responses, Dynamicbranchbean.class);

                                mListReportbranch = branchSalesBean.getBranchdatalist();

                                for (int i = 0; i < mListReportbranch.size(); i++) {

                                        branchhash.put(branchSalesBean.getBranchdatalist().get(i).getBranchName(), branchSalesBean.getBranchdatalist().get(i).getBranchId());

                                        branchlist.add(branchSalesBean.getBranchdatalist().get(i).getBranchName());


                                }

                                ArrayAdapter branchadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, branchlist);
                                branchadapter.setDropDownViewResource(R.layout.simple_list_item_single_choice1);
                                prefs2 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                       MODE_PRIVATE);
                                mEditor = prefs2.edit();

                                mEditor.putString("branchlist", new Gson().toJson(branchlist));
                                mEditor.putString("map", new Gson().toJson(branchhash));
                                mEditor.putString("isIntent", "1");
                                mEditor.commit();


                            } else {

                                //    com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                Preferencehelper prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());
                ;
                try {
                    String param = getString(R.string.URL_GET_BRANCH) + "&contactid=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, BranchWiseACtivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionpay", finalparam);
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

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }
//    public void getBranch() {
//
//
//        Calendar c = Calendar.getInstance();
//        System.out.println("Current time => " + c.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//        String mTodayDate = df.format(c.getTime());
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
//
//
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.d("responseWEEK", response);
//                            // JKHelper jkHelper = new JKHelper();
//                            //  String responses = jkHelper.Decryptapi(response, getApplicationContext());
//                            JKHelper jkHelper = new JKHelper();
//                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
//                            JSONObject jsonObject = new JSONObject(responses);
//                            Log.d("responseWEEKNew", response);
//
//                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
//                                NewBranchsalesbean branchSalesBean = new Gson().fromJson(response, NewBranchsalesbean.class);
//                                mListReportbranch = branchSalesBean.getBranchdatalist();
//
//                                for (int i = 0; i < mListReport.size(); i++) {
//                                    if (!branchSalesBean.getBranchdatalist().get(i).getBranchName().equalsIgnoreCase("all")) {
//                                        branchhash.put(branchSalesBean.getBranchdatalist().get(i).getBranchName(), branchSalesBean.getBranchdatalist().get(i).getBranchId());
//
//                                        branchlist.add(branchSalesBean.getBranchdatalist().get(i).getBranchName());
//                                    }
//
//                                }
//
//
//                                prefs2 = getSharedPreferences("MORNING_PARCEL_GROCERY",
//                                        MODE_PRIVATE);
//                                mEditor = prefs2.edit();
//
//
//                                mEditor.putString("branchlist", new Gson().toJson(branchlist));
//                                mEditor.putString("map", new Gson().toJson(branchhash));
//                                mEditor.putString("isIntent", "1");
//                                mEditor.commit();
//
//
////                                SharedPreferences prefs3 = getSharedPreferences("MORNING_PARCEL_POS",
////                                        MODE_PRIVATE);
////                                mEditor = prefs3.edit();
////                                if (prefs3.getString("resp1", "").isEmpty()) {
////                                    getAllProducts(branchhash.get(spinner_branch.getSelectedItem().toString()));
////
////                                } else {
////                                    mListAllItems = new Gson().fromJson(prefs3.getString("resp1", ""), new TypeToken<List<NewBranchsalesbean>>() {
////                                    }.getType());
////                                }
//
//
//                            } else {
//
//                                //    com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
//                            }
//
//                        } catch (Exception e) {
//
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
//
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//                Preferencehelper prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());
//                ;
//                try {
//                    String param = getString(R.string.URL_GET_BRANCH) + "&contactid=" + prefs.getPrefsContactId();
//                    Log.d("Beforeencrptionpay", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, BranchWiseACtivity.this);
//                    params.put("val", finalparam);
//                    Log.d("afterencrptionpay", finalparam);
//                    return params;
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return params;
//                }
//
//
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
//
//
//    }

    public void getReports() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());
        Preferencehelper prefs;
        prefs = new Preferencehelper(getApplicationContext());

        pbLoading.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            Log.d("responseWEEK", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.d("responseWEEKNew", responses);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                BranchSalesBeanNew BranchSalesBeanNew = new Gson().fromJson(responses, BranchSalesBeanNew.class);
                                mListReport = BranchSalesBeanNew.getBranchdatalist();
                                if (mListReport != null) {
                                } else {
                                    mListReport = new ArrayList<>();
                                }
                                lvProducts.setAdapter(new BranchWiseAdapter(getApplicationContext(), mListReport, mDate, mTime));
                                pbLoading.setVisibility(View.GONE);
                                linearContinue.setVisibility(View.GONE);
                                lvProducts.setVisibility(View.VISIBLE);

                                getBranch();
                            } else {

                                linearContinue.setVisibility(View.VISIBLE);
                                //    com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            swipeRefreshLayout.setRefreshing(false);
                            linearContinue.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        linearContinue.setVisibility(View.VISIBLE);


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.BRANCH_REPORTS) + "&CId=" + prefs.getPrefsContactId() + "&Date=" + mDate + "&timeInterval=" + timeinterval;
                    Log.d("Beforeencrptionpastodr", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                    params.put("val", finalparam);
                    Log.d("afterencrptionpastodr", finalparam);
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

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getReports();

    }
}