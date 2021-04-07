package com.dhanuka.morningparcel.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.ShoplistHelper;
import com.dhanuka.morningparcel.activity.HomeActivity;
import com.dhanuka.morningparcel.activity.ShopMapView;
import com.dhanuka.morningparcel.adapter.ShopListAdapter;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.OnStoreSelectListener;
import com.dhanuka.morningparcel.utils.AppUrls;
import com.dhanuka.morningparcel.utils.JKHelper;


public class SelectShop extends Dialog implements
        View.OnClickListener, OnStoreSelectListener {
    Context ctx;
    Resources res;
    // Gunjan 22/08
    static int mmtype = 0;
    ImageView cartbutton,homebtn;
    boolean isCancelable;
    RecyclerView shoprecyler;
    SelectShop dia;

    FloatingActionButton selectmapshop;

    public SelectShop(Context context, int type, boolean isCancelable) {
        // TODO Auto-generated constructor stub
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        ctx = context;
        res = ctx.getResources();
        mmtype = type;
        this.isCancelable = isCancelable;
    }

    ShopListAdapter shopListAdapter;
    public DatabaseManager dbManager;

    static EditText etSearch;
    ArrayList<ShoplistHelper> shoplist = new ArrayList<>();

    public void filter(String text) {
        ArrayList<ShoplistHelper> filteredList = new ArrayList<>();

        for (ShoplistHelper product : shoplist) {
            if (product.getShopname().toLowerCase().contains(text) || product.getAlartphonenumber().toLowerCase().contains(text)) {
                filteredList.add(product);
            }
        }

        shopListAdapter.filterList(filteredList);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        //     setCanceledOnTouchOutside(true);
        setOnCancelListener(null);
        LayoutInflater inflate = (LayoutInflater) ctx
                .getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View Shoplistdialog = inflate.inflate(R.layout.shoplist_layout, null);
        setContentView(Shoplistdialog);

        SharedPreferences prefs = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                ctx.MODE_PRIVATE);
        dbManager = DatabaseManager.getInstance(ctx);
        // /////////set font///////////////
        dia = this;

        //	backbtnicon = (ImageView) layout.findViewById(R.id.imgBackBtn);

        shoprecyler = Shoplistdialog.findViewById(R.id.shoplistcontainer);
        etSearch = Shoplistdialog.findViewById(R.id.etSearch);
        TextView btnSubmit = Shoplistdialog.findViewById(R.id.btnSubmit);
        cartbutton=Shoplistdialog.findViewById(R.id.cartbutton);
        homebtn=Shoplistdialog.findViewById(R.id.homebtn);
        selectmapshop= Shoplistdialog.findViewById(R.id.selectshopmap);
        TextView txtTtl = Shoplistdialog.findViewById(R.id.txtTtl);
        TextView txtNearbyText = Shoplistdialog.findViewById(R.id.txtNearbyText);
        shoprecyler.setHasFixedSize(true);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, HomeActivity.class));

            }
        });
        selectmapshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, ShopMapView.class).putExtra("mmtype","1"));
            }
        });
        if (prefs.getString("typer", "com").equalsIgnoreCase("com")){
            txtTtl.setText("Select Grocery Store");
            txtNearbyText.setText("Nearby Grocery Store");
        }else{
            txtTtl.setText("Select Restaurant");
            txtNearbyText.setText("Nearby Restaurant");

        }
        if (prefs.getString("shopId", "").isEmpty()) {
            setCancelable(isCancelable);
        } else {
            setCancelable(true);

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
        shoprecyler.setLayoutManager(new LinearLayoutManager(ctx,RecyclerView.VERTICAL,false));
        if (prefs.getString("cntry", "India").equalsIgnoreCase("India")) {
            btnSubmit.setText("Submit");

        } else {
            btnSubmit.setText("Back");
        }
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getString("cntry", "India").equalsIgnoreCase("India")) {
                    try {
                        String shopId = shopListAdapter.getShop();
                        String shopName = shopListAdapter.getShopName();
                        String DeliveryCharge = shopListAdapter.getDeliveryCharge();
                        String ServiceFees = shopListAdapter.getServiceFees();
                        String CheckOutMessage = shopListAdapter.getCheckOutMessage();
                        String MaxOrderAmt = shopListAdapter.getMaxOrderAmt();
                        String MinOrderAmt = shopListAdapter.getMinOrderAmt();
                        String discount = shopListAdapter.getDiscount();
                        String Tax = shopListAdapter.getTax();
                        if (!shopId.isEmpty()) {



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
                            mEditor.putString("Tax", Tax);
                            mEditor.commit();
                            //   mOnStoreSelectListener.
                            shopListAdapter.getShop1();


                            Log.e("MKJGH", mmtype + "");
                            if (dbManager != null) {
                                dbManager.deleteAll();
                            }
                            getAllProducts(ctx, shopId, dia);



                       /* if (prefs.getString("shopId", "").isEmpty()) {
                            getAllProducts(ctx, shopId, Shoplistdialog);
                        } else {*/
                            if (prefs.getString("shopId", "").equalsIgnoreCase(shopId)) {
                              /*  if (type == 1) {
                                    ctx.startActivity(new Intent(ctx, HomeActivity.class));
                                }*/
                                dismiss();
                            } else {
                                new AlertDialog.Builder(ctx)
                                        .setTitle("Safe'O'Fresh")
                                        .setMessage("If there is item in your cart than your cart will be cleared if you change the store. Still want to change the store?")
                                        .setPositiveButton("Ok", new OnClickListener() {
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
                                                mEditor.putString("Tax", Tax);
                                                mEditor.commit();
                                                //   mOnStoreSelectListener.
                                                shopListAdapter.getShop1();

                                                Log.e("MKJGH", mmtype + "");
                                                if (dbManager != null) {
                                                    dbManager.deleteAll();
                                                }
                                                getAllProducts(ctx, shopId, dia);


                                            }
                                        })
                                        .setNegativeButton("Cancel", new OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dismiss();
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
                } else {
                    dismiss();
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
                    String Distance = singleObj.getString("Distance");
                    String City = singleObj.getString("City");
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
                    v.setDistance(Distance);
                    v.setCity(City);
                    v.setTax(Tax);
                    shoplist.add(v);

                }
                Preferencehelper prefsz;
                prefsz = new Preferencehelper(ctx);
                SharedPreferences prefs1 = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                        ctx.MODE_PRIVATE);


                shopListAdapter = new ShopListAdapter(ctx, shoplist,this);
                shoprecyler.setAdapter(shopListAdapter);
                if (!prefsz.getPrefsContactId().isEmpty())
                {
                    if (shopeId.isEmpty())
                    {
                        cartbutton.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        cartbutton.setVisibility(View.GONE);
                    }

                }
                else {
                    cartbutton.setVisibility(View.GONE);

                }


                //Message.message(ctx, "Data fetched Successfuly");
            } else if (success == 0) {
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    String shopeId = "";

    public void getAllProducts(Context ctx, String strStop, Dialog mDIalog) {
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
                                    String MRP = newjson.getString("MRP");
                                    String FileName = newjson.getString("FileName");
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
                                    v.setMRP(MRP);
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
                                    //Log.d("masterlist", String.valueOf(masterlist.size()));
                                };
                                Log.e("ASDSDSD="+mmtype,"kjhsgdjfgdshf");
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
                Preferencehelper prefs;
                prefs = new Preferencehelper(ctx);
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


String strr="";
                    if (prefs.getPrefsContactId().isEmpty()) {
                        strr = "7777";

                    } else {
                        strr = prefs.getPrefsContactId();
                    }

                    String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + strr/*prefs.getPrefsContactId()*/ + "&Type=" + "28" + "&SupplierID=" + strStop;
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


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ctx).add(postRequest);
    }


	@Override
	public void onStoreSelect() {
		SharedPreferences prefs = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
				ctx.MODE_PRIVATE);	try {
			String shopId = shopListAdapter.getShop();
			String shopName = shopListAdapter.getShopName();
			String DeliveryCharge = shopListAdapter.getDeliveryCharge();
			String ServiceFees = shopListAdapter.getServiceFees();
			String CheckOutMessage = shopListAdapter.getCheckOutMessage();
			String MaxOrderAmt = shopListAdapter.getMaxOrderAmt();
			String MinOrderAmt = shopListAdapter.getMinOrderAmt();
            String discount = shopListAdapter.getDiscount();
            String Tax = shopListAdapter.getTax();
            if (!shopId.isEmpty()) {
                       /* if (prefs.getString("shopId", "").isEmpty()) {
                            getAllProducts(ctx, shopId, Shoplistdialog);
                        } else {*/
				if (prefs.getString("shopId", "").equalsIgnoreCase(shopId)) {
					ctx.startActivity(new Intent(ctx, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					((Activity) ctx).finish();
					dismiss();
				} else {
					new AlertDialog.Builder(ctx)
							.setTitle("Safe'O'Fresh")
							.setMessage("If there is item in your cart than your cart will be cleared if you change the store. Still want to change the store?")
							.setPositiveButton("Ok", new OnClickListener() {
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
									mEditor.putString("discount", discount);
									mEditor.putString("Tax", Tax);
									mEditor.putString("MinOrderAmt", MinOrderAmt);
									mEditor.commit();
									//   mOnStoreSelectListener.
									shopListAdapter.getShop1();

									Log.e("MKJGH", mmtype + "");
									if (dbManager != null) {
										dbManager.deleteAll();
									}
									getAllProducts(ctx, shopId, dia);


								}
							})
							.setNegativeButton("Cancel", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dismiss();
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

    @Override
    public void onPromoSelect(String promocode) {

    }
}
