package com.dhanuka.morningparcel.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dhanuka.morningparcel.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import com.dhanuka.morningparcel.Helper.NetworkUtil;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.ReverseGeocodeTask;
import com.dhanuka.morningparcel.utils.log;

public class ChooseLocationActivity extends AppCompatActivity implements View.OnClickListener {
    //Map Callback Timer
// Splash screen timer
    Button btnDemo, btnLive;
    Dialog dialogPaymentMode;


    private static int SPLASH_DISPLAY_TIME = 20000;
    private Handler mHandler = new Handler();

    private static final int REQUEST_ACCESS_FINE_LOCATION = 13112;
    private static final int REQUEST_CHECK_SETTINGS = 13119;
    private boolean mLocationPermissionGranted;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mGoogleMap;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    public void backClick(View view) {
        onBackPressed();
    }

    TextView txtLocation,
            txtAddress;
    private Button btnCancel;
    private Button btnSubmit;
    protected static final String TAG = "location-settings";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    String latitudeToServer, longitudeToServer;
    protected Boolean mRequestingLocationUpdates = false;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private ReverseGeocodeTask task;
    private double bearing = 0.0;
    private String type;
    private String tripdid = "0";
    private MapFragment mapFragment;
    private GoogleMap mMap;
    private LatLng latlng, pickLatLng, editPositionLatLng;
    private String mELat, mELong;
    LatLng camaraLatLong;
    public Double latt, longg;
    boolean isEdit = false, iswoedit = false;
    boolean isMapIconClicked = false;
    boolean iscurrentlocation = false;
    SharedPreferences prefsL;
    SharedPreferences.Editor mEditorL;

    int LOCATION_PERMISSION_REQUEST_CODE = 156;
    Preferencehelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mEditorL = prefsL.edit();

        prefs = new Preferencehelper(this);

        findViews();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionModule permissionModule = new PermissionModule(getApplicationContext());
            permissionModule.checkPermissions();
          /*  ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
       */
        }


        initMap();

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key1), Locale.US);
        }
        PlacesClient placesClient = Places.createClient(this);
        final List<Place.Field> placeFields = new ArrayList<>(Arrays.asList(Place.Field.values()));
        dialogPaymentMode = new Dialog(ChooseLocationActivity.this);
        dialogPaymentMode.setContentView(R.layout.dialog_payment_selection);
        dialogPaymentMode.setCancelable(false);
        btnDemo = dialogPaymentMode.findViewById(R.id.clickok);
        btnLive = dialogPaymentMode.findViewById(R.id.clickok1);


        if (prefs.getPrefsPaymentoption().equalsIgnoreCase("Both") && !prefs.getPrefsContactId().isEmpty()) {
            dialogPaymentMode.show();
        }
        btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setPrefsPaymentoption("2");
                dialogPaymentMode.dismiss();
            }
        });
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setPrefsPaymentoption("1");
                dialogPaymentMode.dismiss();
            }
        });

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                ChooseLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ChooseLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                if (mMap != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longi), 16));
                }

                //     showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static final int REQUEST_LOCATION = 1;

    int intMapCallType = 0;
    LocationManager locationManager;
    String latitude, longitude;

    private void initMap() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }

        mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);
                   /* if (editPositionLatLng != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(editPositionLatLng, 16));

                    } else {
                        if (camaraLatLong != null) {
                            log.e("in zomm lat long==12" + camaraLatLong);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camaraLatLong, 16));
                        }
                    }*/
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setCompassEnabled(false);
                setUpMap();

            }
        });

    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        getLocation();
      /*  if (editPositionLatLng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(editPositionLatLng, 16));

        } else {
            if (latlng != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
            }
        }
*/
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (mMap != null) {
                    latitude = String.valueOf(cameraPosition.target.latitude);
                    longitude = String.valueOf(cameraPosition.target.longitude);
                    getLocationTxt();
                /*    log.e("Employee_location" + cameraPosition.target.toString());
                    camaraLatLong = new LatLng(Double.valueOf(cameraPosition.target.latitude), Double.valueOf(cameraPosition.target.longitude));
                    latitudeToServer = String.valueOf(cameraPosition.target.latitude);
                    longitudeToServer = String.valueOf(cameraPosition.target.longitude);
                    final AppClass globalVariable = (AppClass) getApplicationContext();
                    globalVariable.setlat(cameraPosition.target.latitude);
                    globalVariable.setlong(cameraPosition.target.longitude);
                    rbDay.setChecked(true);*/
                }
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        /*   mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);
      mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);*/
        // mMap.setOnMapClickListener(this);
