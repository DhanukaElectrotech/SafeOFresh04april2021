package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.adapter.SubCategoryPagerAdapterNew;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.fragments.ProductFragment;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeStoreProductsActivity extends BaseActivity implements TextWatcher, ViewPager.OnPageChangeListener, ProductFragment.onSomeEventListener, OnTabSelectListener, SwipeRefreshLayout.OnRefreshListener {
    @Nullable
    @BindView(R.id.txtPending)
    TextView txtPending;
    @Nullable
    @BindView(R.id.llTabs)
    LinearLayout llTabs;
    @Nullable
    @BindView(R.id.llDropDown)
    LinearLayout llDropDown;
    @Nullable
    @BindView(R.id.txtInStore)
    TextView txtInStore;
    TimerTask timerTask;
    Timer timer;
    Handler handler = new Handler();


    @Nullable
    @BindView(R.id.etSearch)
    AutoCompleteTextView etSearch;
    @Nullable
    @BindView(R.id.vpProducts)
    ViewPager vpProducts;
    @Nullable
    @BindView(R.id.tlSubCategory)
    SlidingTabLayout tlSubCategory;
    List<MainCatBean.CatBean> list;
    int intentType = 0;
    Context context;
    /* @Nullable
     @BindView(R.id.swipeRefresh)
     SwipeRefreshLayout swipeRefresh;
 */
    ArrayList<MainCatBean> mListCatMain;
    ArrayList<MainCatBean.CatBean> mListCategories;

    SharedPreferences prefsss;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_store_products_new;
    }

    int mPosition = 0;
    SharedPreferences.Editor mEditor;
    @Nullable
    @BindView(R.id.spinner_branch)
    Spinner spinner_branch;
    ArrayList<String> branchlist = new ArrayList<>();
    ArrayList<String> newlist = new ArrayList<>();
    String branchname, branchid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_store_products_new, container_body);
        ButterKnife.bind(this);
        context = HomeStoreProductsActivity.this;
        list = new ArrayList<>();
        strOpen = "category";
        //     swipeRefresh.setOnRefreshListener(this);
        prefsss = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        Intent intent = getIntent();
        mEditor = prefsss.edit();

        if (intent.hasExtra("branchname")) {
            HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra("map");
            branchlist = (ArrayList<String>) getIntent().getSerializableExtra("branchlist");
            newlist.add("Change Branch");
            newlist.addAll(branchlist);
            branchname = getIntent().getStringExtra("branchname");
            branchid = getIntent().getStringExtra("branchid");
            mEditor.putString("branchname", branchname);
            mEditor.putString("branchid", branchid);
            mEditor.putString("branchlist", new Gson().toJson(newlist));
            mEditor.putString("map", new Gson().toJson(hashMap));


//            .putExtra("map", branchhash).putExtra("branchname", list.get(position).getStoreName()).putExtra("branchlist",branchlist).putExtra("branchid", mCategoryBean.getBranchID())
            mEditor.putString("isIntent", "1");
            mEditor.putString("mClick", "In Store");
            mEditor.commit();
            intentType = 1;
            llTabs.setVisibility(View.GONE);
            llDropDown.setVisibility(View.VISIBLE);

            ArrayAdapter branchadapter = new ArrayAdapter(HomeStoreProductsActivity.this, android.R.layout.simple_list_item_1, newlist);

            spinner_branch.setAdapter(branchadapter);
            spinner_branch.setSelection(branchadapter.getPosition(branchname));
            spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    branchid = hashMap.get(spinner_branch.getSelectedItem().toString());

                    //  getAllProducts();
                    if (position > 0) {
                        getAllProductsindia();
                    }
                  /*  if (position>0)
                    {
                        mListReport.clear();
                        lvProducts.setAdapter(new StockWiseDetailAdapter(getApplicationContext(), mListReport));

                        branchid= hashMap.get(spinner_branch.getSelectedItem().toString());
                        getReports();
                    }*/

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } else {
            mEditor.putString("isIntent", "0");
            mEditor.putString("mClick", "Pending");
            mEditor.commit();
            intentType = 0;
            llTabs.setVisibility(View.VISIBLE);
        }
        // Bundle args = intent.getBundleExtra("BUNDLE");
        // etSearch.addTextChangedListener(this);
        txt_set.setText("Safe'O'Fresh");
        backbtn_layout.setVisibility(View.VISIBLE);
