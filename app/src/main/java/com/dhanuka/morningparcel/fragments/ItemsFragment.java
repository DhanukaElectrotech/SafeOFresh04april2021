package com.dhanuka.morningparcel.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.SelectedProductsAdapter;
import com.dhanuka.morningparcel.beans.ChildCatBean;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.events.OnProductEditListener;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.NetworkMgr;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.adapter.StoreproductsAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.beans.Products;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.OnAddToSToreListener;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

/**
 *
 */
public class ItemsFragment extends Fragment implements onAddCartListener, OnAddToSToreListener, OnProductEditListener {
    List<ItemMasterhelper> mListChecked = new ArrayList<>();
    ImageView imgBackBtn;
    LinearLayout subCatLayout;
    Button selectcartbtn;
    SearchableSpinner spCategory;
    Spinner spSubCategory;
    String stritems;
    RecyclerView rvSelectedItems;
    String selectuom;
    Dialog PayDialog;
    private int sub_category_id, shop_id, cat_id;
    private RecyclerView lvProducts;
    int mType = 0;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue, llCancel;
    private Context context;
    private StoreproductsAdapter adapter;
    private String subCategory;
    List<Products> list = new ArrayList<>();
    ImageView editProducts;

    TextView txtCancel;

    DatabaseManager dbManager;
    CartCountView countView;
    EditText etSearch;
    String mImagePathDataBase, mImageNameDataBase, mCurrentTimeDataBase;
    int masterDataBaseId;
    String lastRowMaterTable;
    private boolean addedToMasterTable = false;

    public static ItemsFragment newInstance() {
        Bundle args = new Bundle();
        ItemsFragment fragment = new ItemsFragment();
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

    String currency;
    Preferencehelper prefs;


    private void init(View view) {

        subCategory = getArguments().getString("sub_category");
        sub_category_id = getArguments().getInt("sub_category_id");
        shop_id = getArguments().getInt("shop_id");
        cat_id = getArguments().getInt("cat_id");
        lvProducts = (RecyclerView) view.findViewById(R.id.lvProducts);
        editProducts = (ImageView) view.findViewById(R.id.editProducts);
        linearContinue = (LinearLayout) view.findViewById(R.id.linearContinue);
        llCancel = (LinearLayout) view.findViewById(R.id.llCancel);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        context = lvProducts.getContext();

        prefs = new Preferencehelper(context);

        if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
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

        PayDialog = new Dialog(getActivity(), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        PayDialog.setContentView(R.layout.dialog_change_category);
        imgBackBtn = PayDialog.findViewById(R.id.imgBackBtn);
        spSubCategory = PayDialog.findViewById(R.id.spSubCategory);
        spCategory = (SearchableSpinner) PayDialog.findViewById(R.id.spCategory);
        subCatLayout = PayDialog.findViewById(R.id.subCatLayout);
        rvSelectedItems = PayDialog.findViewById(R.id.rvSelectedItems);
        selectcartbtn = PayDialog.findViewById(R.id.btnsummit);
        rvSelectedItems.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayDialog.dismiss();
            }
        });

        selectcartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                changeCat();
            }
        });

        editProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainList.size() > 0) {
                    if (adapter != null) {
                        checkUncheck();

                    }

                }
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    mListChecked = new ArrayList<>();
                    mListChecked = adapter.getChckedProducts();
                    if (mListChecked.size() > 0) {
                        rvSelectedItems.setAdapter(new SelectedProductsAdapter(getActivity(), mListChecked));
                        //  spCategory.setAdapter(new CatSpinnerAdapter(getActivity(), mListCategories));
                        spCategory.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mListCategoriessearch));
                        spCategory.setTitle("Select Item");
                        spCategory.setPositiveButton("Ok");
                        // spSubCategory.setAdapter(new SubCatSpinnerAdapter(getActivity(), mListSubCat));

                        spSubCategory.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mListSubCatsearch));

                        PayDialog.show();
                    } else {
                        Toast.makeText(context, "Please select atleast one Item to move ot another category", Toast.LENGTH_SHORT).show();

                    }
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
        list.add(new Products());

        */
        setDropDown1();


    }

    ArrayList<MainCatBean> mListCatMain = new ArrayList<>();
    ArrayList<MainCatBean.CatBean> mListCategories = new ArrayList<>();

    ArrayList<String> mListCategoriessearch = new ArrayList<>();
    HashMap<String, String> subcathashmap = new HashMap<>();


    public void setDropDown1() {
        mListCatMain = new ArrayList<>();
        SharedPreferences prefs = getActivity().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getActivity().MODE_PRIVATE);

        try {
            JSONObject jsonObject = new JSONObject(prefs.getString("resp", ""));

            mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int a = 0; a < mListCatMain.size(); a++) {
            //    for (int b = 0; b < mListCatMain.get(a).getmListCategories().size(); b++) {
            mListCategories.addAll(mListCatMain.get(a).getmListCategories());

            for (int p = 0; p < mListCatMain.get(a).getmListCategories().size(); p++) {
                mListCategoriessearch.add(mListCatMain.get(a).getmListCategories().get(p).getStrName());
                subcathashmap.put(mListCatMain.get(a).getmListCategories().get(p).getStrName(), mListCatMain.get(a).getmListCategories().get(p).getStrId());
            }
            //  }
        }

