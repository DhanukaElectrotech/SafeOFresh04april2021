package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Payfraghelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.RewardAdapter;
import com.dhanuka.morningparcel.utils.JKHelper;

public class RewardPoints extends AppCompatActivity {

    ArrayList<Payfraghelper.paybean> paylist = new ArrayList<>();
    Payfraghelper payfraghelper;
    RewardAdapter rewardAdapter;
    String isCustomer = "";
    @BindView(R.id.rewardcontainer)
    RecyclerView rewardcontainer;
    @BindView(R.id.totalrewards)
    TextView totalrewards;
    @BindView(R.id.bckbtn22)
    ImageView bckbtn22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_points);

        ButterKnife.bind(this);
        bckbtn22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardPoints.super.onBackPressed();
            }
        });
        prefs = new Preferencehelper(getApplicationContext());

        isCustomer = prefs.getPrefsContactId();
        rewardcontainer.setHasFixedSize(true);
        rewardcontainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        loadHistory();
    }


    Preferencehelper prefs;

    public void loadHistory() {

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String mTodayDate = df.format(c.getTime()) + "";
        // formattedDate have current date/time

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mTodayDate + " 00:00"));
        calendar.add(Calendar.DAY_OF_YEAR, -10);
        Date newDate = calendar.getTime();
        String mStartDate = df.format(calendar.getTime());
        mStartDate = mStartDate + "";

        payfraghelper = new Payfraghelper();

      /*  final ProgressDialog mProgressBar = new ProgressDialog(getApplicationContext());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
*/

        String finalMStartDate = mStartDate;
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            try {
                                paylist = new ArrayList<>();
                                Log.d("responsiveorderhistory", response);
                                JKHelper jkHelper = new JKHelper();
                                String responses = jkHelper.Decryptapi(response, getApplicationContext());
                                Log.d("responsive", responses);
                                payfraghelper = new Gson().fromJson(responses, Payfraghelper.class);
                                paylist = payfraghelper.getOrder_data();
                                if (paylist != null && paylist.size() > 0) {
                                    totalrewards.setText(new DecimalFormat("##.##").format(Double.parseDouble(payfraghelper.getCurrentBalance())));
                                    rewardAdapter = new RewardAdapter(paylist, RewardPoints.this);
                                    rewardcontainer.setAdapter(rewardAdapter);
                                }
//                                if (paylist.size() > 0) {
//
//                                } else {
//                                    linearContinue.setVisibility(View.VISIBLE);
//                                }
//
//                                try {
//                                    txtWalletAmt.setText("Available Balance    ₹" + payfraghelper.getCurrentBalance());
//
//                                } catch (Exception e) {
//                                    txtWalletAmt.setText("Available Balance    ₹00.00");
//                                    e.printStackTrace();
//                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //   FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                        } catch (Exception e) {
                            e.printStackTrace();
//                            txtWalletAmt.setText("Available Balance    ₹00.00");
                            //  txtWalletAmt.setText("CurrentBalance");
                        }
                        //   mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        if (paylist.size() > 0) {
//                            linearContinue.setVisibility(View.GONE);
//                        } else {
//                            linearContinue.setVisibility(View.VISIBLE);
//                        } //  FancyToast.makeText(getApplicationContext(), "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(getApplicationContext());

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // contactid=67322&=08-01-2020&=08-19-2020

                    String param = getString(R.string.GET_REWARD_DETAIL) + "&contactid=" + isCustomer + "&strdt=" + finalMStartDate.replace("/", "-") + "&enddt=" + mTodayDate.replace("/", "-");
                    Log.d("Beforeencrptionhistory", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                    params1.put("val", finalparam);
                    Log.d("afterencrptionhistory", finalparam);
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

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }
}