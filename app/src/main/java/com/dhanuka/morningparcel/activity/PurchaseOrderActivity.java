package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.PurchaseBean;
import com.dhanuka.morningparcel.adapter.PurchaseOrderAdapter;
import com.dhanuka.morningparcel.adapter.SubCategorypurchaseAdapter;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.JKHelper;

public class PurchaseOrderActivity extends AppCompatActivity implements onAddCartListener, OnTabSelectListener {


    ArrayList<PurchaseBean> masterlist = new ArrayList<>();


    Preferencehelper preferencehelper;
    DatabaseManager dbManager;
    @Nullable
    @BindView(R.id.txtAll)
    TextView txtAll;
    @Nullable
    @BindView(R.id.txtOOS)
    TextView txtOOS;
    @Nullable
    @BindView(R.id.txtTO)
    TextView txtTO;
    @Nullable
    @BindView(R.id.rvProducts3)
    RecyclerView lvProducts;
    @BindView(R.id.cbAll)
    CheckBox ostockclk;
    PurchaseOrderAdapter mAdapter;
    boolean isSelectall = false;
    @BindView(R.id.btnsumbit)
    Button btnsumbit;
    String sendstr;
    int clickcount = 0;
    @Nullable
    @BindView(R.id.tlSubCategory)
    SlidingTabLayout tlSubCategory;
    SubCategorypurchaseAdapter mSubCategoryPagerAdapterNew;
    @Nullable
    @BindView(R.id.vpProducts)
    ViewPager vpProducts;
    List<PurchaseBean> list;
    List<PurchaseBean> otherslist = new ArrayList<>();
    HashSet<PurchaseBean> hashSet = new HashSet<PurchaseBean>();
    PurchaseBean purchaseBean;

    TimerTask timerTask;
    Timer timer;
    Handler handler = new Handler();


    @Nullable
    @BindView(R.id.etSearch)
    AutoCompleteTextView etSearch;


    public void makeTabClick(View v) {
        if (v.getId() == R.id.txtAll) {
            intType = 1;
            txtAll.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            txtOOS.setBackgroundColor(getResources().getColor(R.color.Base_color));
            txtTO.setBackgroundColor(getResources().getColor(R.color.Base_color));
        } else if (v.getId() == R.id.txtOOS) {
            txtOOS.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            txtAll.setBackgroundColor(getResources().getColor(R.color.Base_color));
            txtTO.setBackgroundColor(getResources().getColor(R.color.Base_color));
            intType = 2;

        } else if (v.getId() == R.id.txtTO) {
            txtTO.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            txtAll.setBackgroundColor(getResources().getColor(R.color.Base_color));
            txtOOS.setBackgroundColor(getResources().getColor(R.color.Base_color));
            intType = 3;
        }

        list.clear();
        getAllProducts();

    }

    public void onBackClick(View v) {
        onBackPressed();
    }

