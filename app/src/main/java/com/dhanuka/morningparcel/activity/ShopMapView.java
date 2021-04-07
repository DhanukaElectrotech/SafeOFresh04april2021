package com.dhanuka.morningparcel.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.restaurant.activity.AddLatLong;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.FusedLocationService;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.MyItem;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.ShoplistHelper;
import com.dhanuka.morningparcel.adapter.ShopListAdapter;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.utils.AppUrls;
import com.dhanuka.morningparcel.utils.JKHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopMapView extends AppCompatActivity implements View.OnClickListener, FusedLocationService.LocationGetCallbacks, OnMapReadyCallback, ResultCallback<LocationSettingsResult>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>, ClusterManager.OnClusterItemClickListener<MyItem> {

    Preferencehelper prefsOld;
    private int maptype;
    private GoogleMap mMap;
    SharedPreferences prefs;
    LatLng latlng;
    static int mmtype = 0;
    OrderBean orederbean;
    List<MyItem> itemss = new ArrayList<MyItem>();
    ArrayList<ShoplistHelper> shoplist = new ArrayList<>();
    ShoplistHelper v = new ShoplistHelper();
    ArrayList<LatLng> shoplistcluster = new ArrayList<>();
    private ClusterManager<MyItem> mClusterManager;
    ShopListAdapter shopListAdapter;
    ShopMapView dia;

    public DatabaseManager dbManager;
    ImageView cancelmapd;

    LinearLayout maptrafficlk,mapdefaultclk,mapterrainclk,deafultmapdetailclk,mapbycyclingclk,mapsatelliteclk;

    @Nullable
    @BindView(R.id.map_type)
    LinearLayout map_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_map_view);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        dbManager = DatabaseManager.getInstance(getApplicationContext());
        mmtype= Integer.parseInt( getIntent().getStringExtra("mmtype"));
        map_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSecond(ShopMapView.this);


            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mClusterManager = new ClusterManager<MyItem>(getApplicationContext(), mMap);
        mMap.setOnInfoWindowClickListener(mClusterManager); //added
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mClusterManager.getMarkerCollection().setOnInfoWindowClickListener(mClusterManager);

        if (mmtype==1)
        {


            getAllShop();
        }
        else
        {
            orederbean=(OrderBean)getIntent().getSerializableExtra("mapbean");


//           mMap.addMarker(new MarkerOptions().position(latlng));
//
//            itemss.add(new MyItem(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())),"","" ));
//            mClusterManager.addItems(itemss);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));
//            // mClusterManager.cluster();
//
//            MyClusterRenderer myClusterRenderer = new MyClusterRenderer(getApplicationContext(), mMap, mClusterManager);
//            mClusterManager.setRenderer(myClusterRenderer);
//            mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems(new MyItem(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), "", "")));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())))
                    .title("Delivery Location")

                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.shoppingcart)));

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(orederbean.getSupplierLat()),Double.parseDouble(orederbean.getSupplierLong())))
                    .title("Store Location")

                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.restauranticon)));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        Log.d("storenameCLickMap", myItem.getStorename());

        Toast.makeText(getApplicationContext(),"Infowindow clicked",Toast.LENGTH_LONG).show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        try {
            Toast.makeText(getApplicationContext(),"entered try clicked",Toast.LENGTH_LONG).show();
            String shopId = myItem.getShoplist().get(myItem.getStoreposition()).getShopid();
            String shopName = myItem.getShoplist().get(myItem.getStoreposition()).getShopname();
            String DeliveryCharge = myItem.getShoplist().get(myItem.getStoreposition()).getDeliveryCharge();
            String ServiceFees = myItem.getShoplist().get(myItem.getStoreposition()).getServiceFees();
            String CheckOutMessage = myItem.getShoplist().get(myItem.getStoreposition()).getCheckOutMessage();
            String MaxOrderAmt = myItem.getShoplist().get(myItem.getStoreposition()).getMaxOrderAmt();
            String MinOrderAmt = myItem.getShoplist().get(myItem.getStoreposition()).getMinOrderAmt();
            String discount = myItem.getShoplist().get(myItem.getStoreposition()).getDiscount();
            String Tax = myItem.getShoplist().get(myItem.getStoreposition()).getTax();
            if (!shopId.isEmpty()) {
                       /* if (prefs.getString("shopId", "").isEmpty()) {
                            getAllProducts(ctx, shopId, Shoplistdialog);
                        } else {*/
                if (prefs.getString("shopId", "").equalsIgnoreCase(shopId)) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
//                    dismiss();
                } else {
                    new AlertDialog.Builder(ShopMapView.this)
                            .setTitle("Safe'O'Fresh")
                            .setMessage("If there is item in your cart than your cart will be cleared if you change the store. Still want to change the store?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                            MODE_PRIVATE);
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


                                    Log.e("MKJGH", mmtype + "");
                                    if (dbManager != null) {
                                        dbManager.deleteAll();
                                    }
                                    getAllProducts(getApplicationContext(), shopId);


                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    dismiss();
                                    //  Toast.makeText(mActivity, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                                }
                            })

                            .show();
                }


                // Shoplistdialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"entered catched clicked",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onLocationReceived(double lat, double lng) {

    }

    public void getAllShop() {

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
                    String storelat = singleObj.getString("Blat");
                    String storelong = singleObj.getString("Blong");

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
                    latlng = new LatLng(Double.parseDouble(storelat), Double.parseDouble(storelong));

                    mMap.setOnCameraIdleListener(mClusterManager);

                    mMap.setOnMarkerClickListener(mClusterManager);
                    String fullimagelink = "http://mmthinkbiz.com/ImageHandler.ashx?image=" + ImageName + filepath;
                    itemss.add(new MyItem(latlng, consignee, deviceId, fullimagelink, "StoreAddress",shoplist,i));
                    mClusterManager.addItems(itemss);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 5));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 5));

                    mMap.setMyLocationEnabled(true);
                    MyClusterRenderer myClusterRenderer = new MyClusterRenderer(getApplicationContext(), mMap, mClusterManager);
                    mClusterManager.setRenderer(myClusterRenderer);
