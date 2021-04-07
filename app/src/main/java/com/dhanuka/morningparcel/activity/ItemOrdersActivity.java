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
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.ItemOrderAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.ItemOrderBean;
import com.dhanuka.morningparcel.events.SearchTextChangedEvent;
import com.dhanuka.morningparcel.utils.EqualSpacingItemDecoration;
import com.dhanuka.morningparcel.utils.JKHelper;

public class ItemOrdersActivity extends AppCompatActivity implements TextWatcher, SwipeRefreshLayout.OnRefreshListener {
    @Nullable
    @BindView(R.id.linearContinue)
    LinearLayout linearContinue;
    @Nullable
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @Nullable
    @BindView(R.id.lvProducts)
    RecyclerView lvProducts;
    ArrayList<ItemOrderBean> list = new ArrayList<>();

    @Override
    public void onRefresh() {
        loadHistory();

    }

    public void makeBackClick(View v) {
        onBackPressed();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        EventBus.getDefault().post(new SearchTextChangedEvent(editable.toString()));
    }
    String strDesc="";
    ItemOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_orders);
        ButterKnife.bind(this);
        swipeRefresh.setOnRefreshListener(this);
        lvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvProducts.hasFixedSize();
        lvProducts.addItemDecoration(new EqualSpacingItemDecoration(15, LinearLayout.VERTICAL));
        strDesc=getIntent().getStringExtra("strDesc");
        loadHistory();
    }

    public void loadHistory() {
        swipeRefresh.setRefreshing(true);

        final ProgressDialog mProgressBar = new ProgressDialog(ItemOrdersActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefresh.setRefreshing(false);
                        try {
                            list = new ArrayList<>();
                            Log.d("responsiveorderhistory", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ItemOrdersActivity.this);
                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                list = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<ItemOrderBean>>() {
                                }.getType());
                                adapter = new ItemOrderAdapter(ItemOrdersActivity.this, list);
                                lvProducts.setAdapter(adapter);
                                 if (list.size() > 0) {
                                    linearContinue.setVisibility(View.GONE);
                                    lvProducts.setVisibility(View.VISIBLE);
                                } else {
                                    linearContinue.setVisibility(View.VISIBLE);
                                    lvProducts.setVisibility(View.GONE);


                                }
                            } else {
                                linearContinue.setVisibility(View.VISIBLE);
                                lvProducts.setVisibility(View.GONE);
                                FancyToast.makeText(ItemOrdersActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(ItemOrdersActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(ItemOrdersActivity.this);

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.GET_ORDER_BY_ITEM) + "&itemdesc=" + strDesc + "&contactid=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionhistory", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ItemOrdersActivity.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }


}