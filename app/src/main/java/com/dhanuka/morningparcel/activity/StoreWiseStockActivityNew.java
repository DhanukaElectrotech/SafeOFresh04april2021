package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.barcode.Bar;
import com.dhanuka.morningparcel.adapter.StoreproductsAdapter;
import com.dhanuka.morningparcel.beans.BranchSalesBean;
import com.dhanuka.morningparcel.beans.ChildCatBean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.beans.NewBranchsalesbean;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.Newclick;
import com.dhanuka.morningparcel.events.OnAddToSToreListener;
import com.dhanuka.morningparcel.events.OnProductEditListener;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import pub.devrel.easypermissions.EasyPermissions;

public class StoreWiseStockActivityNew extends AppCompatActivity implements Newclick, onAddCartListener, OnAddToSToreListener, OnProductEditListener, EasyPermissions.PermissionCallbacks {
    SharedPreferences.Editor mEditor;

    String[] item;
    TimerTask timerTask;
    Timer timer;
    List<NewBranchsalesbean.beanchinnerbean> mListReport = new ArrayList<>();
    Handler handler = new Handler();
    ArrayList<String> newlist = new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();
    ArrayList<String> branchlist = new ArrayList<>();
    HashMap<String, String> branchhash = new HashMap<>();
    @Nullable
    @BindView(R.id.etSearch)
    EditText mSearchView;

    @BindView(R.id.btn_submit)
    Button btn_submit;

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

    private ProgressBar pbLoading;
    String strGroupId = "";
    String strItemName = "";
    String strBranchId = "";
    ArrayList<BranchSalesBean.beanchinnerbean> mListBranches = new ArrayList<>();
    ArrayList<MainCatBean> mListCatMain = new ArrayList<>();
    ArrayList<MainCatBean.CatBean> mListCategories = new ArrayList<>();
    List<MainCatBean.CatBean> list = new ArrayList<>();

