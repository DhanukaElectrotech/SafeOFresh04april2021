package com.dhanuka.morningparcel.activity.retail;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;/*
import com.dhanuka.mmthinkbiz.PreferenceHelper;
import com.dhanuka.mmthinkbiz.R;
import com.dhanuka.mmthinkbiz.activities.Retail.adapter.BillItemsAdapter;
import com.dhanuka.mmthinkbiz.activities.Retail.beans.CustomerBean;
import com.dhanuka.mmthinkbiz.activities.Retail.beans.ItemBean;
import com.dhanuka.mmthinkbiz.activities.Retail.beans.ItemMasterhelper;
import com.google.gson.Gson;*/
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.pos.sdk.cardreader.PosMifareCardReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.AppController;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.InterfacePackage.Addressadd;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.activity.HomeStoreActivity;
import com.dhanuka.morningparcel.activity.barcode.Bar;
import com.dhanuka.morningparcel.activity.retail.adapter.BillItemsAdapter;
import com.dhanuka.morningparcel.activity.retail.adapter.OnDeleteListener;
import com.dhanuka.morningparcel.activity.retail.beans.ItemBean;
import com.dhanuka.morningparcel.activity.retail.beans.ItemMasterhelper;
import com.dhanuka.morningparcel.adapter.DeliveryHelper;
import com.dhanuka.morningparcel.adapter.Delivery_Option_Adapter;
import com.dhanuka.morningparcel.adapter.Payhelper;
import com.dhanuka.morningparcel.adapter.Payment_Option_Adapter;
import com.dhanuka.morningparcel.beans.DeliveryBoysBean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.NewBranchsalesbean;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
/*
import pub.devrel.easypermissions.PermissionRequest;
*/

public class NewBillActivity extends AppCompatActivity implements OnDeleteListener, EasyPermissions.PermissionCallbacks, CartItemsadd, Addressadd {

    public static final int REQUEST_PERMISSION = 0x01;

    //PosMifareCardReader cardReader = null;
    final static int MAX_TRY_CNT = 5;
    ArrayList<String> branchlist = new ArrayList<>();
    HashMap<String, String> branchhash = new HashMap<>();
    String mstatus = "0";

    ArrayList<NewBranchsalesbean.beanchinnerbean> mListReport = new ArrayList<>();
    @Nullable
    @BindView(R.id.rvBillItems)
    RecyclerView rvBillItems;
    @Nullable
    @BindView(R.id.btn_back)
    TextView btn_back;
    @Nullable
    @BindView(R.id.btnMinus)
    TextView btnMinus;
    @Nullable
    @BindView(R.id.btnAdd)
    TextView btnAdd;
    @Nullable
    @BindView(R.id.edtQty)
    EditText edtQty;
    @Nullable
    @BindView(R.id.edtDiscountP)
    EditText edtDiscountP;
    @Nullable
    @BindView(R.id.edtDiscount)
    EditText edtDiscount;
    @Nullable
    @BindView(R.id.ttlQty)
    TextView ttlQty;
    @Nullable
    @BindView(R.id.ttlAmt)
    TextView ttlAmt;
    @Nullable
    @BindView(R.id.ttlDisc)
    TextView ttlDisc;
    @Nullable
    @BindView(R.id.edtWeight)
    EditText edtWeight;
    @Nullable
    @BindView(R.id.itemAmnt)
    EditText itemAmnt;
    @Nullable
    @BindView(R.id.btnUpdate)
    TextView btnUpdate;
    @Nullable
    @BindView(R.id.btnCancel)
    TextView btnCancel;
    @Nullable
    @BindView(R.id.itemAmntT)
    EditText itemAmntT;
    @Nullable
    @BindView(R.id.searchItem)
    AutoCompleteTextView searchItem;
    @Nullable
    @BindView(R.id.searchCustomer)
    AutoCompleteTextView searchCustomer;
    @Nullable
    @BindView(R.id.imgAddNewCustomer)
    ImageView imgAddNewCustomer;
    @Nullable
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @Nullable
    @BindView(R.id.btnPay)
    TextView btnPay;
    int mSelected = -1;

    SharedPreferences prefs;
    SharedPreferences.Editor mEditor;
    ArrayList<ItemMasterhelper> mListAllItems = new ArrayList<>();
    ArrayList<ItemMasterhelper> mList = new ArrayList<>();

    Float finaltaxamount = 0.0f, finaltaxrate = 0.0f;

    public void backCLick(View v) {

        startActivity(new Intent(getApplicationContext(), HomeStoreActivity.class));
        finish();
    }

    BillItemsAdapter mBillAdapter;

    String paymentmode, paymentype;
    Double strDiscP = 10.0;
    String strSubTotal = "";
    String strTotal = "";
    String strTotalItems = "";
    String strItems = "";
    String strContactId = "";
    String strCustId = "0", strPhoneforotp = "0";
    Float saleuomint = 0.0f, slaerateclc = 0.0f, taxamount = 0.0f;

    public void barcodescaneResult(String strCode) {
        String[] item = filterData(strCode);
        if (item.length > 0) {
            searchItem.setText(item[0]);
            setAutoData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getString("resp2", "") == null || prefs.getString("resp2", "").isEmpty()) {
            getAllusers(NewBillActivity.this);

        } else {
            mListCustomers = new ArrayList<>();
            mListCustomers = new Gson().fromJson(prefs.getString("resp2", ""), new TypeToken<List<DeliveryBoysBean>>() {
            }.getType());  // startActivity(new Intent(NewBillActivity.this, NewBillActivity.class));

        }
    }

