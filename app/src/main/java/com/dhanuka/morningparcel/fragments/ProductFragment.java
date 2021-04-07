package com.dhanuka.morningparcel.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.adapter.FilterAdapter;
import com.dhanuka.morningparcel.beans.ChildCatBean;
import com.dhanuka.morningparcel.events.OnItemFilterListener;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.NetworkMgr;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.adapter.ProductsAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.beans.Products;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.SpacesItemDecoration;
import com.dhanuka.morningparcel.utils.Utility;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ProductFragment extends Fragment implements onAddCartListener, OnItemFilterListener {
    private int sub_category_id, shop_id, cat_id;
    private ListView lvProducts;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue;
    private Context context;
    private ProductsAdapter adapter;
    private String subCategory;
    EditText etSearch;
    ArrayList<CartProduct> mcartlist=new ArrayList<>();
    TextView msgheader;
    SharedPreferences.Editor mEditorL;
    SharedPreferences prefsL;
    RecyclerView rvChildCat;
    Preferencehelper prefs1;

    List<Products> list = new ArrayList<>();
    com.dhanuka.morningparcel.database.DatabaseManager dbManager;
    CartCountView countView;
    @Nullable

    Preferencehelper preferencehelper;

    public static ProductFragment newInstance() {
        Bundle args = new Bundle();
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();

    ArrayList<ItemMasterhelper> newmasterlist = new ArrayList<>();
    ArrayList<ItemMasterhelper> mListFilteredItems = new ArrayList<>();
    ArrayList<ChildCatBean> mListSubCat = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    public void loadChilCat() {
        mListSubCat = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        if (prefs.getString("respSubCat", "").isEmpty()) {
            loadSubCategories("SafeOKart1");

        } else {
            try {

                JSONObject jsonObject = new JSONObject(prefs.getString("respSubCat", ""));

                for (int a = 0; a < jsonObject.getJSONArray("returnds").length(); a++) {
                    JSONObject mJSONObject = jsonObject.getJSONArray("returnds").getJSONObject(a);
                    if (mJSONObject.getString("GroupType").equalsIgnoreCase(subCategory)) {
                        ChildCatBean childCatBean = new ChildCatBean();
                        childCatBean.setDescription(mJSONObject.getString("Description"));
                        childCatBean.setGroupType(mJSONObject.getString("GroupType"));
                        childCatBean.setGroupId(mJSONObject.getString("GroupId"));
                        childCatBean.setImageName(mJSONObject.getString("ImageName"));
                        childCatBean.setWeight(mJSONObject.getString("Weight"));
                        childCatBean.setIsSubSubGroup(mJSONObject.getString("IsSubSubGroup"));
                        mListSubCat.add(childCatBean);
                    }
                }
                //  mListSubCat = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<ChildCatBean>>() {
                //  }.getType());
                if (mListSubCat.size() > 0) {
                    if (mListSubCat.size() > 1) {

                        ChildCatBean childCatBean = new ChildCatBean();
                        childCatBean.setDescription("All");
                        childCatBean.setGroupType("0000");
                        childCatBean.setGroupId("0000");
                        childCatBean.setIsSubSubGroup("1");

                        mListSubCat.add(0, childCatBean);
                    }
                    rvChildCat.setVisibility(View.VISIBLE);

                    rvChildCat.setAdapter(new FilterAdapter(getActivity(), mListSubCat, this, 0));
                } else {
                    rvChildCat.setVisibility(View.GONE);

                }


            } catch (Exception e) {
                e.printStackTrace();
                rvChildCat.setVisibility(View.GONE);
            }
        }

    }

    private void init(View view) {
        preferencehelper = new Preferencehelper(getActivity().getApplicationContext());
        subCategory = getArguments().getString("sub_category");
        sub_category_id = getArguments().getInt("sub_category_id");
        shop_id = getArguments().getInt("shop_id");
        cat_id = getArguments().getInt("cat_id");
        lvProducts = (ListView) view.findViewById(R.id.lvProducts);
        rvChildCat = (RecyclerView) view.findViewById(R.id.rvChildCat);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        msgheader = (TextView) view.findViewById(R.id.msgheader);
        context = lvProducts.getContext();
        rvChildCat.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin1dp);
        rvChildCat.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        rvChildCat.setHasFixedSize(true);


        SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);

        if (prefs.getString("discount", "0.0").isEmpty()) {
            dbDiscount = 0.0;

        } else {
            try {
                dbDiscount = Double.parseDouble(prefs.getString("discount", "0.0"));
            } catch (Exception e) {
                dbDiscount = 0.0;
            }
        }
        msgheader.setText(preferencehelper.getPrefsTag2());
        msgheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferencehelper.getPrefsTag2Desc().isEmpty()) {


                } else {
                    JKHelper.openCommentDialog(getActivity(), preferencehelper.getPrefsTag2Desc());

                }

            }
        });

        linearContinue = (LinearLayout) view.findViewById(R.id.linearContinue);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        countView = (CartCountView) LayoutInflater.from(context).inflate(R.layout.action_cart, null);
        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(context);



        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                    lvProducts.invalidate();
                }
            }
        });
