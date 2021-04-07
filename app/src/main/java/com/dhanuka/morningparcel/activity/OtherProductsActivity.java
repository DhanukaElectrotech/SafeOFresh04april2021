package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.SqlDatabase.All_Item_Master;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.ProductsAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.Utility;

public class OtherProductsActivity extends BaseActivity implements onAddCartListener {
    TextView msgheader;
    ListView lvProducts;
    Context context;
    CartCountView countView;
    DatabaseManager dbManager;
    Preferencehelper preferencehelper;
    ProductsAdapter adapter;

    String type = "1";
    String subCategory = "";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_other_products;
    }

    ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_other_products);
        View view = getLayoutInflater().inflate(R.layout.activity_other_products, container_body);
        ButterKnife.bind(this);
        context = OtherProductsActivity.this;
        dbManager = DatabaseManager.getInstance(context);
        countView = (CartCountView) LayoutInflater.from(context).inflate(R.layout.action_cart, null);
        txt_set.setText("Safe'O'Fresh");
        backbtn_layout.setVisibility(View.VISIBLE);
        preferencehelper = new Preferencehelper(getApplicationContext());
        msgheader = (TextView) view.findViewById(R.id.msgheader);
        lvProducts = (ListView) view.findViewById(R.id.lvProducts);
        type = getIntent().getStringExtra("type");

        if (type.equalsIgnoreCase("1")) {
            msgheader.setText("Deals Of The Day");
            subCategory="1111111";
        } else if (type.equalsIgnoreCase("2")) {
            msgheader.setText("New Arrivals");
            subCategory="1111112";

        } else if (type.equalsIgnoreCase("3")) {
            msgheader.setText("Previously Ordered");
            subCategory="1111111";

        }

  /*   msgheader = (TextView) findViewById(R.id.msgheader);
        if (preferencehelper.getPrefsTag2Desc().isEmpty()) {

            msgheader.setVisibility(View.GONE);
        } else {
            msgheader.setVisibility(View.GONE);

        }
        msgheader.setText(preferencehelper.getPrefsTag2());*/
        SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);


        try {
          //  if (prefs.getString("resp111", "-1").equalsIgnoreCase("-1")) {
             //   getAllProducts();
            getAllProducts(OtherProductsActivity.this, "", preferencehelper.getPrefsContactId());
 /* } else {
                loadLocal(prefs.getString("resp111", "-1"));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            getAllProducts(OtherProductsActivity.this, "", preferencehelper.getPrefsContactId());
        }
    }

    private void setCartCount() {
        countView.setCount(dbManager.getTotalQty());
        Preferencehelper prefs4=new Preferencehelper(getApplicationContext());
        if (prefs4.getPREFS_trialuser().equalsIgnoreCase("0")) {
            countView.setVisibility(View.GONE);

        }
        else
        {
            countView.setVisibility(View.VISIBLE);

        }

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
                    String MRP = newjson.getString("MRP");
                    String HSNCode = newjson.getString("HSNCode");
                    String FileName = newjson.getString("FileName");
                    String FilePath = newjson.getString("filepath");

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
                    v.setMRP(MRP);
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

                   /* if (dbManager.exists(Integer.parseInt(ItemID), ItemSKU)) {
                        CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(ItemID), ItemSKU);
                        v.setQuantity(cartProduct.getQuantity() + "");
                    }*/

                    if (type.equalsIgnoreCase("1") && newjson.getString("IsDeal").equalsIgnoreCase("1")) {

                        masterlist.add(v);

                    } else if (type.equalsIgnoreCase("2") && newjson.getString("IsNewListing").equalsIgnoreCase("1")) {

                        masterlist.add(v);
                    } else if (type.equalsIgnoreCase("3") && newjson.getString("RepeatOrder").equalsIgnoreCase("1")) {

                        masterlist.add(v);
                    }

                    //Log.d("masterlist", String.valueOf(masterlist.size()));

                }
            }
            adapter = new ProductsAdapter(context, masterlist, this);
            lvProducts.setAdapter(adapter);
            lvProducts.setVisibility(View.VISIBLE);


        } catch (Exception e) {
            e.printStackTrace();
            getAllProducts(OtherProductsActivity.this, "", preferencehelper.getPrefsContactId());

        }
    }

    @Override
    protected void onSideSliderClick() {

    }

    double dbDiscount = 0.0;

    @Override
    public void onAddCart(ItemMasterhelper product, int type) {
       /* if (dbManager.getRowCount() > 0 && !dbManager.isShopIdExists(product.getShopId())) {
            Toast.makeText(context, "You cannot add products from another shop", Toast.LENGTH_LONG).show();
        } else {*/
        if (!preferencehelper.getPrefsContactId().isEmpty()) {
            if (type == 3) {
                CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(product.getItemID()), product.getItemSKU());
                dbManager.delete(Integer.parseInt(product.getItemID()));
                dbManager.update(cartProduct);
                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"3");



            } else if (dbManager.exists(Integer.parseInt(product.getItemID()), product.getItemSKU())) {
                CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(product.getItemID()), product.getItemSKU());
                if (type == 1) {
                    cartProduct.setQuantity(cartProduct.getQuantity() + 1);


                } else {
                    if (cartProduct.getQuantity() > 0) {
                        cartProduct.setQuantity(cartProduct.getQuantity() - 1);

                    } else {
                        dbManager.delete(Integer.parseInt(product.getItemID()));
                        Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"3");


                    }
                }
                dbManager.update(cartProduct);
                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),"2");

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
                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getApplicationContext(),String.valueOf("1"));

            }
            countView.setCount(Integer.parseInt(dbManager.getTotalQty() + ""));
            FancyToast.makeText(OtherProductsActivity.this, "Item added to cart", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            // setCartCount();
        } else {
            FancyToast.makeText(OtherProductsActivity.this, "Your are not logged in , please login first", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

        }
    }

    public void getAllProducts() {
        final ProgressDialog mProgressBar = new ProgressDialog(OtherProductsActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.GET_ITEM_MASTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);

                        String res = response;
                        mProgressBar.dismiss();
                        try {

                            mEditor.putString("resp111", response);
                            mEditor.commit();

                            JSONObject jsonObject = new JSONObject(res);
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
                                    String MRP = newjson.getString("MRP");
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
                                    v.setPurchaseUOM(PurchaseUOM);
                                    v.setMRP(MRP);
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

                                        All_Item_Master masterdao = new All_Item_Master(database, OtherProductsActivity.this);
                                        ArrayList<ItemMasterhelper> list = masterlist;
                                        masterdao.deleteAll();
                                        masterdao.insert(list);
                                        Toast.makeText(OtherProductsActivity.this, "Data saved", Toast.LENGTH_LONG).show();

                                    }
                                });

                                loadLocal(response);


                                ;
                               
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
                prefs = new Preferencehelper(OtherProductsActivity.this);
                SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);

                Map<String, String> params = new HashMap<String, String>();
                //    params.put("contactid", prefs.getPrefsContactId());
                params.put("contactid", "1");
                params.put("type", "28");
                params.put("SupplierID", prefs1.getString("shopId", ""));


                return params;


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(OtherProductsActivity.this).add(postRequest);
    }

    public void getAllProducts(Context ctx, String shopId, String contactid) {
      //  pbLoading.setVisibility(View.VISIBLE);
  final ProgressDialog mProgressBar = new ProgressDialog(OtherProductsActivity.this);
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
                        mProgressBar.dismiss();
                        Log.e("Response393", response);
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, OtherProductsActivity.this);

                        String res = responses;
                      //  pbLoading.setVisibility(View.GONE);
                        try {

                        /*    mEditor.putString("resp1", response);
                            mEditor.commit();
*/
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {


                                loadLocal(responses);

                            } else {
                                getAllProducts(OtherProductsActivity.this, "", preferencehelper.getPrefsContactId());

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
                      // pbLoading.setVisibility(View.GONE);
                        mProgressBar.dismiss();

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(OtherProductsActivity.this);
                SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);

                Map<String, String> params = new HashMap<String, String>();
                //    params.put("contactid", prefs.getPrefsContactId());
              /*  params.put("contactid", "1");
                params.put("type", "28");
                params.put("SupplierID", prefs1.getString("shopId", ""));

*/

                params.put("supplierid", prefs1.getString("shopId", ""));
                String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + contactid + "&Type=" + "0" + "&supplierid=" + prefs1.getString("shopId", "66738") + "&GroupId=" + subCategory;
                Log.e("BEFORE_PRODUCTS", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, OtherProductsActivity.this);
                params.put("val", finalparam);
                Log.d("afterencrptionmaster", finalparam);


                return params;


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(OtherProductsActivity.this).add(postRequest);
    }



    @Override
    public void onResume() {
        super.onResume();

        try {
            setCartCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}