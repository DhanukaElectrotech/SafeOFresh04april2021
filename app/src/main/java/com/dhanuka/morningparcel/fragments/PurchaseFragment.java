package com.dhanuka.morningparcel.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.PurchaseBean;
import com.dhanuka.morningparcel.activity.Message;
import com.dhanuka.morningparcel.adapter.PurchaseOrderAdapter;
import com.dhanuka.morningparcel.adapter.SaleproductsAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.OnAddToSToreListener;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.NetworkMgr;
import com.dhanuka.morningparcel.utils.log;

/**
 *
 */
public class PurchaseFragment extends Fragment implements onAddCartListener, OnAddToSToreListener {
    private RecyclerView lvProducts;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue;
    private Context context;
    private SaleproductsAdapter adapter;
    String supplier_id;
    String supplier_name;

    DatabaseManager dbManager;
    CartCountView countView;
    PurchaseOrderAdapter mAdapter;
    EditText etSearch;
    ArrayList<PurchaseBean> masterlist = new ArrayList<>();
    List<PurchaseBean> list;
    HashSet<PurchaseBean> hashSet = new HashSet<PurchaseBean>();

    SharedPreferences.Editor mEditorL;
    SharedPreferences prefsL;

    Preferencehelper prefs1;
    @Nullable
    @BindView(R.id.cbAll)
    CheckBox ostockclk;
    @Nullable
    @BindView(R.id.btnsumbit)
    Button btnsumbit;
    ArrayList<PurchaseBean> mListSelected = new ArrayList<>();

