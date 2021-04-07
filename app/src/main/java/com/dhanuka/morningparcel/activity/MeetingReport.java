package com.dhanuka.morningparcel.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.adapter.CollectionAdapter;
import com.dhanuka.morningparcel.adapter.MeetingAdapter;
import com.dhanuka.morningparcel.beans.CollectionBean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.MeetingBean;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingReport extends AppCompatActivity {

    List<MeetingBean> mListItems = new ArrayList<>();
    @Nullable
    @BindView(R.id.lvProducts)
    RecyclerView lvProducts;

    MeetingAdapter meetingAdapter;
    @Nullable
    @BindView(R.id.backbtnicon)
    ImageView backbtnicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_report);
        ButterKnife.bind(this);
        lvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvProducts.hasFixedSize();
        loadmeetingdetail();
        backbtnicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void loadmeetingdetail() {


        final ProgressDialog mProgressBar = new ProgressDialog(MeetingReport.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        //   http://mmthinkbiz.com/mobileservice.aspx?method=Itemorderhistory_Web&strdt=07-01-2020&enddt=08-31-2020&contactid=66373&itemid=16921
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            Log.d("rawresponse", response);
                            mListItems = new ArrayList<>();

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, MeetingReport.this);
                            Log.d("responsiveitemhistory", responses);
                            JSONObject jsonObject = new JSONObject(responses);


                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {


                                mListItems = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MeetingBean>>() {
                                }.getType());

                                meetingAdapter = new MeetingAdapter(MeetingReport.this, mListItems);

                                lvProducts.setAdapter(meetingAdapter);


                            } else {
                                //FancyToast.makeText(MeetingReport.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();


                        FancyToast.makeText(MeetingReport.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(MeetingReport.this);

                Map<String, String> params1 = new HashMap<String, String>();


                try {
                    String param = getString(R.string.Meeting_REPORT) + "&ContactId=" + prefs.getPrefsContactId() + "&CompanyId=" + prefs.getCID();
                    Log.d("withoutencryption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, MeetingReport.this);
                    params1.put("val", finalparam);
                    Log.d("params1params1", params1.toString());
                    return params1;


                } catch (Exception e) {
                    e.printStackTrace();
                    return params1;
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