//{Type=0, supplierid=66738, ContactID=1}
        //method=All_Item_Master

        try {
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* if (vpProducts != null) {
            vpProducts.setAdapter(new SubCategoryPagerAdapter(getSupportFragmentManager(), list, Integer.valueOf("2")));
            tlSubCategory.setViewPager(vpProducts);
        }*/

        vpProducts.setCurrentItem(mPosition);
        setCartCount();
        loadSubCategories("SafeOKart1");
        if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
            getAllProductsindia();

        } else {
            getAllProducts();
        }

        etSearch.setText("");
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            setCartCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.VISIBLE);
            // txt_set.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCartCount() {
    }

    @Override
    protected void onSideSliderClick() {

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //  etSearch.removeTextChangedListener(this);
        //etSearch.setText("");
        // etSearch.addTextChangedListener(this);
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        //   filterData(editable.toString());

        //  EventBus.getDefault().post(new SearchTextChangedEvent(editable.toString()));
    }


    public List<MainCatBean.CatBean> filterData(String text) {


        List<MainCatBean.CatBean> filteredList = new ArrayList<>();

        if (text.length() > 0) {
            for (MainCatBean.CatBean product : list) {
                if (product.getStrName().toLowerCase().contains(text)) {
                    filteredList.add(product);
                }
            }
        } else {
            filteredList = list;
        }
        return filteredList;
      /*  mSubCategoryPagerAdapterNew = new SubCategoryPagerAdapterNew(getSupportFragmentManager(), filteredList, Integer.valueOf("2"));
        vpProducts.setAdapter(mSubCategoryPagerAdapterNew);
        tlSubCategory.setViewPager(vpProducts);
*/
        //adapter.filterList(filteredList);

    }

    @Override
    public void onTabSelect(int position) {
        vpProducts.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {
        vpProducts.setCurrentItem(position);
    }

/*
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchTextChanged(SearchTextChangedEvent event) {

    }*/

    @Override
    public void someEvent(String s) {

    }

    public void getAllProducts() {
        //     swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreProductsActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
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
                        Log.e("Response39311", response);


                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, HomeStoreProductsActivity.this);


                            mEditor.putString("resp1", responses);
                            mEditor.commit();

                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String ItemID = newjson.getString("ItemID");
                                    String ItemName = newjson.getString("ItemName");
                                    String companyid = newjson.getString("companyid");
                                    String GroupID = newjson.getString("GroupID");
                                    String OpeningStock = newjson.getString("OpeningStock");
                                    String ROQ = newjson.getString("ROQ");
                                    String MRP = newjson.getString("MRP");
                                    String DbSalerate1 = newjson.getString("DbSalerate1");
                                    String MOQ = newjson.getString("MOQ");
                                    String PurchaseUOM = newjson.getString("PurchaseUOM");
                                    String PurchaseUOMId = newjson.getString("PurchaseUOMId");
                                    String SaleUOM = newjson.getString("SaleUOM");
                                    String SaleUOMID = newjson.getString("SaleUOMID");
                                    String PurchaseRate = newjson.getString("PurchaseRate");
                                    String SaleRate = newjson.getString("SaleRate");
                                    String ItemSKU = newjson.getString("ItemSKU");
                                    String ItemBarcode = newjson.getString("ItemBarcode");
                                    String StockUOM = newjson.getString("StockUOM");
                                    String ItemImage = newjson.getString("ItemImage");
                                    String HSNCode = newjson.getString("HSNCode");
                                    String FileName = newjson.getString("FileName");
                                    String FilePath = newjson.getString("filepath");
                                    String VendorID = newjson.getString("VendorID");
                                    String ToShow = newjson.getString("ToShow");
                                    String AvailableQty = newjson.getString("AvailableQty");
                                    String StoreSTatus = newjson.getString("StoreSTatus");
                                    String IsDeal = newjson.getString("IsDeal");
                                    String IsNewListing = newjson.getString("IsNewListing");

                                    if (ItemID.equalsIgnoreCase("3221")) {
                                        String ss = "";
                                        Log.e("ss44444", StoreSTatus.toString());
                                    }
                                    Log.e("StoreSTatusStoreSTatus", StoreSTatus.toString());

                                    ItemMasterhelper v = new ItemMasterhelper();
                                    v.setIsNewListing(IsNewListing);
                                    v.setIsDeal(IsDeal);
                                    v.setVendorID(VendorID);
                                    v.setToShow(ToShow);
                                    v.setDbSalerate1(DbSalerate1);
                                    v.setStoreSTatus(StoreSTatus);
                                    v.setAvailableQty(AvailableQty);
                                    v.setFileName(FileName);
                                    v.setMRP(MRP);
                                    v.setItemBarcode(ItemBarcode);
                                    v.setFilepath(FilePath);
                                    v.setItemID(ItemID);
                                    v.setItemName(ItemName);
                                    v.setCompanyid(companyid);
                                    v.setGroupID(GroupID);
                                    v.setOpeningStock(OpeningStock);
                                    v.setROQ(ROQ);
                                    v.setMOQ(MOQ);
                                    v.setPurchaseUOM(PurchaseUOM);
                                    v.setPurchaseUOMId(PurchaseUOMId);
                                    v.setSaleUOM(SaleUOM);
                                    v.setSaleUOMID(SaleUOMID);
                                    v.setPurchaseRate(PurchaseRate);
                                    v.setSaleRate(SaleRate);
                                    v.setItemSKU(ItemSKU);

                                    v.setStockUOM(StockUOM);
                                    v.setItemImage(ItemImage);
                                    v.setHSNCode(HSNCode);
                                    try {
                                        v.setTotalPurchase(newjson.getString("TotalPurchase"));
                                        v.setTotalSale(newjson.getString("TotalSale"));
                                        v.setInQty(newjson.getString("InQty"));
                                        v.setOutQty(newjson.getString("OutQty"));
                                        v.setBalance(newjson.getString("Balance"));
                                    } catch (Exception e) {

                                    }
                                    masterlist.add(v);
                                    // //Log.d("masterlist", String.valueOf(masterlist.size()));

                                }

                                if (masterlist.size() > 0) {
                                    storeData();
                                }

                            } else {

                            }

                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();

                        // swipeRefresh.setRefreshing(false);
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(HomeStoreProductsActivity.this);

                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = "";
                    String branchId = "";
                    if (intentType == 1) {
                        branchId = "&BranchId=" + branchid;
                        param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + prefs.getPrefsContactId() + "&SupplierId=66738" + "&Type=" + "805" + branchId;
                    } else {
                        param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + prefs.getPrefsContactId() + "&Type=" + "805" + branchId;

                    }
                    Log.d("Beforeencrptionproduct", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, HomeStoreProductsActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionproduct", finalparam);
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


    public void getAllProductsindia() {
        //     swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreProductsActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        // response
                        //      swipeRefresh.setRefreshing(false);
                        Log.e("Response39322", responses);


                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String response=jkHelper.Decryptapi(responses,HomeStoreProductsActivity.this);


                            mEditor.putString("resp1", response);
                            mEditor.commit();

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String ItemID = newjson.getString("ItemID");
                                    String ItemName = newjson.getString("ItemName");
                                    String companyid = newjson.getString("companyid");
                                    String GroupID = newjson.getString("GroupID");
                                    String OpeningStock = newjson.getString("OpeningStock");
                                    String ROQ = newjson.getString("ROQ");
                                    String MRP = newjson.getString("MRP");
                                    String MOQ = newjson.getString("MOQ");
                                    String PurchaseUOM = newjson.getString("PurchaseUOM");
                                    String PurchaseUOMId = newjson.getString("PurchaseUOMId");
                                    String SaleUOM = newjson.getString("SaleUOM");
                                    String SaleUOMID = newjson.getString("SaleUOMID");
                                    String PurchaseRate = newjson.getString("PurchaseRate");
                                    String SaleRate = newjson.getString("SaleRate");
                                    String ItemSKU = newjson.getString("ItemSKU");
                                    String ItemBarcode = newjson.getString("ItemBarcode");
                                    String StockUOM = newjson.getString("StockUOM");
                                    String ItemImage = newjson.getString("ItemImage");
                                    String HSNCode = newjson.getString("HSNCode");
                                    String FileName = newjson.getString("FileName");
                                    String DbSalerate1 = newjson.getString("DbSalerate1");
                                    String FilePath = newjson.getString("filepath");
                                    String VendorID = newjson.getString("VendorID");
                                    String ToShow = newjson.getString("ToShow");
                                    String AvailableQty = newjson.getString("AvailableQty");
                                    String StoreSTatus = newjson.getString("StoreSTatus");
                                    String IsDeal = newjson.getString("IsDeal");
                                    String IsNewListing = newjson.getString("IsNewListing");

                                    if (ItemID.equalsIgnoreCase("3221")) {
                                        String ss = "";
                                        Log.e("ss44444", StoreSTatus.toString());
                                    }
                                    Log.e("StoreSTatusStoreSTatus", StoreSTatus.toString());

                                    ItemMasterhelper v = new ItemMasterhelper();
                                    v.setIsNewListing(IsNewListing);
                                    v.setIsDeal(IsDeal);
                                    v.setVendorID(VendorID);
                                    v.setDbSalerate1(DbSalerate1);
                                    v.setToShow(ToShow);
                                    v.setStoreSTatus(StoreSTatus);
                                    v.setAvailableQty(AvailableQty);
                                    v.setFileName(FileName);
                                    v.setMRP(MRP);
                                    v.setItemBarcode(ItemBarcode);
                                    v.setFilepath(FilePath);
                                    v.setItemID(ItemID);
                                    v.setItemName(ItemName);
                                    v.setCompanyid(companyid);
                                    v.setGroupID(GroupID);
                                    v.setOpeningStock(OpeningStock);
                                    v.setROQ(ROQ);
                                    v.setMOQ(MOQ);
                                    v.setPurchaseUOM(PurchaseUOM);
                                    v.setPurchaseUOMId(PurchaseUOMId);
                                    v.setSaleUOM(SaleUOM);
                                    v.setSaleUOMID(SaleUOMID);
                                    v.setPurchaseRate(PurchaseRate);
                                    v.setSaleRate(SaleRate);
                                    v.setItemSKU(ItemSKU);

                                    v.setStockUOM(StockUOM);
                                    v.setItemImage(ItemImage);
                                    v.setHSNCode(HSNCode);
                                    try {
                                        v.setTotalPurchase(newjson.getString("TotalPurchase"));
                                        v.setTotalSale(newjson.getString("TotalSale"));
                                        v.setInQty(newjson.getString("InQty"));
                                        v.setBalance(newjson.getString("Balance"));
                                        v.setOutQty(newjson.getString("OutQty"));
                                    } catch (Exception e) {

                                    }   try {
                                         v.setIsImage(newjson.getString("checkimage"));
                                         v.setIsname(newjson.getString("checkname"));
                                    } catch (Exception e) {

                                    }
                                    masterlist.add(v);
                                    // //Log.d("masterlist", String.valueOf(masterlist.size()));

                                }

                                if (masterlist.size() > 0) {
                                    storeData();

                                }

                            } else {

                            }

                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();

                        // swipeRefresh.setRefreshing(false);
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(HomeStoreProductsActivity.this);

                Map<String, String> params = new HashMap<String, String>();

                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

