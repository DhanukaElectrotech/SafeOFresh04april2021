package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.dhanuka.morningparcel.adapter.StockWiseDetailAdapter;
import com.dhanuka.morningparcel.beans.StorewiseBean;
import com.dhanuka.morningparcel.beans.FancyToast;

public class StoreWiseCountActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView lvProducts;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue;
    List<StorewiseBean.storeinnerbean> mListReport = new ArrayList<>();
    String timeinterval = "", mDate, mTime;
    @BindView(R.id.backbtnicon12)
    ImageView backbtnicon;
    String branchname, branchid;
    @BindView(R.id.branchnametxt)
    TextView branchnametxt;
    @BindView(R.id.spinner_branch)
    Spinner spinner_branch;
    ArrayList<String> branchlist=new ArrayList<>();
    ArrayList<String> newlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storewise_count);

        ButterKnife.bind(this);
        lvProducts = findViewById(R.id.lvProducts);
        linearContinue = (LinearLayout) findViewById(R.id.linearContinue);

        Intent intent = getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");


        branchlist = (ArrayList<String>) getIntent().getSerializableExtra("branchlist");
       newlist.add("Change Branch");
        newlist.addAll(branchlist);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        lvProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//        timeinterval = getIntent().getStringExtra("tinterval");
        branchname = getIntent().getStringExtra("branchname");
        branchid = getIntent().getStringExtra("branchid");
        branchnametxt.setText(branchname);
        // Log.d("ShowmymTime1",getIntent().getStringExtra("mTime"));
        backbtnicon.setOnClickListener(this);


        ArrayAdapter branchadapter = new ArrayAdapter(StoreWiseCountActivity.this,android.R.layout.simple_list_item_1,newlist);

        spinner_branch.setAdapter(branchadapter);
        spinner_branch.setSelection(branchadapter.getPosition(branchname));
       spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               if (position>0)
               {
                   mListReport.clear();
                   lvProducts.setAdapter(new StockWiseDetailAdapter(getApplicationContext(), mListReport));

                   branchid= hashMap.get(spinner_branch.getSelectedItem().toString());
                   getReports();
               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
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

        mListReport.clear();
        SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        ;
        Log.d("myparambranchdata","http://mmthinkbiz.com/MobileService.aspx?method=StoreWiseCurrentStockDetail_Web" + "&CId=" + prefs.getPrefsContactId() + "&storeId=" + branchid);

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=StoreWiseCurrentStockDetail_Web" + "&CId=" + prefs.getPrefsContactId() + "&storeId=" + branchid,


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
                                lvProducts.setAdapter(new StockWiseDetailAdapter(getApplicationContext(), mListReport));
                                pbLoading.setVisibility(View.GONE);
                                linearContinue.setVisibility(View.GONE);
                                lvProducts.setVisibility(View.VISIBLE);


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
        ) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

//                    String param = getString(R.string.STORE_WISE_REPORTS)  ;
//                    Log.d("Beforeencrptionpastodr", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
//                    params.put("val", finalparam);
                    params.put("", "");
//                    Log.d("afterencrptionpastodr", finalparam);
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
}