    public static PurchaseFragment newInstance() {
        Bundle args = new Bundle();
        PurchaseFragment fragment = new PurchaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    String type = "Pending";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_order, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    int clickcount = 0;

    private void init(View view) {
        ButterKnife.bind(this, view);
        supplier_name = getArguments().getString("suppliername");
        supplier_id = getArguments().getString("supplier_id");
        Log.e("last_sdata", supplier_name + " - ");
        lvProducts = (RecyclerView) view.findViewById(R.id.lvProducts);
        linearContinue = (LinearLayout) view.findViewById(R.id.linearContinue);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        context = lvProducts.getContext();
        countView = (CartCountView) LayoutInflater.from(context).inflate(R.layout.action_cart, null);
        lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        dbManager = DatabaseManager.getInstance(context);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
      /*  ostockclk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mAdapter
                }else{

                }
            }
        });*/
        ostockclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (mAdapter != null) {
                        if (ostockclk.isChecked()) {
                            mAdapter.selectAll();

                        } else {
                            mAdapter.unselectall();

                        }

                        //first time clicked to do this

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        btnsumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mAdapter != null) {
                        mListSelected = new ArrayList<>();
                        mListSelected = mAdapter.getSelected();
                        if (mListSelected.size() > 0) {
                            uploadFieldwork();
                        } else {
                            FancyToast.makeText(getContext(), "Please select atleast one Item", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAdapter != null) {
                    mAdapter.filter(s.toString());
                    /*
                    adapter.filter(s.toString());
                    lvProducts.invalidate();
*/
                }
            }
        });

        if (NetworkMgr.isNetworkAvailable(context)) {
            SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                    context.MODE_PRIVATE);
            try {
                if (!prefs.getString("resppurchase", "-1").equalsIgnoreCase("-1")) {
                    loadLocal(prefs.getString("resppurchase", "-1"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                //    getAllProducts();
            }
            pbLoading.setVisibility(View.GONE);


        } else {
            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            pbLoading.setVisibility(View.GONE);
        }
        //    getAllProducts();


    /*    prefsL = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);
        mEditorL = prefsL.edit();
        prefs1 = new Preferencehelper(getActivity());
        SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {

            if (prefs1.getPrefsContactId().isEmpty()) {
                getAllProducts(getActivity(), "67263", "67263");


            } else {
                getAllProducts(getActivity(), prefs1.getPrefsContactId(), prefs1.getPrefsContactId());
            }


        } else {
            prefs1 = new Preferencehelper(getActivity());
            if (prefs.getString("shopId", "").isEmpty()) {
                getAllProducts(getActivity(), prefs1.getPrefsContactId(), "67266");

            } else {
                prefs1 = new Preferencehelper(getActivity());
                getAllProducts(getActivity(),prefs1.getPrefsContactId(), prefs1.getPrefsContactId());
            }

        }
*/

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (NetworkMgr.isNetworkAvailable(context)) {
//            SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
//                    context.MODE_PRIVATE);
//            try {
//                if (!prefs.getString("resppurchase", "-1").equalsIgnoreCase("-1")) {
//                    loadLocal(prefs.getString("resppurchase", "-1"));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                //    getAllProducts();
//            }
//            pbLoading.setVisibility(View.GONE);
//
//
//        } else {
//            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
//            pbLoading.setVisibility(View.GONE);
//        }

    }

    //    public void getAllProducts(Context ctx, String shopId, String contactid) {
//        pbLoading.setVisibility(View.VISIBLE);
///*  final ProgressDialog mProgressBar = new ProgressDialog(getActivity());
//        mProgressBar.setTitle("Safe'O'Fresh");
//        mProgressBar.setMessage("Loading...");
//        mProgressBar.setCancelable(false);
//        mProgressBar.show();
//*/        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
//                getActivity().MODE_PRIVATE);
//        SharedPreferences.Editor mEditor = prefs.edit();
//
//        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
//        StringRequest postRequest = new StringRequest(Request.Method.POST, getActivity().getString(R.string.URL_BASE_URL),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.e("Response393", response);
//                        JKHelper jkHelper = new JKHelper();
//                        String responses = jkHelper.Decryptapi(response, getActivity());
//
//                        String res = responses;
//                        pbLoading.setVisibility(View.GONE);
//                        try {
//
//                        /*    mEditor.putString("resp1", response);
//                            mEditor.commit();
//*/
//                            JSONObject jsonObject = new JSONObject(res);
//                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
//                            if (jsonObject.getInt("success") == 1) {
//
//
//                                loadLocal(responses);
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
//                        pbLoading.setVisibility(View.GONE);
//
//                        // error
//                        //   Log.e("Error.Response", error.getMessage());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Preferencehelper prefs;
//                prefs = new Preferencehelper(getActivity());
//                SharedPreferences prefs1 = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
//                        getActivity().MODE_PRIVATE);
//
//                Map<String, String> params = new HashMap<String, String>();
//                //    params.put("contactid", prefs.getPrefsContactId());
//              /*  params.put("contactid", "1");
//                params.put("type", "28");
//                params.put("SupplierID", prefs1.getString("shopId", ""));
//
//*/
//
//                params.put("supplierid", prefs1.getString("shopId", ""));
//                String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + contactid + "&Type=" + "0" + "&supplierid=" + shopId+ "&GroupId=" + subCategory;
//                Log.e("BEFORE_PRODUCTS", param);
//                JKHelper jkHelper = new JKHelper();
//                String finalparam = jkHelper.Encryptapi(param, getActivity());
//                params.put("val", finalparam);
//                Log.d("afterencrptionmaster", finalparam);
//
//
//                return params;
//
//
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        Volley.newRequestQueue(getActivity()).add(postRequest);
//    }

    @Override
    public void onPause() {

        super.onPause();
//        if (NetworkMgr.isNetworkAvailable(context)) {
//            SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
//                    context.MODE_PRIVATE);
//            try {
//                if (!prefs.getString("resppurchase", "-1").equalsIgnoreCase("-1")) {
//                    loadLocal(prefs.getString("resppurchase", "-1"));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                //    getAllProducts();
//            }
//            pbLoading.setVisibility(View.GONE);
//
//
//        } else {
//            Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
//            pbLoading.setVisibility(View.GONE);
//        }
    }

  /*  @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchTextChanged(SearchTextChangedEvent event) {
        if (adapter != null) {
            filter(event.getText());
            //  adapter.filter(event.getText());
            lvProducts.invalidate();
        }
    }*/

//    private void filter(String text) {
//        ArrayList<ItemMasterhelper> filteredList = new ArrayList<>();
//
//        for (ItemMasterhelper product : MainList) {
//            if (product.getItemBarcode().toLowerCase().contains(text) || product.getItemName().toLowerCase().contains(text) || product.getHSNCode().toLowerCase().contains(text) || product.getItemSKU().toLowerCase().contains(text)) {
//                filteredList.add(product);
//            }
//        }
//
//        adapter.filterList(filteredList);
//    }

    public void loadLocal(String resp) {

        masterlist = new ArrayList<>();


        try {


            Log.e("responsesasdf", resp);
            JSONObject jsonObject = new JSONObject(resp);
            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
            if (jsonObject.getInt("success") == 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newjson = jsonArray.getJSONObject(i);
                    String itembarcode = newjson.getString("itembarcode");
                    String companycosting = newjson.getString("companycosting");
                    String ItemName = newjson.getString("ItemName");
                    String mrp = newjson.getString("mrp");
                    String FileName = newjson.getString("FileName");
                    String filepath = newjson.getString("filepath");
                    String taxrate = newjson.getString("taxrate");
                    String SupplierName = newjson.getString("SupplierName");

                    String SupplierID = newjson.getString("SupplierID");
                    String MarginP = newjson.getString("MarginP");
                    String Saleinlastoneweek = newjson.getString("Saleinlastoneweek");

                    String groupname = newjson.getString("groupname");

                    String TotalSale = newjson.getString("TotalSale");
                    String TotalPurchase = newjson.getString("TotalPurchase");

                    String TotalSale1 = newjson.getString("TotalSale1");
                    String TotalPurchase1 = newjson.getString("TotalPurchase1");
                   // String Currentstock = newjson.getString("Currentstock");
                    String Currentstock = newjson.getString("Balance");
                    String Balance = newjson.getString("Balance");
                    String itemid = newjson.getString("itemid");
                   String InQty = newjson.getString("InQty");
                    String OutQty = newjson.getString("OutQty");
                    String BranchName = newjson.getString("BranchName");
                    String arr[] = Currentstock.split(".");
                    PurchaseBean v = new PurchaseBean();
                    if (SupplierName.equalsIgnoreCase(supplier_name)) {
                        Log.e("SPNNNMMM", SupplierName + "  ==  " + Currentstock);
                        v.setItembarcode(itembarcode);
                        v.setOutQty(OutQty);
                        v.setInQty(InQty);
                        v.setBalance(Balance);
                        v.setItemName(ItemName);
                        v.setItemid(itemid);
                        v.setMarginP(MarginP);
                        v.setSaleinlastoneweek(Saleinlastoneweek);
                        v.setGroupname(groupname);
                        v.setCompanycosting(companycosting);
                        v.setMrp(mrp);
                        v.setTotalPurchase(TotalPurchase);
                        v.setFileName(FileName);
                        v.setFilepath(filepath);
                        v.setSuppliername(SupplierName);
                        v.setSupplierid(SupplierID);
                        v.setTaxrate(taxrate);
                        v.setTotalSale(TotalSale);
                        v.setTotalPurchase(TotalPurchase);
                        v.setTotalSale1(TotalSale1);
                        v.setTotalPurchase1(TotalPurchase1);
                        v.setBranchName(BranchName);
                        try {
                            v.setCurrentstock((int) (Double.parseDouble(Currentstock)) + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                            v.setCurrentstock(Currentstock);
                        }

                        masterlist.add(v);

                    }


                    //Log.d("masterlist", String.valueOf(masterlist.size()));


                }


                mAdapter = new PurchaseOrderAdapter(getActivity(), masterlist, (onAddCartListener) getActivity(), false);
                lvProducts.setAdapter(mAdapter);


            }


        } catch (Exception e) {
            // mProgressBar.dismiss();

            e.printStackTrace();
            masterlist = new ArrayList<>();
//            ostockclk.setChecked(false);
//            mAdapter = new PurchaseOrderAdapter(getActivity(), masterlist, (onAddCartListener) getActivity(), false);
//            lvProducts.setAdapter(mAdapter);
        }
//        try {
//            JSONObject jsonObject = new JSONObject(resp);
//            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
//            if (jsonObject.getInt("success") == 1) {
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject newjson = jsonArray.getJSONObject(i);
//                    Log.e("subCategory",subCategory+" ===  "+newjson.getString("GroupID"));
//                    if (newjson.getString("GroupID").equalsIgnoreCase(subCategory)) {
//
//                        String ItemID = newjson.getString("ItemID");
//                        String ItemName = newjson.getString("ItemName");
//                        String companyid = newjson.getString("companyid");
//                        String GroupID = newjson.getString("GroupID");
//                        String OpeningStock = newjson.getString("OpeningStock");
//                        String ROQ = newjson.getString("ROQ");
//                        String MOQ = newjson.getString("MOQ");
//                        String PurchaseUOM = newjson.getString("PurchaseUOM");
//                        String PurchaseUOMId = newjson.getString("PurchaseUOMId");
//                        String SaleUOM = newjson.getString("SaleUOM");
//                        String SaleUOMID = newjson.getString("SaleUOMID");
//                        String PurchaseRate = newjson.getString("PurchaseRate");
//                        String SaleRate = newjson.getString("SaleRate");
//                        String ItemSKU = newjson.getString("ItemSKU");
//                        String ItemBarcode = newjson.getString("ItemBarcode");
//                        String StockUOM = newjson.getString("StockUOM");
//                        String ItemImage = newjson.getString("ItemImage");
//                        String HSNCode = newjson.getString("HSNCode");
//                        String FileName = newjson.getString("FileName");
//                        String FilePath = newjson.getString("filepath");
//                        String VendorID = newjson.getString("VendorID");
//                        String ToShow = newjson.getString("ToShow");
//                        String AvailableQty = newjson.getString("AvailableQty");
//                        String StoreSTatus = newjson.getString("StoreSTatus");
//                        String MRP = newjson.getString("MRP");
//                        String IsDeal = newjson.getString("IsDeal");
//                        String IsNewListing = newjson.getString("IsNewListing");
//
//                        if (ItemID.equalsIgnoreCase("3221")) {
//                            String ss = "";
//                            Log.e("ss44444", StoreSTatus.toString());
//                        }
//                        Log.e("StoreSTatusStoreSTatus", StoreSTatus.toString());
//
//                        PurchaseBean v = new PurchaseBean();
//                        v.setVendorID(VendorID);
//                        v.setToShow(ToShow);
//                        v.setStoreSTatus(StoreSTatus);
//                        v.setAvailableQty(AvailableQty);
//                        v.setIsNewListing(IsNewListing);
//                        v.setIsDeal(IsDeal);
//                        v.setFileName(FileName);
//                        v.setFilepath(FilePath);
//                        v.setItemID(ItemID);
//                        v.setItemName(ItemName);
//                        v.setCompanyid(companyid);
//                        v.setGroupID(GroupID);
//                        v.setOpeningStock(OpeningStock);
//                        v.setROQ(ROQ);
//                        v.setMOQ(MOQ);
//                        v.setPurchaseUOM(PurchaseUOM);
//                        v.setPurchaseUOMId(PurchaseUOMId);
//                        v.setSaleUOM(SaleUOM);
//                        v.setSaleUOMID(SaleUOMID);
//                        v.setPurchaseRate(PurchaseRate);
//                        v.setSaleRate(SaleRate);
//                        v.setItemSKU(ItemSKU);
//                        v.setItemBarcode(ItemBarcode);
//                        v.setStockUOM(StockUOM);
//                        v.setItemImage(ItemImage);
//                        v.setHSNCode(HSNCode);
//                        v.setMRP(MRP);
//                        masterlist.add(v);
//
//
//
//                        //     masterlist.add(v);
//                    }
//                }
//            }
//
//
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            // getAllProducts();
//        }
    }

    @Override
    public void onAddCart(ItemMasterhelper product, int type) {
       /* if (dbManager.getRowCount() > 0 && !dbManager.isShopIdExists(product.getShopId())) {
            Toast.makeText(context, "You cannot add products from another shop", Toast.LENGTH_LONG).show();
        } else {*/
        if (dbManager.exists(Integer.parseInt(product.getItemID()), product.getItemSKU())) {
            CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(product.getItemID()), product.getItemSKU());
            if (type == 1) {
                cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                Log.e("Iammrp", cartProduct.getMRP());
            } else {
                if (cartProduct.getQuantity() > 0) {
                    cartProduct.setQuantity(cartProduct.getQuantity() - 1);
                    Log.e("Iammrp", cartProduct.getMRP());
                } else {
                    dbManager.delete(Integer.parseInt(product.getItemID()));
                }
            }
            dbManager.update(cartProduct);
        } else {
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
            cartProduct.setSaleRate(product.getSaleRate());
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
        FancyToast.makeText(getContext(), "Item added to cart", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        // setCartCount();
        // }
    }

    @Override
    public void onAddToSTore(String strItemId) {
     /*   SharedPreferences prefss = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefss.edit();
        mEditor.putString("resp1", "-1");
        mEditor.commit();*/
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

    String serverid = "";

    private void setCartCount() {
        countView.setCount(dbManager.getTotalQty());
        someEventListener.someEvent(dbManager.getTotalQty() + "");
    }

    private void filter(String text) {
        ArrayList<PurchaseBean> filteredList = new ArrayList<>();

        for (PurchaseBean product : masterlist) {
            if (product.getItemName().toLowerCase().contains(text) || product.getItemName().toLowerCase().contains(text) || product.getItembarcode().toLowerCase().contains(text)) {
                filteredList.add(product);
            }
        }

        mAdapter.filterList(filteredList);
    }


    private void uploadFieldwork() {

        //   Date latestdate = globalVariable.getgdate();


        final ProgressDialog prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_UPLOAD_GRMASTER),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        String res = response;
                        prgDialog.dismiss();

                        if (res.length() > 0) {
                            log.e("res ateendfsad " + res);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {
                                String phoneno, deviceid, action, messagetosend = "";
                                JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");
                                //  for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject loopObjects = jsonArray.getJSONObject(0);
                                serverid = loopObjects.getString("uid");
                                //  }

                                Toast.makeText(getActivity(), "GR Created Successfully!!!", Toast.LENGTH_SHORT).show();
                                uploadPO();

                                //    updateserverphotoid();
                                //   uploadimage();
                                  /*  startActivity(new Intent(getActivity(), GRNDetail.class)
                                            .putExtra("id", serverid)
                                            .putExtra("tax", spnrTax.getSelectedItem().toString())
                                            .putExtra("supplier", strbillables)
                                            .putExtra("invoicenumber", et_invoiceno.getText().toString())
                                            .putExtra("invoicedate", etDate.getText().toString())
                                            .putExtra("type", "")
                                            .putExtra("supplierID", ConsignorID)); // 0=normal, 1=priority
                                    clearContols();
*/
                                // log.e("Uploaded Fieldwork Successful = " + spnrTax.getSelectedItem().toString());
                                //JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            } else if (success == 0) {
                                Toast toast = Toast.makeText(getActivity(), "Fieldwork was not updated", Toast.LENGTH_SHORT);
                                toast.show();
                                Message.message(getActivity(), "Error on Fieldwork Upload");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        prgDialog.dismiss();

                        Message.message(getActivity(), "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                        //  updateserverphotoid();
                        //uploadimage();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params = generateUploadParams();
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(postRequest);

    }

    private Map<String, String> generateUploadParams() {
        generateOTP();
        Map<String, String> params = new HashMap<>();
        com.dhanuka.morningparcel.Helper.Preferencehelper prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(getActivity());
        params.put("status", 0 + "");
        params.put("type", "1");// 1 = GRN, 2 = Stock transfer
        if (prefs.getPrefsContactId() != null) {
            params.put("contactid", prefs.getPrefsContactId());
        } else {
            params.put("contactid", "7508");
        }
        params.put("mobiledate", JKHelper.getCurrentDate());
        params.put("battery", JKHelper.getBatteryLevel(getActivity()) + "");
        params.put("comments", "");
        params.put("worktype", "PO");

      /*  if (strworktype.equalsIgnoreCase("2"))//1 - GRN, 2- Stock Transfer
        {*/
        params.put("from", "");
        params.put("to", "");
        params.put("clientname", supplier_name);
        params.put("ConsignorID", supplier_id);
        /*} else {
            params.put("from", "");
            params.put("to", "");
            params.put("clientname", spnrbillable.getSelectedItem().toString());
            if (dataList != null) {
                for (int i = 0; i < dataList.size(); i++) {
                    if (spnrbillable.getSelectedItem().toString().equalsIgnoreCase(dataList.get(i).getmPrintName())) {
                        ConsignorID = dataList.get(i).getmClientId().toString();
                    }
                }
            }
            params.put("ConsignorID", ConsignorID);
        }*/


        params.put("docID", strRandom);
        params.put("createddate", JKHelper.getCurrentDate());
        params.put("fieldworkdate", JKHelper.getCurrentDate());
        Log.e("ParamsOfOOSMastewr", params.toString());
        return params;
    }

    String strRandom = "";

    public void generateOTP() {
        Random otp = new Random();

        StringBuilder builder = new StringBuilder();
        for (int count = 0; count < 6; count++) {
            builder.append(otp.nextInt(10));
        }
        strRandom = builder.toString();
        Log.d("Number", " " + builder.toString());

    }


    private void uploadPO() {
        // final AppClass globalVariable = (AppClass) getApplicationContext();
        // Date latestdate = globalVariable.getgdate();


        final ProgressDialog prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();
        String strUrl = "";
           /* if (et_item_IDs.getText().toString().equalsIgnoreCase("0")) {
                strUrl = "http://mmthinkbiz.com/MobileService.aspx/getds?method=CreateGR_Detail_Edit";
            } else {

            }*/


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_UPLOAD_GRDETAIL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        String res = response;
                        prgDialog.dismiss();

                        if (res.length() > 0) {
                            log.e("res ateendfsad " + res);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {
                                //   isEditable = true;
                                String phoneno, deviceid, action, messagetosend = "";
                                JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject loopObjects = jsonArray.getJSONObject(i);
                                   // serverid = loopObjects.getString("uid");
                                }

                                    
                                    /*
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity() );
                                    alertDialog.setTitle("Do you want to Add Items to this Fieldwork?");
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(getActivity(), fieldworkadditem.class).putExtra("fieldworkid", serverid).putExtra("type", "0"));
                                            finish();
                                        }
                                    });
                                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    alertDialog.show();
*/
                                //JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            } else if (success == 0) {
                                Toast toast = Toast.makeText(getActivity(), "Item was not updated", Toast.LENGTH_SHORT);
                                toast.show();
                                Message.message(getActivity(), "Error on Fieldwork Upload");
                            }

                            mCOunt++;
                            if (mListSelected.size() > mCOunt) {
                                uploadPO();
                            } else {

                                PackageManager pm = getActivity().getPackageManager();
                                try {
                                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                                    waIntent.setType("text/plain");
                                    String text="";
                                    for (int a=0;a<mListSelected.size();a++){
                                        text = text+"Item - " + mListSelected.get(a).getItemName()+ "\nBarcode - " + mListSelected.get(a).getItembarcode() + "\nQty - " + mListSelected.get(a).getCurrentstock() + "\n\n"; // Replace with your own message.

                                    }

                                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                    //Check if package exists or not. If not then code
                                    //in catch block will be called
                                    waIntent.setPackage("com.whatsapp");
                                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                                    startActivity(Intent.createChooser(waIntent, "Share Vehicle Info with"));
                                } catch (PackageManager.NameNotFoundException e) {
                                    FancyToast.makeText(getActivity(), "WhatsApp not Installed", FancyToast.LENGTH_SHORT, FancyToast.INFO, false)
                                            .show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mAdapter.unselectall();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        prgDialog.dismiss();

                        Message.message(getActivity(), "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                        //updateserverphotoid();
                        //uploadimage();
                        //clearContols();
                        //finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params = generateUploadParamsNew();
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(postRequest);

    }


    int mCOunt = 0;

    private Map<String, String> generateUploadParamsNew() {
        Map<String, String> params = new HashMap<>();
        com.dhanuka.morningparcel.utils.Preferencehelper prefs = new com.dhanuka.morningparcel.utils.Preferencehelper(getActivity());
        //  final AppClass globalVariable = (AppClass) getApplicationContext();
        params.put("status", 0 + "");
        params.put("type", "PO");
        if (prefs.getProfileId() != null) {
            params.put("contactid", prefs.getProfileId());
        } else {
            params.put("contactid", "7508");
        }
        String amountwithtax = "0";
        String taxamt = "0";


        params.put("mobiledate", JKHelper.getCurrentDate());
        params.put("battery", JKHelper.getBatteryLevel(getActivity()) + "");
        params.put("comments", "");
        params.put("masterid", serverid);
        params.put("taxamt", "0");
        params.put("amountwithtax", amountwithtax);
        params.put("itemID", mListSelected.get(mCOunt).getItemid());
        params.put("itembarcode", mListSelected.get(mCOunt).getItembarcode());
        params.put("boxcode", mListSelected.get(mCOunt).getItembarcode());
        params.put("itemname", mListSelected.get(mCOunt).getItemName());


        params.put("cessper", "0");

        params.put("cessAmt", "0");

        params.put("MRP", mListSelected.get(mCOunt).getMrp());
        params.put("tax", mListSelected.get(mCOunt).getTaxrate());
        params.put("Amount", mListSelected.get(mCOunt).getCompanycosting());
        params.put("HSNCode", "0");
        params.put("qty", mListSelected.get(mCOunt).getCurrentstock());
        params.put("manufacturingdate", "");
        params.put("expired", "0");
        params.put("damaged", "0");
        params.put("createddate", JKHelper.getCurrentDate());
        params.put("unit", "0");
        //(,,,,amountwithtax,taxamt)

        Log.e("ParamsOfOOSDetail", params.toString());
        return params;
    }


}