//                    String param = AppUrls.GET_ITEM_MASTER_URL+"&ContactID=" +prefs.getPrefsContactId() + "&Type=" +"805";
//                    Log.d("Beforeencrptionproduct", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, HomeStoreProductsActivity.this);
                    if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {

                    try {
                        // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                        String param = AppUrls.GET_ITEM_MASTER_URL+"&supplierid=66738"
                                +"&ContactID=" +prefs.getPrefsContactId();
                        if (intentType == 1) {
                            param=param+"&BranchId="+ branchid;
                        }
                        Log.d("Beforeencrptionproduct", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, HomeStoreProductsActivity.this);
                        params.put("val", finalparam);
                        Log.d("afterencrptionproduct", finalparam);
                        return params;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return params;
                    }

                    } else {
                    try {
                        // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                        String param = AppUrls.GET_ITEM_MASTER_URL+"&supplierid="+prefs.getPrefsContactId()
                                +"&ContactID=" +"1"+"&Type=805";
                        if (intentType == 1) {
                            param=param+"&BranchId="+ branchid;
                        }
                        Log.d("Beforeencrptionproduct", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, HomeStoreProductsActivity.this);
                        params.put("val", finalparam);
                        Log.d("afterencrptionproduct", finalparam);
                        return params;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return params;
                    }

                    }



            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
    }

    //    public void getAllProductsindia() {
