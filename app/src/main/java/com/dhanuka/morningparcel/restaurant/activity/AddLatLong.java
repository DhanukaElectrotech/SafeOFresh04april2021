package com.dhanuka.morningparcel.restaurant.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.CartActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.BaseActivity;
import com.dhanuka.morningparcel.activity.HomeActivity;
import com.dhanuka.morningparcel.activity.Message;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBean;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.ReverseGeocodeTask;
import com.dhanuka.morningparcel.utils.log;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddLatLong extends BaseActivity implements View.OnClickListener {
    //Map Callback Timer
// Splash screen timer
    private static int SPLASH_DISPLAY_TIME = 20000;
    private Handler mHandler = new Handler();
    GeoFenceBean geoFenceBean;
    String geofenceid = "0";
    private static final int REQUEST_ACCESS_FINE_LOCATION = 13112;
    private static final int REQUEST_CHECK_SETTINGS = 13119;
    private boolean mLocationPermissionGranted;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mGoogleMap;
    ProgressDialog prgDialog;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    ImageView cancelmapd;

    LinearLayout maptrafficlk,mapdefaultclk,mapterrainclk,deafultmapdetailclk,mapbycyclingclk,mapsatelliteclk;


    public void backClick(View view) {
        // onBackPressed();
    }

    String splithome[];
    private RadioButton rbDay;
    private ImageView backbtnicon;
    private Button btnCancel;
    private Button btnSubmit;
    BottomSheetDialog dialog;
    MaterialEditText addnewsociety, lanmarktxt, saveastxt;
    Button btn_submitdialog;
    View mRbStartDay;
    Place place;
    protected static final String TAG = "location-settings";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    String latitudeToServer, longitudeToServer;
    protected Boolean mRequestingLocationUpdates = false;
    private LinearLayout ll_photo_sign;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private ReverseGeocodeTask task;
    private double bearing = 0.0;
    private TextView toolbarTitle;
    private String type;
    private int maptype;
    private String tripdid = "0";
    private ImageView ivcamera, ivSign;
    private MapFragment mapFragment;
    private GoogleMap mMap;
    private LatLng latlng, pickLatLng, editPositionLatLng;
    private String mELat, mELong;
    LatLng camaraLatLong;
    public Double latt, longg;
    boolean isEdit = false, iswoedit = false;
    boolean isMapIconClicked = false;

    boolean isMapIconClicked1 = false;
    boolean iscurrentlocation = false;
    boolean issearchselected = false;
    LinearLayout mMainLayout;

    @Nullable
    @BindView(R.id.map_type)
    LinearLayout map_type;

    TextView chooseadd, addressselected, cityname, chooseadddialog, addressselecteddialog, citynamed;

    @Override
    protected int getLayoutResourceId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_address);
        ButterKnife.bind(this);
        findViews();


        Bundle b = getIntent().getExtras();

        backbtnicon.setVisibility(View.GONE);

        mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issearchselected = true;

            }
        });