    String[] item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order);
        ButterKnife.bind(this);
        lvProducts.setHasFixedSize(true);
        lvProducts.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(getApplicationContext());
        ostockclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if ((clickcount % 2) == 0) {
                        mAdapter.selectAll();

                    } else {

                        mAdapter.unselectall();
                    }
                    //first time clicked to do this

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        btnsumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < masterlist.size(); i++) {
                    if (ostockclk.isChecked()) {
                        if (i == 0) {
                            sendstr = masterlist.get(i).getItemid() + "/" + masterlist.get(i).getCurrentstock();


                        } else {
                            sendstr = sendstr + "//" + masterlist.get(i).getItemid() + "/" + masterlist.get(i).getCurrentstock();

                        }
                    } else {
                        if (masterlist.get(i).getSetchoose().equalsIgnoreCase("1")) {
                            if (i == 0) {
                                sendstr = masterlist.get(i).getItemid() + "/" + masterlist.get(i).getCurrentstock();


                            } else {
                                sendstr = sendstr + "//" + masterlist.get(i).getItemid() + "/" + masterlist.get(i).getCurrentstock();

                            }
                        }
                    }

                }
                Log.d("sendtstrval", sendstr);

            }
        });
        list = new ArrayList<>();
        list.clear();
        hashSet.clear();
        masterlist.clear();
        intType = 2;
        txtOOS.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        txtAll.setBackgroundColor(getResources().getColor(R.color.Base_color));
        txtTO.setBackgroundColor(getResources().getColor(R.color.Base_color));
        getAllProducts();


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    item = getitemnamesearch(charSequence.toString());
                    ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, item);
                    etSearch.setAdapter(myAdapter);
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (item != null) {
                    final ProgressDialog mProgressBar = new ProgressDialog(PurchaseOrderActivity.this);
                    mProgressBar.setTitle("Safe'O'Fresh");
                    mProgressBar.setMessage("Loading...");
                    mProgressBar.show();

                    timer = new Timer();
                    timerTask = new TimerTask() {
                        public void run() {

                            //use a handler to run a toast that shows the current timestamp
                            handler.post(new Runnable() {
                                public void run() {
                                    for (int a = 0; a < list.size(); a++) {
                                        if (list.get(a).getSuppliername().equalsIgnoreCase(etSearch.getText().toString())) {
                                            Log.e("KJHGFD", list.get(a).getSuppliername() + " = " + etSearch.getText().toString());
                                            vpProducts.setCurrentItem(a);
                                            tlSubCategory.setCurrentTab(a);
                                        }
                                    }
                                    mProgressBar.dismiss();
                                    if (timer != null) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                }
                            });
                        }
                    };

                    //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
                    timer.schedule(timerTask, 1000, 1000); //
                    // etSearch.setText(item[position]);

                }
            }
        });
        etSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (item != null) {
                    final ProgressDialog mProgressBar = new ProgressDialog(PurchaseOrderActivity.this);
                    mProgressBar.setTitle("Safe'O'Fresh");
                    mProgressBar.setMessage("Loading...");
                    mProgressBar.show();

                    timer = new Timer();
                    timerTask = new TimerTask() {
                        public void run() {

                            //use a handler to run a toast that shows the current timestamp
                            handler.post(new Runnable() {
                                public void run() {
                                    for (int a = 0; a < list.size(); a++) {
                                        if (list.get(a).getSuppliername().equalsIgnoreCase(etSearch.getText().toString())) {
                                            Log.e("KJHGFD", list.get(a).getSuppliername() + " = " + etSearch.getText().toString());
                                            vpProducts.setCurrentItem(a);
                                            tlSubCategory.setCurrentTab(a);
                                        }
                                    }
                                    mProgressBar.dismiss();
                                    if (timer != null) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                }
                            });
                        }
                    };

                    //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
                    timer.schedule(timerTask, 1000, 1000); //
                    // etSearch.setText(item[position]);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public String[] getitemnamesearch(String searchTerm) {
        ArrayList<String> result = new ArrayList<>();
        try {
            for (int z = 0; z < list.size(); z++) {
                if (list.get(z).getSuppliername().toLowerCase().contains(searchTerm.toLowerCase())) {
                    result.add(list.get(z).getSuppliername());
                }
            }
        } catch (Exception e) {

        }
        String item[] = result.toArray(new String[result.size()]);
        return item;
    }

    int intType = 2;

    public void getAllProducts() {
        final ProgressDialog mProgressBar = new ProgressDialog(PurchaseOrderActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        masterlist = new ArrayList<>();
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

                        String res = response;
                        try {

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, PurchaseOrderActivity.this);

                            Log.e("responsesasdf", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String itembarcode = newjson.getString("itembarcode");
                                    String companycosting = newjson.getString("companycosting");
                                    String ItemName = newjson.getString("ItemName");
                                    String mrp = newjson.getString("mrp");
                                    String taxrate = newjson.getString("taxrate");
                                    String SupplierName = newjson.getString("SupplierName");
                                    String SupplierID = newjson.getString("SupplierID");
                                    String FileName = newjson.getString("FileName");
                                    String filepath = newjson.getString("filepath");

                                    String TotalSale1 = newjson.getString("TotalSale1");
                                    String TotalPurchase1 = newjson.getString("TotalPurchase1");

                                    String MarginP = newjson.getString("MarginP");
                                    String Saleinlastoneweek = newjson.getString("Saleinlastoneweek");

                                    String groupname = newjson.getString("groupname");


                                    String TotalSale = newjson.getString("TotalSale");
                                    String TotalPurchase = newjson.getString("TotalPurchase");
                                    String Currentstock = newjson.getString("Currentstock");
                                    String Balance  = newjson.getString("Balance");
                                    String itemid = newjson.getString("itemid");
                                    if (SupplierName.equalsIgnoreCase("")) {
                                        SupplierName = "Others";
                                    }
                                    if (SupplierID.equalsIgnoreCase("")) {
                                        SupplierID = "0";
                                    }
                                    purchaseBean = new PurchaseBean();
                                    purchaseBean.setItembarcode(itembarcode);
                                    purchaseBean.setBalance(Balance);
                                    purchaseBean.setItemName(ItemName);
                                    purchaseBean.setItemid(itemid);
                                    purchaseBean.setCompanycosting(companycosting);
                                    purchaseBean.setMrp(mrp);
                                    purchaseBean.setTotalPurchase(TotalPurchase);
                                    purchaseBean.setMarginP(MarginP);
                                    purchaseBean.setSaleinlastoneweek(Saleinlastoneweek);
                                    purchaseBean.setGroupname(groupname);
                                    purchaseBean.setFilepath(filepath);
                                    purchaseBean.setFileName(FileName);
                                    purchaseBean.setSuppliername(SupplierName);
                                    purchaseBean.setSupplierid(SupplierID);
                                    purchaseBean.setTaxrate(taxrate);
                                    purchaseBean.setTotalSale(TotalSale);
                                    purchaseBean.setTotalPurchase(TotalPurchase);
                                    purchaseBean.setCurrentstock(Currentstock);
                                    purchaseBean.setTotalSale1(TotalSale1);
                                    purchaseBean.setTotalPurchase1(TotalPurchase1);
                                    masterlist.add(purchaseBean);


                                    list.add(purchaseBean);


                                    mEditor.putString("resppurchase", responses);
                                    mEditor.commit();


                                    //Log.d("masterlist", String.valueOf(masterlist.size()));


                                }


                                list = removeDuplicates((ArrayList<PurchaseBean>) list);

                                if (list.get(0).getSuppliername().equalsIgnoreCase("Others")) {
                                    PurchaseBean mBean = list.get(0);
                                    list.remove(0);
                                    list.add(mBean);
                                }
                            /*    for (int l = 0; l < masterlist.size(); l++) {
                                    if (masterlist.get(l).getSuppliername().equalsIgnoreCase("")) {
                                        masterlist.get(l).setSuppliername("Others");
                                        otherslist.add(masterlist.get(l));

                                    } else {
                                        list.add(masterlist.get(l));
                                    }

                                }*/
/*
                                hashSet.addAll(list);
                                list.clear();
                                list.addAll(hashSet);
                                list.addAll(otherslist);*/
                                ostockclk.setChecked(false);
//                                mAdapter = new PurchaseOrderAdapter(PurchaseOrderActivity.this, masterlist, (onAddCartListener) PurchaseOrderActivity.this, false);
//                                lvProducts.setAdapter(mAdapter);
                                mSubCategoryPagerAdapterNew = new SubCategorypurchaseAdapter(getSupportFragmentManager(), list, 2);
                                vpProducts.setAdapter(mSubCategoryPagerAdapterNew);
                                tlSubCategory.setViewPager(vpProducts);
                                mProgressBar.dismiss();


                            }


                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
                            masterlist = new ArrayList<>();
                            ostockclk.setChecked(false);
                            mSubCategoryPagerAdapterNew = new SubCategorypurchaseAdapter(getSupportFragmentManager(), list, 2);
                            vpProducts.setAdapter(mSubCategoryPagerAdapterNew);
                            tlSubCategory.setViewPager(vpProducts);
//                            mAdapter = new PurchaseOrderAdapter(PurchaseOrderActivity.this, masterlist, (onAddCartListener) PurchaseOrderActivity.this, false);
//                            lvProducts.setAdapter(mAdapter);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        masterlist = new ArrayList<>();

                        ostockclk.setChecked(false);
                        mAdapter = new PurchaseOrderAdapter(PurchaseOrderActivity.this, masterlist, (onAddCartListener) PurchaseOrderActivity.this, false);
                        lvProducts.setAdapter(mAdapter);
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


                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);


                    String param = AppUrls.GET_PURCHASE_ORDER_ITEMS + "&supplierid=" + prefs.getPrefsContactId() + "&strdt=&enddt=&companyid=0&type=" + intType;

                    Log.e("BEFORE_PRODUCTS", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, PurchaseOrderActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionmaster", finalparam);
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

        Volley.newRequestQueue(PurchaseOrderActivity.this).add(postRequest);
    }


    public ArrayList<PurchaseBean> removeDuplicates(ArrayList<PurchaseBean> list) {
        Set<PurchaseBean> set = new TreeSet(new Comparator<PurchaseBean>() {

            @Override
            public int compare(PurchaseBean o1, PurchaseBean o2) {
                if (o1.getSuppliername().equalsIgnoreCase(o2.getSuppliername())) {
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);

        final ArrayList newList = new ArrayList(set);
        return newList;
    }

    @Override
    public void onAddCart(ItemMasterhelper mItemMasterhelper, int type) {

    }

    @Override
    public void onTabSelect(int position) {
        vpProducts.setCurrentItem(position);

    }

    @Override
    public void onTabReselect(int position) {
        vpProducts.setCurrentItem(position);

    }
}