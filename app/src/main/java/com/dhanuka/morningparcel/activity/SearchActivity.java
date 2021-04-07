package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.SqlDatabase.All_Item_Master;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.ProductsAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.onAddCartListener;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, onAddCartListener {

    @BindView(R.id.rvProducts)
    ListView lvProducts;
    @BindView(R.id.txtProducts)
    AppCompatTextView txtProducts;
    @BindView(R.id.imgBack)
    AppCompatImageView imgBack;
    @BindView(R.id.txtSrch)
    TextView txtSrch;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.mSearchView)
    SearchView mSearchView;
    @BindView(R.id.layoutTwo)
    LinearLayout layoutTwo;
    @BindView(R.id.linearContinue)
    LinearLayout linearContinue;
    DatabaseManager dbManager;

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
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        ButterKnife.bind(this);
        preferencehelper = new Preferencehelper(SearchActivity.this);
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


        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
        dbManager = DatabaseManager.getInstance(SearchActivity.this);

        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            mSearchView.setIconified(false);
            mSearchView.setOnQueryTextListener(this);
        }
        getDta();
        setCartCount();
    }

    public void getDta() {
        try {
            com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {

                    All_Item_Master masterdao = new All_Item_Master(database, SearchActivity.this);
                    if (masterdao.getRowCount() > 0) {
                        ArrayList<ItemMasterhelper> list = masterlist;
                        newdblist = masterdao.selectAll();
                        Toast.makeText(SearchActivity.this, "Data saved", Toast.LENGTH_LONG).show();
                    } else {
                        getAllProducts();

                    }
                }
            });
            if (newdblist.size() > 0) {
                Toast.makeText(SearchActivity.this, "Data exist", Toast.LENGTH_LONG).show();
                Log.d("Newdblistsize", String.valueOf(newdblist.size()));

            }
            if (prefs.getString("resp1", "-1").equalsIgnoreCase("-1")) {
                // getAllProducts();
            } else {
                // loadLocal(prefs.getString("resp1", "-1"));
                masterlist = newdblist;
            }
        } catch (Exception e) {
            e.printStackTrace();
            getAllProducts();
        }
    }

    public void makeCartClick(View view) {
        startActivity(new Intent(SearchActivity.this, CartActivity.class));
    }

    public void getAllProducts() {
        final ProgressDialog mProgressBar = new ProgressDialog(SearchActivity.this);
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
                        Log.e("Response393", response);


                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, SearchActivity.this);

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

                                    ItemMasterhelper v = new ItemMasterhelper();
                                    v.setItemID(ItemID);
                                    v.setItemName(ItemName);
                                    v.setCompanyid(companyid);
                                    v.setGroupID(GroupID);
                                    v.setOpeningStock(OpeningStock);
                                    v.setROQ(ROQ);
                                    v.setMOQ(MOQ);
                                    v.setMRP(MRP);
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
                                    //Log.d("masterlist", String.valueOf(masterlist.size()));

                                }
                                com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                    @Override
                                    public void run(SQLiteDatabase database) {

                                        All_Item_Master masterdao = new All_Item_Master(database, SearchActivity.this);
                                        ArrayList<ItemMasterhelper> list = masterlist;
                                        masterdao.deleteAll();
                                        masterdao.insert(list);
                                        Log.d("Savedataindb", String.valueOf(list.size()));
                                        //Toast.makeText(SearchActivity.this, "Data saved", Toast.LENGTH_LONG).show();

                                    }
                                });
                                getDta();
                                // loadLocal(response);


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

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(SearchActivity.this);

                Map<String, String> params = new HashMap<String, String>();


                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = AppUrls.GET_ITEM_MASTER_URL + "&contactid=" + prefs.getPrefsContactId() + "&type=" + "28";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SearchActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrption", finalparam);
                    return params;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }


            }
        };


        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(SearchActivity.this);
            Log.d("SettingRequestqueuegl", "Setting a new request queue");
        }
        requestQueue.add(postRequest);
    }

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

        } else if (newText.length() > 2) {
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
            getAllProducts();
        }
    }

    ProductsAdapter adapter;

    public void setSearchData(ArrayList<ItemMasterhelper> mList) {
        adapter = new ProductsAdapter(SearchActivity.this, mList, this);
        lvProducts.setAdapter(adapter);
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
        if (!preferencehelper.getPrefsContactId().isEmpty()) {

            if (dbManager.exists(Integer.parseInt(product.getItemID()), product.getItemSKU())) {
                CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(product.getItemID()), product.getItemSKU());
                if (type == 1) {
                    cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                } else {
                    if (cartProduct.getQuantity() > 0) {
                        cartProduct.setQuantity(cartProduct.getQuantity() - 1);
                    } else {
                        dbManager.delete(Integer.parseInt(product.getItemID()));
                    }
                }
                dbManager.update(cartProduct);
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


                dbManager.insert(cartProduct);
            }
            setCartCount();
            com.shashank.sony.fancytoastlib.FancyToast.makeText(SearchActivity.this, "Item added to cart", com.shashank.sony.fancytoastlib.FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            // setCartCount();
            // }
            // setCartCount();
        } else {
            FancyToast.makeText(SearchActivity.this, "Your are not logged in , please login first", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

        }
    }


    private void setCartCount() {
        tvCount.setText(dbManager.getTotalQty() + "");
    }
}