//                    mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
//                        @Override
//                        public boolean onClusterItemClick(MyItem item) {
//                            Toast.makeText(getApplicationContext(), item.getStorename(), Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                    });
//                    MyCustomAdapterForItems markerWindowView = new MyCustomAdapterForItems(new MyItem(latlng, consignee, deviceId, fullimagelink, "StoreAddress"));
//               mMap.setInfoWindowAdapter(markerWindowView);

                    mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems(new MyItem(latlng, "", "", "", "StoreAddress",shoplist,i)));
                    mMap.setInfoWindowAdapter(new MyCustomAdapterForItems(new MyItem(latlng, consignee, deviceId, fullimagelink, "StoreAddress",shoplist,i)));


                }




                //Message.message(ctx, "Data fetched Successfuly");
            } else if (success == 0) {
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onClusterItemClick(MyItem myItem) {

        Toast.makeText(getApplicationContext(), myItem.getStorename(), Toast.LENGTH_LONG).show();

        return false;
    }

    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {

        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item,
                                                   MarkerOptions markerOptions) {

//            BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
//
//            markerOptions.icon(markerDescriptor);
//            final Drawable clusterIcon = getResources().getDrawable(R.drawable.shoppingcart);
//            clusterIcon.setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
//
//            mClusterIconGenerator.setBackground(clusterIcon);
            if (prefs.getString("typer", "").equalsIgnoreCase("com")) {
                markerOptions.title(item.getStorename()).icon(BitmapDescriptorFactory.fromResource(R.drawable.shoppingcart));


            } else {
                markerOptions.title(item.getStorename()).icon(BitmapDescriptorFactory.fromResource(R.drawable.restauranticon));

            }

        }


    }


