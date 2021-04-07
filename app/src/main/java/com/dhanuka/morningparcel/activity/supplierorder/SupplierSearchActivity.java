package com.dhanuka.morningparcel.activity.supplierorder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.supplierorder.adapter.SupplierAdapter;
import com.dhanuka.morningparcel.activity.supplierorder.bean.DeliveryBoysBean;
import com.dhanuka.morningparcel.utils.JKHelper;

public class SupplierSearchActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<DeliveryBoysBean> mListDeliveryBoys = new ArrayList<>();
    @Nullable
    @BindView(R.id.etSearch)
    AutoCompleteTextView etSearch;
    @BindView(R.id.custcontainer)
    RecyclerView custcontainer;
    @BindView(R.id.backbtnicon)
    ImageView backbtnicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);
        ButterKnife.bind(this);
        backbtnicon.setOnClickListener(this);
        custcontainer.setHasFixedSize(true);
        custcontainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//                item = getitemnamesearch(charSequence.toString());
//                ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, item);
//                etSearch.setAdapter(myAdapter);

                if (s.length() > 2) {
                    Log.d("onqueryinside", s.toString());

                    getAllusers(s.toString());
                } else {
                    Log.d("onqueryoutside", s.toString());

                    SupplierAdapter mAdapter = new SupplierAdapter(SupplierSearchActivity.this, mListDeliveryBoys);
                    custcontainer.setAdapter(mAdapter);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {




            }
        });
    }

    public void getAllusers(String searchcust) {
        //     swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(SupplierSearchActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
//        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //      swipeRefresh.setRefreshing(false);
                        Log.e("Response393", response);


//                        mProgressBar.dismiss();

                        try {

                            Log.d("resp_userlst", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, SupplierSearchActivity.this);
                            Log.d("resp_usrlst", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mListDeliveryBoys = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<DeliveryBoysBean>>() {
                                }.getType());
                                if (mListDeliveryBoys.size() > 0) {
                                    ArrayList<String> mListBoys = new ArrayList<>();

                                    for (int a = 0; a < mListDeliveryBoys.size(); a++) {
                                       /* if (mListDeliveryBoys.get(a).getFirstName().isEmpty()) {
                                            mListDeliveryBoys.remove(a);
                                        } else {*/
                                        mListBoys.add(mListDeliveryBoys.get(a).getFirstName());
                                        mListBoys.add(mListDeliveryBoys.get(a).getAlartphonenumber());

                                        // }
                                    }
                                    SupplierAdapter mAdapter = new SupplierAdapter(SupplierSearchActivity.this, mListDeliveryBoys);
                                    custcontainer.setAdapter(mAdapter);
                                    //dialogAssignOrder.show();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        mProgressBar.dismiss();

                        // swipeRefresh.setRefreshing(false);
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(SupplierSearchActivity.this);

                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);


                    String param = AppUrls.GET_USER_LIST + "&contactid=" + prefs.getPrefsContactId() + "&companyid=" +prefs.getCID()+"&roleid=1057"+"&searchby="+searchcust;
                    Log.d("Beforeencrptionuserlst", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SupplierSearchActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionusrlst", finalparam);
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
        switch (v.getId())
        {
            case R.id.backbtnicon:
                onBackPressed();
                break;
        }

    }
}