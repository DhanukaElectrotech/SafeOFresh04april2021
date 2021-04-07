package com.dhanuka.morningparcel.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.SaleproductsAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.beans.Products;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.OnAddToSToreListener;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.NetworkMgr;

/**
 *
 */
public class SaleItemsFragment extends Fragment implements onAddCartListener, OnAddToSToreListener {
    private int sub_category_id, shop_id, cat_id;
    private RecyclerView lvProducts;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue;
    private Context context;
    private SaleproductsAdapter adapter;
    private String subCategory;
    List<Products> list = new ArrayList<>();
    DatabaseManager dbManager;
    CartCountView countView;
    EditText etSearch;

    SharedPreferences.Editor mEditorL;
    SharedPreferences prefsL;

    Preferencehelper prefs1;

    public static SaleItemsFragment newInstance() {
        Bundle args = new Bundle();
        SaleItemsFragment fragment = new SaleItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    String type = "Pending";

    ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
    ArrayList<ItemMasterhelper> MainList = new ArrayList<>();
    ArrayList<ItemMasterhelper> masterlist1 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_products, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void init(View view) {
        subCategory = getArguments().getString("sub_category");
        sub_category_id = getArguments().getInt("sub_category_id");
        shop_id = getArguments().getInt("shop_id");
        cat_id = getArguments().getInt("cat_id");
        lvProducts = (RecyclerView) view.findViewById(R.id.lvProducts);
        linearContinue = (LinearLayout) view.findViewById(R.id.linearContinue);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        context = lvProducts.getContext();
        countView = (CartCountView) LayoutInflater.from(context).inflate(R.layout.action_cart, null);
        lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        dbManager = DatabaseManager.getInstance(context);
        etSearch = (EditText) view.findViewById(R.id.etSearch);

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
                    filter(s.toString());
                    /*
                    adapter.filter(s.toString());
                    lvProducts.invalidate();
*/
                }
            }
        });


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
        if (NetworkMgr.isNetworkAvailable(context)) {
            SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                    context.MODE_PRIVATE);
            try {
                if (!prefs.getString("resp1", "-1").equalsIgnoreCase("-1")) {
                    loadLocal(prefs.getString("resp1", "-1"));
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

    }
    public void getAllProducts(Context ctx, String shopId, String contactid) {
        pbLoading.setVisibility(View.VISIBLE);
/*  final ProgressDialog mProgressBar = new ProgressDialog(getActivity());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
*/        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
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
                String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + contactid + "&Type=" + "0" + "&supplierid=" + shopId+ "&GroupId=" + subCategory;
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

    @Override
    public void onPause() {

        super.onPause();
    }

  /*  @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchTextChanged(SearchTextChangedEvent event) {
        if (adapter != null) {
            filter(event.getText());
            //  adapter.filter(event.getText());
            lvProducts.invalidate();
        }
    }*/

    private void filter(String text) {
        ArrayList<ItemMasterhelper> filteredList = new ArrayList<>();

        for (ItemMasterhelper product : MainList) {
            if (product.getItemBarcode().toLowerCase().contains(text) || product.getItemName().toLowerCase().contains(text) || product.getHSNCode().toLowerCase().contains(text) || product.getItemSKU().toLowerCase().contains(text)) {
                filteredList.add(product);
            }
        }

        adapter.filterList(filteredList);
    }

    public void loadLocal(String resp) {
        SharedPreferences prefss = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);

        masterlist = new ArrayList<>();
        masterlist1 = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(resp);
            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
            if (jsonObject.getInt("success") == 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newjson = jsonArray.getJSONObject(i);
                    Log.e("subCategory",subCategory+" ===  "+newjson.getString("GroupID"));
                    if (newjson.getString("GroupID").equalsIgnoreCase(subCategory)) {

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
                        String VendorID = newjson.getString("VendorID");
                        String ToShow = newjson.getString("ToShow");
                        String AvailableQty = newjson.getString("AvailableQty");
                        String StoreSTatus = newjson.getString("StoreSTatus");
                        String MRP = newjson.getString("MRP");
                        String IsDeal = newjson.getString("IsDeal");
                        String IsNewListing = newjson.getString("IsNewListing");

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
                        v.setIsNewListing(IsNewListing);
                        v.setIsDeal(IsDeal);
                        v.setFileName(FileName);
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
                        v.setMRP(MRP);
                        masterlist.add(v);
//                        ArrayList<ItemMasterhelper> mListProducts1 = new ArrayList<>();
//                        ArrayList<ItemMasterhelper> mListProducts2 = new ArrayList<>();
//                        if (!prefss.getString("mClick", "Pending").equalsIgnoreCase("Pending")) {
//                            /*if (!StoreSTatus.equalsIgnoreCase("In Store")) {*/
//                            if (StoreSTatus.equalsIgnoreCase("In Store")) {
//
//                            }                            //     }
//                        } else {
//                            if (!StoreSTatus.equalsIgnoreCase("In Store")) {
//                                masterlist1.add(v);
//                            }
//
//                        }


                        //     masterlist.add(v);
                    }
                }
            }
            MainList = new ArrayList<>();
            if (!prefss.getString("mClick", "Pending").equalsIgnoreCase("Pending")) {
              //Log.d("masterlist", String.valueOf(masterlist.size()));
                type = "In Store";
                MainList = masterlist;
            } else {
                type = "Pending";
                MainList = masterlist1;

//                adapter = new ProductsAdapterStore(context, masterlist1, this, "Pending");
                Log.d("masterlist1", String.valueOf(masterlist1.size()));

            }

            adapter = new SaleproductsAdapter(context, MainList, type, this);
            lvProducts.setAdapter(adapter);
            lvProducts.setVisibility(View.VISIBLE);
            if (MainList.size() < 1) {
                linearContinue.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // getAllProducts();
        }
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
                Log.e("Iammrp" ,cartProduct.getMRP());
            } else {
                if (cartProduct.getQuantity() > 0) {
                    cartProduct.setQuantity(cartProduct.getQuantity() - 1);
                    Log.e("Iammrp" ,cartProduct.getMRP());
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


    private void setCartCount() {
        countView.setCount(dbManager.getTotalQty());
        someEventListener.someEvent(dbManager.getTotalQty() + "");
    }

 /*   private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

*/
    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    if (adapter != null) {
                        filter(newText);
                        //   adapter.filter(newText);
                        lvProducts.invalidate();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

*/
}