//        @Override
//        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
//            Log.d("cluster_location", String.valueOf(clusterItem.getPosition().latitude + "/" + clusterItem.getPosition().longitude));
//            super.onClusterItemRendered(clusterItem, marker);
//        }
//
//       @Override
//       protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
//
//           final Drawable clusterIcon = getResources().getDrawable(R.drawable.shoppingcart);
//       clusterIcon.setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
//
//        mClusterIconGenerator.setBackground(clusterIcon);
//
//         markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.shoppingcart));
//
//           //modify padding for one or two digit numbers
//            if (cluster.getSize() < 3) {
//                mClusterIconGenerator.setContentPadding(40, 20, 0, 0);
//            } else {
//                mClusterIconGenerator.setContentPadding(30, 20, 0, 0);
//            }
//
//         Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//          markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//       }
//

    public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter  {

        private final View myContentsView;
        MyItem myItem;

        MyCustomAdapterForItems(MyItem myItem) {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.infowindowshop, null);
            this.myItem = myItem;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {


            LinearLayout ll_stop = myContentsView.findViewById(R.id.ll_stop);
            Typeface font = Typeface.createFromAsset(
                    getApplicationContext().getAssets(),
                    "fonts/sansation-bold.ttf");
            TextView resttitle = ((TextView) myContentsView
                    .findViewById(R.id.restaurantname));
            TextView restaddrs = ((TextView) myContentsView
                    .findViewById(R.id.restaddrs));
            ImageView restimage = ((ImageView) myContentsView
                    .findViewById(R.id.restaurantimage));
            resttitle.setTypeface(font);
            restaddrs.setTypeface(font);
            for (int i=0;i<myItem.getShoplist().size();i++)
            {
                resttitle.setText(myItem.getShoplist().get(i).getShopname());
                restaddrs.setText(myItem.getShoplist().get(i).getShopname());
            }
            if (prefs.getString("typer", "").equalsIgnoreCase("com")) {


                Picasso.with(getApplicationContext()).load(myItem.getImagelink()).placeholder(R.drawable.no_image).into(restimage);

            } else {

            }
            ll_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("storenameCLickMap", myItem.getStorename());
                    //         Toast.makeText(getApplicationContext(),"Infowindow clicked",Toast.LENGTH_LONG).show();


                }
            });

            return myContentsView;
        }
    }

    String shopeId = "";

    public void getAllProducts(Context ctx, String strStop) {
        shopeId = strStop;
        final ProgressDialog mProgressBar = new ProgressDialog(ShopMapView.this);
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

//                            if (mDIalog != null) {
//                                shopListAdapter.getShop1();
//                                mDIalog.dismiss();
//                            }
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
                                    String MRP = newjson.getString("MRP");
                                    String ItemBarcode = newjson.getString("ItemBarcode");
                                    String StockUOM = newjson.getString("StockUOM");
                                    String ItemImage = newjson.getString("ItemImage");
                                    String HSNCode = newjson.getString("HSNCode");
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
                                    startActivity(new Intent(ctx, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    public void showDialogSecond(final AppCompatActivity mContext) {

        final Dialog dialogmap = new BottomSheetDialog(mContext, android.R.style.Theme_Translucent);
        View view = getLayoutInflater().inflate(R.layout.map_overlay_light, null);
        dialogmap.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialogmap.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialogmap.setCancelable(true);
        dialogmap.setContentView(R.layout.map_overlay_light);
        maptrafficlk=dialogmap.findViewById(R.id.maptrafficlk);
        mapdefaultclk=dialogmap.findViewById(R.id.mapdefaultclk);
        mapterrainclk=dialogmap.findViewById(R.id.mapterrainclk);
        deafultmapdetailclk=dialogmap.findViewById(R.id.deafultmapdetailclk);
        mapbycyclingclk=dialogmap.findViewById(R.id.mapbycyclingclk);
        mapsatelliteclk=dialogmap.findViewById(R.id.mapsatelliteclk);
        cancelmapd=dialogmap.findViewById(R.id.cancelmapd);

        maptrafficlk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapbycyclingclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                deafultmapdetailclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                maptrafficlk.setBackground(getResources().getDrawable(R.drawable.backgroun_corners));
                mMap.setTrafficEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));

            }
        });
        cancelmapd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogmap.dismiss();

            }
        });
        mapdefaultclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapsatelliteclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                mapterrainclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                mapdefaultclk.setBackground(getResources().getDrawable(R.drawable.backgroun_corners));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        });
        mapterrainclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapsatelliteclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                mapdefaultclk.setBackground(getResources().getDrawable(R.drawable.background_white));

                mapterrainclk.setBackground(getResources().getDrawable(R.drawable.backgroun_corners));
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                maptype = 2;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));

            }
        });
        mapbycyclingclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deafultmapdetailclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                maptrafficlk.setBackground(getResources().getDrawable(R.drawable.background_white));
                mapbycyclingclk.setBackground(getResources().getDrawable(R.drawable.backgroun_corners));
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 19));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 19));

            }
        });
        mapsatelliteclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapterrainclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                mapdefaultclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                mapsatelliteclk.setBackground(getResources().getDrawable(R.drawable.backgroun_corners));
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                maptype = 3;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));

            }
        });
        deafultmapdetailclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deafultmapdetailclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                maptrafficlk.setBackground(getResources().getDrawable(R.drawable.background_white));
                deafultmapdetailclk.setBackground(getResources().getDrawable(R.drawable.backgroun_corners));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setTrafficEnabled(false);
                mMap.setBuildingsEnabled(false);
                maptype = 0;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(orederbean.getDeliveryAddLat()),Double.parseDouble(orederbean.getDeliveryAddLong())), 16));

            }
        });



        dialogmap.show();
    }


}
