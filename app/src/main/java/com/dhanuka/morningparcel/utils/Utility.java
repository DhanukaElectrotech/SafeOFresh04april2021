package com.dhanuka.morningparcel.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.ShoplistHelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.activity.HomeActivity;
import com.dhanuka.morningparcel.adapter.ShopListAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.customViews.TouchImageView;
import com.dhanuka.morningparcel.database.DatabaseManager;

import com.google.android.material.tabs.TabLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Utility {


    public static void addMoneyToWallet(Context ctx, String strAmt, String strTxnId, String strType) {


        Preferencehelper prefs = new Preferencehelper(ctx);


        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseSignup", response);


                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ctx);
                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {
                                if (strType.equalsIgnoreCase("3")) {
                                    prefs.setPREFS_currentbal((Double.parseDouble(prefs.getPREFS_currentbal()) + Double.parseDouble(strAmt)) + "");
                                } else {
                                    //  prefs.setPREFS_currentbal((Double.parseDouble(prefs.getPREFS_currentbal()) - Double.parseDouble(strAmt)) + "");

                                }
                            } else {
                                addMoneyToWallet(ctx, strAmt, strTxnId, strType);
                            }

                        } catch (Exception e) {
                            //mProgressBar.dismiss();
                            addMoneyToWallet(ctx, strAmt, strTxnId, strType);

                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        addMoneyToWallet(ctx, strAmt, strTxnId, strType);


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String servicetype;

                try {
//method=CreateWalletTransaction_Web&Status=0&Type=1&=66280&=10&=bill1&=1&TransactionDate=08-11-2020
                    JKHelper jkHelper = new JKHelper();
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                    String mTodayDate = df.format(c.getTime());

                    String param = "method=CreateWalletTransaction&Status=0&Type=" + strType + "&Contactid=" + prefs.getPrefsContactId() + "&Amount=" + strAmt + "&TransactionDate=" + mTodayDate + "&BillNo=" + strTxnId + "&CreatedBy=" + prefs.getPrefsContactId();
                    Log.e("beforeenc", param);
                    param = jkHelper.Encryptapi(param, ctx);
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

        Volley.newRequestQueue(ctx).add(postRequest);


    }


    Context ctx;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showCenterToast(Context mContext, int type, String msg) {
        FancyToast.makeText(mContext, msg, FancyToast.LENGTH_SHORT, type, false).show();
    }

    public static void strikeText(TextView mTextView, String msg) {
        mTextView.setText(msg);
        mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void strikeTextWithHtml(TextView mTextView, String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTextView.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
        } else {
            mTextView.setText(Html.fromHtml(msg));
        }
        mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void textWithHtml(TextView mTextView, String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTextView.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
        } else {
            mTextView.setText(Html.fromHtml(msg));
        }
    }

    static ShopListAdapter shopListAdapter;
    public static DatabaseManager dbManager;
    static EditText etSearch;
    static ArrayList<ShoplistHelper> shoplist = new ArrayList<>();

    public static void filter(String text) {
        ArrayList<ShoplistHelper> filteredList = new ArrayList<>();

        for (ShoplistHelper product : shoplist) {
            if (product.getShopname().toLowerCase().contains(text) || product.getAlartphonenumber().toLowerCase().contains(text)) {
                filteredList.add(product);
            }
        }

        shopListAdapter.filterList(filteredList);
    }

    static int mmtype = 0;

    double PreviousAdjustedAmount = 0.0;
    int checkoutTime = 0;


    public static void checkoutOrder(CartProduct cartProduct, String contactid, Context context, String type) {


        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, context);
                            Log.e("delivresponses123", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]


                                //   com.dhanuka.morningparcel.beans.FancyToast.makeText(context, "Order Service Successfully.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.SUCCESS, false).show();

                                // context.startActivity(new Intent(context, OrderSuccessActivity.class).putExtra("orderId", orderId).putExtra("totalamount", txtPaybleAm1.getText().toString()));

                                //  checkoutOrderDetails();
//}

                            } else {

                                //       com.dhanuka.morningparcel.beans.FancyToast.makeText(context, "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        com.dhanuka.morningparcel.beans.FancyToast.makeText(context, "Something went wrong with checkout.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(context);
                Map<String, String> params = new HashMap<String, String>();

                float finaltaxamount = 0.0f, finaltaxrate = 0.0f, finalTaxrate = 0.0f, finalTaxamount = 0.0f;


                try {

                    if (cartProduct.getStockUOM().equalsIgnoreCase("") || cartProduct.getStockUOM().isEmpty())
                    {
                        cartProduct.setStockUOM("0");
                    }


                    if (type.equalsIgnoreCase("1"))
                    {
                        finaltaxrate = Float.parseFloat(cartProduct.getStockUOM());
                    }
                    else
                    {
                        finaltaxrate = 0.0f;

                    }



                    SharedPreferences mData = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                            context.MODE_PRIVATE);
                    String Ramount, Discount;
                    if (cartProduct.getRAmount() == null) {
                        Ramount = String.valueOf(Float.parseFloat(cartProduct.getSaleRate()) * cartProduct.getQuantity());
                    } else {
                        Ramount = String.valueOf(Integer.parseInt(cartProduct.getSaleRate()) * cartProduct.getQuantity());

                    }
                    if (cartProduct.getDiscount() == null) {
                        Discount = "0";
                    } else {
                        Discount = cartProduct.getDiscount();

                    }


                    String param = context.getString(R.string.CREATE_MASTER_CARTITEMS) + "&OID=" + "0" + "&CID=" + prefs.getPrefsContactId()
                            + "&DetailID=" + "0" + "&ItemID=" + cartProduct.getItemID() + "&ItemDesc=" + cartProduct.getItemName()
                            + "&RQty=" + cartProduct.getQuantity() + "&UOM=" + Ramount
                            + "&Rate=" + cartProduct.getSaleRate() + "&RAmount=" + Ramount + "&Status=" + "500" + "&Type=" + type + "&RefOrderID=" + "0" + "&BarCode=" + cartProduct.getItemBarcode()
                            + "&MRP=" + cartProduct.getMRP() + "" + "&Discount=" + Discount + "" + "&Val1=" + "" + "&Val2=" +
                            "" + "&CompanyID=" + prefs.getCID() + "&CustomerID=" + contactid + "&RItemCount=" + "1" + "&EntryFrom=2" + "&CreatedBy=" + contactid + "&ItemTaxAmount=" + finaltaxamount + "&ItemTaxRate=" + finaltaxrate;
                    ;
                    Log.d("BeforeencrptionCHECKOUT", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, context);
                    params.put("val", finalparam);
                    Log.d("afterencrptionCHECKOUT", finalparam);
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

        Volley.newRequestQueue(context).add(postRequest);


    }

    public static void selectShop(Context ctx, int type, boolean isCancelable/*, OnStoreSelectListener onStoreSelectListener*/) {
        mmtype = type;
        //mOnStoreSelectListener=onStoreSelectListener;
        Dialog Shoplistdialog;
        RecyclerView shoprecyler;
        SharedPreferences prefs = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                ctx.MODE_PRIVATE);

        dbManager = DatabaseManager.getInstance(ctx);
        Shoplistdialog = new Dialog(ctx, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        // Shoplistdialog = new Dialog(ctx);
        Shoplistdialog.setContentView(R.layout.shoplist_layout);
        shoprecyler = Shoplistdialog.findViewById(R.id.shoplistcontainer);
        etSearch = Shoplistdialog.findViewById(R.id.etSearch);
        TextView btnSubmit = Shoplistdialog.findViewById(R.id.btnSubmit);
        TextView txtTtl = Shoplistdialog.findViewById(R.id.txtTtl);
        shoprecyler.setHasFixedSize(true);
        if (prefs.getString("shopId", "").isEmpty()) {
            Shoplistdialog.setCancelable(isCancelable);
        } else {
            Shoplistdialog.setCancelable(true);

        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shopListAdapter != null) {
                    filter(s.toString());
                }
            }
        });
        shoprecyler.setLayoutManager(new GridLayoutManager(ctx, 2));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String shopId = shopListAdapter.getShop();
                    String shopName = shopListAdapter.getShopName();
                    String DeliveryCharge = shopListAdapter.getDeliveryCharge();
                    String ServiceFees = shopListAdapter.getServiceFees();
                    String CheckOutMessage = shopListAdapter.getCheckOutMessage();
                    String MaxOrderAmt = shopListAdapter.getMaxOrderAmt();
                    String MinOrderAmt = shopListAdapter.getMinOrderAmt();
                    String discount = shopListAdapter.getDiscount();
                    if (!shopId.isEmpty()) {
                       /* if (prefs.getString("shopId", "").isEmpty()) {
                            getAllProducts(ctx, shopId, Shoplistdialog);
                        } else {*/
                        if (prefs.getString("shopId", "").equalsIgnoreCase(shopId)) {
                              /*  if (type == 1) {
                                    ctx.startActivity(new Intent(ctx, HomeActivity.class));
                                }*/
                            Shoplistdialog.dismiss();
                        } else {
                            new AlertDialog.Builder(ctx)
                                    .setTitle("Safe'O'Fresh")
                                    .setMessage("If there is item in your cart than your cart will be cleared if you change the store. Still want to change the store?")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            SharedPreferences prefs = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                                                    ctx.MODE_PRIVATE);
                                            SharedPreferences.Editor mEditor = prefs.edit();
                                            mEditor.putString("shopId", shopId);
                                            mEditor.putString("shopName", shopName);
                                            mEditor.putString("DeliveryCharge", DeliveryCharge);
                                            mEditor.putString("ServiceFees", ServiceFees);
                                            mEditor.putString("CheckOutMessage", CheckOutMessage);
                                            mEditor.putString("MaxOrderAmt", MaxOrderAmt);
                                            mEditor.putString("MinOrderAmt", MinOrderAmt);
                                            mEditor.putString("discount", discount);
                                            mEditor.commit();
                                            //   mOnStoreSelectListener.
                                            shopListAdapter.getShop1();

                                            Log.e("MKJGH", type + "");
                                            if (dbManager != null) {
                                                dbManager.deleteAll();
                                            }
                                            getAllProducts(ctx, shopId, Shoplistdialog);


                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Shoplistdialog.dismiss();
                                            dialog.dismiss();
                                            //  Toast.makeText(mActivity, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                                        }
                                    })

                                    .show();
                        }


                        // Shoplistdialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            JSONObject jsonObject = new JSONObject(prefs.getString("shopss", ""));
            int success = jsonObject.getInt("success");
            //  Log.e("success" + success);
            Log.d("success", String.valueOf(+success));
            if (success == 1) {

                JSONArray jsonArray = jsonObject.getJSONArray("AssignVehicle");

                shoplist = new ArrayList<ShoplistHelper>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject singleObj = jsonArray.getJSONObject(i);
                    String consignee = singleObj.getString("BranchName");
                    String deviceId = singleObj.getString("BranchId");
                    String Alartphonenumber = singleObj.getString("Alartphonenumber");
                    String PhonePe = singleObj.getString("PhonePe");
                    String PaytmNumber = singleObj.getString("PaytmNumber");
                    String GooglePay = singleObj.getString("GooglePay");
                    String Currency = singleObj.getString("Currency");
                    String DeliveryCharge = singleObj.getString("DeliveryCharge");
                    String ServiceFees = singleObj.getString("ServiceFees");
                    String CheckOutMessage = singleObj.getString("CheckOutMessage");
                    String MaxOrderAmt = singleObj.getString("MaxOrderAmt");
                    String MinOrderAmt = singleObj.getString("MinOrderAmt");
                    String filepath = singleObj.getString("filepath");
                    String ImageName = singleObj.getString("ImageName");
                    String Discount = singleObj.getString("Discount");
                    String Tax = singleObj.getString("Tax");


                    ShoplistHelper v = new ShoplistHelper();
                    v.setAlartphonenumber(Alartphonenumber);
                    v.setPhonePe(PhonePe);
                    v.setMinOrderAmt(MinOrderAmt);
                    v.setMaxOrderAmt(MaxOrderAmt);
                    v.setPaytmNumber(PaytmNumber);
                    v.setCheckOutMessage(CheckOutMessage);
                    v.setGooglePay(GooglePay);
                    v.setShopid(deviceId);
                    v.setShopname(consignee);
                    v.setCurrency(Currency);
                    v.setDeliveryCharge(DeliveryCharge);
                    v.setServiceFees(ServiceFees);
                    v.setImageName(ImageName);
                    v.setFilepath(filepath);
                    v.setDiscount(Discount);
                    v.setTax(Tax);
                    shoplist.add(v);

                }
                //    shopListAdapter = new ShopListAdapter(ctx, shoplist);
                shoprecyler.setAdapter(shopListAdapter);


                //Message.message(ctx, "Data fetched Successfuly");
            } else if (success == 0) {
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        Shoplistdialog.show();
    }

    public static void Dialog_Confirmation(Context ctx, String imgStr) {


        Dialog dialog_confirmation = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);

        dialog_confirmation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_confirmation.setContentView(R.layout.dialog_full_view_image);
        Window window = dialog_confirmation.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog_confirmation.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        TouchImageView img = (TouchImageView) dialog_confirmation.findViewById(R.id.img);
        Picasso.with(ctx).load(imgStr).placeholder(R.drawable.no_image).into(img);
        ImageView imgClose = (ImageView) dialog_confirmation.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirmation.dismiss();
            }
        });
        img.setMaxZoom(4f);

        dialog_confirmation.setCanceledOnTouchOutside(true);
        dialog_confirmation.setCancelable(true);
      /*  adapter = new ViewPagerAdapterstd(ctx, imgList);
        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getItems(posi), true);
*/

        dialog_confirmation.show();
    }


    public static void opePaymentInfo(Context ctx, String shopId) {
        Dialog Shoplistdialog;
        RecyclerView shoprecyler;
        SharedPreferences prefs = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                ctx.MODE_PRIVATE);

        Shoplistdialog = new Dialog(ctx);
        Shoplistdialog.setContentView(R.layout.payment_dialog);

        TextView txtPaytm = Shoplistdialog.findViewById(R.id.txtPaytm);
        TextView txtGooglePay = Shoplistdialog.findViewById(R.id.txtGooglePay);
        TextView txtPhonePe = Shoplistdialog.findViewById(R.id.txtPhonePe);

        try {
            JSONObject jsonObject = new JSONObject(prefs.getString("shopss", ""));
            int success = jsonObject.getInt("success");
            if (success == 1) {

                JSONArray jsonArray = jsonObject.getJSONArray("AssignVehicle");

                final ArrayList<ShoplistHelper> shoplist = new ArrayList<ShoplistHelper>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject singleObj = jsonArray.getJSONObject(i);

                    if (shopId.equalsIgnoreCase(singleObj.getString("BranchId"))) {
                        txtPaytm.setText(singleObj.getString("PaytmNumber"));
                        txtGooglePay.setText(singleObj.getString("GooglePay"));
                        txtPhonePe.setText(singleObj.getString("PhonePe"));
                    }

                }

                //Message.message(ctx, "Data fetched Successfuly");
            } else if (success == 0) {
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        Shoplistdialog.show();
    }

    static String shopeId;

    public static void getAllProducts(Context ctx, String strStop, Dialog mDIalog) {
        shopeId = strStop;
        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                ctx.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);

                        mProgressBar.dismiss();
                        try {


                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ctx);

                            mEditor.putString("resp1", responses);
                            mEditor.commit();

                            String res = responses;

                            if (mDIalog != null) {
                                shopListAdapter.getShop1();
                                mDIalog.dismiss();
                            }
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
                                    String ItemSKU = newjson.getString("ItemSKU");
                                    String ItemBarcode = newjson.getString("ItemBarcode");
                                    String StockUOM = newjson.getString("StockUOM");
                                    String ItemImage = newjson.getString("ItemImage");
                                    String HSNCode = newjson.getString("HSNCode");
                                    String FileName = newjson.getString("FileName");
                                    String MRP = newjson.getString("MRP");
                                    String FilePath = newjson.getString("filepath");
                                    try {
                                        Log.e("mmmmgggg", ItemImage);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    ItemMasterhelper v = new ItemMasterhelper();
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
                                    v.setMRP(MRP);
                                    v.setItemImage(ItemImage);
                                    v.setHSNCode(HSNCode);
                                    masterlist.add(v);
                                    //Log.d("masterlist", String.valueOf(masterlist.size()));

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
                                if (mmtype == 1) {
                                    ctx.startActivity(new Intent(ctx, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    ((Activity) ctx).finish();

                                    //    (HomeActivity)ctx.finish();
                                }
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
                com.dhanuka.morningparcel.Helper.Preferencehelper prefs;
                prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(ctx);
                SharedPreferences prefs1 = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                        ctx.MODE_PRIVATE);

                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
/*if (prefs1.getString("typer","grocery").equalsIgnoreCase("grocery")){
    shopeId="66562";
}else{
    shopeId="66543";

}*/
                    String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + strStop/*prefs.getPrefsContactId()*/ + "&Type=" + "28" + "&SupplierID=" + strStop;
                    Log.d("Beforeentionmaster", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrptionmaster", finalparam);
                    return params;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }
                /*


                Map<String, String> params = new HashMap<String, String>();
                params.put("contactid", prefs.getPrefsContactId());
                 params.put("type", "28");
                params.put("SupplierID", shopId);
                Log.d("masterlist", params.toString());

                return params;
*/


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ctx).add(postRequest);
    }

    public static void DialogOrderFeedback(Context ctx, String orderId, int type) {


        Dialog dialog_confirmation = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);

        dialog_confirmation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_confirmation.setContentView(R.layout.layout_order_feedback);
        Window window = dialog_confirmation.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog_confirmation.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        AppCompatRatingBar mRatingBar = (AppCompatRatingBar) dialog_confirmation.findViewById(R.id.mRatingBar);
        MaterialEditText et_comment = (MaterialEditText) dialog_confirmation.findViewById(R.id.et_comment);
        mRatingBar.setRating(5.0f);

        Button btn_cancel = (Button) dialog_confirmation.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirmation.dismiss();
            }
        });
        com.dhanuka.morningparcel.Helper.Preferencehelper prefs = new Preferencehelper(ctx);

        Button btn_save = (Button) dialog_confirmation.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type != 1) {

                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                        requestForSubmittingRate("1", orderId, mRatingBar.getRating() + "", et_comment.getText().toString(), prefs.getPrefsContactId(), ctx, dialog_confirmation);
                    } else {
                        requestForSubmittingRate("2", orderId, mRatingBar.getRating() + "", et_comment.getText().toString(), prefs.getPrefsContactId(), ctx, dialog_confirmation);

                    }
                } else {
                    dialog_confirmation.dismiss();
                }
            }
        });

        dialog_confirmation.setCanceledOnTouchOutside(true);
        dialog_confirmation.setCancelable(true);
      /*  adapter = new ViewPagerAdapterstd(ctx, imgList);
        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getItems(posi), true);
*/

        dialog_confirmation.show();
    }

    public static void requestForSubmittingRate(final String type, final String orderid, final String strRating, final String strComment, String contactId, Context ctx, Dialog md) {
        final ProgressDialog prgDialog = new ProgressDialog(ctx);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();


        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        String res = response;
                        prgDialog.dismiss();
                        if (res.length() > 0) {
                            log.e("res" + res);

                        }
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ctx);
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {
                                Crouton.showText((Activity) ctx, "Rating Submitted Successful", Style.CONFIRM);
                                //  requestCaptureSignature();
                                Handler h = new Handler();
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        md.dismiss();
                                    }
                                }, 1000);

                            } else if (success == 0) {
                                Crouton.showText((Activity) ctx, "Error Submitting Rating", Style.ALERT);
                                Handler h = new Handler();
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        md.dismiss();
                                    }

                                }, 1000);
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

                        log.e("in failure");
                        Crouton.showText((Activity) ctx, "Error Submitting Rating", Style.ALERT);
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                md.dismiss();
                            }
                        }, 2000);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("contactid", contactId);