/*
        List<Products> list = new ArrayList<>();
        list.add(new Products());
        list.add(new Products());
        list.add(new Products());
        list.add(new Products());
        list.add(new Products());
        list.add(new Products());
        list.add(new Products());
        list.add(new Products());
        list.add(new Products());*/

        // getAllProducts();
        prefsL = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);
        mEditorL = prefsL.edit();
        prefs1 = new Preferencehelper(getActivity());
        if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {
            msgheader.setVisibility(View.GONE);
            loadChilCat();
            if (prefs1.getPrefsContactId().isEmpty()) {
                getAllProducts(getActivity(), "66738", "67263");


            } else {
                getAllProducts(getActivity(), "66738", prefs1.getPrefsContactId());
            }


        } else {
            if (preferencehelper.getPrefsTag2Desc().isEmpty()) {
                msgheader.setVisibility(View.GONE);
            } else {
                msgheader.setVisibility(View.VISIBLE);
            }
            prefs1 = new Preferencehelper(getActivity());
            if (prefs.getString("shopId", "").isEmpty()) {
                getAllProducts(getActivity(), prefs.getString("shopId", "66738"), "67266");

            } else {
                prefs1 = new Preferencehelper(getActivity());
                getAllProducts(getActivity(), prefs.getString("shopId", ""), prefs1.getPrefsContactId());
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkMgr.isNetworkAvailable(context)) {
            SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                    context.MODE_PRIVATE);


            try {
                if (prefs.getString("resp1", "-1").equalsIgnoreCase("-1")) {
                    //getAllProducts();
                } else {
                    //loadLocal(prefs.getString("resp1", "-1"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                //  getAllProducts();
            }
            //    pbLoading.setVisibility(View.GONE);


        } else {
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            //    pbLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
/*

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchTextChanged(SearchTextChangedEvent event) {
       */
/* if (adapter != null) {
            adapter.filter(event.getText());
            lvProducts.invalidate();
        }*//*

    }
*/

    public void getAllProducts(Context ctx, String shopId, String contactid) {
        pbLoading.setVisibility(View.VISIBLE);
/*  final ProgressDialog mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
*/
        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getActivity().getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, getActivity());

                        String res = responses;
                        pbLoading.setVisibility(View.GONE);
                        try {

                        /*    mEditor.putString("resp1", response);
                            mEditor.commit();
*/
                            JSONObject jsonObject = new JSONObject(res);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {


                                loadLocal(responses);

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
                        pbLoading.setVisibility(View.GONE);

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(getActivity());
                SharedPreferences prefs1 = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                        getActivity().MODE_PRIVATE);

                Map<String, String> params = new HashMap<String, String>();
                //    params.put("contactid", prefs.getPrefsContactId());
              /*  params.put("contactid", "1");
                params.put("type", "28");
                params.put("SupplierID", prefs1.getString("shopId", ""));

*/

                params.put("supplierid", prefs1.getString("shopId", ""));
                String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + contactid + "&Type=" + "0" + "&supplierid=" + shopId + "&GroupId=" + subCategory;
                Log.e("BEFORE_PRODUCTS", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, getActivity());
                params.put("val", finalparam);
                Log.d("afterencrptionmaster", finalparam);


                return params;


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getActivity()).add(postRequest);
    }


    public void loadLocal(String resp) {
        masterlist = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(resp);
            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
            if (jsonObject.getInt("success") == 1) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newjson = jsonArray.getJSONObject(i);
                    Log.e("new_data", newjson.toString());
                    //  if (newjson.getString("GroupID").equalsIgnoreCase(subCategory)) {
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
                    String Discount = newjson.getString("Discount");
                    String ItemImage = newjson.getString("ItemImage");
                    String MRP = newjson.getString("MRP");

                    String Margin = newjson.getString("Margin");
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
                    v.setMargin(Margin);
                    v.setMRP(MRP);
                    v.setMOQ(MOQ);
                    v.setPurchaseUOM(PurchaseUOM);
                    v.setPurchaseUOMId(PurchaseUOMId);
                    v.setSaleUOM(SaleUOM);
                    v.setSaleUOMID(SaleUOMID);
                    v.setDiscount(Discount);
                    v.setPurchaseRate(PurchaseRate);
                    v.setSaleRate(SaleRate);
                    v.setItemSKU(ItemSKU);
                    v.setItemBarcode(ItemBarcode);
                    v.setStockUOM(StockUOM);
                    v.setItemImage(ItemImage);
                    v.setHSNCode(HSNCode);
                    newmasterlist.add(v);
                    if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {
                        v.setSubSGroup(newjson.getString("SubSGroup"));
                        v.setSubSID(newjson.getString("SubSID"));

                    }

                    if (dbManager.exists(Integer.parseInt(ItemID), ItemSKU)) {
                        CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(ItemID), ItemSKU);
                        v.setQuantity(cartProduct.getQuantity() + "");
                    }


                    masterlist.add(v);
                    //Log.d("masterlist", String.valueOf(masterlist.size()));
                    //}
                }
            }
            adapter = new ProductsAdapter(context, masterlist, this);
            lvProducts.setAdapter(adapter);
            lvProducts.setVisibility(View.VISIBLE);
