package com.dhanuka.morningparcel.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.android.material.navigation.NavigationView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.OnQueryChangeListener;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.ProductsAdapter;
import com.dhanuka.morningparcel.adapter.ProductsAdapterNew;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.Utility;

public class SearchActivityNew extends AppCompatActivity implements SearchView.OnQueryTextListener, OnQueryChangeListener, onAddCartListener {

    @BindView(R.id.rvProducts)
    RecyclerView lvProducts;
    @BindView(R.id.txtProducts)
    AppCompatTextView txtProducts;
    @BindView(R.id.imgBack)
    AppCompatImageView imgBack;
    @BindView(R.id.txtSrch)
    TextView txtSrch;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.mSearchView)
    EditText mSearchView;
    @BindView(R.id.layoutTwo)
    LinearLayout layoutTwo;
    @BindView(R.id.linearContinue)
    LinearLayout linearContinue;
    DatabaseManager dbManager;
    @Nullable
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.lastCart)
    LinearLayout lastCart;

    ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
    ArrayList<ItemMasterhelper> mListProducts = new ArrayList<>();
    ArrayList<ItemMasterhelper> newdblist = new ArrayList<>();
    Preferencehelper preferencehelper;
    RequestQueue requestQueue;

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    double dbDiscount = 0.0;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new);
        ButterKnife.bind(this);


        preferencehelper = new Preferencehelper(SearchActivityNew.this);
        if (preferencehelper.getPREFS_trialuser().equalsIgnoreCase("0")) {
            tvCount.setVisibility(View.GONE);
            lastCart.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
            lastCart.setVisibility(View.VISIBLE);
        }
        prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        if (prefs.getString("discount", "0.0").isEmpty()) {
            dbDiscount = 0.0;

        } else {
            try {
                dbDiscount = Double.parseDouble(prefs.getString("discount", "0.0"));
            } catch (Exception e) {
                dbDiscount = 0.0;
            }
        }

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    Log.d("onqueryinside", s.toString());
                    masterlist = new ArrayList<>();
                    getAllProducts(s.toString());
                } else {
                    Log.d("onqueryoutside", s.toString());
                    masterlist = new ArrayList<>();
                    ProductsAdapterNew mAdapter = new ProductsAdapterNew(SearchActivityNew.this, masterlist, SearchActivityNew.this);
                    lvProducts.setAdapter(mAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        SearchManager searchManager = (SearchManager) SearchActivityNew.this.getSystemService(Context.SEARCH_SERVICE);
        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(getApplicationContext());


      /*  if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            mSearchView.setIconified(false);
            mSearchView.setOnQueryTextListener(this);
        }*/

        lvProducts.setLayoutManager(new LinearLayoutManager(SearchActivityNew.this, RecyclerView.VERTICAL, false));
        lvProducts.setHasFixedSize(true);


        setCartCount();
    }

//    public void getDta() {
//        try {
//            com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
//                @Override
//                public void run(SQLiteDatabase database) {
//
//                    All_Item_Master masterdao = new All_Item_Master(database, SearchActivityNew.this);
//                    if (masterdao.getRowCount() > 0) {
//                        ArrayList<ItemMasterhelper> list = masterlist;
//                        newdblist = masterdao.selectAll();
//                        Toast.makeText(SearchActivityNew.this, "Data saved", Toast.LENGTH_LONG).show();
//                    } else {
//                        getAllProducts();
//
//                    }
//                }
//            });
//            if (newdblist.size() > 0) {
//                Toast.makeText(SearchActivityNew.this, "Data exist", Toast.LENGTH_LONG).show();
//                Log.d("Newdblistsize", String.valueOf(newdblist.size()));
//
//            }
//            if (prefs.getString("resp1", "-1").equalsIgnoreCase("-1")) {
//                // getAllProducts();
//            } else {
//                // loadLocal(prefs.getString("resp1", "-1"));
//                masterlist = newdblist;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            getAllProducts();
//        }
//    }

    public void makeCartClick(View view) {
        startActivity(new Intent(SearchActivityNew.this, CartActivity.class));
        finish();
    }

//    public void getAllProducts() {
//        final ProgressDialog mProgressBar = new ProgressDialog(SearchActivityNew.this);
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
//                        Log.e("Response393", response);
//
//
//                        mProgressBar.dismiss();
//                        try {
//                            JKHelper jkHelper = new JKHelper();
//                            String responses = jkHelper.Decryptapi(response, SearchActivityNew.this);
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
//
//                                    ItemMasterhelper v = new ItemMasterhelper();
//                                    v.setItemID(ItemID);
//                                    v.setItemName(ItemName);
//                                    v.setCompanyid(companyid);
//                                    v.setGroupID(GroupID);
//                                    v.setOpeningStock(OpeningStock);
//                                    v.setROQ(ROQ);
//                                    v.setMOQ(MOQ);
//                                    v.setMRP(MRP);
//                                    v.setPurchaseUOM(PurchaseUOM);
//                                    v.setPurchaseUOMId(PurchaseUOMId);
//                                    v.setSaleUOM(SaleUOM);
//                                    v.setSaleUOMID(SaleUOMID);
//                                    v.setPurchaseRate(PurchaseRate);
//                                    v.setSaleRate(SaleRate);
//                                    v.setItemSKU(ItemSKU);
//                                    v.setItemBarcode(ItemBarcode);
//                                    v.setStockUOM(StockUOM);
//                                    v.setItemImage(ItemImage);
//                                    v.setHSNCode(HSNCode);
//                                    masterlist.add(v);
//                                    //Log.d("masterlist", String.valueOf(masterlist.size()));
//
//                                }
//
//                                getDta();
//                                // loadLocal(response);
//
//
//                                ;
//                              /*  masterlistitem = new ArrayList<>();
//                                All_Item_Master pd = new All_Item_Master(database, ItemMasterActivity.this);
//                                masterlistitem = pd.selectAll();
//                                Log.d("listsize1", String.valueOf(masterlistitem.size()));
//                                masteradpter = new ItemMasterAdapter(masterlistitem, getApplicationContext());
//                                mastercontainer.setAdapter(masteradpter);
//
//*/
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
//                        // error
//                        //   Log.e("Error.Response", error.getMessage());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Preferencehelper prefs;
//                prefs = new Preferencehelper(SearchActivityNew.this);
//
//                Map<String, String> params = new HashMap<String, String>();
//
//
//                try {
//                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//
//                    String param = AppUrls.GET_ITEM_MASTER_URL + "&contactid=" + prefs.getPrefsContactId() + "&type=" + "28";
//                    Log.d("Beforeencrption", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, SearchActivityNew.this);
//                    params.put("val", finalparam);
//                    Log.d("afterencrption", finalparam);
//                    return params;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return params;
//                }
//
//
//            }
//        };
//
//
//        if (requestQueue == null) {
//            requestQueue = Volley.newRequestQueue(SearchActivityNew.this);
//            Log.d("SettingRequestqueuegl", "Setting a new request queue");
//        }
//        requestQueue.add(postRequest);
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // startActivity(new Intent(SearchActivity.this, ProductsActivity.class));
        return false;
    }

    public void makebackiClick(View v) {
        onBackPressed();
    }

    String msrch = "";

    @Override
    public boolean onQueryTextChange(String newText) {


        return false;
    }


    public void loadLocal(String resp) {
        masterlist = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(resp);
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
                    String HSNCode = newjson.getString("HSNCode");
                    String FileName = newjson.getString("FileName");
                    String FilePath = newjson.getString("filepath");
                    String MRP = newjson.getString("MRP");

                    ItemMasterhelper v = new ItemMasterhelper();
                    v.setFileName(FileName);
                    v.setFilepath(FilePath);
                    v.setItemID(ItemID);
                    v.setItemID(ItemID);
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
                    v.setMRP(MRP);

                    if (dbManager.exists(Integer.parseInt(ItemID), ItemSKU)) {
                        CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(ItemID), ItemSKU);
                        v.setQuantity(cartProduct.getQuantity() + "");
                    }


                    masterlist.add(v);


                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    ProductsAdapter adapter;

    public void setSearchData(ArrayList<ItemMasterhelper> mList) {
        Log.e(":asddd", mList.size() + "");
        ProductsAdapterNew mAdapter = new ProductsAdapterNew(SearchActivityNew.this, mListProducts, SearchActivityNew.this);
        lvProducts.setAdapter(mAdapter);
        if (mList.size() < 1) {
            txtSrch.setText("No products found with : " + msrch);
            lvProducts.setVisibility(View.GONE);
            linearContinue.setVisibility(View.VISIBLE);
        } else {
            lvProducts.setVisibility(View.VISIBLE);
            linearContinue.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setCartCount();
    }

    @Override
    public void onAddCart(ItemMasterhelper product, int type) {
       /* if (dbManager.getRowCount() > 0 && !dbManager.isShopIdExists(product.getShopId())) {
            Toast.makeText(context, "You cannot add products from another shop", Toast.LENGTH_LONG).show();
        } else {*/
//        if (!preferencehelper.getPrefsContactId().isEmpty()) {
//            if (type == 3) {
//                CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(product.getItemID()), product.getItemSKU());
//                dbManager.delete(Integer.parseInt(product.getItemID()));
//                dbManager.update(cartProduct);
//                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"3");
//
//
//            } else if (dbManager.exists(Integer.parseInt(product.getItemID()), product.getItemSKU())) {
//                CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(product.getItemID()), product.getItemSKU());
//                if (type == 1) {
//                    cartProduct.setQuantity(cartProduct.getQuantity() + 1);
//
//                } else {
//                    if (cartProduct.getQuantity() > 0) {
//                        cartProduct.setQuantity(cartProduct.getQuantity() - 1);
//
//                    } else {
//                        dbManager.delete(Integer.parseInt(product.getItemID()));
//                        Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"3");
//
//
//                    }
//                }
//                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"2");
//
//                dbManager.update(cartProduct);
//            } else {
//                DecimalFormat precision = new DecimalFormat("0.00");
//                double amount = Double.parseDouble(product.getSaleRate());
//                double res = (amount / 100.0f) * dbDiscount;
//                Float finalprice = Float.parseFloat(product.getSaleRate()) - Float.parseFloat(String.valueOf(res));
//
//
//                CartProduct cartProduct = new CartProduct();
//                cartProduct.setItemID(Integer.parseInt(product.getItemID()));
//                cartProduct.setCompanyid(product.getCompanyid());
//                cartProduct.setItemName(product.getItemName());
//                cartProduct.setGroupID(product.getGroupID());
//                cartProduct.setOpeningStock(product.getOpeningStock());
//                cartProduct.setMOQ(product.getMOQ());
//                cartProduct.setROQ(product.getROQ());
//                cartProduct.setPurchaseUOM(product.getPurchaseUOM());
//                cartProduct.setPurchaseUOMId(product.getPurchaseUOMId());
//                cartProduct.setSaleUOM(product.getSaleUOM());
//                cartProduct.setSaleUOMID(product.getSaleUOMID());
//                cartProduct.setPurchaseRate(product.getPurchaseRate());
//                cartProduct.setSaleRate(precision.format(Double.parseDouble(String.valueOf(finalprice))));
//                cartProduct.setItemSKU(product.getItemSKU());
//                cartProduct.setItemBarcode(product.getItemBarcode());
//                cartProduct.setStockUOM(product.getStockUOM());
//                cartProduct.setItemImage(product.getFileName() + "&filePath=" + product.getFilepath());
//                cartProduct.setHSNCode(product.getHSNCode());
//                cartProduct.setQuantity(Integer.parseInt(product.getQuantity()));
//                cartProduct.setSubCategory(product.getGroupID());
//                cartProduct.setMRP(product.getMRP());
//                // Log.d("cartproductprint",cartProduct.getMRP()+"\n"+product.getMRP()+"\n");
//
//
//                dbManager.insert(cartProduct);
//                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),String.valueOf("1"));
//
//            }
//            setCartCount();
//            FancyToast.makeText(getApplicationContext(), "Item added to cart", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
//            // setCartCount();
//        } else {
//            FancyToast.makeText(getApplicationContext(), "Your are not logged in , please login first", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
//
//        }


        if (!preferencehelper.getPrefsContactId().isEmpty()) {


            if (type == 3) {
                CartProduct cartProduct = dbManager.getProductFromItem(Integer.parseInt(product.getItemID()));

                Utility.checkoutOrder(cartProduct, preferencehelper.getPrefsContactId(), getApplicationContext(), "3");
                dbManager.delete(Integer.parseInt(product.getItemID()));
                dbManager.update(cartProduct);
                // Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),String.valueOf(type));

            } else if (dbManager.exists1(product.getItemID())) {
                CartProduct cartProduct = dbManager.getProductFromItem(Integer.parseInt(product.getItemID()));
                if (type == 1) {

                    cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                    dbManager.update(cartProduct);
                    Utility.checkoutOrder(cartProduct, preferencehelper.getPrefsContactId(), getApplicationContext(), "2");


                } else if (type == 2) {

                    cartProduct.setQuantity(Integer.parseInt(product.getQuantity()));
                    dbManager.update(cartProduct);
                    Utility.checkoutOrder(cartProduct, preferencehelper.getPrefsContactId(), getApplicationContext(), "2");

                } else if (type == 6) {

//                    dbManager.update(cartProduct);
//                    Log.d("filterquantityinside", String.valueOf(cartProduct.getQuantity()));
//                    Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),"2");
//                    dbManager.update(cartProduct);


                    cartProduct.setQuantity(Integer.parseInt(product.getQuantity()));
                    dbManager.update(cartProduct);
                    Log.e("filterquantityinside", cartProduct.getQuantity() + "\n" + Integer.parseInt(product.getQuantity()));
                    Utility.checkoutOrder(cartProduct, preferencehelper.getPrefsContactId(), getApplicationContext(), "2");
                    // Utility.checkoutOrder(cartList.get(position), preferencehelper.getPrefsContactId(), getApplicationContext(), "2");
                    dbManager.update(cartProduct);
                } else {
                    if (cartProduct.getQuantity() > 0) {
                        cartProduct.setQuantity(cartProduct.getQuantity() - 1);
                        dbManager.delete(Integer.parseInt(product.getItemID()));

                    } else {

                        Utility.checkoutOrder(cartProduct, preferencehelper.getPrefsContactId(), getApplicationContext(), "3");
                        dbManager.delete(Integer.parseInt(product.getItemID()));

                    }
                }

//                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),"2");
//                dbManager.update(cartProduct);
            } else {
                DecimalFormat precision = new DecimalFormat("0.00");
                double amount = Double.parseDouble(product.getSaleRate());
                double res = (amount / 100.0f) * dbDiscount;
                Float finalprice = Float.parseFloat(product.getSaleRate()) - Float.parseFloat(String.valueOf(res));


                CartProduct cartProduct = new CartProduct();
                cartProduct.setItemID(Integer.parseInt(product.getItemID()));
                cartProduct.setCompanyid(product.getCompanyid());
                cartProduct.setItemName(product.getItemName());
                cartProduct.setGroupID(product.getGroupID());
                cartProduct.setOpeningStock(product.getOpeningStock());
                cartProduct.setMOQ(product.getMOQ());
                cartProduct.setROQ(product.getROQ());
                cartProduct.setPurchaseUOM(product.getPurchaseUOM());
                cartProduct.setPurchaseUOMId(product.getPurchaseUOMId());
                cartProduct.setSaleUOM(product.getSaleUOM());
                cartProduct.setSaleUOMID(product.getSaleUOMID());
                cartProduct.setPurchaseRate(product.getPurchaseRate());
                cartProduct.setSaleRate(precision.format(Double.parseDouble(String.valueOf(finalprice))));
                cartProduct.setItemSKU(product.getItemSKU());
                cartProduct.setItemBarcode(product.getItemBarcode());
                cartProduct.setStockUOM(product.getStockUOM());
                cartProduct.setItemImage(product.getFileName() + "&filePath=" + product.getFilepath());
                cartProduct.setHSNCode(product.getHSNCode());
                cartProduct.setQuantity(Integer.parseInt(product.getQuantity()));
                cartProduct.setSubCategory(product.getGroupID());
                cartProduct.setMRP(product.getMRP());

                // Log.d("cartproductprint",cartProduct.getMRP()+"\n"+product.getMRP()+"\n");


                dbManager.insert(cartProduct);

                Utility.checkoutOrder(cartProduct, preferencehelper.getPrefsContactId(), getApplicationContext(), String.valueOf("1"));
            }
            setCartCount();
            FancyToast.makeText(getApplicationContext(), "Item added to cart", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            // setCartCount();
        } else {
            FancyToast.makeText(getApplicationContext(), "Your are not logged in , please login first", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

        }
    }

    private void setCartCount() {
        tvCount.setText(dbManager.getTotalQty() + "");

    }

//    @Override
//    public void onAddCart(ItemMasterhelper product, int type) {
//       /* if (dbManager.getRowCount() > 0 && !dbManager.isShopIdExists(product.getShopId())) {
//            Toast.makeText(context, "You cannot add products from another shop", Toast.LENGTH_LONG).show();
//        } else {*/
//        if (!preferencehelper.getPrefsContactId().isEmpty()) {
//            if (type == 3) {
//                CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(product.getItemID()), product.getItemSKU());
//                dbManager.delete(Integer.parseInt(product.getItemID()));
//                dbManager.update(cartProduct);
//                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"3");
//
//
//            } else if (dbManager.exists(Integer.parseInt(product.getItemID()), product.getItemSKU())) {
//                CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(product.getItemID()), product.getItemSKU());
//                if (type == 1) {
//                    cartProduct.setQuantity(cartProduct.getQuantity() + 1);
//
//                } else {
//                    if (cartProduct.getQuantity() > 0) {
//                        cartProduct.setQuantity(cartProduct.getQuantity() - 1);
//
//                    } else {
//                        dbManager.delete(Integer.parseInt(product.getItemID()));
//                        Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"3");
//
//
//                    }
//                }
//                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"2");
//
//                dbManager.update(cartProduct);
//            } else {
//                DecimalFormat precision = new DecimalFormat("0.00");
//                double amount = Double.parseDouble(product.getSaleRate());
//                double res = (amount / 100.0f) * dbDiscount;
//                Float finalprice = Float.parseFloat(product.getSaleRate()) - Float.parseFloat(String.valueOf(res));
//
//
//                CartProduct cartProduct = new CartProduct();
//                cartProduct.setItemID(Integer.parseInt(product.getItemID()));
//                cartProduct.setCompanyid(product.getCompanyid());
//                cartProduct.setItemName(product.getItemName());
//                cartProduct.setGroupID(product.getGroupID());
//                cartProduct.setOpeningStock(product.getOpeningStock());
//                cartProduct.setMOQ(product.getMOQ());
//                cartProduct.setROQ(product.getROQ());
//                cartProduct.setPurchaseUOM(product.getPurchaseUOM());
//                cartProduct.setPurchaseUOMId(product.getPurchaseUOMId());
//                cartProduct.setSaleUOM(product.getSaleUOM());
//                cartProduct.setSaleUOMID(product.getSaleUOMID());
//                cartProduct.setPurchaseRate(product.getPurchaseRate());
//                cartProduct.setSaleRate(precision.format(Double.parseDouble(String.valueOf(finalprice))));
//                cartProduct.setItemSKU(product.getItemSKU());
//                cartProduct.setItemBarcode(product.getItemBarcode());
//                cartProduct.setStockUOM(product.getStockUOM());
//                cartProduct.setItemImage(product.getFileName() + "&filePath=" + product.getFilepath());
//                cartProduct.setHSNCode(product.getHSNCode());
//                cartProduct.setQuantity(Integer.parseInt(product.getQuantity()));
//                cartProduct.setSubCategory(product.getGroupID());
//                cartProduct.setMRP(product.getMRP());
//                // Log.d("cartproductprint",cartProduct.getMRP()+"\n"+product.getMRP()+"\n");
//
//
//                dbManager.insert(cartProduct);
//                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),String.valueOf("1"));
//
//            }
//            setCartCount();
//            FancyToast.makeText(getApplicationContext(), "Item added to cart", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
//            // setCartCount();
//        } else {
//            FancyToast.makeText(getApplicationContext(), "Your are not logged in , please login first", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
//
//        }
//    }

//    private void setCartCount() {
//        tvCount.setText(dbManager.getTotalQty() + "");
//
//    }


    @Override
    public void onQueryChange(String strValue) {
        if (strValue.length() > 2) {
            Log.d("onqueryinside", strValue);
            masterlist = new ArrayList<>();
            getAllProducts(strValue);
        } else {
            Log.d("onqueryoutside", strValue);
            masterlist = new ArrayList<>();
            ProductsAdapterNew mAdapter = new ProductsAdapterNew(SearchActivityNew.this, masterlist, SearchActivityNew.this);
            lvProducts.setAdapter(mAdapter);
        }
    }


    public void setSearchDataNew(String newText) {
        msrch = newText;
    /*    if (newText.toString().length() >= 1) {
            imgClear.setVisibility(View.VISIBLE);
        } else {
            imgClear.setVisibility(View.GONE);
        }*/
        String text = newText.toLowerCase();
        Log.e("texttext", text);
        mListProducts = new ArrayList<>();
        if (newText.length() == 0) {
            mListProducts = new ArrayList<>();
            Log.e("conditionsss", "1");

        } else {
            Log.e("conditionsss", "2 " + newdblist.size());

            for (ItemMasterhelper product : newdblist) {
                Log.e("new Data", product.getItemBarcode() + " == " + product.getItemName() + " == " + product.getHSNCode() + " == " + product.getItemSKU() + "\n" + text);

                if (product.getItemBarcode().toLowerCase().contains(text) || product.getItemName().toLowerCase().contains(text) || product.getHSNCode().toLowerCase().contains(text) || product.getItemSKU().toLowerCase().contains(text)) {
                    mListProducts.add(product);
                    Log.e("conditionsss", "3");

                }
            }
        }

        Log.e("conditionsss", "" + mListProducts.size());

        setSearchData(mListProducts);

    }

    @Override
    public void onBlank() {
        setSearchDataNew("");

    }


    public void getAllProducts(String search) {

        masterlist.clear();
        ProductsAdapterNew mAdapter = new ProductsAdapterNew(SearchActivityNew.this, masterlist, SearchActivityNew.this);
        lvProducts.setAdapter(mAdapter);
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
                            String responses = jkHelper.Decryptapi(response, SearchActivityNew.this);


                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String ItemID = newjson.getString("ItemID");
                                    String ItemName = newjson.getString("ItemName");
                                    String GroupID = newjson.getString("GroupID");
                                    String MRP = newjson.getString("MRP");
                                    String FileName = newjson.getString("FileName");
                                    String filepath = newjson.getString("filepath");
                                    String SaleUOM = newjson.getString("SaleUOM");
                                    String SaleRate = newjson.getString("SaleRate");
                                    String IsDeal = newjson.getString("IsDeal");
                                    String ItemSKU = newjson.getString("ItemSKU");
                                    String ItemBarcode = newjson.getString("ItemBarcode");
                                    String StockUOM = newjson.getString("StockUOM");

                                    String IsNewListing = newjson.getString("IsNewListing");
                                    String RepeatOrder = newjson.getString("RepeatOrder");
                                    String ItemImage = newjson.getString("ItemImage");
                                    String GroupName = newjson.getString("GroupName");
                                    ItemMasterhelper v = new ItemMasterhelper();
                                    v.setItemID(ItemID);
                                    v.setItemName(ItemName);
                                    v.setIsDeal(IsDeal);
                                    v.setGroupID(GroupID);
                                    v.setMRP(MRP);
                                    v.setStockUOM(StockUOM);
                                    v.setFileName(FileName);
                                    v.setItemBarcode(ItemBarcode);
                                    v.setIsNewListing(IsNewListing);
                                    v.setItemSKU(ItemSKU);
                                    v.setSaleUOM(SaleUOM);
                                    v.setSaleRate(SaleRate);
                                    v.setItemImage(ItemImage);
                                    v.setFilepath(filepath);
                                    v.setRepeatOrder(RepeatOrder);
                                    v.setGroupName(GroupName);

                                    if (dbManager.exists(Integer.parseInt(ItemID), ItemSKU)) {
                                        CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(ItemID), ItemSKU);
                                        v.setQuantity(cartProduct.getQuantity() + "");
                                    }
                                    masterlist.add(v);


                                    //Log.d("masterlist", String.valueOf(masterlist.size()));


                                }
                                ProductsAdapterNew mAdapter = new ProductsAdapterNew(SearchActivityNew.this, masterlist, SearchActivityNew.this);
                                lvProducts.setAdapter(mAdapter);


                            }


                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
//                            masterlist = new ArrayList<>();
//                            ProductsAdapterNew mAdapter = new ProductsAdapterNew(SearchActivityNew.this, masterlist, SearchActivityNew.this);
//                            lvProducts.setAdapter(mAdapter);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        ProductsAdapterNew mAdapter = new ProductsAdapterNew(SearchActivityNew.this, masterlist, SearchActivityNew.this);
//                        lvProducts.setAdapter(mAdapter);
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

                    String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + prefs.getPrefsContactId() + "&Type=" + "28" + "&SupplierID=" + "66738" + "&filterItemName=" + search;
                    Log.e("BEFORE_PRODUCTS", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SearchActivityNew.this);
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

        Volley.newRequestQueue(SearchActivityNew.this).add(postRequest);
    }

}