    Spinner spinner_branch;
    @BindView(R.id.referetbtn)
    ImageView referetbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);
        spinner_branch = findViewById(R.id.spinner_branch44);

        ButterKnife.bind(this);
        prefs = getSharedPreferences("MORNING_PARCEL_POS",
                MODE_PRIVATE);
        mEditor = prefs.edit();
        mEditor.putString("mValues", "");
        mEditor.commit();
        branchlist.add("Select Branch");
        branchhash.put("Select BRanch", "0");

        mList = new ArrayList<>();

        mBillAdapter = new BillItemsAdapter(this, mList, this, mSelected);
        rvBillItems.hasFixedSize();
        rvBillItems.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvBillItems.setAdapter(mBillAdapter);

        getReports();
        referetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner_branch.getSelectedItem().toString().equalsIgnoreCase("Select Branch")) {
                    Toast.makeText(getApplicationContext(), "Select Branch", Toast.LENGTH_LONG).show();

                } else {

                    getAllProducts(NewBillActivity.this, branchhash.get(spinner_branch.getSelectedItem().toString()));


                }
            }
        });

        if (TextUtils.isEmpty(prefs.getString("resp2", ""))) {
            getAllusers(NewBillActivity.this);


        } else {
            mListCustomers = new ArrayList<>();
            mListCustomers = new Gson().fromJson(prefs.getString("resp2", ""), new TypeToken<List<DeliveryBoysBean>>() {
            }.getType());  // startActivity(new Intent(NewBillActivity.this, NewBillActivity.class));

        }

        if (prefs.getString("resp1", "").isEmpty()) {


        } else {
            mListAllItems = new Gson().fromJson(prefs.getString("resp1", ""), new TypeToken<List<ItemMasterhelper>>() {
            }.getType());
        }

        spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!TextUtils.isEmpty(getIntent().getStringExtra("branchname"))) {
                    if (spinner_branch.getSelectedItem().toString().equalsIgnoreCase(getIntent().getStringExtra("branchname"))) {
                        mListAllItems = new Gson().fromJson(prefs.getString("resp1", ""), new TypeToken<List<ItemMasterhelper>>() {
                        }.getType());

                    } else {
                        if (position > 0) {


                            getAllProducts(NewBillActivity.this, branchhash.get(spinner_branch.getSelectedItem().toString()));


                        } else {
                            // getAllProducts(getApplicationContext(), branchhash.get(spinner_branch.getSelectedItem().toString()));
                        }

                    }


                } else {
                    if (position > 0) {


                        getAllProducts(NewBillActivity.this, branchhash.get(spinner_branch.getSelectedItem().toString()));


                    } else {
                        // getAllProducts(getApplicationContext(), branchhash.get(spinner_branch.getSelectedItem().toString()));
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //  myAdapter.notifyDataSetChanged();


                String[] item = filterData(charSequence.toString());
                ArrayAdapter myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_search, item);
                searchItem.setAdapter(myAdapter);
                searchItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // searchItem.setText(item[position]);
                        setAutoData();
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchItem.getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {


                //checkvalue();
            }
        });
        itemAmntT.setFocusable(false);
        itemAmntT.setFocusableInTouchMode(false);
        edtDiscount.setFocusable(false);
        edtDiscount.setFocusableInTouchMode(false);
        edtDiscountP.setFocusable(false);
        edtDiscountP.setFocusableInTouchMode(false);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionUtils.hasPermission(NewBillActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(NewBillActivity.this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(NewBillActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionModule permissionModule = new PermissionModule(NewBillActivity.this);
                    permissionModule.checkPermissions();

                } else {
                    startActivityForResult(new Intent(NewBillActivity.this, Bar.class), 10125);
                }
            }
        });
        edtQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //  myAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    try {
                        if (!edtDiscountP.getText().toString().equalsIgnoreCase("")) {
                            strDiscP = Double.parseDouble(edtDiscountP.getText().toString());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        strDiscP = 0.0;
                        edtDiscountP.setText(strDiscP + "");
                    }

                    Double dblData = (Double.parseDouble(edtQty.getText().toString()) * Double.parseDouble(itemAmnt.getText().toString()));
                    edtDiscount.setText(((dblData * strDiscP) / 100) + "");
                    dblData = dblData - ((dblData * strDiscP) / 100);
                    itemAmntT.setText(dblData + "");


                } catch (Exception e) {
                    e.printStackTrace();
                    //  itemAmntT.setText(Double.parseDouble(mItemMasterhelper.getSaleRate()) + "");
                }

                //checkvalue();
            }
        });
        itemAmnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //  myAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    try {
                        strDiscP = Double.parseDouble(edtDiscountP.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        strDiscP = 0.0;
                        edtDiscountP.setText(strDiscP + "");
                    }

                    Double dblData = (Double.parseDouble(edtQty.getText().toString()) * Double.parseDouble(itemAmnt.getText().toString()));
                    edtDiscount.setText(((dblData * strDiscP) / 100) + "");
                    dblData = dblData - ((dblData * strDiscP) / 100);
                    itemAmntT.setText(dblData + "");


                } catch (Exception e) {
                    e.printStackTrace();
                    //  itemAmntT.setText(Double.parseDouble(mItemMasterhelper.getSaleRate()) + "");
                }

                //checkvalue();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemMasterhelper = new ItemMasterhelper();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem.setText("");
                if (mItemMasterhelper != null && mItemMasterhelper.getItemName() != null) {
                    int a1 = -1;
                    for (int a = 0; a < mList.size(); a++) {
                        if (mList.get(a).getItemID().equalsIgnoreCase(mItemMasterhelper.getItemID())) {
                            a1 = a;
                        }
                    }


                    mItemMasterhelper.setAvailableQty(edtQty.getText().toString());
                    mItemMasterhelper.setSaleUOM(edtDiscount.getText().toString());
                    mItemMasterhelper.setSaleUOMID(edtDiscountP.getText().toString());
                    ArrayList<ItemMasterhelper> mListIt = new ArrayList<>();
                    if (a1 != -1) {
                        mList.get(a1).setAvailableQty(edtQty.getText().toString());
                        if (!itemAmnt.getText().toString().isEmpty()) {
                            mList.get(a1).setSaleRate(itemAmnt.getText().toString());
                            mList.get(a1).setWeight(edtWeight.getText().toString());
                            mList.get(a1).setSaleUOM(edtDiscount.getText().toString());
                            mList.get(a1).setSaleUOMID(edtDiscountP.getText().toString());

                            ItemMasterhelper mHelper = mList.get(a1);
                            mList.remove(a1);
                            mList.add(0, mHelper);
                        }
                    } else {
                        mList.add(0, mItemMasterhelper);
                        mListIt.add(mItemMasterhelper);

                    }
                    //   mBillAdapter.addItems(mListIt);
                    mBillAdapter.notifyDataSetChanged();
                    mBillAdapter = new BillItemsAdapter(NewBillActivity.this, mList, NewBillActivity.this, mSelected);
                    rvBillItems.setAdapter(mBillAdapter);
                    calculate();
                }
                mItemMasterhelper = new ItemMasterhelper();
                edtQty.setText("0");
                itemAmnt.setText("00.00");
                itemAmntT.setText("00.00");
                edtWeight.setText("00");
                searchItem.setText("");
            }
        });
        //selectShop(NewBillActivity.this, true);

        searchCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                Log.e("searchCustomer", searchCustomer.getText().toString());
                String[] item = filterData1(searchCustomer.getText().toString());
                ArrayAdapter myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_search, item);
                searchCustomer.setAdapter(myAdapter);
                searchCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // searchItem.setText(item[position]);
                        //  setAutoData();
                        // txtCustomer.setText("");
                        Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_LONG).show();
                        mList1 = new ArrayList<>();
                        for (int a = 0; a < mListCustomers.size(); a++) {
                            if (searchCustomer.getText().toString().contains(mListCustomers.get(a).getAlartphonenumber())) {
                                mList1.add(mListCustomers.get(a).getAlartphonenumber());
                                // searchCustomer.setText("   " + mListCustomers.get(a).getEmpNameContact() + "(" + mListCustomers.get(a).getAlartphonenumber() + ")   ");
                                strCustId = mListCustomers.get(a).getContactID();
                                strPhoneforotp = mListCustomers.get(a).getAlartphonenumber();
                                Log.d("customerCustid", strCustId);
                            }
                        }

                        //  rvBills.setAdapter(new BillsAdapter(NewBillActivity.this, mList1));


                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchCustomer.getWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        myAdapter.notifyDataSetChanged();
                        // clientDialog.dismiss();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("searchCustomerAfter", searchCustomer.getText().toString());


                //checkvalue();
            }
        });


        imgAddNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMob = searchCustomer.getText().toString();
                if (strMob.isEmpty()) {
                    strMob = "1";
                }
                startActivityForResult(new Intent(NewBillActivity.this, AddCustomerActivity.class).putExtra("mobile", strMob), 22222);
            }
        });
        permission();
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeStoreActivity.class));
        finish();
    }

    int isPermit = 0;

    @AfterPermissionGranted(REQUEST_PERMISSION)
    private void permission() {
        String[] perms = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                "com.pos.permission.SECURITY",
                "com.pos.permission.ACCESSORY_DATETIME",
                "com.pos.permission.ACCESSORY_LED",
                "com.pos.permission.ACCESSORY_BEEP",
                "com.pos.permission.ACCESSORY_RFREGISTER",
                "com.pos.permission.CARD_READER_ICC",
                "com.pos.permission.CARD_READER_PICC",
                "com.pos.permission.CARD_READER_MAG",
                "com.pos.permission.COMMUNICATION",
                "com.pos.permission.PRINTER",
                "com.pos.permission.ACCESSORY_RFREGISTER",
                "com.pos.permission.EMVCORE"
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Already Permission", Toast.LENGTH_SHORT).show();
            try {
                isPermit = 1;
                //   ServiceManager.getInstence().init(getApplicationContext());
                // startActivity(new Intent(NewBillActivity.this, GRNDetail.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // LogUtil.openLog();
        } else {
            isPermit = 0;
            // Do not have permissions, request them now
      /*   try{   EasyPermissions.requestPermissions(
                    new PermissionRequest
                            .Builder(this, REQUEST_PERMISSION, perms)
                            .setRationale("Dear users\n need to apply for storage Permissions for\n your better use of this application")
                            .setNegativeButtonText("NO")
                            .setPositiveButtonText("YES")
                            .build()
            );}catch (Exception e){
             e.printStackTrace();
         }*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Granted", "onRequestPermissionsResult:" + requestCode);
        if (requestCode == 1) {
            try {
                isPermit = 1;
                //   ServiceManager.getInstence().init(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // LogUtil.openLog();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        isPermit = 1;

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        isPermit = 0;
    }


    ArrayList<String> sku = new ArrayList<>();

    public String[] filterData(String searchTerm) {

        sku = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        for (int z = 0; z < mListAllItems.size(); z++) {
            if (mListAllItems.get(z).getItemBarcode().toLowerCase().contains(searchTerm)) {

                result.add(mListAllItems.get(z).getItemName());
                sku.add(mListAllItems.get(z).getItemSKU());
            } else if (mListAllItems.get(z).getItemBarcode().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.add(mListAllItems.get(z).getItemName());
                sku.add(mListAllItems.get(z).getItemSKU());
            } else if (mListAllItems.get(z).getItemName().toLowerCase().contains(searchTerm.toLowerCase())) {

                result.add(mListAllItems.get(z).getItemName());
                sku.add(mListAllItems.get(z).getItemSKU());
            }

        }
        String item[] = result.toArray(new String[result.size()]);
        return item;
    }

    public void onPayClick(View view) {
        getdeliverylist();
        if (mList.size() > 0) {
            finaltaxamount = 0.0f;
            finaltaxrate = 0.0f;
            strSubTotal = dblTotal + "";
            strTotal = dblTotal + "";
            strTotalItems = mList.size() + "";
            strContactId = "66270";
            ArrayList<ItemBean> mListSendData = new ArrayList<>();

            strItems = "";
            try {
                JSONArray jsonArray = new JSONArray();
                for (int a = 0; a < mList.size(); a++) {


                    if (TextUtils.isEmpty(mList.get(a).getStockUOM())) {
                        saleuomint = 0.0f;
                        slaerateclc = 0.0f;
                        finaltaxamount = 0.0f;
                        finaltaxrate = 0.0f;
                        taxamount = 0.0f;
                    } else {
                        saleuomint = Float.parseFloat(mList.get(a).getStockUOM());
                        slaerateclc = Float.parseFloat(mList.get(a).getSaleRate());
                        taxamount = saleuomint * slaerateclc / 100.0f;
                        finaltaxamount = taxamount + finaltaxamount;
                        finaltaxrate = saleuomint + finaltaxrate;

                    }


                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("ItemDesc", mList.get(a).getItemName());
                    jsonObject.put("barcode", mList.get(a).getItemBarcode());
                    jsonObject.put("sku", mList.get(a).getItemSKU());
                    jsonObject.put("itemId", mList.get(a).getItemID());
                    jsonObject.put("Rate", mList.get(a).getSaleRate());
                    jsonObject.put("qty", mList.get(a).getAvailableQty());
                    jsonObject.put("Type", "0");
                    jsonObject.put("ItemTaxRate", mList.get(a).getStockUOM());
                    jsonObject.put("ItemTaxAmount", taxamount.toString());
                    jsonObject.put("Status", "0");
                    jsonObject.put("Discount", mList.get(a).getSaleUOM());
                    jsonObject.put("DetailID", "0");
                    jsonObject.put("RAmount", (Double.parseDouble(mList.get(a).getAvailableQty()) * Double.parseDouble(mList.get(a).getSaleRate())) + "");


                    jsonArray.put(jsonObject);
                    ItemBean itemBean = new ItemBean();
                    itemBean.setItemDesc(mList.get(a).getItemName());
                    itemBean.setBarcode(mList.get(a).getItemBarcode());
                    itemBean.setSku(mList.get(a).getItemSKU());
                    itemBean.setItemId(mList.get(a).getItemID());

                    itemBean.setRate(mList.get(a).getSaleRate());
                    itemBean.setQty(mList.get(a).getAvailableQty());
                    itemBean.setType("0");
                    itemBean.setStatus("0");
                    itemBean.setDiscount(mList.get(a).getSaleUOM());
                    itemBean.setDetailID("0");
                    itemBean.setRAmount((Double.parseDouble(mList.get(a).getAvailableQty()) * Double.parseDouble(mList.get(a).getSaleRate())) + "");
                    mListSendData.add(itemBean);
                }
                strItems = jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            strItems = new Gson().toJson(mListSendData);

            //    finalBill(NewBillActivity.this, mMap.toString());

            dialogBillingDetail(NewBillActivity.this);

        } else {
            //   startService(new Intent(NewBillActivity.this, PrinterService.class));

        }
    }


    Double dblTotal = 0.0;

    public void btnMinusCLick(View view) {
        if (mItemMasterhelper != null && mItemMasterhelper.getItemName() != null) {

            if (edtQty.getText().toString().isEmpty() || Double.parseDouble(edtQty.getText().toString()) < 2) {
                edtQty.setText("1");
            } else {
                edtQty.setText((Double.parseDouble(edtQty.getText().toString()) - 1) + "");
            }
            try {
                itemAmntT.setText((Double.parseDouble(edtQty.getText().toString()) * Double.parseDouble(mItemMasterhelper.getSaleRate())) + "");
            } catch (Exception e) {
                e.printStackTrace();
                itemAmntT.setText(Double.parseDouble(mItemMasterhelper.getSaleRate()) + "");
            }

        }
    }

    Double totalQty = 0.0;
    Double totalDisc = 0.0;

    public void btnAddCLick(View view) {
        if (mItemMasterhelper != null && mItemMasterhelper.getItemName() != null) {

            if (mItemMasterhelper.getWeight() != null) {
                edtWeight.setText(mItemMasterhelper.getWeight());
            } else {
                edtWeight.setText("00");
            }
            if (edtQty.getText().toString().isEmpty()) {
                edtQty.setText("1");
            } else {
                edtQty.setText((Double.parseDouble(edtQty.getText().toString()) + 1) + "");
            }
            try {
                itemAmntT.setText((Double.parseDouble(edtQty.getText().toString()) * Double.parseDouble(mItemMasterhelper.getSaleRate())) + "");
            } catch (Exception e) {
                e.printStackTrace();
                itemAmntT.setText(Double.parseDouble(mItemMasterhelper.getSaleRate()) + "");
            }
        }

    }

    public void calculate() {
        dblTotal = 0.0;
        totalQty = 0.0;
        totalDisc = 0.0;
        for (int a = 0; a < mList.size(); a++) {
            totalDisc = totalDisc + Double.parseDouble(mList.get(a).getSaleUOM());
            totalQty = totalQty + Double.parseDouble(mList.get(a).getAvailableQty());

            try {
                if (Double.parseDouble(mList.get(a).getWeight()) > 0) {
                    dblTotal = dblTotal + (Double.parseDouble(mList.get(a).getWeight()) * Double.parseDouble(mList.get(a).getSaleRate()));
                } else {
                    dblTotal = dblTotal + (Double.parseDouble(mList.get(a).getAvailableQty()) * Double.parseDouble(mList.get(a).getSaleRate()));

                }
            } catch (Exception e) {
                e.printStackTrace();
                dblTotal = dblTotal + (0.0) * Double.parseDouble(mList.get(a).getSaleRate());
            }
        }
        ttlDisc.setText(new DecimalFormat("##.##").format(totalDisc) + "");
        ttlAmt.setText(new DecimalFormat("##.##").format(dblTotal) + "");
        ttlQty.setText(mList.size() + "/" + totalQty);
    }

    @Override
    public void onItemDelete(int position) {
        mList.remove(position);
        calculate();
        mBillAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemUpdate(int position, ItemMasterhelper itemMasterhelper) {
        mItemMasterhelper = new ItemMasterhelper();
        mItemMasterhelper = itemMasterhelper;
        edtQty.setText("0");
        itemAmnt.setText("00.00");
        itemAmntT.setText("00.00");
        edtWeight.setText("00");
        String strPname = searchItem.getText().toString();

        if (mItemMasterhelper != null && mItemMasterhelper.getItemName() != null) {
            edtQty.setText(mItemMasterhelper.getAvailableQty());
            if (mItemMasterhelper.getWeight() != null) {
                edtWeight.setText(mItemMasterhelper.getWeight());
            }
            itemAmnt.setText(mItemMasterhelper.getSaleRate());
            edtDiscount.setText(mItemMasterhelper.getSaleUOM());
            edtDiscountP.setText(mItemMasterhelper.getSaleUOMID());
            itemAmntT.setText((Double.parseDouble(edtQty.getText().toString()) * Double.parseDouble(mItemMasterhelper.getSaleRate())) + "");
            searchItem.setText(mItemMasterhelper.getItemName());
            // edtWeight.setText(mItemMasterhelper.get());


        }


    }


    public void getAllProducts(final Context ctx, String branchid) {
        final ProgressDialog mProgressBar = new ProgressDialog(NewBillActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();


        String strr = "66705";
        //    http://mmthinkbiz.com/MobileService.aspx?method=All_Item_Master=&ContactID=66705&Type=2
        String param = getString(R.string.all_item_api) + "&ContactID=1&Type=2&supplierid=66738";

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);

                        mProgressBar.dismiss();
                        try {

                            Log.e("Response393", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, NewBillActivity.this);


                            JSONObject jsonObject = new JSONObject(responses);

                            if (jsonObject.getInt("success") == 1) {
                                mEditor.putString("resp1", jsonObject.getString("returnds"));
                                mEditor.commit();
                                mListAllItems = new Gson().fromJson(prefs.getString("resp1", ""), new TypeToken<List<ItemMasterhelper>>() {
                                }.getType());  // startActivity(new Intent(NewBillActivity.this, NewBillActivity.class));

                            } else {
//                                getAllProducts(ctx, branchid);
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
                        getAllProducts(ctx, branchid);

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);
                try {

                    // String param = getString(R.string.all_item_api) + "&ContactID=1&Type=2&supplierid=66738";

                    params.put("supplierid", prefs1.getString("shopId", ""));
                    String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=1" + "&Type=" + "0" + "&supplierid=" + "66738" + "&GroupId=" + "&Isfilter=" + "&BranchId=" + branchid;
                    Log.e("BEFORE_PRODUCTS", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, NewBillActivity.this);
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

        Volley.newRequestQueue(ctx).add(postRequest);
    }

    public void getAllCustomers(final Context ctx) {


        String strr = "66705";
        Preferencehelper prefs2 = new Preferencehelper(getApplicationContext());

        String param = getString(R.string.all_cust_api) + "&compId=" + prefs2.getCID();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, param,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);

                        try {


                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("success") == 1) {

                                mListCustomers = new ArrayList<>();
                                mListCustomers = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<Delivery_Option_Adapter>>() {
                                }.getType());  // startActivity(new Intent(NewBillActivity.this, NewBillActivity.class));

                                mEditor.putString("resp2", jsonObject.getString("returnds"));
                                mEditor.commit();
                                // startActivity(new Intent(DashboardActivity.this, NewBillActivity.class));

                            } else {
                                getAllCustomers(ctx);
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
                        getAllCustomers(ctx);

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("", "");
                try {
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


    public void getAllusers(final Context ctx) {
        //     swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(NewBillActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
//        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<com.dhanuka.morningparcel.Helper.ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //      swipeRefresh.setRefreshing(false);
                        Log.e("Response393", response);


//                        mProgressBar.dismiss();

                        try {

                            Log.d("resp_userlst", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, NewBillActivity.this);
                            Log.d("resp_usrlst", responses);
                            JSONObject jsonObject = new JSONObject(responses);

                            if (jsonObject.getInt("success") == 1) {

                                mListCustomers = new ArrayList<>();
                                mListCustomers = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<DeliveryBoysBean>>() {
                                }.getType());

                                mEditor.putString("resp2", jsonObject.getString("returnds"));
                                mEditor.commit();


                                // startActivity(new Intent(NewBillActivity.this, NewBillActivity.class));

                                // startActivity(new Intent(DashboardActivity.this, NewBillActivity.class));

                            } else {
                                getAllusers(ctx);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        mProgressBar.dismiss();

                        // swipeRefresh.setRefreshing(false);
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(NewBillActivity.this);

                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);


                    String param = AppUrls.GET_USER_LIST + "&contactid=" + prefs.getPrefsContactId() + "&companyid=" + prefs.getCID() + "&roleid=1057" + "&searchby=" + "";
                    Log.d("Beforeencrptionuserlst", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, NewBillActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionusrlst", finalparam);
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

    ItemMasterhelper mItemMasterhelper;

    public void setAutoData() {
        mItemMasterhelper = new ItemMasterhelper();
        edtQty.setText("0");
        itemAmnt.setText("00.00");
        itemAmntT.setText("00.00");
        edtWeight.setText("00");
        edtDiscountP.setText(strDiscP + "");
        edtDiscount.setText("0");

        String strPname = searchItem.getText().toString();
        for (int a = 0; a < mListAllItems.size(); a++) {
            if (mListAllItems.get(a).getItemName().equalsIgnoreCase(strPname)) {
                mItemMasterhelper = mListAllItems.get(a);
            }
        }
        if (mItemMasterhelper != null && mItemMasterhelper.getItemName() != null) {
            edtQty.setText("1");
            itemAmnt.setText(mItemMasterhelper.getSaleRate());
            itemAmntT.setText(mItemMasterhelper.getSaleRate());
            edtDiscount.setText(((Double.parseDouble(edtDiscountP.getText().toString()) * Double.parseDouble(mItemMasterhelper.getSaleRate())) / 100) + "");
            // edtWeight.setText(mItemMasterhelper.get());
            int a1 = -1;
            for (int a = 0; a < mList.size(); a++) {
                if (mList.get(a).getItemID().equalsIgnoreCase(mItemMasterhelper.getItemID())) {
                    a1 = a;
                }
            }

            ArrayList<ItemMasterhelper> mListIt = new ArrayList<>();
            if (a1 != -1) {
                edtQty.setText((Double.parseDouble(mList.get(a1).getAvailableQty()) + 1) + "");
                itemAmnt.setText(mList.get(a1).getSaleRate());
                itemAmntT.setText(mList.get(a1).getSaleRate());

                mItemMasterhelper.setSaleUOM(edtDiscount.getText().toString());
                mItemMasterhelper.setSaleUOMID(edtDiscountP.getText().toString());
                mList.get(a1).setAvailableQty(edtQty.getText().toString());
                mList.get(a1).setSaleUOM(edtDiscount.getText().toString());
                mList.get(a1).setSaleUOMID(edtDiscountP.getText().toString());
                mList.get(a1).setSaleRate(itemAmnt.getText().toString());
                mList.get(a1).setWeight(edtWeight.getText().toString());

                ItemMasterhelper mHelper = mList.get(a1);
                mList.remove(a1);
                mList.add(0, mHelper);

            } else {
                mItemMasterhelper.setAvailableQty(edtQty.getText().toString());
                mItemMasterhelper.setSaleUOM(edtDiscount.getText().toString());
                mItemMasterhelper.setSaleUOMID(edtDiscountP.getText().toString());
                mItemMasterhelper.setWeight(edtWeight.getText().toString());
                mList.add(0, mItemMasterhelper);
                mListIt.add(mItemMasterhelper);

            }           //   mBillAdapter.addItems(mListIt);
            mBillAdapter.notifyDataSetChanged();
            mBillAdapter = new BillItemsAdapter(this, mList, this, mSelected);
            rvBillItems.setAdapter(mBillAdapter);
            calculate();


        }
        searchItem.setText("");

        // getData();
    }

    String mTTL = "0.0";
    String mDisc = "0.0";
    HashMap<String, String> mMap;
    ImageView imgCLose;

    TextView txtSTotal;
    TextView txtSubTotal;
    EditText txtADiscount;
    TextView txtDiscount;
    TextView txtTotalSales;
    Button btnPay1, btnPay2;
    Dialog paymentDialog;

    String orderId, catcodeid, catcodedesc, deliverycgarge;
    ArrayList<Payhelper> paylist = new ArrayList<Payhelper>();
    ArrayList<DeliveryHelper> deliverylist, alldeliverylist;
    RecyclerView deliverytypemode, paytypemode;

    public void dialogBillingDetail(final Context ctx) {
        //   EditText shoprecyler;

        paymentDialog = new Dialog(ctx);
        paymentDialog.setContentView(R.layout.dialog_bill_payment);
        //  shoprecyler = clientDialog.findViewById(R.id.shoplistcontainer);
        paymentDialog.setCancelable(false);
        paytypemode = paymentDialog.findViewById(R.id.paytypemode);
        deliverytypemode = paymentDialog.findViewById(R.id.delivtypemode);

        deliverytypemode.setHasFixedSize(true);
        paytypemode.setHasFixedSize(true);
        deliverytypemode.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        paytypemode.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        imgCLose = paymentDialog.findViewById(R.id.imgCLose);
        btnPay1 = paymentDialog.findViewById(R.id.btnPay1);
        btnPay2 = paymentDialog.findViewById(R.id.btnPay2);
        txtSTotal = paymentDialog.findViewById(R.id.txtSTotal);
        txtSubTotal = paymentDialog.findViewById(R.id.txtSubTotal);
        txtADiscount = paymentDialog.findViewById(R.id.txtADiscount);
        txtDiscount = paymentDialog.findViewById(R.id.txtDiscount);
        txtTotalSales = paymentDialog.findViewById(R.id.txtTotalSales);
        txtTotalSales.setText(new DecimalFormat("##.##").format(Double.parseDouble(ttlAmt.getText().toString())));
        txtDiscount.setText(new DecimalFormat("##.##").format(Double.parseDouble(ttlDisc.getText().toString())));
        txtADiscount.setText("00.00");


        try {
            txtSubTotal.setText("" + (Double.parseDouble(txtTotalSales.getText().toString()) - Double.parseDouble(txtDiscount.getText().toString()) - Double.parseDouble(txtADiscount.getText().toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtSTotal.setText(txtSubTotal.getText().toString());

        txtADiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    txtSubTotal.setText("" + (Double.parseDouble(txtTotalSales.getText().toString()) - Double.parseDouble(txtDiscount.getText().toString()) - Double.parseDouble(txtADiscount.getText().toString())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                txtSTotal.setText(txtSubTotal.getText().toString());
            }
        });
        imgCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paymentDialog.dismiss();
            }
        });
        btnPay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Calendar c = Calendar.getInstance();
//                System.out.println("Current time => " + c.getTime());
//
//                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//                String mTodayDate = df.format(c.getTime());
//
//                mTTL = txtSTotal.getText().toString();
//                mDisc = txtDiscount.getText().toString();
////                strContactId = "66705";
//                strContactId = new Preferencehelper(NewBillActivity.this).getPrefsContactId();
//                strContactId = "66705";
//
//                SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
//                        MODE_PRIVATE);
//                mMap = new HashMap<>();
//                mMap.put("OID", "0");
//                mMap.put("CustID", strCustId);
//                mMap.put("CID", strCustId);
//                mMap.put("CreatedBy", strContactId);
//                //66738
//                mMap.put("CompID", "66738");
//                mMap.put("RAmount", txtSTotal.getText().toString());
//                mMap.put("CurrentPaidAmount", txtSTotal.getText().toString());
//                mMap.put("RItemCount", strTotalItems);
//                mMap.put("OrderDate", mTodayDate);
//                mMap.put("RandNumber", "0");
//                mMap.put("Status", "0");
//                mMap.put("OType", "SO");
//                mMap.put("DeliveryMode", "817421");
//                mMap.put("PaymentMode", paymentmode);
//                mMap.put("PayemntType", paymentype);
//
//                mMap.put("PhoneforOTP", searchCustomer.getText().toString());
//                mMap.put("TimeSlotDate", mTodayDate);
//                mMap.put("TimeSLotTime", "BILLING BY APP");
//                mMap.put("PreviousAdjustedAmount", "0");
//                mMap.put("PaymentTxnId", "0");
//                mMap.put("Comment", "0");
//                mMap.put("DeliveryCharge", "0");
//                mMap.put("items", strItems);
//                mMap.put("HeaderTotalTaxAmount", finaltaxamount.toString());
//                mMap.put("HeaderTotalTaxRate", finaltaxrate.toString());
//                Log.e("mMapmMap", mMap.toString());

//                if (strCustId.isEmpty()) {
//                    Toast.makeText(ctx, "Please Select an Customer or add new", Toast.LENGTH_SHORT).show();
//
//                } else {
                makeOrder(ctx, mMap, 1);
                // }
//                }

            }
        });
        btnPay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mMap = new HashMap<>();
//                mMap.put("OID", "0");
//                mMap.put("CustID", strCustId);
//                mMap.put("CID", strCustId);
//                mMap.put("CreatedBy", strContactId);
//                //66738
//                mMap.put("CompID", "66738");
//                //  mMap.put("method", "CreateOrderMaster_Web");
//                //  mMap.put("subTotal", txtSTotal.getText().toString());
//                mMap.put("RAmount", txtSTotal.getText().toString());
//                mMap.put("CurrentPaidAmount", txtSTotal.getText().toString());
//                mMap.put("RItemCount", strTotalItems);
//                mMap.put("OrderDate", mTodayDate);
//                mMap.put("RandNumber", "0");
//                mMap.put("Status", "0");
//                mMap.put("OType", "SO");
//                mMap.put("DeliveryMode", "817421");
//                mMap.put("PaymentMode", paymentmode);
//                mMap.put("PayemntType", paymentype);
//
//                mMap.put("PhoneforOTP", searchCustomer.getText().toString());
//                mMap.put("TimeSlotDate", mTodayDate);
//                mMap.put("TimeSLotTime", "BILLING BY APP");
//                mMap.put("PreviousAdjustedAmount", "0");
//                mMap.put("PaymentTxnId", "0");
//                mMap.put("Comment", "0");
//                mMap.put("DeliveryCharge", "0");
//                mMap.put("items", strItems);
//                mMap.put("HeaderTotalTaxAmount", finaltaxamount.toString());
//                mMap.put("HeaderTotalTaxRate", finaltaxamount.toString());
//                Log.e("mMapmMap", mMap.toString());
//                if (strCustId.isEmpty()) {
//                    Toast.makeText(ctx, "Please Select an Customer or add new", Toast.LENGTH_SHORT).show();
//
//                } else {
                // if (isPermit == 1) {
                makeOrder(ctx, mMap, 1);
                //   }
//                }

            }
        });

        paymentDialog.show();
    }

    ArrayList<DeliveryBoysBean> mListCustomers = new ArrayList<>();
    ArrayList<String> mList1 = new ArrayList<>();


    public String[] filterData1(String searchTerm) {

        sku = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        for (int z = 0; z < mListCustomers.size(); z++) {
            if (mListCustomers.get(z).getAlartphonenumber().toLowerCase().contains(searchTerm) || mListCustomers.get(z).getFirstName().toLowerCase().contains(searchTerm) || mListCustomers.get(z).getFlatNo().toLowerCase().contains(searchTerm)) {
                Log.d("Customerid", mListCustomers.get(z).getContactID());

                if (mListCustomers.get(z).getFirstName().isEmpty()) {
                    mListCustomers.get(z).setFirstName("No name");
                }
                if (mListCustomers.get(z).getFlatNo().equalsIgnoreCase("") || mListCustomers.get(z).getFlatNo().isEmpty()) {
                    mListCustomers.get(z).setFlatNo("0");

                }
                if (mListCustomers.get(z).getAlartphonenumber().equalsIgnoreCase("") || mListCustomers.get(z).getAlartphonenumber().isEmpty()) {
                    mListCustomers.get(z).setAlartphonenumber("0");

                }


                if (!mListCustomers.get(z).getFirstName().isEmpty() && !mListCustomers.get(z).getFlatNo().isEmpty() && !mListCustomers.get(z).getAlartphonenumber().isEmpty()) {
                    result.add(mListCustomers.get(z).getFirstName() + "( " + mListCustomers.get(z).getAlartphonenumber() + " ) " + "  \nFlat No :" + mListCustomers.get(z).getFlatNo());
                }

            } /*else if (mListCustomers.get(z).getEmpPhoneContact().toLowerCase().contains(searchTerm)) {

                result.add(mListCustomers.get(z).getEmpPhoneContact());
            }*/
        }
        Log.e("resultsize", result.size() + "");
        Log.e("resultsize", result.size() + "");
        String item[] = result.toArray(new String[result.size()]);
        return item;
    }

    public void makeOrder(final Context ctx, final HashMap<String, String> map, int type) {


        final ProgressDialog mProgressBar = new ProgressDialog(NewBillActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();


        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);
                        // Toast.makeText(ctx, "" + response, Toast.LENGTH_SHORT).show();
                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();

                            String responses = jkHelper.Decryptapi(response, getApplicationContext());


                            JSONObject jsonObject = new JSONObject(responses);

                            if (jsonObject.getInt("success") == 1) {

                                //    {"success":"1","returnds":[{"OrderID":"957","successmsg":"Successful"}]}

                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String oID = jsonObject1.getString("OrderID");
                                Toast.makeText(ctx, "" + oID, Toast.LENGTH_LONG).show();
                                String strOID = "";
                                if (oID.length() < 5) {
                                    for (int a = 0; a < (5 - oID.length()); a++) {
                                        strOID = strOID + "0";
                                    }
                                }
                                strOID = strOID + oID;

                                String currentTime = new SimpleDateFormat("HH:mm a, dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                                String str[] = currentTime.split("-");
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("                                ");
                                stringBuilder.append("     SAFEOKART SUPERMARKET      ");
                                stringBuilder.append("  R-903 M3M Urbana, Golf Course ");
                                stringBuilder.append("  Ext. Road sector 67, Gurugram ");
                                stringBuilder.append("     GSTIN: 06AADCD9450B1ZY     ");
                                stringBuilder.append("       PH. No. 8826000390       ");
                                stringBuilder.append("--------------------------------\n");
                                stringBuilder.append("TIME/DATE:" + currentTime + " ");
                                stringBuilder.append("BILL: SAF/" + mTTL + "/" + strOID + "\n");
                                stringBuilder.append(" \n");
                                stringBuilder.append("--------------------------------");
                                //    stringBuilder.append("TAX  INVOICE CUM  BILL OF SUPPLY\n");
                                stringBuilder.append("ITEM         RATE    QTY   TOTAL\n");
                                stringBuilder.append("-------------------------------\n");

                                double grandTTL = 0.0;
                                double grndTTLITEM = 0.0;
                                for (int a = 0; a < mList.size(); a++) {
                                    String pName = mList.get(a).getItemName();
                                    String pPrice = mList.get(a).getSaleRate();
                                    String pQTY = mList.get(a).getAvailableQty();
                                    if (pName.length() < 11) {
                                        for (int b = 0; b < (11 - pName.length()); b++) {

                                            pName = pName + " ";
                                        }
                                    } else {
                                        pName = pName.substring(0, 10);
                                    }
                                    if (pPrice.length() < 8) {
                                        for (int b = 0; b < (8 - pPrice.length()); b++) {

                                            pPrice = pPrice + " ";
                                        }
                                    } else {
                                        pPrice = pPrice.substring(0, 8);
                                    }
                                    if (pQTY.length() < 4) {
                                        for (int b = 0; b < (4 - pQTY.length()); b++) {

                                            pQTY = pQTY + " ";
                                        }
                                    } else {
                                        pQTY = pQTY.substring(0, 4);
                                    }
                                    double ttlP = 0.0;
                                    try {
                                        if (Double.parseDouble(mList.get(a).getWeight()) > 0) {
                                            ttlP = Double.parseDouble(mList.get(a).getSaleRate()) * Double.parseDouble(mList.get(a).getWeight());
                                        } else {
                                            ttlP = Double.parseDouble(mList.get(a).getSaleRate()) * Double.parseDouble(mList.get(a).getAvailableQty());

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        ttlP = Double.parseDouble(mList.get(a).getSaleRate()) * Double.parseDouble("0.0");
                                    }
                                    grndTTLITEM = Double.parseDouble(mList.get(a).getAvailableQty()) + grndTTLITEM;
                                    grandTTL = grandTTL + ttlP;
                                    String strttlP = "" + ttlP;
                                    if (strttlP.length() < 5) {
                                        for (int b = 0; b < (5 - strttlP.length()); b++) {

                                            strttlP = " " + strttlP;
                                        }
                                    } else {
                                        strttlP = strttlP.substring(0, 5);
                                    }
                                    stringBuilder.append(pName + " " + pPrice + " " + pQTY + " " + strttlP + " \n");
                                }
                                String strgrandTTL = "" + grandTTL;
                                if (strgrandTTL.length() < 8) {
                                    for (int b = 0; b < (8 - strgrandTTL.length()); b++) {

                                        strgrandTTL = " " + strgrandTTL;
                                    }
                                } else {
                                    strgrandTTL = strgrandTTL.substring(0, 8);
                                }
                                for (int a = 0; a < (7 - mDisc.length()); a++) {
                                    mDisc = " " + mDisc;
                                }
                                for (int a = 0; a < (7 - mTTL.length()); a++) {
                                    mTTL = " " + mTTL;
                                }
                                String strQTY = mList.size() + "/" + grndTTLITEM;
                                for (int a = 0; a < (15 - strQTY.length()); a++) {
                                    strQTY = strQTY + " ";
                                }


                                stringBuilder.append("--------------------------------\n");
                                stringBuilder.append("SUB TOTAL                " + strgrandTTL + "\n");
                                stringBuilder.append("DISCOUNT                 " + mDisc + "\n");
                                stringBuilder.append("TOTAL                    " + mTTL + "\n");
                                stringBuilder.append("ITEM(s)/QTY  : " + strQTY + "\n");
                                stringBuilder.append("*******************************\n");
                                stringBuilder.append("TAX DESC.       TAXABLE      TAX\n");
                                stringBuilder.append("CGST@0.00%     " + strgrandTTL + "     0.00\n");
                                stringBuilder.append("SGST@0.00%     " + strgrandTTL + "     0.00\n");
                                stringBuilder.append("-------------------------------\n");
                                stringBuilder.append("TOTAL         " + strgrandTTL + "     00.00\n");
                                //stringBuilder.append("CASH                   " + strgrandTTL + "\n");
                                stringBuilder.append("*******************************\n");
                                stringBuilder.append("     THANK YOU VISIT AGAIN      \n");
                                stringBuilder.append("                                \n");
                                stringBuilder.append("                                \n");
                                stringBuilder.append("                                \n");
                                stringBuilder.append("    SAF/" + mTTL + "/" + strOID + "              \n");
                                //     stringBuilder.append("                                \n");
                                //   stringBuilder.append("                                \n");
                                stringBuilder.append("                               \n");
                                mEditor.putString("mValues", stringBuilder.toString());

                                mTTL = mTTL.replace(" ", "");
                                Log.e("mTTL", mTTL);
                                if (mTTL.contains(".")) {
                                    String[] arrSplittedAmt = mTTL.split("\\.");
                                    mTTL = arrSplittedAmt[0];
                                }
                                try {
                                    mTTL = Integer.parseInt(mTTL) + "";
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(ctx, "Unable to parse the value", Toast.LENGTH_SHORT).show();

                                }
                                if (mTTL.length() < 4) {
                                    for (int a = 0; a < (4 - mTTL.length()); a++) {
                                        mTTL = "0" + mTTL;
                                    }
                                }

                                mEditor.putString("barcode", "SAF/" + mTTL + "/" + strOID);
                                mEditor.commit();
                                if (isPermit == 1) {

                                    print_bill(stringBuilder.toString(), "SAF/" + mTTL + "/" + strOID);
                                }

                                //    ctx.startService(new Intent(ctx, PrinterService.class));
                                finalBill(NewBillActivity.this, oID);


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

                Map<String, String> params = new HashMap<String, String>();

                mTTL = txtSTotal.getText().toString();
                mDisc = txtDiscount.getText().toString();
                strContactId = new Preferencehelper(NewBillActivity.this).getPrefsTempContactid();
                SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());


                String param = getString(R.string.CREATE_MASTER_ORDER) + "&OID=" + "0" + "&CustID=" + strCustId + "&CID=" + strCustId + "&CreatedBy=" + strContactId + "&CompID=" + new Preferencehelper(NewBillActivity.this).getCID() + "&RAmount=" + txtSTotal.getText().toString() + "&CurrentPaidAmount=" + txtSTotal.getText().toString() +
                        "&RItemCount=" + strTotalItems + "&OrderDate=" + mTodayDate + "&RandNumber=" + "0" + "&Status=" + "10" + "&OType=" + "POSM" + "&DeliveryMode=" + "817421" + "&PaymentMode=" + paymentmode + "&PaymentType=" + paymentype + "&PhoneforOTP=" + strPhoneforotp + "&TimeSlotDate=" + mTodayDate + "&TimeSLotTime=" + "BILLING BY APP" +
                        "&PreviousAdjustedAmount=0" + "&PaymentTxnId=0" + "&Comment=0" + "&DeliveryCharge=" + "0" + "&items=" + strItems + "&HeaderTotalTaxAmount=" + finaltaxamount.toString() + "&HeaderTotalTaxRate=" + finaltaxrate.toString() + "&Type=0" + "&BranchId=" + branchhash.get(spinner_branch.getSelectedItem().toString());
                Log.d("Beforeencrption", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                params.put("val", finalparam);
                Log.d("afterencrption", finalparam);
                return params;


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ctx).add(postRequest);
    }


    public void print_bill(String mValue, String strbar) {
       /* PrinterClass printerClass = new PrinterClass(NewBillActivity.this);
        printerClass.printerInit();
        printerClass.centerAlignedPrintText(mValue.toString(), TextPrintLine.FONT_NORMAL, false);
        printerClass.paperFeed(2, TextPrintLine.FONT_SMALL);
        printerClass.centerAlignedPrintText("THANKYOU", TextPrintLine.FONT_SMALL, true);//48 Chars
        printerClass.paperFeed(2);

        printerClass.printerInit();
        printerClass.printBarcode(strbar);
        printerClass.paperFeed(3);
*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 22222) {
                strCustId = data.getStringExtra("custId");
                searchCustomer.setText(data.getStringExtra("mobileNumber"));


            } else if (requestCode == 10125) {
                try {
                    String strEditText = data.getStringExtra("mBar");
                    //searchItem.setText(strEditText);
                    barcodescaneResult(strEditText);
                    // loaditemmaster(et_ItemBarcode.getText().toString());
//  checkBarcode(strEditText, 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    Dialog clientDialog;
    Button newBill;
    ImageView imgCLose1;
    TextView data;

    public void finalBill(final Context ctx, String mData) {

        AutoCompleteTextView searchCustomer;
        clientDialog = new Dialog(ctx);
        clientDialog.setContentView(R.layout.dialog_final_bill);
        //  shoprecyler = clientDialog.findViewById(R.id.shoplistcontainer);
        clientDialog.setCancelable(false);
        imgCLose1 = clientDialog.findViewById(R.id.imgCLose);
        data = clientDialog.findViewById(R.id.data);
        newBill = clientDialog.findViewById(R.id.newBill);

        newBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, NewBillActivity.class).putExtra("branchname", spinner_branch.getSelectedItem().toString()));

                clientDialog.dismiss();
            }
        });
        data.setText("Order ID : " + mData + "\nYour order generated successfully");

        clientDialog.show();
    }

    public void getdeliverylist() {

        Log.e("dsfkjjfk", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(NewBillActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            Log.e("responses123", responses);
                            JSONObject jsonObject = new JSONObject(responses);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                deliverylist = new ArrayList<DeliveryHelper>();
                                alldeliverylist = new ArrayList<>();
                                deliverylist.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    catcodeid = jsonObject1.getString("CatCodeID");
                                    catcodedesc = jsonObject1.getString("CodeDescription");
                                    DeliveryHelper deliveryHelper2 = new DeliveryHelper();
                                    deliveryHelper2.setDeliveryid(catcodeid);
                                    deliveryHelper2.setDeliverytype(catcodedesc);
                                    alldeliverylist.add(deliveryHelper2);

                                    // if (jsonObject1.getString("Val1").equalsIgnoreCase(mData.getString("shopId", "1"))) {
                                    catcodeid = jsonObject1.getString("CatCodeID");
                                    catcodedesc = jsonObject1.getString("CodeDescription");
                                    DeliveryHelper deliveryHelper = new DeliveryHelper();
                                    if (deliverylist.size() > 0) {


                                        for (int l = 0; l < alldeliverylist.size(); l++) {
                                            if (alldeliverylist.get(i).getDeliveryid().equalsIgnoreCase("")) {

                                                catcodeid = alldeliverylist.get(i).getDeliveryid();
                                                catcodedesc = alldeliverylist.get(i).getDeliverytype();
                                                DeliveryHelper deliveryHelper1 = new DeliveryHelper();
                                                deliveryHelper1.setDeliveryid(catcodeid);
                                                deliveryHelper1.setDeliverytype(catcodedesc);
                                                deliverylist.add(deliveryHelper);
                                            }


                                        }


                                    } else {
                                        deliveryHelper.setDeliveryid(catcodeid);
                                        deliveryHelper.setDeliverytype(catcodedesc);
                                        deliverylist.add(deliveryHelper);


                                    }
                                    //}


                                }


                                deliverytypemode.setAdapter(new Delivery_Option_Adapter(getApplicationContext(), deliverylist, NewBillActivity.this, NewBillActivity.this));

//}

                            } else {

                                FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(getApplicationContext(), "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());

                SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);


                com.dhanuka.morningparcel.Helper.Preferencehelper prefs;
                prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());

                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "112" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=" + prefs.getCID();
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                    params.put("val", finalparam);
                    Log.d("afterencrption", finalparam);
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

    public void getpaylist(String delivyvalid) {

        Log.e("dsfkjjfk11", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(NewBillActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsivepaylist", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            Log.e("otherresponses123", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                paylist.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    Log.d("valsss", jsonObject1.getString("Val1") + " = " + delivyvalid);

                                    if (jsonObject1.getString("Val1").equalsIgnoreCase(delivyvalid)) {
                                        catcodeid = jsonObject1.getString("CatCodeID");
                                        catcodedesc = jsonObject1.getString("CodeDescription");
                                        Payhelper payhelper = new Payhelper();
                                        payhelper.setPayid(catcodeid);
                                        payhelper.setStringpaytype(catcodedesc);
                                        paylist.add(payhelper);
                                        Log.e("OKOK", "" + catcodedesc);
                                    }

                                }
                                Log.e("HJHJJH", new Gson().toJson(paylist));
                                paytypemode.setAdapter(new Payment_Option_Adapter(getApplicationContext(), paylist, (CartItemsadd) NewBillActivity.this));

//}

                            } else {

                                FancyToast.makeText(NewBillActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(NewBillActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());

                SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);


                com.dhanuka.morningparcel.Helper.Preferencehelper prefs;
                prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(NewBillActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "113" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=" + prefs.getCID();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, NewBillActivity.this);
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

        Volley.newRequestQueue(NewBillActivity.this).add(postRequest);


    }


    @Override
    public void sendtimeslotval(String res, String delverrate) {

    }

    @Override
    public void sendpayval(Payhelper res) {

        paymentmode = res.getPayid();
        paymentype = res.getStringpaytype();
        if (paymentype.equalsIgnoreCase("COD")) {
            mstatus = "10";
        } else {
            mstatus = "0";
        }
        Toast.makeText(getApplicationContext(), paymentype, Toast.LENGTH_LONG).show();

    }

    @Override
    public void senddeliveryval(DeliveryHelper res) {
        getpaylist(res.getDeliveryid());

    }

    @Override
    public void selectaddress(DeliveryHelper type) {

    }


    public void getReports() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responseWEEK", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.d("responseWEEKNew", responses);

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

                                if (!TextUtils.isEmpty(getIntent().getStringExtra("branchname"))) {
                                    spinner_branch.setSelection(branchadapter.getPosition(getIntent().getStringExtra("branchname")));

                                }
//                                if (prefs.getString("resp1", "").isEmpty()) {
//                                    getAllProducts(NewBillActivity.this, branchhash.get(spinner_branch.getSelectedItem().toString()));
//
//                                } else {
//                                    mListAllItems = new Gson().fromJson(prefs.getString("resp1", ""), new TypeToken<List<ItemMasterhelper>>() {
//                                    }.getType());
//                                }


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
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                Preferencehelper prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());
                ;
                try {
                    String param = getString(R.string.URL_GET_BRANCH) + "&contactid=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, NewBillActivity.this);
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

}