//
//            try {
//                //creating a directory in SD card
//
//
//                File mydir = new File(Environment.getExternalStorageDirectory()
//                        + "/SAFEOFRESH/CATEGORY_PRODUCT/"); //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
//                if (!mydir.exists()) {
//                    mydir.mkdirs();
//                }
//
//                //getting the full path of the PDF report name
//                String mPath = Environment.getExternalStorageDirectory().toString()
//                        + "/SAFEOFRESH/CATEGORY_PRODUCT/" //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
//                        + subCategory+ ".pdf"; //reportName could be any name
//
//                createPdfbew(mPath,newmasterlist.size());
//
//            } catch (Exception e) {
//                e.printStackTrace();
//
//
//            }
            if (masterlist.size() < 1) {
                linearContinue.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    double dbDiscount = 0.0;

    @Override
    public void onAddCart(ItemMasterhelper product, int type) {
       /* if (dbManager.getRowCount() > 0 && !dbManager.isShopIdExists(product.getShopId())) {
            Toast.makeText(context, "You cannot add products from another shop", Toast.LENGTH_LONG).show();
        } else {*/
        if (!preferencehelper.getPrefsContactId().isEmpty()) {



            if (type == 3) {
                CartProduct cartProduct = dbManager.getProductFromItem(Integer.parseInt(product.getItemID()));

                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),"3");
                dbManager.delete(Integer.parseInt(product.getItemID()));
                dbManager.update(cartProduct);
               // Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),String.valueOf(type));

            } else if (dbManager.exists1(product.getItemID())) {
                CartProduct cartProduct = dbManager.getProductFromItem(Integer.parseInt(product.getItemID()));
                if (type == 1) {

                    cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                    dbManager.update(cartProduct);
                    Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),"2");


                } else if (type == 2)
                {

                    cartProduct.setQuantity(Integer.parseInt(product.getQuantity()));
                    dbManager.update(cartProduct);
                    Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),"2");

                }

                else  if (type==6 )
                {

//                    dbManager.update(cartProduct);
//                    Log.d("filterquantityinside", String.valueOf(cartProduct.getQuantity()));
//                    Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),"2");
//                    dbManager.update(cartProduct);


                    cartProduct.setQuantity(Integer.parseInt(product.getQuantity()));
                    dbManager.update(cartProduct);
                    Log.e("filterquantityinside",cartProduct.getQuantity()+"\n"+ Integer.parseInt(product.getQuantity()));
                    Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),"2");
                    // Utility.checkoutOrder(cartList.get(position), preferencehelper.getPrefsContactId(), getApplicationContext(), "2");
                    adapter.notifyDataSetChanged();

                    dbManager.update(cartProduct);
                }
                else {
                    if (cartProduct.getQuantity() > 0) {
                        cartProduct.setQuantity(cartProduct.getQuantity() - 1);
                        dbManager.delete(Integer.parseInt(product.getItemID()));

                    } else {

                        Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),"3");
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
                mcartlist.add(cartProduct);
                Utility.checkoutOrder(cartProduct,preferencehelper.getPrefsContactId(),getActivity(),String.valueOf("1"));
            }
            setCartCount();
            FancyToast.makeText(getContext(), "Item added to cart", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            // setCartCount();
        } else {
            FancyToast.makeText(getContext(), "Your are not logged in , please login first", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

        }
    }

    @Override
    public void onFilterClick(ChildCatBean mChildCatBean) {
        mListFilteredItems = new ArrayList<>();
        if (mChildCatBean.getGroupId().equalsIgnoreCase("0000")) {

            adapter = new ProductsAdapter(context, masterlist, this);
            lvProducts.setAdapter(adapter);
            lvProducts.setVisibility(View.VISIBLE);
            if (masterlist.size() < 1) {
                linearContinue.setVisibility(View.VISIBLE);
            } else {
                linearContinue.setVisibility(View.GONE);

            }

        } else {

            for (int a = 0; a < masterlist.size(); a++) {
                if (masterlist.get(a).getSubSID().equalsIgnoreCase(mChildCatBean.getGroupId())) {
                    mListFilteredItems.add(masterlist.get(a));
                }
            }


            adapter = new ProductsAdapter(context, mListFilteredItems, this);
            lvProducts.setAdapter(adapter);
            lvProducts.setVisibility(View.VISIBLE);
            if (mListFilteredItems.size() < 1) {
                linearContinue.setVisibility(View.VISIBLE);
            } else {
                linearContinue.setVisibility(View.GONE);

            }
        }
    }

    public interface onSomeEventListener {
        public void someEvent(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    onSomeEventListener someEventListener;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItem item1 = menu.findItem(R.id.shop);
        item.setActionView(countView);
        Preferencehelper prefs4=new Preferencehelper(getActivity());
        if (prefs4.getPREFS_trialuser().equalsIgnoreCase("0")) {
            item.setVisible(false);

        }
        else
        {
            item.setVisible(true);

        }
        item1.setVisible(false);
    }

    private void setCartCount() {
        countView.setCount(dbManager.getTotalQty());
        someEventListener.someEvent(dbManager.getTotalQty() + "");
    }

    public void loadSubCategories(final String donutid) {

        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        final ProgressDialog mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        // StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail",
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail_Store",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mEditor.putString("respSubCat", response);
                                mEditor.commit();

                                loadChilCat();
                            } else {
                                rvChildCat.setVisibility(View.GONE);
                                com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            rvChildCat.setVisibility(View.GONE);
                        }
                        mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                        rvChildCat.setVisibility(View.GONE);


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

        Volley.newRequestQueue(getActivity()).add(postRequest);


    }




    public void createPdfbew(String dest,int listsize) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(4);// Set a row and the three column of
        // A4 paper
        table.setWidthPercentage(100);
        Image imageFromWeb = null;
        Phrase ph = null;

        try {


        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("sizepdflist", String.valueOf(newmasterlist.size()));

        PdfPCell cell, cell1;
//        for (int r = 0; r <= masterlist.size(); r++) {// Set display two lines
            for (int c = 0; c < newmasterlist.size(); c++) {// Set to display a row of three  columns
                cell = new PdfPCell();
//                cell1 = new PdfPCell(imageFromWeb , true);
                cell.setPaddingLeft(20);
                cell.setPaddingTop(70);

                cell.setPaddingRight(10);
//                cell.addElement(new Paragraph(" Test "));
//                cell.addElement(new Paragraph(" Rs.15 "));
//                cell.addElement(imageFromWeb);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String imageUrl;

             //   String imageUrl = "https://s.yimg.com/os/mit/ape/w/d8f6e02/dark/partly_cloudy_day.png";


                if (newmasterlist.get(c).getFilepath().equalsIgnoreCase(""))
                {
                     imageUrl= "https://s.yimg.com/os/mit/ape/w/d8f6e02/dark/partly_cloudy_day.png";

                }
                else
                {
                     imageUrl= "http://mmthinkbiz.com/ImageHandler.ashx?image=" + masterlist.get(c).getFileName() + "&filePath=" + masterlist.get(c).getFilepath();


                }
              //  String imageUrl = "https://s.yimg.com/os/mit/ape/w/d8f6e02/dark/partly_cloudy_day.png";
                imageFromWeb = Image.getInstance(new URL(imageUrl));

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(150);// Control the fixed height of each cell
                ph = new Phrase(new Chunk(imageFromWeb, 0, 20));
                Log.d("itempdfname", String.valueOf(newmasterlist.get(c).getItemName()));
                ph.add(new Chunk("\n"+newmasterlist.get(c).getItemName()+"\n"+"Rs. "+newmasterlist.get(c).getSaleRate()));

                cell.addElement(ph);
                table.addCell(cell);
//                table.addCell(cell1);


            }
//        }
        document.add(table);
        document.close();
    }


}