//        spCategory.setAdapter(new CatSpinnerAdapter(getActivity(), mListCategories));
        spCategory.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mListCategoriessearch));

        spCategory.setTitle("Select Item");
        spCategory.setPositiveButton("Ok");
        loadChilCat(mListCategories.get(0).getStrId());
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                subcathashmap.get(spCategory.getSelectedItem().toString());
                // loadChilCat(mListCategories.get(position).getStrId());
                loadChilCat(mListCategories.get(spCategory.getSelectedItemPosition()).getStrId());
                //  Toast.makeText(getActivity(),String.valueOf(subcathashmap.get(spCategory.getSelectedItem().toString())),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    ArrayList<ChildCatBean> mListSubCat = new ArrayList<>();
    ArrayList<String> mListSubCatsearch = new ArrayList<>();
    HashMap<String, String> hashmapmaincat = new HashMap<>();

    public void loadChilCat(String subcat) {
        mListSubCat = new ArrayList<>();
        mListSubCatsearch = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        if (prefs.getString("respSubCat", "").isEmpty()) {
            loadSubCategories("SafeOKart1");

        } else {
            try {

                JSONObject jsonObject = new JSONObject(prefs.getString("respSubCat", ""));

                for (int a = 0; a < jsonObject.getJSONArray("returnds").length(); a++) {
                    JSONObject mJSONObject = jsonObject.getJSONArray("returnds").getJSONObject(a);
                    if (mJSONObject.getString("GroupType").equalsIgnoreCase(subcat)) {
                        ChildCatBean childCatBean = new ChildCatBean();
                        childCatBean.setDescription(mJSONObject.getString("Description"));
                        childCatBean.setGroupType(mJSONObject.getString("GroupType"));
                        childCatBean.setGroupId(mJSONObject.getString("GroupId"));
                        childCatBean.setImageName(mJSONObject.getString("ImageName"));
                        childCatBean.setWeight(mJSONObject.getString("Weight"));
                        childCatBean.setIsSubSubGroup(mJSONObject.getString("IsSubSubGroup"));
                        mListSubCat.add(childCatBean);
                        mListSubCatsearch.add(mJSONObject.getString("Description"));
                        hashmapmaincat.put(mJSONObject.getString("Description"), mJSONObject.getString("GroupId"));
                    }
                }
                //  mListSubCat = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<ChildCatBean>>() {
                //  }.getType());
                if (mListSubCat.size() > 0) {
                    if (mListSubCat.size() < 1) {

                        ChildCatBean childCatBean = new ChildCatBean();
                        childCatBean.setDescription("All");
                        childCatBean.setGroupType("0000");
                        childCatBean.setGroupId("0000");
                        childCatBean.setIsSubSubGroup("1");

                        mListSubCatsearch.add(childCatBean.getDescription());
                        hashmapmaincat.put(childCatBean.getDescription(), childCatBean.getGroupId());


                        mListSubCat.add(0, childCatBean);
                    }
                    subCatLayout.setVisibility(View.VISIBLE);

                    spSubCategory.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mListSubCatsearch));


                    //   spSubCategory.setAdapter(new SubCatSpinnerAdapter(getActivity(), mListSubCat));
                } else {
                    subCatLayout.setVisibility(View.GONE);

                }


            } catch (Exception e) {
                e.printStackTrace();
                subCatLayout.setVisibility(View.GONE);
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
                    Log.e("subCategory", subCategory + " ===  " + newjson.getString("GroupID"));
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
                        String DbSalerate = newjson.getString("DbSalerate");
                        String DbSalerate1 = newjson.getString("DbSalerate1");
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
                        v.setDbSalerate1(DbSalerate1);
                        v.setStoreSTatus(StoreSTatus);
                        v.setAvailableQty(AvailableQty);
                        v.setIsNewListing(IsNewListing);
                        v.setIsDeal(IsDeal);
                        v.setFileName(FileName);
                        v.setFilepath(FilePath);
                        v.setItemID(ItemID);
                        v.setItemName(ItemName);
                        v.setCompanyid(companyid);
                        v.setDbSalerate(DbSalerate);
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
                        try {
                            v.setIsImage(newjson.getString("isImage"));
                            v.setIsname(newjson.getString("isname"));
                        } catch (Exception e) {
                            try{
                                v.setIsImage(newjson.getString("checkimage"));
                                v.setIsname(newjson.getString("checkname"));

                            }catch (Exception er){}

                        }
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
                editProducts.setVisibility(View.VISIBLE);
            } else {
                type = "Pending";
                MainList = masterlist1;
                editProducts.setVisibility(View.GONE);

//                adapter = new ProductsAdapterStore(context, masterlist1, this, "Pending");
                Log.d("masterlist1", String.valueOf(masterlist1.size()));

            }
