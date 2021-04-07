package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.BranchwiseHelper;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.BranchwiseStockAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.NewBranchsalesbean;
import com.dhanuka.morningparcel.beans.StorewiseBean;
import com.dhanuka.morningparcel.events.OnAddToSToreListener;
import com.dhanuka.morningparcel.events.OnProductEditListener;
import com.dhanuka.morningparcel.utils.JKHelper;

public class BranchWiseStock extends AppCompatActivity implements OnAddToSToreListener, OnProductEditListener {
    Spinner spinner_branch;
    Spinner spinner_branch445;
    ArrayList<NewBranchsalesbean.beanchinnerbean> mListReport = new ArrayList<>();
    HashMap<String, String> branchhash = new HashMap<>();
    SharedPreferences.Editor mEditor;
    BranchwiseStockAdapter adapter;
    ArrayList<BranchwiseHelper> mListAllItems = new ArrayList<BranchwiseHelper>();
    ArrayList<String> branchlist = new ArrayList<>();

    ArrayList<BranchwiseHelper> mList = new ArrayList<BranchwiseHelper>();
    ArrayList<com.dhanuka.morningparcel.Helper.BranchwiseHelper> masterlist = new ArrayList<>();
    SharedPreferences prefs;
    String type = "Pending";
    @Nullable
    @BindView(R.id.branchstockcontainer)
    RecyclerView lvProducts;
    int mType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_wise_stock);
        ButterKnife.bind(this);
        lvProducts.setLayoutManager(new LinearLayoutManager(BranchWiseStock.this, RecyclerView.VERTICAL, false));
        lvProducts.setHasFixedSize(true);
        prefs = getSharedPreferences("MORNING_PARCEL_POS",
                MODE_PRIVATE);
        mEditor = prefs.edit();
        mEditor.putString("mValues", "");
        mEditor.commit();
        spinner_branch = findViewById(R.id.spinner_branch44);
        spinner_branch445 = findViewById(R.id.spinner_branch445);


        spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getAllProducts(branchhash.get(spinner_branch.getSelectedItem().toString()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_branch445.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    getAllProducts(branchhash.get(spinner_branch.getSelectedItem().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        getReports();
        getBranch();
    }


    public void getBranch() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());
        com.dhanuka.morningparcel.Helper.Preferencehelper prefs;


        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetBranchList",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responseWEEK", response);
                            JKHelper jkHelper = new JKHelper();
//                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("responseWEEKNew", response);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                NewBranchsalesbean branchSalesBean = new Gson().fromJson(response, NewBranchsalesbean.class);
                                mListReport=branchSalesBean.getBranchdatalist();

                                for (int i = 0; i < mListReport.size(); i++) {
                                    if (!branchSalesBean.getBranchdatalist().get(i).getBranchName().equalsIgnoreCase("all")) {
                                        branchhash.put(branchSalesBean.getBranchdatalist().get(i).getBranchName(), branchSalesBean.getBranchdatalist().get(i).getBranchId());

                                        branchlist.add(branchSalesBean.getBranchdatalist().get(i).getBranchName());
                                    }

                                }

                                ArrayAdapter branchadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, branchlist);
                                branchadapter.setDropDownViewResource(R.layout.simple_list_item_single_choice1);

                                spinner_branch.setAdapter(branchadapter);
                                mEditor.putString("branchlist", new Gson().toJson(branchlist));
                                mEditor.putString("map", new Gson().toJson(branchhash));
                                mEditor.putString("isIntent", "1");
                                mEditor.commit();


                                SharedPreferences prefs3 = getSharedPreferences("MORNING_PARCEL_POS",
                                        MODE_PRIVATE);
                                mEditor = prefs3.edit();
                                if (prefs3.getString("resp1", "").isEmpty()) {
                                    getAllProducts(branchhash.get(spinner_branch.getSelectedItem().toString()));

                                } else {
                                    mListAllItems = new Gson().fromJson(prefs3.getString("resp1", ""), new TypeToken<List<NewBranchsalesbean>>() {
                                    }.getType());
                                }


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
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                try {
                    Preferencehelper prefs=new Preferencehelper(getApplicationContext());

                    params.put("contactid", prefs.getPrefsContactId());
                    Log.d("printparam", params.toString());

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

    public void getReports() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());


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
        );


        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }

    public void getAllProducts(final String branchid) {

//        pnDialog.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        masterlist = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);
//                        pnDialog.setVisibility(View.GONE);

                        String res = response;
                        try {

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, BranchWiseStock.this);


                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String GroupName = newjson.getString("GroupName");
                                    String ItemName = newjson.getString("ItemName");
                                    String SubGroup = newjson.getString("SubGroup");
                                    String Balance = newjson.getString("Balance");

                                    String BranchName = newjson.getString("BranchName");
                                    String ItemID = newjson.getString("ItemID");
                                    String InQty = newjson.getString("InQty");
                                    String FilePath = newjson.getString("filepath");
                                    String FileName = newjson.getString("FileName");
                                    String totalpurchase = newjson.getString("totalpurchase");
                                    String totalsale = newjson.getString("totalsale");
                                    String OutQty = newjson.getString("OutQty");
                                    String MRP = newjson.getString("MRP");

//                                    if (ItemID.equalsIgnoreCase("3221")) {
//                                        String ss = "";
//                                        Log.e("ss44444", StoreSTatus.toString());
//                                    }
//                                    Log.e("StoreSTatusStoreSTatus", StoreSTatus.toString());

                                    com.dhanuka.morningparcel.Helper.BranchwiseHelper v = new com.dhanuka.morningparcel.Helper.BranchwiseHelper();
                                    v.setGroupName(GroupName);
                                    v.setItemName(ItemName);
                                    v.setSubSGroup(SubGroup);
                                    v.setFileName(FileName);
                                    v.setFilepath(FilePath);
                                    v.setBranchName(BranchName);
                                    v.setItemID(ItemID);
                                    v.setBalance(Balance);
                                    v.setInQty(InQty);
                                    v.setTotalPurchase(totalpurchase);
                                    v.setTotalSale(totalsale);
                                    v.setOutQty(OutQty);
                                    v.setMRP(MRP);
                                    type = "Pending";


//                                    try {
//
//                                        v.setTotalPurchase(newjson.getString("TotalPurchase"));
//
//                                        v.setTotalSale(newjson.getString("TotalSale"));
//                                        v.setInQty(newjson.getString("InQty"));
//                                        v.setBalance(newjson.getString("Balance"));
//                                        v.setOutQty(newjson.getString("OutQty"));
//                                        v.setBranchName(newjson.getString("BranchName"));
//                                        v.setBranchID(newjson.getString("BranchID"));
//
//                                        BranchSalesBean.beanchinnerbean beanchinnerbean = new BranchSalesBean.beanchinnerbean();
//                                        beanchinnerbean.setBranchId(newjson.getString("BranchID"));
//                                        beanchinnerbean.setBranchName(newjson.getString("BranchName"));
//
//                                        mListBranches.add(beanchinnerbean);
//                                    } catch (Exception e) {
//
//                                    }
                                    masterlist.add(v);
                                    // //Log.d("masterlist", String.valueOf(masterlist.size()));

                                }


                            }
                            setAdapterData();

                            Log.d("Mylistsize", String.valueOf(masterlist.size()));


                        } catch (Exception e) {
                            // mProgressBar.dismiss();
//                            pnDialog.setVisibility(View.GONE);

                            e.printStackTrace();
                            masterlist = new ArrayList<>();
                            type = "Pending";
                            setAdapterData();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        masterlist = new ArrayList<>();
//                        pnDialog.setVisibility(View.GONE);

                        type = "Pending";
                        setAdapterData();

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(getApplicationContext());
                SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);
                int intType = 0;
                if (spinner_branch445.getSelectedItem().toString().equalsIgnoreCase("All")) {
                    intType = 0;
                } else if (spinner_branch445.getSelectedItem().toString().equalsIgnoreCase("Scanned Items")) {
                    intType = 1;
                } else if (spinner_branch445.getSelectedItem().toString().equalsIgnoreCase("Unmatched Items")) {
                    intType = 2;
                } else if (spinner_branch445.getSelectedItem().toString().equalsIgnoreCase("Pending Scan Items")) {
                    intType = 3;
                } else if (spinner_branch445.getSelectedItem().toString().equalsIgnoreCase("Extra Scan Items")) {
                    intType = 4;
                }


                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = AppUrls.GET_STOCK_COUNT + "&CId=" + prefs.getPrefsContactId() + "&BranchId=" + branchid + "&type=" + intType;
                    Log.e("BEFORE_PRODUCTS", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, BranchWiseStock.this);
                    params.put("val", finalparam);
                    Log.e("afterencrptionmaster", finalparam);
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

        Volley.newRequestQueue(BranchWiseStock.this).add(postRequest);
    }

    public void setAdapterData() {
        adapter = new BranchwiseStockAdapter(BranchWiseStock.this, masterlist, type, BranchWiseStock.this, BranchWiseStock.this, mType);
        lvProducts.setAdapter(adapter);

    }

    @Override
    public void onAddToSTore(String strItemId) {

    }

    @Override
    public void onProductEdit(ItemMasterhelper mItemMasterhelper, int mPosition) {

    }
}

