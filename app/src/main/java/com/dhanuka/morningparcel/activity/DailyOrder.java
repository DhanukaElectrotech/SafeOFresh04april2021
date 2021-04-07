package com.dhanuka.morningparcel.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.adapter.DailyOrderAdapter;
import com.dhanuka.morningparcel.adapter.DailyOrderAdapter;
import com.dhanuka.morningparcel.beans.CollectionBean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.events.Returnlistner;
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

public class DailyOrder extends AppCompatActivity implements Returnlistner   , SwipeRefreshLayout.OnRefreshListener{


    List<CollectionBean> mListItems = new ArrayList<>();
    DailyOrderAdapter dailyOrderAdapter;
    @Nullable
    @BindView(R.id.dailyordercontainer)
    RecyclerView dailyordercontainer;

    @Nullable
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Nullable
    @BindView(R.id.searchcollctn)
    EditText searchcollctn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order);
        ButterKnife.bind(this);
        dailyordercontainer.setHasFixedSize(true);

        dailyordercontainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));

        loadHistory("0");

        searchcollctn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (dailyordercontainer != null) {
                    filter(s.toString());
                }
            }
        });

    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadHistory("0");
    }

    public void filter(String text) {
        ArrayList<CollectionBean> filteredList = new ArrayList<>();

        for (CollectionBean product : mListItems) {
            if (product.getBalance().toLowerCase().contains(text) || product.getContactNo().toLowerCase().contains(text) || product.getCustomerName().toLowerCase().contains(text) || product.getFlatNo().toLowerCase().contains(text)) {
                filteredList.add(product);
            }
        }

        dailyOrderAdapter.filterList(filteredList);
    }


    public void loadHistory(String credittytpe) {

        swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(DailyOrder.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        //   http://mmthinkbiz.com/mobileservice.aspx?method=Itemorderhistory_Web&strdt=07-01-2020&enddt=08-31-2020&contactid=66373&itemid=16921
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefresh.setRefreshing(false);

                        try {
                            Log.d("rawresponse", response);
                            mListItems = new ArrayList<>();

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, DailyOrder.this);
                            Log.d("responsiveitemhistory", responses);
                            JSONObject jsonObject = new JSONObject(responses);


                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {


                                mListItems = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<CollectionBean>>() {
                                }.getType());

                                dailyOrderAdapter = new DailyOrderAdapter(DailyOrder.this, mListItems, "image", DailyOrder.this);

                                dailyordercontainer.setAdapter(dailyOrderAdapter);



                            } else {
                                //FancyToast.makeText(DailyOrder.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        swipeRefresh.setRefreshing(false);

                        FancyToast.makeText(DailyOrder.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(DailyOrder.this);

                Map<String, String> params1 = new HashMap<String, String>();

                try {
                    String param = getString(R.string.ORDER_CREDIT) + "&ContactID=" + prefs.getPrefsContactId() + "&Credittype=" + credittytpe + "&custId=" + "" + "&Rptype=0";
                    Log.d("withoutencryption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, DailyOrder.this);
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


    @Override
    public void returnmoneyluster(CollectionBean collectionBean) {
        
    }

    @Override
    public void showreturndialog(boolean showme) {

    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {

        loadHistory("0");

    }
}