    Spinner spinner_branch;
    String branchid;
    @BindView(R.id.spinner_Item)
    Spinner spinner_Item;
    DatabaseManager dbManager;
    @Nullable
    @BindView(R.id.rvProducts)
    RecyclerView lvProducts;
    ArrayList<String> mListCategoriessearch = new ArrayList<>();
    HashMap<String, String> subcathashmap = new HashMap<>();
    @Nullable
    @BindView(R.id.rgroupstock)
    RadioGroup rstockgroup;
    @Nullable
    @BindView(R.id.allstockbtn)
    RadioButton allstockbtn;
    @Nullable
    @BindView(R.id.storestockbtn)
    RadioButton storestockbtn;
    RadioButton radioButton;
    @androidx.annotation.Nullable
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.cleartxt)
    TextView cleartxt;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 10125) {
                try {
                    String strEditText = data.getStringExtra("mBar");
                    //searchItem.setText(strEditText);
                    mSearchView.setText(strEditText);
                    getAllProducts(spinner_branch.getSelectedItem().toString(), spinner_Item.getSelectedItem().toString(), mSearchView.getText().toString(),"0");

                    // loaditemmaster(et_ItemBarcode.getText().toString());
//  checkBarcode(strEditText, 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
@BindView(R.id.pnDialog)
ProgressBar pnDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_wise_stock_new);



        ButterKnife.bind(this);
        spinner_branch = findViewById(R.id.spinner_branch44);
        mListCategories = new ArrayList<>();
        mListCatMain = new ArrayList<>();



        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.hasPermission(StoreWiseStockActivityNew.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(StoreWiseStockActivityNew.this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(StoreWiseStockActivityNew.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionModule permissionModule = new PermissionModule(StoreWiseStockActivityNew.this);
                    permissionModule.checkPermissions();

                } else {
                    startActivityForResult(new Intent(StoreWiseStockActivityNew.this, Bar.class), 10125);
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        mEditor = prefs.edit();
        lvProducts.setLayoutManager(new LinearLayoutManager(StoreWiseStockActivityNew.this, RecyclerView.VERTICAL, false));
        lvProducts.setHasFixedSize(true);
        getReports();
        mListCatMain = new ArrayList<>();
        SharedPreferences prefss = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        try {
            JSONObject jsonObject = new JSONObject(prefss.getString("resp", ""));

            mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int a = 0; a < mListCatMain.size(); a++) {
            //    for (int b = 0; b < mListCatMain.get(a).getmListCategories().size(); b++) {
            mListCategoriessearch.add("All");
            subcathashmap.put("All", "0");
            mListCategories.addAll(mListCatMain.get(a).getmListCategories());

            for (int p = 0; p < mListCatMain.get(a).getmListCategories().size(); p++) {
                mListCategoriessearch.add(mListCatMain.get(a).getmListCategories().get(p).getStrName());
                subcathashmap.put(mListCatMain.get(a).getmListCategories().get(p).getStrName(), mListCatMain.get(a).getmListCategories().get(p).getStrId());
            }
            //  }
        }

        ArrayAdapter<String> groupadapter = new ArrayAdapter<String>(StoreWiseStockActivityNew.this, android.R.layout.simple_spinner_dropdown_item, mListCategoriessearch);
        spinner_Item.setAdapter(groupadapter);
        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(getApplicationContext());
        rstockgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {




                radioButton = findViewById(checkedId);

                if (radioButton.getText().toString().equalsIgnoreCase("All"))
                {
                    getAllProducts(radioButton.getText().toString(), spinner_Item.getSelectedItem().toString(), mSearchView.getText().toString(),"0");
                }
                else  if (radioButton.getText().toString().equalsIgnoreCase("In store"))
                {
                    getAllProducts(spinner_branch.getSelectedItem().toString(), spinner_Item.getSelectedItem().toString(), mSearchView.getText().toString(),"1");

                }

            }
        });
        cleartxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    rstockgroup.clearCheck();


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             /*   if (s.toString().length() > 2) {
                    getAllProducts(spinner_branch.getSelectedItem().toString(), spinner_Item.getSelectedItem().toString(), mSearchView.getText().toString(),"0");

                } else {
                    masterlist = new ArrayList<>();
                    NewStorextockwiseadapter mAdapter = new NewStorextockwiseadapter(StoreWiseStockActivityNew.this, masterlist, StoreWiseStockActivityNew.this);
                    lvProducts.setAdapter(mAdapter);

                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllProducts(spinner_branch.getSelectedItem().toString(), spinner_Item.getSelectedItem().toString(), mSearchView.getText().toString(),"0");


            }
        });


    }


    public void getReports() {


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
                                NewBranchsalesbean storewiseBean = new Gson().fromJson(responses, NewBranchsalesbean.class);

                                mListReport = storewiseBean.getBranchdatalist();


                                for (int i = 0; i < mListReport.size(); i++) {
                                    branchhash.put(storewiseBean.getBranchdatalist().get(i).getBranchName(), storewiseBean.getBranchdatalist().get(i).getBranchId());

                                    branchlist.add(storewiseBean.getBranchdatalist().get(i).getBranchName());
                                }
                                ArrayAdapter branchadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, branchlist);
                                branchadapter.setDropDownViewResource(R.layout.simple_list_item_single_choice1);

                                spinner_branch.setAdapter(branchadapter);
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
                    String finalparam = jkHelper.Encryptapi(param, StoreWiseStockActivityNew.this);
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

    ArrayList<ChildCatBean> mListSubCat = new ArrayList<>();
    ArrayList<String> mListSubCatsearch = new ArrayList<>();
    HashMap<String, String> hashmapmaincat = new HashMap<>();


    ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
    ArrayList<ItemMasterhelper> mListProducts = new ArrayList<>();
    ArrayList<ItemMasterhelper> newdblist = new ArrayList<>();

    public void getAllProducts(final String branchid, final String strGroupId, String search,String strinstore) {

        pnDialog.setVisibility(View.VISIBLE);
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
                        pnDialog.setVisibility(View.GONE);

                        String res = response;
                        try {

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, StoreWiseStockActivityNew.this);

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
                                    String DbSalerate = newjson.getString("DbSalerate");
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
                                    v.setDbSalerate(DbSalerate);
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
                                        v.setBranchName(newjson.getString("BranchName"));
                                        v.setBranchID(newjson.getString("BranchID"));

                                        BranchSalesBean.beanchinnerbean beanchinnerbean = new BranchSalesBean.beanchinnerbean();
                                        beanchinnerbean.setBranchId(newjson.getString("BranchID"));
                                        beanchinnerbean.setBranchName(newjson.getString("BranchName"));

                                        mListBranches.add(beanchinnerbean);
                                    } catch (Exception e) {

                                    }
                                    masterlist.add(v);
                                    // //Log.d("masterlist", String.valueOf(masterlist.size()));

                                }
                                type = "Pending";

                                Log.d("Mylistsize", String.valueOf(masterlist.size()));


                            }
                            setAdapterData();


                        } catch (Exception e) {
                            // mProgressBar.dismiss();
                            pnDialog.setVisibility(View.GONE);

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
                        pnDialog.setVisibility(View.GONE);

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
                String branchkey = "", groupkey = "";
                if (branchid.equalsIgnoreCase("")) {
                    branchkey = "";
                } else {
                    branchkey = String.valueOf(branchhash.get(branchid));


                }
                if (strGroupId.equalsIgnoreCase("")) {
                    groupkey = "";
                } else {
                    groupkey = String.valueOf(subcathashmap.get(strGroupId));
                }


                Map<String, String> params = new HashMap<String, String>();
                try {

                    String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + prefs.getPrefsContactId() + "&Type=" + "28" + "&SupplierID=" + prefs.getCID() + "&filterItemName=" + search + "&BranchId=" + branchkey + "&GroupId=" + groupkey + "&Isfilter=1"+"&Instore="+strinstore;
                    Log.e("BEFORE_PRODUCTS", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, StoreWiseStockActivityNew.this);
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

        Volley.newRequestQueue(StoreWiseStockActivityNew.this).add(postRequest);
    }

    int mType = 0;

    @Override
    public void senddata(ItemMasterhelper orderBean, int pos) {


        getAllProducts(spinner_branch.getSelectedItem().toString(), spinner_Item.getSelectedItem().toString(), orderBean.getItemName(),"0");


    }

    String type = "Pending";

    StoreproductsAdapter adapter;

    public void setAdapterData() {
        adapter = new StoreproductsAdapter(StoreWiseStockActivityNew.this, masterlist, type, this, this, mType);
        lvProducts.setAdapter(adapter);

    }

    @Override
    public void onAddToSTore(String strItemId) {

    }

    @Override
    public void onProductEdit(ItemMasterhelper mItemMasterhelper, int mPosition) {

    }
    int isPermit = 0;
    @Override
    public void onAddCart(ItemMasterhelper mItemMasterhelper, int type) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        isPermit = 1;

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        isPermit = 0;

    }
}