Log.e("masterlist",new Gson().toJson(masterlist));
            adapter = new StoreproductsAdapter(context, MainList, type, this, this, mType);
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

    int mPosition;
    int isEdit = 0;
    EditText tvQty;
    EditText tvMRP;
    EditText rg;
    EditText edtPrice1;
    ImageView ivProduct;
    ImageView editName;
    ImageView ivEdit;
    EditText tvName;
    TextView tvName1;
    ItemMasterhelper mMasterhelper;

    @Override
    public void onProductEdit(ItemMasterhelper mItemMasterhelper, int mPosition) {
        mImagePathDataBase = "";
        mImageNameDataBase = "";
        mCurrentTimeDataBase = "";
        masterDataBaseId = 0;
        lastRowMaterTable = "";
        addedToMasterTable = false;

        mMasterhelper = mItemMasterhelper;
        this.mPosition = mPosition;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_edit_product);

        tvQty = (EditText) dialog.findViewById(R.id.tvQty);
        tvMRP = (EditText) dialog.findViewById(R.id.tvMRP);
        rg = (EditText) dialog.findViewById(R.id.edtPrice);
        edtPrice1 = (EditText) dialog.findViewById(R.id.edtPrice1);
        ivProduct = (ImageView) dialog.findViewById(R.id.ivProduct);
        editName = (ImageView) dialog.findViewById(R.id.editName);
        ivEdit = (ImageView) dialog.findViewById(R.id.ivEdit);
        tvName = (EditText) dialog.findViewById(R.id.tvName);
        tvName1 = (TextView) dialog.findViewById(R.id.tvName1);
        final TextView tvOriginalPrice = (TextView) dialog.findViewById(R.id.tvOriginalPrice);

        isEdit = 0;
        try {
            edtPrice1.setText(mItemMasterhelper.getDbSalerate1());
        } catch (Exception e) {
            e.printStackTrace();
        }
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEdit == 0) {
                    isEdit = 1;
                    editName.setImageResource(R.drawable.ic_baseline_check_24);
                    tvName.setVisibility(View.VISIBLE);
                    tvName1.setVisibility(View.GONE);
                    tvName.requestFocus();
                    tvName.setFocusable(true);
                } else {
                    tvName1.setText(tvName.getText().toString());
                    editName.setImageResource(R.drawable.ic_edit);
                    isEdit = 0;
                    tvName1.setVisibility(View.VISIBLE);
                    tvName.setVisibility(View.GONE);
                }


            }
        });

        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        try {
            rg.setText(new DecimalFormat("##.##").format(Double.parseDouble(mItemMasterhelper.getDbSalerate())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rg.requestFocus();
        rg.setFocusable(true);
        tvName1.setText(mItemMasterhelper.getItemName());
        tvName.setText(mItemMasterhelper.getItemName());
        try {
            if (!mItemMasterhelper.getAvailableQty().isEmpty()) {
                tvQty.setText(new DecimalFormat("##.##").format(Double.parseDouble(mItemMasterhelper.getAvailableQty())));
            } else {
                tvQty.setText("0");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                tvName.setSelection(tvName.getText().toString().length());


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Glide.with(context)
                .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + mItemMasterhelper.getFileName() + "&filePath=" + mItemMasterhelper.getFilepath())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(ivProduct);


        tvOriginalPrice.setText(/*context.getString(R.string.rupee) +*/currency + new DecimalFormat("##.##").format(Double.parseDouble(mItemMasterhelper.getSaleRate())));

        try {
            tvMRP.setText("" + new DecimalFormat("##.##").format(Double.parseDouble(mItemMasterhelper.getMRP())));
        } catch (Exception e) {
            e.printStackTrace();
        } //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
        final Button btnAddToCart = (Button) dialog.findViewById(R.id.btnAddToCart);


        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("mClickType", "Update");
                String status = rg.getText().toString();
                if (tvName.getText().toString().isEmpty()) {
                    com.dhanuka.morningparcel.beans.FancyToast.makeText(context, "Please Enter Item Name.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();

                } else if (status.isEmpty()) {
                    com.dhanuka.morningparcel.beans.FancyToast.makeText(context, "Please Enter Amount.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();

                } else {
                    status = new DecimalFormat("##.##").format(Double.parseDouble(status)) + "";
                    MainList.get(mPosition).setItemName(tvName.getText().toString());
                    MainList.get(mPosition).setAvailableQty(tvQty.getText().toString());


                    updateProduct(context, mItemMasterhelper.getItemID(), tvQty.getText().toString(), "0", status, "3", tvName.getText().toString(), edtPrice1.getText().toString());

                    dialog.dismiss();
                }

            }
        });


        // Display the custom alert dialog on interface
        dialog.show();


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

    String orderId = "";

    public void updateProduct(Context ctx, String ItemID, String AvailableQty, String ToShow, String SaleRate, String Type, String strItemname, String salerate1) {

        ToShow1 = ToShow;
        strType = Type;
        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ctx);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                if (type.equalsIgnoreCase("4")) {
                                }
                                orderId = ItemID;

                                updateserverphotoid();
                                uploadimage();

                                adapter.notifyDataSetChanged();

                            } else {
                                if (!type.equalsIgnoreCase("7")) {
                                    com.dhanuka.morningparcel.beans.FancyToast.makeText(ctx, "failed to update the order, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgressBar.dismiss();

                        if (type.equalsIgnoreCase("7")) {
                            if (count < mListChecked.size()) {
                                count++;
                                changeCat();
                            } else {
                            }

                            setAdapter();
                            PayDialog.dismiss();

                        }
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();

                        if (!type.equalsIgnoreCase("7")) {
                            com.dhanuka.morningparcel.beans.FancyToast.makeText(ctx, "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());


                Preferencehelper prefs;
                prefs = new Preferencehelper(ctx);
                Map<String, String> params = new HashMap<String, String>();
                String mSaleRate = "";
                String strmrp = "0";
                try {
                    mSaleRate = (Double.parseDouble(SaleRate)) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                    mSaleRate = SaleRate;
                }
                String mQty = "";
                try {
                    mQty = (int) (Double.parseDouble(AvailableQty)) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                    mQty = AvailableQty;
                }

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    int as = 0;
                    String saleuom = "";

                    if (Type.equalsIgnoreCase("5A")) {
                        strType = Type.replace("A", "");
                        ToShow1 = "1";
                        as = 1;
                    } else if (Type.equalsIgnoreCase("5B")) {
                        strType = Type.replace("B", "");
                        ToShow1 = "0";
                        as = 1;

                    } else if (Type.equalsIgnoreCase("6C")) {
                        strType = Type.replace("C", "");
                        ToShow1 = "1";
                        as = 1;

                    } else if (Type.equalsIgnoreCase("6D")) {
                        strType = Type.replace("D", "");
                        ToShow1 = "0";
                        as = 1;

                    } else {
                        ToShow1 = ToShow;
                        strType = Type;
                        as = 0;
                    }

                    try {
                        if (mQty.isEmpty()) {
                            mQty = "0";
                        }
                    } catch (Exception e) {
                        mQty = "0";
                        e.printStackTrace();
                    }
                    if (as == 1) {
                        strType = strType + "&isdeal=" + ToShow1;
                    } else {
                        strType = strType;
                    }


                    String sale = "";

                    if (strType.equalsIgnoreCase("3")) {
                        sale = "&SaleRate1=" + salerate1;
                        strmrp = tvMRP.getText().toString();
                    }

                    String param = ctx.getString(R.string.CREATE_ORDER_ITEMS) + "&ItemID=" + ItemID + "&VendorID=" + prefs.getPrefsContactId() + "&Createdby=" +
                            prefs.getPrefsContactId() + "&AvailableQty=" + mQty
                            + "&ToShow=" + ToShow1 + "&SaleRate=" + mSaleRate + sale + "&Type=" + strType + "&SaleUOMID=" + "0" + "&SaleUOM=" + saleuom + "&MRP=" + strmrp;
                    if (Type.equalsIgnoreCase("8")) {
                        strType = Type;


                        param = ctx.getString(R.string.CREATE_ORDER_ITEMS) + "&ItemID=" + "0" + "&VendorID=" + prefs.getPrefsContactId() + "&Createdby=" +
                                prefs.getPrefsContactId() + "&AvailableQty=" + mQty
                                + "&ToShow=" + ToShow1 + "&SaleRate=" + mSaleRate + sale + "&Type=" + strType + "&SaleUOMID=" + "0" + "&SaleUOM=" + ItemID;

                    }
                    Log.d("Beforeencrptionadd", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrptionadd", finalparam);
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

        Volley.newRequestQueue(ctx).add(postRequest);


    }

    String strType;
    String ToShow1;

    public void captureImage() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
             /*   Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 66);
*/
            Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
            intent1.putExtra(IS_NEED_CAMERA, true);
            intent1.putExtra(Constant.MAX_NUMBER, 1);
            intent1.putExtra(IS_NEED_FOLDER_LIST, true);
            startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

//                launchGalleryIntent();
        }
    }

    private String filePath;

    File mFile1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == Constant.REQUEST_CODE_TAKE_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    //   mImages.add(path);
                    // mAdapter.addItems(mImages);
                    RequestOptions options = new RequestOptions();
                    options.centerCrop();

                    Glide.with(context)
                            .load(path)
                            .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                            .into(ivProduct);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }

            Bitmap bitmap = null;

            if (requestCode == Constant.REQUEST_CODE_PICK_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    RequestOptions options = new RequestOptions();
                    options.centerCrop();

                    Glide.with(context)
                            .load(path)
                            .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                            .into(ivProduct);

                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }


        } else if (resultCode != RESULT_CANCELED) {
            //  showDialog(((Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR)).getMessage());
        }
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        //    prefs.setNearByApiStatus(true);
                        break;
                    case RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery) {
            this.fromGallery = fromGallery;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {
                filePath = compressImage(params[0]);
            } catch (Exception e) {
                Log.e("exception", e.getMessage());

            }

            return filePath;
        }

        public String compressImage(String imageUri) {

            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filename = getFilename(imageUri);
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filename;

        }

        private String getRealPathFromURI(String contentURI) {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }

        public String getFilename(String imageUri) {
            File file = getActivity().getExternalFilesDir("MmThinkBiz/Images");
            if (!file.exists()) {
                file.mkdirs();
            }

            String filename = imageUri.substring(imageUri.lastIndexOf("/") + 1);

            log.e("file name in compress image== " + filename);

            String uriSting = (file.getAbsolutePath() + "/" + filename);
            log.e("uri string compress image ==" + uriSting);

            mImagePathDataBase = uriSting;
            mImageNameDataBase = filename;
            mCurrentTimeDataBase = JKHelper.getCurrentDate();

            log.e("mimage path database==" + mImagePathDataBase);
            log.e("image name==" + mImageNameDataBase);
            log.e("current image type==" + mCurrentTimeDataBase);

            return uriSting;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            log.e("post excute  ");
            addToDatabase();
        }

        private void addToDatabase() {

            if (!addedToMasterTable) {
                final ArrayList<DbImageMaster> arrayList = new ArrayList<>();
                DbImageMaster modle = new DbImageMaster();
                modle.setmDate(JKHelper.getCurrentDate());
                modle.setmUploadStatus(0);
                modle.setmDescription("item_master1");
                modle.setmImageType("item_master1");
                arrayList.add(modle);

                com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, getActivity());
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageMasterDAO dao = new ImageMasterDAO(database, getActivity());
                    masterDataBaseId = dao.getlatestinsertedid();
                    lastRowMaterTable = String.valueOf(masterDataBaseId);
                    //   mListLastRows.add(lastRowMaterTable);
                    log.e("string id ============== " + masterDataBaseId);
                }
            });


            final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
            DbImageUpload modle = new DbImageUpload();
            modle.setmDate(mCurrentTimeDataBase);
            modle.setmImageUploadStatus(0);
            modle.setmDescription("item_master1");
            modle.setmImageType("item_master1");
            modle.setmImageId(masterDataBaseId);
            modle.setmImagePath(mImagePathDataBase);
            modle.setmImageName(mImageNameDataBase);
            arrayListUpload.add(modle);


            com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, getActivity());
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            //    setPhotoCount();


        }
    }

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(getActivity()) && !JKHelper.isServiceRunning(getActivity(), ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            getActivity().startService(new Intent(getActivity(), ImageUploadService.class));
        } else {
            getActivity().stopService(new Intent(getActivity(), ImageUploadService.class));
            getActivity().startService(new Intent(getActivity(), ImageUploadService.class));
        }
    }

    private void updateserverphotoid() {
        if (lastRowMaterTable != null && !lastRowMaterTable.isEmpty()) {
            SQLiteDatabase database = com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().openDatabase();
            ImageUploadDAO pd = new ImageUploadDAO(database, getActivity());
            pd.setWorkIdToTable(String.valueOf(orderId), lastRowMaterTable);
            ImageMasterDAO pds = new ImageMasterDAO(database, getActivity());
            pds.setServerDetails(Integer.parseInt(lastRowMaterTable), Integer.valueOf(orderId), 0);
            ImageMasterDAO iDao = new ImageMasterDAO(database, getActivity());
            String serverId = iDao.getServerIdById(1);
            com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().closeDatabase();
            log.e("in last row master table inserting  ");
        }
    }


    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    String TAG = "SAFEOBUDDYIMAGE";
    public static final int REQUEST_IMAGE = 100;


    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public void checkUncheck() {
        if (mType == 0) {
            mType = 1;
            editProducts.setImageResource(R.drawable.ic_baseline_close_24);
            llCancel.setVisibility(View.VISIBLE);
        } else {
            editProducts.setImageResource(R.drawable.ic_edit);
            mType = 0;
            llCancel.setVisibility(View.GONE);
        }
        adapter.changeToEdit(mType);
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
                            mEditor.putString("respSubCat", "");
                            mEditor.commit();

                            Log.d("responsiverespSubCat", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mEditor.putString("respSubCat", response);
                                mEditor.commit();
                                try {
                                    loadChilCat(mListCategories.get(0).getStrId());
                                } catch (Exception w) {
                                    w.printStackTrace();
                                }
                            } else {

                                com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();


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

    int count = 0;

    public void changeCat() {

        for (int i = 0; i < mListChecked.size(); i++) {
            if (i == 0) {
                stritems = mListChecked.get(i).getItemID();

            } else {
                stritems = stritems + "/" + mListChecked.get(i).getItemID();
            }

        }
        strType = "8";

        updateProduct(context, stritems, mListCategories.get(spCategory.getSelectedItemPosition()).getStrId(), mListSubCat.get(spSubCategory.getSelectedItemPosition()).getGroupId(), mListChecked.get(count).getSaleRate(), "8", mListChecked.get(count).getItemName(), "");
        Log.e("update_params", mListCategories.get(spCategory.getSelectedItemPosition()).getStrId() + " == " + mListSubCat.get(spSubCategory.getSelectedItemPosition()).getGroupId());
        setAdapter();
        PayDialog.dismiss();

    }

    private void setAdapter() {
     /*   for (int a = 0; a < MainList.size(); a++) {
            for (int b = 0; b < mListChecked.size(); b++) {
                if (mListChecked.get(b).getItemID().equalsIgnoreCase(MainList.get(a).getItemID())) {
                    MainList.remove(a);
                }
            }
        }*/

        checkUncheck();
        adapter = new StoreproductsAdapter(context, MainList, type, this, this, mType);
        lvProducts.setAdapter(adapter);
        adapter.clearAll(mType);
    }
}
