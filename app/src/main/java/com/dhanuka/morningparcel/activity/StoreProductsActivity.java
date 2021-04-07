package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
 import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

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
import com.dhanuka.morningparcel.adapter.CategoryPagerAdapter;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.beans.StoreBean;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreProductsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    /*   @Nullable
       @BindView(R.id.rv_category)
       RecyclerView mRecyclerView;
   */
    @Nullable
    @BindView(R.id.vpProducts)
    ViewPager vpProducts;
    @Nullable
    @BindView(R.id.tlSubCategory)
    TabLayout tlSubCategory;

    @Nullable
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    ArrayList<ArrayList<StoreBean>> mListStoreData = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_store_products;
    }

    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_store_products, container_body);

        ButterKnife.bind(this);

        list = new ArrayList<>();
        //  list.add("All");
        list.add("In Store");
        list.add("Pending");

        swipeRefresh.setOnRefreshListener(this);


      /*  mRecyclerView.setLayoutManager(new LinearLayoutManager(StoreProductsActivity.this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(new HomeStoreAdapter(StoreProductsActivity.this, mListCat));
*/

        getAllProducts();

    }

    @Override
    protected void onSideSliderClick() {

    }


    public void getAllProducts() {
        swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(StoreProductsActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST,getString(R.string.URL_BASE_URL) ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        swipeRefresh.setRefreshing(false);
                        Log.e("Response393", response);

                        String res = response;
                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper= new JKHelper();
                            String responses=jkHelper.Decryptapi(response,StoreProductsActivity.this);

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
                                    String MRP = newjson.getString("MRP");
                                    String HSNCode = newjson.getString("HSNCode");
                                    String FileName = newjson.getString("FileName");
                                    String FilePath = newjson.getString("filepath");
                                    String VendorID = newjson.getString("VendorID");
                                    String ToShow = newjson.getString("ToShow");
                                    String AvailableQty = newjson.getString("AvailableQty");
                                    String StoreSTatus = newjson.getString("StoreSTatus");

                                    if (ItemID.equalsIgnoreCase("3221")) {
                                        String ss = "";
                                        Log.e("ss44444", StoreSTatus.toString());
                                    }
                                    Log.e("StoreSTatusStoreSTatus", StoreSTatus.toString());

                                    ItemMasterhelper v = new ItemMasterhelper();
                                    v.setVendorID(VendorID);
                                    v.setToShow(ToShow);
                                    v.setStoreSTatus(StoreSTatus);
                                    v.setAvailableQty(AvailableQty);
                                    v.setFileName(FileName);
                                    v.setMRP(MRP);
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
                                    v.setItemBarcode(ItemBarcode);
                                    v.setStockUOM(StockUOM);
                                    v.setItemImage(ItemImage);
                                    v.setHSNCode(HSNCode);
                                    masterlist.add(v);
                                    // //Log.d("masterlist", String.valueOf(masterlist.size()));

                                }

                                if (masterlist.size() > 0) {
                                    storeData();

                                }
                              /*  DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                    @Override
                                    public void run(SQLiteDatabase database) {

                                        All_Item_Master masterdao = new All_Item_Master(database, getApplicationContext());
                                        ArrayList<ItemMasterhelper> list = masterlist;
                                        masterdao.deleteAll();
                                        masterdao.insert(list);
                                        Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_LONG).show();

                                    }
                                });*/
                                ;
                              /*  masterlistitem = new ArrayList<>();
                                All_Item_Master pd = new All_Item_Master(database, ItemMasterActivity.this);
                                masterlistitem = pd.selectAll();
                                Log.d("listsize1", String.valueOf(masterlistitem.size()));
                                masteradpter = new ItemMasterAdapter(masterlistitem, getApplicationContext());
                                mastercontainer.setAdapter(masteradpter);

*/
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

                        swipeRefresh.setRefreshing(false);
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(StoreProductsActivity.this);

                Map<String, String> params = new HashMap<String, String>();


                try {
                    JKHelper jkHelper= new JKHelper();
                    String param = AppUrls.GET_ITEM_MASTER_URL+ "&ContactID=" + prefs.getPrefsContactId()+ "&Type=" + "805";
                    Log.e("beforeenc", param);
                    param = jkHelper.Encryptapi(param, StoreProductsActivity.this);
                    params.put("val", param);
                    Log.e("afterenc", param);
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

    ArrayList<MainCatBean> mListCatMain;
    //  ArrayList<StoreBean> mListCatAll;
    ArrayList<StoreBean> mListCatInStore;
    ArrayList<StoreBean> mListCatPending;
    ArrayList<MainCatBean.CatBean> mListCategories;
    ArrayList<ItemMasterhelper> mListProductsAll = new ArrayList<>();

    public void storeData() {
        mListCategories = new ArrayList<>();
        mListCatMain = new ArrayList<>();
        //    mListCatAll = new ArrayList<>();
        mListCatInStore = new ArrayList<>();
        mListCatPending = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        try {
            JSONObject jsonObject = new JSONObject(prefs.getString("resp", ""));

            mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(prefs.getString("resp1", ""));
            Gson gson = new Gson();
            mListProductsAll = gson.fromJson(jsonObject.getString("returnds"), new TypeToken<List<ItemMasterhelper>>() {
            }.getType());
            Log.e("mListProductsAll", mListProductsAll.size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int a = 0; a < mListCatMain.size(); a++) {
            //    for (int b = 0; b < mListCatMain.get(a).getmListCategories().size(); b++) {
            mListCategories.addAll(mListCatMain.get(a).getmListCategories());
            //  }
        }

        //  if (type.equalsIgnoreCase("All")) {
        for (int as = 0; as < mListCategories.size(); as++) {

            ArrayList<ItemMasterhelper> mListProducts = new ArrayList<>();
            ArrayList<ItemMasterhelper> mListProducts1 = new ArrayList<>();
            // ArrayList<ItemMasterhelper> mListProducts2 = new ArrayList<>();
            for (int bs = 0; bs < mListProductsAll.size(); bs++) {
                if (mListProductsAll.get(bs).getItemID().equalsIgnoreCase("3221")) {
                    String ss = "";
                    Log.e("ss44444", mListProductsAll.get(bs).getStoreSTatus().toString() + "\n" + mListProductsAll.get(bs).getGroupID() + "\n" + mListCategories.get(as).getStrId());
                }
                if (mListProductsAll.get(bs).getGroupID().equalsIgnoreCase(mListCategories.get(as).getStrId()) && mListProductsAll.get(bs).getStoreSTatus().equalsIgnoreCase("In Store")) {
                    mListProducts.add(mListProductsAll.get(bs));
                    Log.e("sder", mListProductsAll.get(bs).getStoreSTatus());
                }
                if (mListProductsAll.get(bs).getGroupID().equalsIgnoreCase(mListCategories.get(as).getStrId()) && !mListProductsAll.get(bs).getStoreSTatus().equalsIgnoreCase("In Store")) {
                    mListProducts1.add(mListProductsAll.get(bs));
                }
               /* if (mListProductsAll.get(bs).getGroupID().equalsIgnoreCase(mListCategories.get(as).getStrId())) {
                    mListProducts2.add(mListProductsAll.get(bs));

                }*/
            }
/*
            if (mListProducts2.size() > 0) {
                StoreBean storeBean = new StoreBean();
                storeBean.setmBean(mListCategories.get(as));
                storeBean.setmListProducts(mListProducts2);
                mListCatAll.add(storeBean);

            }*/

            if (mListProducts1.size() > 0) {
                StoreBean storeBean1 = new StoreBean();
                storeBean1.setmBean(mListCategories.get(as));
                storeBean1.setmListProducts(mListProducts1);
                mListCatPending.add(storeBean1);
            }

            if (mListProducts.size() > 0) {
                StoreBean storeBean2 = new StoreBean();
                storeBean2.setmBean(mListCategories.get(as));
                storeBean2.setmListProducts(mListProducts);
                mListCatInStore.add(storeBean2);
            }


        }
        //  Log.e("GFGFGF", mListCatAll.size() + " = " + mListCatInStore.size() + " = " + mListCatPending.size());

        //     mListStoreData.add(mListCatAll);
        mListStoreData.add(mListCatInStore);
        mListStoreData.add(mListCatPending);


        if (vpProducts != null) {
            vpProducts.setAdapter(new CategoryPagerAdapter(getSupportFragmentManager(), list, Integer.valueOf("2"), mListStoreData));
            tlSubCategory.setupWithViewPager(vpProducts);
        }


    }


    @Override
    public void onRefresh() {

        getAllProducts();


    }
}