//        if (b != null) {
//
//            backbtnicon.setVisibility(View.VISIBLE);
//            Log.e("JJJJJ", "hgdgf");
//            tripdid = b.getString("tripdids"); //b.getString("tripdids");
//            type = b.getString("type"); // 1 =geofence, 2=boarded, 3=deboarded , 4=
//            if (type.equalsIgnoreCase("1")) {
//                ll_photo_sign.setVisibility(View.GONE);
//                requstForemployeecoordinate();
//            } else if (type.equalsIgnoreCase("2")) {
//                ll_photo_sign.setVisibility(View.GONE);
//                toolbarTitle.setText("Updated Boarding Status");
//            } else if (type.equalsIgnoreCase("3")) {
//                ll_photo_sign.setVisibility(View.GONE);
//                toolbarTitle.setText("Updated DeBoarding Status");
//            } else if (type.equalsIgnoreCase("4")) {
//                ll_photo_sign.setVisibility(View.GONE);
//                toolbarTitle.setText("Upload Pickup Coordinate-Cab");
//                //Ram 12/17/2018
//                requstForemployeecoordinate();
//            } else if (type.equalsIgnoreCase("5")) {
//                ll_photo_sign.setVisibility(View.GONE);
//                toolbarTitle.setText("Upload Pickup Coordinate-TT");
//                requstForemployeecoordinate();
//            }
//        }

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key1), Locale.US);
        }
        PlacesClient placesClient = Places.createClient(this);
        final List<Place.Field> placeFields = new ArrayList<>(Arrays.asList(Place.Field.values()));

        chooseadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);
                String cntry = prefs1.getString("cntry", "India");


                if (type.equalsIgnoreCase("5")) {
                    if (cntry.equalsIgnoreCase("India")) {
                        Intent autocompleteIntent =
                                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields).setCountry("IN")
                                        .build(AddLatLong.this);
                        startActivityForResult(autocompleteIntent, 10022);


                    } else {
                        Intent autocompleteIntent =
                                new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields).setCountry("IN")
                                        .build(AddLatLong.this);
                        startActivityForResult(autocompleteIntent, 10022);

                    }
                } else {
                    Intent autocompleteIntent =
                            new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields).setCountry("IN")
                                    .build(AddLatLong.this);
                    startActivityForResult(autocompleteIntent, 10022);

                }


            }
        });
    }

    @Override
    protected void onSideSliderClick() {

    }

    int intMapCallType = 0;

    private void requstForemployeecoordinate() {


        final ProgressDialog prgDialog = new ProgressDialog(AddLatLong.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_EMPLOYEE_COORDINATES),
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
                            JSONObject jsonObject = new JSONObject(res);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject loopObjects = jsonArray.getJSONObject(i);
                                        mELat = loopObjects.getString("lat");
                                        mELong = loopObjects.getString("longs");
                                    }
                                    if (mELat.length() > 1) {
                                        isEdit = true;
                                        editPositionLatLng = new LatLng(Double.valueOf(mELat), Double.valueOf(mELong));
                                        camaraLatLong = editPositionLatLng;
                                        Log.e("mELat", mELat + "");
                                        if (mMap != null) {
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(editPositionLatLng, 16));

                                        } else {
                                            initMap();
                                        }
                                    } else {
                                        iscurrentlocation = true;
                                    }
                                    intMapCallType = 1;
                                    latitudeToServer = "";
                                    //mHandler.postDelayed(mSplashScreenTask, SPLASH_DISPLAY_TIME);

                                    //initMap();

                                } else {
                                    Message.message(AddLatLong.this, "No Coordinates Defined.");
                                    // finish();
                                }
                            } else if (success == 0) {
                                Message.message(AddLatLong.this, "No Data Exist");
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

                        Message.message(AddLatLong.this, "Failed To Retrieve Data");
                        finish();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (prefs.getPrefsContactId() != null) {
                    params.put("contactid", prefs.getPrefsContactId());
                } else {
                    params.put("contactid", "0");
                }
                params.put("type", type);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);
    }

    private void initMap() {
        mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));


        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);

                if (type.equalsIgnoreCase("6")) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitudeToServer), Double.parseDouble(longitudeToServer)), 16.0f);
                    mMap.animateCamera(cameraUpdate);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitudeToServer), Double.parseDouble(longitudeToServer)), 16.0f));
                    addressselected.setText(splithome[0]);
                    cityname.setText(splithome[1]);
                }


                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {

                        issearchselected = true;


                        return false;
                    }
                });
                mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int reason) {
                        if (reason == REASON_GESTURE) {
                            issearchselected = true;
                        }
                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng arg0) {
                        Log.i("onMapClick", "Horray!");
                        issearchselected = true;
                    }
                });
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        //get latlng at the center by calling
                        if (issearchselected == true) {
                            LatLng midLatLng = mMap.getCameraPosition().target;
                            latitudeToServer = String.valueOf(midLatLng.latitude);
                            longitudeToServer = String.valueOf(midLatLng.longitude);

                            Geocoder geocoder;

                            List<Address> addresses;
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(Double.parseDouble(latitudeToServer), Double.parseDouble(longitudeToServer), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                addressselected.setText(knownName + city + "," + state + "," + postalCode + "," + country);
                                cityname.setText(city);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        issearchselected = false;

                    }
                });


            }
        });

    }


    private void findViews() {
        chooseadd = (TextView) findViewById(R.id.chooseadd);
        addressselected = (TextView) findViewById(R.id.addressselected);

        cityname = (TextView) findViewById(R.id.cityname);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        rbDay = (RadioButton) findViewById(R.id.rb_day);
        mRbStartDay = (View) findViewById(R.id.v_rb_day_start);
        mMainLayout = findViewById(R.id.frametouch);

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        ivSign = (ImageView) findViewById(R.id.iv_sign);
        backbtnicon = (ImageView) findViewById(R.id.backbtnicon);
        ivSign.setOnClickListener(this);
        ivcamera = (ImageView) findViewById(R.id.iv_camera);
        ivcamera.setOnClickListener(this);


        ll_photo_sign = (LinearLayout) findViewById(R.id.ll_photo_sign);

        rbDay.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        mRbStartDay.setOnClickListener(this);
        View view = getLayoutInflater().inflate(R.layout.add_addressbottomsheet, null);
        dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog); // Style here
        dialog.setContentView(view);
        addressselecteddialog = dialog.findViewById(R.id.addressselecteddialog);

        addnewsociety = dialog.findViewById(R.id.addnewsociety);
        saveastxt = dialog.findViewById(R.id.saveastxt);
        citynamed = dialog.findViewById(R.id.citynamed);

        lanmarktxt = dialog.findViewById(R.id.lanmarktxt);
        chooseadddialog = dialog.findViewById(R.id.chooseadddialog);
        initMap();
        type = getIntent().getStringExtra("type");

        map_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSecond(AddLatLong.this);


            }
        });


        if (type.equalsIgnoreCase("6")) {
            geoFenceBean = (GeoFenceBean) getIntent().getSerializableExtra("geofencebean");
            saveastxt.setText(geoFenceBean.getStriconname());
            splithome = geoFenceBean.getStrdescription().split(":");
//            addressselected.setText(splithome[0]);


            geofenceid = geoFenceBean.getStrgeocodeid();
            latitudeToServer = geoFenceBean.getStrlat();
            longitudeToServer = geoFenceBean.getStrlong();
            //    Toast.makeText(getApplicationContext(),String.valueOf(latitudeToServer),Toast.LENGTH_LONG).show();


            try {
                addressselecteddialog.setText(splithome[0]);
                citynamed.setText(splithome[1]);
                addnewsociety.setText(splithome[2]);
                lanmarktxt.setText(splithome[3]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.show();


        } else {

            final List<Place.Field> placeFields = new ArrayList<>(Arrays.asList(Place.Field.values()));
            Intent autocompleteIntent =
                    new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields).setCountry("IN")
                            .build(AddLatLong.this);
            startActivityForResult(autocompleteIntent, 10022);


        }

        btn_submitdialog = dialog.findViewById(R.id.btn_submittxt);
        btn_submitdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (type.equalsIgnoreCase("5")) {

                    addressselected.setText(addressselected.getText().toString());
                    creategeofence("5");
                } else if (type.equalsIgnoreCase("6")) {

                    creategeofence("6");
                }


                //  type delete=3 update=6 create=5
            }

        });
        chooseadddialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Place.Field> placeFields = new ArrayList<>(Arrays.asList(Place.Field.values()));
                Intent autocompleteIntent =
                        new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields).setCountry("IN")
                                .build(AddLatLong.this);
                startActivityForResult(autocompleteIntent, 10022);


            }
        });

        //requstForemployeecoordinate();

    }

    @Override
    public void onClick(View v) {
        if (v == mRbStartDay) {
            // Handle clicks for rbDay
        }
/*
        if(v==mLocationButton){
            checkLocationSettings();
        }
*/
        else if (v == btnCancel) {
            finish();
        } else if (v == btnSubmit) {
            {

                if (!lanmarktxt.getText().toString().isEmpty()) {
                    lanmarktxt.setText("");
                }

                if (!addnewsociety.getText().toString().isEmpty()) {
                    addnewsociety.setText("");
                }
                citynamed.setText(cityname.getText().toString());
                addressselecteddialog.setText(addressselected.getText().toString());
                dialog.show();
            }
        } else if (v == ivcamera) {
            JKHelper.closeKeyboard(this, v);
        } else if (v.getId() == R.id.iv_sign) {
            {
                //  prefs.setIsSignatureTaker(false);
                //      startActivity(new Intent(this, CaptureSignAcitivity.class).putExtra("description", "Boarding_DeBoardingSignature").putExtra("imagetype", "Roster_Open_Detail").putExtra("pk",String.valueOf("22")).putExtra("tablenm", "Roster_Open_Detail").putExtra("imageid","22").putExtra("doctype","Boarding Deboarding Signature"));
            }
        }
    }

    private void giveDayAttendence() {
        double lat = latt;
        double longs = longg;


        if (lat == 0.0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Could not fetched Coordinates, Turn On GPS or Go to Open Space and Try Attandance Again!!!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            final ProgressDialog prgDialog = new ProgressDialog(AddLatLong.this);
            prgDialog.setMessage("Please Wait.....");
            prgDialog.setCancelable(false);
            prgDialog.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_UPDATE_EMPLOYEE_GEOFENCE),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Responsen", response);
                            String res = response;
                            prgDialog.dismiss();

                            if (res.length() > 0) {
                                log.e("res ateendfsad " + res);
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                int success = jsonObject.getInt("success");
                                log.e("successnew" + success);
                                if (success == 1) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");
                                    String attendanceid = "";

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject loopObjects = jsonArray.getJSONObject(i);
                                        attendanceid = loopObjects.getString("uid");
                                    }
                                    Toast toast = Toast.makeText(getApplicationContext(), "Updated Successfully!!!", Toast.LENGTH_SHORT);
                                    toast.show();
                                    startActivity(new Intent(AddLatLong.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    finish();
                                } else if (success == 0) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");
                                    String errormessage = "";
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject loopObjects = jsonArray.getJSONObject(i);
                                        errormessage = loopObjects.getString("returnmessage");
                                        if (errormessage.equalsIgnoreCase("Updated Successfully")) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Updated Successfully!!!", Toast.LENGTH_SHORT);
                                            toast.show();
                                            startActivity(new Intent(AddLatLong.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                            finish();
                                        }
                                    }
                                    Toast toast = Toast.makeText(getApplicationContext(), errormessage, Toast.LENGTH_SHORT);
                                    toast.show();
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

                            Message.message(AddLatLong.this, "Failed to Upload Status");
                            //   Log.e("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    //todo pass real contact id
                    if (prefs.getPrefsContactId() != null) {
                        params.put("contactid", prefs.getPrefsContactId());
                    } else {
                        params.put("contactid", "7508");
                    }
                    params.put("type", "1");
                    params.put("tme", JKHelper.getCurrentDate());
                    if (prefs.getPREFS_phoneno() != null) {
                        params.put("phoneno", prefs.getPREFS_phoneno());
                    } else {
                        params.put("phoneno", "9727000000");
                    }

                    params.put("odometer", "0");
                    params.put("lat", latitudeToServer);
                    params.put("long", longitudeToServer);
                    params.put("comment", "");
                    params.put("gpsodometer", "");
                    params.put("battery", JKHelper.getBatteryLevel(AddLatLong.this) + "");

                    params.put("attendanceid", "0");


                    // enter here GPS odometer
                    log.e("params==" + params.toString());

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(this).add(postRequest);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 10022) {

                place = Autocomplete.getPlaceFromIntent(data);

                Log.i(TAG, "PlaceA: " + place.getName() + ", " + place.getLatLng().latitude);

                // TODO: Get info about the selected place.
                Log.e("NAMEA", place.getName() + "\n" + place.getAddress());
                Log.e("NAME11A", place.getLatLng().latitude + "\n" + place.getLatLng().longitude);
                latitudeToServer = place.getLatLng().latitude + "";
                longitudeToServer = place.getLatLng().longitude + "";

                mMap.clear();

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16.0f);

                mMap.animateCamera(cameraUpdate);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16));


                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(Double.parseDouble(latitudeToServer), Double.parseDouble(longitudeToServer), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    String city = addresses.get(0).getLocality();
//                    String state = addresses.get(0).getAdminArea();
//                    String country = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();

                    addressselected.setText(place.getName() + "," + place.getAddress());
                    cityname.setText(city);
                    issearchselected = false;
                    try {
                        dialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        //startLocationUpdates();

                        if (editPositionLatLng != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(editPositionLatLng, 16));

                        } else {
                            if (latlng != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
                            }
                        }

                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;


                }
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        log.e("on start is called");
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    protected void startLocationUpdates() {
/*
        fecthingYourLocation=new ProgressDialog(AddLatLong.this);
        fecthingYourLocation.setMessage("Fetching Your Location...");
        fecthingYourLocation.setCancelable(false);
        fecthingYourLocation.show();

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            ).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    mRequestingLocationUpdates = true;
                }
            });
            //fecthingYourLocation.dismiss();
        }
        catch(Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
            fecthingYourLocation.dismiss();
        }
*/
    }

    public void creategeofence(String type) {


        prgDialog = new ProgressDialog(AddLatLong.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);



                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, AddLatLong.this);
                        if (!responses.equalsIgnoreCase("")) {


                            try {
                                JSONObject jsonObject = new JSONObject(responses);
                                int successstr = jsonObject.getInt("success");
                                if (successstr==1) {
                                    prgDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "address added successfully for restaturant", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    finish();
                                    onBackPressed();

                                } else {
                                    prgDialog.dismiss();

                                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();

                                    dialog.dismiss();
                                }

                            } catch (Exception e) {
                                prgDialog.dismiss();
                               // Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Uploaded", Toast.LENGTH_LONG).show();
                            prgDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prgDialog.dismiss();
                        // error
                        Message.message(AddLatLong.this, "Failed to Upload Status");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Preferencehelper prefs = new Preferencehelper(getApplicationContext());
//                params.put("GeofenceID", );
//                params.put("type", );
//                params.put("contactid", );
//                params.put("DeviceCode", );
//                params.put("Description", );
//                params.put("Lat", );
//                params.put("Long",  + "");
//                params.put("RadiousinKM", );
//                params.put("IconName", "office");
//                params.put("Status", "");
                try {

                    String param = getString(R.string.URL_CREATE_GEOFENCE) + "&GeofenceID=" + geofenceid + "&type=" + type + "&contactid=" + prefs.getPrefsContactId() +
                            "&DeviceCode=" + "0" + "&putdevicecode=0" + "&Description=" + addressselecteddialog.getText().toString() + ":" + citynamed.getText().toString() + ":" + addnewsociety.getText().toString() + ":" + lanmarktxt.getText().toString() + "&Lat=" + latitudeToServer + "&RadiousinKM=" + "0"
                            + "&Long=" + longitudeToServer + "&IconName=" + saveastxt.getText().toString() + "&Status=0";
                    Log.e("beforeenc", param);
                    JKHelper jkHelper = new JKHelper();
                    param = jkHelper.Encryptapi(param, AddLatLong.this);
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

        Volley.newRequestQueue(this).add(postRequest);
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16));

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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16));
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16));

            }
        });
        mapbycyclingclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deafultmapdetailclk.setBackground(getResources().getDrawable(R.drawable.background_white));
                maptrafficlk.setBackground(getResources().getDrawable(R.drawable.background_white));
                mapbycyclingclk.setBackground(getResources().getDrawable(R.drawable.backgroun_corners));
                mMap.setBuildingsEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 19));

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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16));

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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 16));

            }
        });



        dialogmap.show();
    }

}