//        //     swipeRefresh.setRefreshing(true);
//        final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreProductsActivity.this);
//        mProgressBar.setTitle("Safe'O'Fresh");
//        mProgressBar.setMessage("Loading...");
//        mProgressBar.setCancelable(false);
//        mProgressBar.show();
//        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
//                MODE_PRIVATE);
//        SharedPreferences.Editor mEditor = prefs.edit();
//
//        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
//        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        //      swipeRefresh.setRefreshing(false);
//                        Log.e("Response393", response);
//
//
//                        mProgressBar.dismiss();
//                        try {
//                            JKHelper jkHelper= new JKHelper();
//                            String responses=jkHelper.Decryptapi(response,HomeStoreProductsActivity.this);
//
//
//                            mEditor.putString("resp1", responses);
//                            mEditor.commit();
//
//                            JSONObject jsonObject = new JSONObject(responses);
//                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
//                            if (jsonObject.getInt("success") == 1) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject newjson = jsonArray.getJSONObject(i);
//                                    String ItemID = newjson.getString("ItemID");
//                                    String ItemName = newjson.getString("ItemName");
//                                    String companyid = newjson.getString("companyid");
//                                    String GroupID = newjson.getString("GroupID");
//                                    String OpeningStock = newjson.getString("OpeningStock");
//                                    String ROQ = newjson.getString("ROQ");
//                                    String MRP = newjson.getString("MRP");
//                                    String MOQ = newjson.getString("MOQ");
//                                    String PurchaseUOM = newjson.getString("PurchaseUOM");
//                                    String PurchaseUOMId = newjson.getString("PurchaseUOMId");
//                                    String SaleUOM = newjson.getString("SaleUOM");
//                                    String SaleUOMID = newjson.getString("SaleUOMID");
//                                    String PurchaseRate = newjson.getString("PurchaseRate");
//                                    String SaleRate = newjson.getString("SaleRate");
//                                    String ItemSKU = newjson.getString("ItemSKU");
//                                    String ItemBarcode = newjson.getString("ItemBarcode");
//                                    String StockUOM = newjson.getString("StockUOM");
//                                    String ItemImage = newjson.getString("ItemImage");
//                                    String HSNCode = newjson.getString("HSNCode");
//                                    String FileName = newjson.getString("FileName");
//                                    String FilePath = newjson.getString("filepath");
//                                    String VendorID = newjson.getString("VendorID");
//                                    String ToShow = newjson.getString("ToShow");
//                                    String AvailableQty = newjson.getString("AvailableQty");
//                                    String StoreSTatus = newjson.getString("StoreSTatus");
//                                    String IsDeal = newjson.getString("IsDeal");
//                                    String IsNewListing = newjson.getString("IsNewListing");
//
//                                    if (ItemID.equalsIgnoreCase("3221")) {
//                                        String ss = "";
//                                        Log.e("ss44444", StoreSTatus.toString());
//                                    }
//                                    Log.e("StoreSTatusStoreSTatus", StoreSTatus.toString());
//
//                                    ItemMasterhelper v = new ItemMasterhelper();
//                                    v.setIsNewListing(IsNewListing);
//                                    v.setIsDeal(IsDeal);
//                                    v.setVendorID(VendorID);
//                                    v.setToShow(ToShow);
//                                    v.setStoreSTatus(StoreSTatus);
//                                    v.setAvailableQty(AvailableQty);
//                                    v.setFileName(FileName);
//                                    v.setMRP(MRP);
//                                    v.setItemBarcode(ItemBarcode);
//                                    v.setFilepath(FilePath);
//                                    v.setItemID(ItemID);
//                                    v.setItemName(ItemName);
//                                    v.setCompanyid(companyid);
//                                    v.setGroupID(GroupID);
//                                    v.setOpeningStock(OpeningStock);
//                                    v.setROQ(ROQ);
//                                    v.setMOQ(MOQ);
//                                    v.setPurchaseUOM(PurchaseUOM);
//                                    v.setPurchaseUOMId(PurchaseUOMId);
//                                    v.setSaleUOM(SaleUOM);
//                                    v.setSaleUOMID(SaleUOMID);
//                                    v.setPurchaseRate(PurchaseRate);
//                                    v.setSaleRate(SaleRate);
//                                    v.setItemSKU(ItemSKU);
//
//                                    v.setStockUOM(StockUOM);
//                                    v.setItemImage(ItemImage);
//                                    v.setHSNCode(HSNCode);
//                                    masterlist.add(v);
//                                    // //Log.d("masterlist", String.valueOf(masterlist.size()));
//
//                                }
//
//                                if (masterlist.size() > 0) {
//                                    storeData();
//
//                                }
//
//                            } else {
//
//                            }
//
//                        } catch (Exception e) {
//                            // mProgressBar.dismiss();
//
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        mProgressBar.dismiss();
//
//                        // swipeRefresh.setRefreshing(false);
//                        // error
//                        //   Log.e("Error.Response", error.getMessage());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Preferencehelper prefs;
//                prefs = new Preferencehelper(HomeStoreProductsActivity.this);
//
//                Map<String, String> params = new HashMap<String, String>();
//                try {
//                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//
//                    String param = AppUrls.GET_ITEM_MASTER_URL+"&ContactID=" +prefs.getPrefsContactId() + "&Type=" +"805";
//                    Log.d("Beforeencrptionproduct", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, HomeStoreProductsActivity.this);
//                    params.put("val", finalparam);
//                    Log.d("afterencrptionproduct", finalparam);
//                    return params;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return params;
//                }
//
//
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
//    }
    @Override
    public void onRefresh() {

        if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
            getAllProductsindia();

        } else {
            getAllProducts();
        }
    }

    public void storeData() {
        mListCategories = new ArrayList<>();
        mListCatMain = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        try {
            JSONObject jsonObject = new JSONObject(prefs.getString("resp", ""));

            mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int a = 0; a < mListCatMain.size(); a++) {
            //    for (int b = 0; b < mListCatMain.get(a).getmListCategories().size(); b++) {
            list.addAll(mListCatMain.get(a).getmListCategories());
            //  }
        }


        if (vpProducts != null) {
          /*  mEditor.putString("mClick", "Pending");
            mEditor.commit();
          */
            txtPending.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            txtInStore.setBackgroundColor(getResources().getColor(R.color.Base_color));
            mSubCategoryPagerAdapterNew = new SubCategoryPagerAdapterNew(getSupportFragmentManager(), list, Integer.valueOf("2"));
            vpProducts.setAdapter(mSubCategoryPagerAdapterNew);
            tlSubCategory.setViewPager(vpProducts);
        }
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                item = getitemnamesearch(charSequence.toString());
                ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, item);
                etSearch.setAdapter(myAdapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (item != null) {
                    final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreProductsActivity.this);
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
                                        if (list.get(a).getStrName().equalsIgnoreCase(etSearch.getText().toString())) {
                                            Log.e("KJHGFD", list.get(a).getStrName() + " = " + etSearch.getText().toString());
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
                    final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreProductsActivity.this);
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
                                        if (list.get(a).getStrName().equalsIgnoreCase(etSearch.getText().toString())) {
                                            Log.e("KJHGFD", list.get(a).getStrName() + " = " + etSearch.getText().toString());
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
       /*        etSearch.setThreshold(2);
        etSearch.setAdapter(adapter);
        etSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vpProducts.setCurrentItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    String[] item;
    SubCategoryPagerAdapterNew mSubCategoryPagerAdapterNew;

    public String[] getitemnamesearch(String searchTerm) {
        ArrayList<String> result = new ArrayList<>();
        for (int z = 0; z < list.size(); z++) {
            if (list.get(z).getStrName().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.add(list.get(z).getStrName());
            }
        }
        String item[] = result.toArray(new String[result.size()]);
        return item;
    }


    public void makeButtonsClick(View v) {
        try {
            if (v.getId() == R.id.txtPending) {
                txtPending.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                txtInStore.setBackgroundColor(getResources().getColor(R.color.Base_color));
                mEditor.putString("mClick", "Pending");
                mEditor.commit();
            } else if (v.getId() == R.id.txtInStore) {
                txtInStore.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                txtPending.setBackgroundColor(getResources().getColor(R.color.Base_color));
                mEditor.putString("mClick", "In Store");
                mEditor.commit();

            }

            if (vpProducts != null) {
                vpProducts.setAdapter(new SubCategoryPagerAdapterNew(getSupportFragmentManager(), list, Integer.valueOf("2")));
                tlSubCategory.setViewPager(vpProducts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
                    getAllProductsindia();

                } else {
                    getAllProducts();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_store, menu);
        return true;
    }


    public void loadSubCategories(final String donutid) {

        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();


        // StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail",
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail_Store",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mEditor.putString("respSubCat", "");
                            mEditor.commit();

                            Log.d("responsiverespSubCat", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mEditor.putString("respSubCat", response);
                                mEditor.commit();


                            } else {

                                //    FancyToast.makeText(HomeStoreProductsActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //   mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  mProgressBar.dismiss();
                        // FancyToast.makeText(HomeStoreProductsActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("storename", donutid);
                return params1;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);


    }

}
