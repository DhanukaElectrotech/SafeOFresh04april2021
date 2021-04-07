package com.dhanuka.morningparcel.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.CityDAO;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.utils.AppUrls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AddNewSociety extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback,  View.OnScrollChangeListener {
     LatLng mPosition;
    float mZoom;
    boolean mTimerIsRunning;

    @BindView(R.id.spinnerCity)
    Spinner spinnercity;
    @BindView(R.id.addnewsociety)
    MaterialEditText addsociety;
    @BindView(R.id.addPhone)
    MaterialEditText addPhone;
    @BindView(R.id.spinnerStatee)
    MaterialEditText state;
    @BindView(R.id.submitbtn)
    Button submitbtn;
    String cityName = "";
    Dialog Localdialog;
    TextView textView;
    Button clickok;


    ArrayList<CatcodeHelper> listdatabasecity = new ArrayList<>();
    ArrayList<String> citylist = new ArrayList<>();
    ArrayList<String> cityid = new ArrayList<>();
    HashMap<String, String> cityhash = new HashMap<>();
    String selectedcity;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_society);
        ButterKnife.bind(this);
        selectedcity = getIntent().getStringExtra("city");
         SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        getItemFromCity();


        int count = citylist.size();

        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                //    String strString="";
                // if (citylist.get(j).length()>4){
                String[] strString = citylist.get(j).split(" ");

                // }else{
                //    strString=citylist.get(j);
//
                //  }

                if (citylist.get(i).contains(strString[0])) {
                    citylist.remove(j--);
                    count--;
                }
            }
        }
        ArrayAdapter cityadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, citylist);
        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercity.setAdapter(cityadapter);
        spinnercity.setSelection(cityadapter.getPosition(selectedcity));


        // CommonHelper.getbuilding("56", SignUpActivity.this);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (cityName==""){
                cityName = spinnercity.getSelectedItem().toString();
                //  }
/*                if (!cityName.equalsIgnoreCase(spinnercity.getSelectedItem().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Selected Address is not found on selected city.",Toast.LENGTH_LONG).show();


                }else */
                if (addsociety.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Society Name", Toast.LENGTH_LONG).show();


                } else if (addPhone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Phone Number", Toast.LENGTH_LONG).show();


                } else {
                    //  Toast.makeText(getApplicationContext(),"good to go ",Toast.LENGTH_LONG).show();

                    addsociety();
                }
            }
        });

        Localdialog = new Dialog(AddNewSociety.this);

        Localdialog.setContentView(R.layout.custom_dialogbox);
        Localdialog.setCancelable(false);
        Localdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textView = Localdialog.findViewById(R.id.custom_dialogtext);
        clickok = Localdialog.findViewById(R.id.clickok);


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);


        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

                mTimerIsRunning = true;
            }
        });


        try {
            Location loc = googleMap.getMyLocation();
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            mPosition = latLng;
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mMap.setMyLocationEnabled(false);


        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Cleaning all the markers.
                if (googleMap != null) {
                    googleMap.clear();
                }

                mPosition = googleMap.getCameraPosition().target;
                mZoom = googleMap.getCameraPosition().zoom;

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = new ArrayList<>();
                try {
                    if (String.valueOf(mPosition.longitude).length() > 3) {
                        addresses = geocoder.getFromLocation(mPosition.latitude, mPosition.longitude, 1);
                        try {
                            cityName = addresses.get(0).getLocality();
                        } catch (Exception e) {
                            cityName = "";
                        }
                        String stateName = addresses.get(0).getAdminArea();
                        state.setText(stateName);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (mTimerIsRunning) {

                }

            }
        });


    }

    public void getItemFromCity() {


        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        CityDAO pd = new CityDAO(database, this);
        listdatabasecity = pd.selectAll();

        Log.d("stringbuildinglist", String.valueOf(listdatabasecity.size()));
        for (int i = 0; i < listdatabasecity.size(); i++) {
            citylist.add(listdatabasecity.get(i).getCityname());
            cityid.add(listdatabasecity.get(i).getCityid());
            cityhash.put(listdatabasecity.get(i).getCityname(), listdatabasecity.get(i).getCityname());


        }


    }

    public void addsociety() {


        final ProgressDialog mProgressBar = new ProgressDialog(AddNewSociety.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseSignup", response);
                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper= new JKHelper();
                            String responses=jkHelper.Decryptapi(response,AddNewSociety.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("returnmessage");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String returnmessage = newjson.getString("returnmsg");
                                    try {
                                        int uid = Integer.parseInt(returnmessage);
                                        Localdialog.show();
                                        String sourceString = "<b>" + "Thank you for sumbit your details." + "</b> ";

                                        textView.setText(Html.fromHtml(sourceString) + "\n \n Our Executive will reach you in 24 hours or you can reach us on +91-8826000390");
                                        clickok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(getApplicationContext(), SignUpActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("city", spinnercity.getSelectedItem().toString()).putExtra("society", addsociety.getText().toString()));

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Localdialog.show();
                                        String sourceString = "<b>" + "Thank you for sumbit your details." + "</b> ";
                                        textView.setText(Html.fromHtml(sourceString) + "\nOur Executive will reach you in 24 hours or you can reach us on +91-8826000390");
                                        clickok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(getApplicationContext(), SignUpActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("city", spinnercity.getSelectedItem().toString()).putExtra("society", addsociety.getText().toString()));

                                            }
                                        });
                                    }
                                  /*  if (!returnmessage.equalsIgnoreCase("user already registered")) {

                                        Localdialog.show();
                                        textView.setText("User Register successfully");
                                        clickok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                finish();
                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), returnmessage, Toast.LENGTH_SHORT).show();
                                    }*/

                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        try {

                            mProgressBar.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();





                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = AppUrls.strAddsociety + "&lng=" + String.valueOf(mPosition.latitude) + "&lat=" + String.valueOf(mPosition.longitude)
                            + "&phoneno=" + addPhone.getText().toString() + "&citycode=" + spinnercity.getSelectedItem().toString() +
                            "&societyname=" + addsociety.getText().toString();
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, AddNewSociety.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }


    public void backClick(View view) {
        onBackPressed();
    }


}