//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//
//            public void onInfoWindowClick(Marker marker) {
//
//                String[] arr = marker.getSnippet().split("#");
//
//                if (arr.length > 0) {
//                    int id = Integer.parseInt(arr[0]);
//
//                    if (id > 3) {
//                        Intent i = new Intent(TestReversGeoCodeAcitivity.this, TestReversGeoCodeAcitivity.class);
//                        i.putExtra("pick_address", pickAddress);
//                        i.putExtra("pick_lat", pickLatLng.latitude);
//                        i.putExtra("pick_lng", pickLatLng.longitude);
//                        i.putExtra("isEditBooking", false);
//                        startActivity(i);
//                        marker.remove();
//                    }
//                }
//
//
//            }
//        });


        // updateMarkersOnMap();

        //getMyLocation();
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
    }

    View locationButton;

   /* private void getMyLocation() {
        locationButton = mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
    }*/

    public void getLocations() {
        ActivityCompat.requestPermissions(ChooseLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
      /*  switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 } else {
                    getLocations();
                }
                return;
            }

        }*/
    }

    private void findViews() {
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtAddress = (TextView) findViewById(R.id.txtAddress);


        btnSubmit.setOnClickListener(this);
    }

    public void getLocationTxt() {
        new fetchLocation().execute();

    }

    String country = "";

    @Override
    public void onClick(View v) {
     /*
        if(v==mLocationButton){
            checkLocationSettings();
        }
*/
        if (v == btnSubmit) {

            if (NetworkUtil.isConnectedToNetwork(ChooseLocationActivity.this)) {
                try {
                    Log.e("JHGFHJ", country);
                    //  country = "United States";
                    if (country.equalsIgnoreCase("India")) {

                        mEditorL.putString("cntry", country);
                        mEditorL.commit();

                        startActivity(new Intent(ChooseLocationActivity.this, HomeActivity.class));
                        finish();
                    } else if (country.equalsIgnoreCase("United States")) {
                        mEditorL.putString("cntry", country);
                        mEditorL.putString("addrs", txtAddress.getText().toString());
                        mEditorL.commit();
                        startActivity(new Intent(ChooseLocationActivity.this, OptionChooserActivity.class));
                        finish();
                    } else {

                        final CharSequence[] options = {"India", "United States", "Cancel"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLocationActivity.this);
                        builder.setMessage("Hi Dear, Our operation is only in india and USA.\n For other countries please whatsapp on +91-8826000390");

                        builder.setItems(options, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int item) {

                                if (options[item].equals("India")) {
                                    mEditorL.putString("cntry", "India");
                                    mEditorL.commit();
                                    startActivity(new Intent(ChooseLocationActivity.this, HomeActivity.class));
                                    finish();

                                    //   openCamera();
                                } else if (options[item].equals("United States")) {
                                    mEditorL.putString("cntry", "United States");
                                    mEditorL.commit();
                                    startActivity(new Intent(ChooseLocationActivity.this, OptionChooserActivity.class));
                                    finish();

                                } else if (options[item].equals("Cancel")) {

                                    dialog.dismiss();
                                }
                            }
                        });
                        builder.show();


                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    final CharSequence[] options = {"India", "United States", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLocationActivity.this);
                    builder.setMessage("Hi Dear, having issue while fetching the location , please select one of the our servicing location");

                    builder.setItems(options, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (options[item].equals("India")) {
                                mEditorL.putString("cntry", "India");
                                mEditorL.commit();
                                startActivity(new Intent(ChooseLocationActivity.this, HomeActivity.class));
                                finish();

                                //   openCamera();
                            } else if (options[item].equals("United States")) {
                                mEditorL.putString("cntry", "United States");
                                mEditorL.commit();
                                startActivity(new Intent(ChooseLocationActivity.this, OptionChooserActivity.class));
                                finish();

                            } else if (options[item].equals("Cancel")) {

                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();

                }
            } else {
                Crouton.showText(ChooseLocationActivity.this, "Please Connect To Internet", Style.ALERT);
            }
            JKHelper.closeKeyboard(ChooseLocationActivity.this, v);


        } else if (v.getId() == R.id.iv_sign) {
            {
                //  prefs.setIsSignatureTaker(false);
                //      startActivity(new Intent(this, CaptureSignAcitivity.class).putExtra("description", "Boarding_DeBoardingSignature").putExtra("imagetype", "Roster_Open_Detail").putExtra("pk",String.valueOf("22")).putExtra("tablenm", "Roster_Open_Detail").putExtra("imageid","22").putExtra("doctype","Boarding Deboarding Signature"));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10011) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            Log.i(TAG, "PlaceA: " + place.getName() + ", " + place.getLatLng().latitude);

            // TODO: Get info about the selected place.
            Log.e("NAMEA", place.getName() + "\n" + place.getAddress());
            Log.e("NAME11A", place.getLatLng().latitude + "\n" + place.getLatLng().longitude);
            latitudeToServer = place.getLatLng().latitude + "";
            longitudeToServer = place.getLatLng().longitude + "";

            mMap.clear();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 10.0f);

            mMap.animateCamera(cameraUpdate);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 10));


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
                    case 10011:


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
        getLocation();

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

    List<Address> addresses;

    public class fetchLocation extends AsyncTask<Void, Void, String> {
        String userResponse = "";

        private fetchLocation() {
        }

        protected void onPreExecute() {
            userResponse = "";
            addresses = new ArrayList<>();
            txtLocation.setText("Locating...");
            txtAddress.setText("");

            btnSubmit.setEnabled(false);

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Geocoder geocoder;

                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String city = addresses.get(0).getLocality();
                country = addresses.get(0).getCountryName();
                userResponse = "1";
                Log.e("JHGFHJ", country + "\n" + new Gson().toJson(addresses.get(0), Address.class));
            } catch (Exception e) {
                e.printStackTrace();
                userResponse = "";
            }
            return userResponse;
        }

        @Override
        protected void onPostExecute(String result) {

            if (!userResponse.isEmpty()) {
                StringBuilder sb = new StringBuilder();
               if (addresses.size()>0)
               {
                   for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                       if (i == addresses.get(0).getMaxAddressLineIndex()) {
                           sb.append(addresses.get(0).getAddressLine(i));
                       } else {
                           sb.append(addresses.get(0).getAddressLine(i) + ",");
                       }
                   }

                   if (addresses.size()>0)
                   {
                       txtLocation.setText(addresses.get(0).getCountryName());
                       txtAddress.setText(sb);
                   }

                   btnSubmit.setEnabled(true);
               }
            }
        }


    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.BLUE);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };
}