//                params.put("rating", strRating);
//                params.put("comment", strComment);
//                params.put("orderid", orderid);
//                params.put("type", type);
                String param = ctx.getString(R.string.TEST_URL_ASSIGN_ORDER_RATING) + "&contactid=" + contactId + "&rating=" + strRating + "&comment=" + strComment + "&orderid=" + orderid + "&type=" + type;
                Log.d("Beforeencrptionupdate", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, ctx);
                params.put("val", finalparam);
                Log.d("afterencrptionupdate", finalparam);
                log.e("param to string==" + params.toString() + "\n" + R.string.TEST_URL_ASSIGN_RATING);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(ctx).add(postRequest);
    }

    public static class SliderTimer extends TimerTask {
        Activity act;

        public SliderTimer(Activity mAct) {
            act = mAct;
        }

        @Override
        public void run() {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("MSMMS0", "dfh");
                    if (viewPager.getCurrentItem() < icon.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    static List<String> icon;
    static List<String> iconName;
    static ViewPager viewPager;
    static TabLayout indicator;

   /* public static void openBannerDialog(Activity mActivity, ViewGroup viewGroup) {

        String[] array = {"http://app-editor.farmykey.in/agriculture/admin799/api/myfolder/banner_one.jpeg", "http://app-editor.farmykey.in/agriculture/admin799/api/myfolder/banner_one.jpeg", "http://app-editor.farmykey.in/agriculture/admin799/api/myfolder/banner_one.jpeg"};
         Window window = mActivity.getWindow();
         final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.CustomAlertDialog);
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.banner_dialog, viewGroup, false);
         builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        viewPager = dialogView.findViewById(R.id.viewPager);
        indicator = dialogView.findViewById(R.id.indicator);
        ImageView close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        icon = new ArrayList<>();
        iconName = new ArrayList<>();
        icon.add("http://app-editor.farmykey.in/agriculture/admin799/api/myfolder/Banner_Glaves.png");
        icon.add("http://app-editor.farmykey.in/agriculture/admin799/api/myfolder/Banner_Mask.png");
        icon.add("http://app-editor.farmykey.in/agriculture/admin799/api/myfolder/Banner_thermometer.png");
        iconName.add("");
        iconName.add("");
        iconName.add("");
        viewPager.setAdapter(new SliderAdapter(mActivity, icon, iconName));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(mActivity), 5000, 10000);
         alertDialog.show();


    }*/
}
