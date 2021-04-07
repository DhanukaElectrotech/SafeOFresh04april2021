package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.example.librarymain.DhanukaMain;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.InterfacePackage.OnLocationClickListener;
import com.dhanuka.morningparcel.adapter.LocationAdapterNew;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.SpacesItemDecoration;

public class LocationSelectionAcitivityNew extends AppCompatActivity implements OnLocationClickListener {
    int mType = 0;
    SharedPreferences prefsL;
    SharedPreferences.Editor mEditorL;
    @BindView(R.id.spinnerCountry)
    RecyclerView spinnerCountry;
    @BindView(R.id.llCity)
    LinearLayout llCity;
    @BindView(R.id.spinnerCity)
    RecyclerView spinnerCity;
    ArrayList<CatcodeHelper> arrayList = new ArrayList<>();
    ArrayList<CatcodeHelper> mListCities = new ArrayList<>();
    LocationAdapterNew countrycityAdapter;
    LocationAdapterNew cityAdapter;
    @BindView(R.id.llcountry)
    LinearLayout llcountry;

    Preferencehelper prefs;
    public void onBackClick(View view) {
        onBackPressed();
    }
    @BindView(R.id.llbanner)
    LinearLayout llbanner;
    @BindView(R.id.bannerimage)
    ImageView bannerimage;
    @BindView(R.id.bannertext)
    TextView bannertext;

    @BindView(R.id.backbtn)
    ImageView backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection_acitivity_new);
        ButterKnife.bind(this);
        bannerimage.setImageDrawable(getResources().getDrawable(R.drawable.imagecntry));
        bannertext.setText("Shop at your Convenience");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llcountry.getVisibility()==View.GONE)

                {
                    llcountry.setVisibility(View.VISIBLE);
                    llCity.setVisibility(View.GONE);
                    llbanner.setVisibility(View.VISIBLE);
                    bannerimage.setImageDrawable(getResources().getDrawable(R.drawable.citybanner));
                    bannertext.setText("We bring your Favourite local retailers and restaurant to your finger tip");

                }
                else
                {
                  onBackPressed();



                }
            }
        });


        llcountry.setVisibility(View.VISIBLE);
        mType = getIntent().getIntExtra("type", 1);

        prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mEditorL = prefsL.edit();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin1dp);
        spinnerCountry.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        spinnerCountry.setLayoutManager(new GridLayoutManager(this, 3));
        spinnerCountry.setHasFixedSize(true);
        spinnerCity.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        spinnerCity.setLayoutManager(new GridLayoutManager(this, 3));
        spinnerCity.setHasFixedSize(true);

        CatcodeHelper v = new CatcodeHelper();
        v.setCityname("India");
        v.setCityid("124");
        arrayList.add(v);

        CatcodeHelper v1 = new CatcodeHelper();
        v1.setCityname("United States");
        v1.setCityid("120");
        arrayList.add(v1);
        llCity.setVisibility(View.GONE);

        //        arrayList.add("Europe");
//       arrayList.add("Australia");
        cityAdapter = new LocationAdapterNew(getApplicationContext(), arrayList, true, this);
        countrycityAdapter = new LocationAdapterNew(getApplicationContext(), arrayList, false, this);
        spinnerCountry.setAdapter(countrycityAdapter);


    }


    public void getCity(String type, Context ctx) {

        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        mListCities = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        try {
                            String responses = DhanukaMain.SafeOBuddyDecryptUtils(response);
                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String catcodeid = newjson.getString("CatCodeID");
                                    String catcodedes = newjson.getString("CodeDescription");
                                    CatcodeHelper v = new CatcodeHelper();
                                    v.setCityname(catcodedes);
                                    v.setCityid(catcodeid);
                                    mListCities.add(v);
                                    llbanner.setVisibility(View.VISIBLE);
                                    bannerimage.setImageDrawable(getResources().getDrawable(R.drawable.citybanner));
                                    bannertext.setText("We bring your Favourite local retailers and restaurant to your finger tip");
                                    Log.d("catcodelist", String.valueOf(mListCities.size()));

                                }


                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                        if (mListCities.size() > 0) {
                            llCity.setVisibility(View.VISIBLE);

                        } else {
                            llCity.setVisibility(View.GONE);

                        }
                        cityAdapter = new LocationAdapterNew(LocationSelectionAcitivityNew.this, mListCities, true, LocationSelectionAcitivityNew.this);
                        spinnerCity.setAdapter(cityAdapter);
                        mProgressBar.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        cityAdapter = new LocationAdapterNew(LocationSelectionAcitivityNew.this, arrayList, true, LocationSelectionAcitivityNew.this);
                        spinnerCity.setAdapter(cityAdapter);
                        if (mListCities.size() > 0) {
                            llCity.setVisibility(View.VISIBLE);

                        } else {
                            llCity.setVisibility(View.GONE);

                        }   // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocationSelectionAcitivityNew.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("tokenval");
                    editor.commit();
                    String param = AppUrls.GET_REASON_LIST + "&contactid=" + "0" + "&type=" + type;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, LocationSelectionAcitivityNew.this);
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

        Volley.newRequestQueue(ctx).add(postRequest);
    }

    String strCid = "";

    @Override
    public void onLocationClick(String strId, String strType,String cityname) {

        prefs= new Preferencehelper(getApplicationContext());
        if (strType.equalsIgnoreCase("2")) {

            strCid = strId;
            llcountry.setVisibility(View.GONE);
            llbanner.setVisibility(View.GONE);
            getCity(strId, LocationSelectionAcitivityNew.this);
        } else {
            if (strCid.equalsIgnoreCase("120")) {
                mEditorL.putString("cntry", "United States");
                mEditorL.commit();
                prefs.setPrefsCountry("USA");
                //startActivity(new Intent(LocationSelectionAcitivityNew.this, EnterContactActivity.class).putExtra("signup,","2"));

                if (getIntent().getStringExtra("signup").equalsIgnoreCase("3"))
                {

                    startActivity(new Intent(LocationSelectionAcitivityNew.this, LoginActivity.class).putExtra("signup","2"));

                }
                else if (getIntent().getStringExtra("signup").equalsIgnoreCase("4"))
                {
                    startActivity(new Intent(LocationSelectionAcitivityNew.this, EnterContactActivity.class).putExtra("signup","1").putExtra("cityname",cityname));

                }
                else if (getIntent().getStringExtra("signup").equalsIgnoreCase("5"))
                {

                    startActivity(new Intent(LocationSelectionAcitivityNew.this, OptionChooserActivity.class).putExtra("signup","1").putExtra("cityname",cityname));

                }
            } else {
                mEditorL.putString("cntry", "India");
                mEditorL.commit();
                prefs.setPrefsCountry("India");

               if (getIntent().getStringExtra("signup").isEmpty())
               {

                   startActivity(new Intent(LocationSelectionAcitivityNew.this, OptionChooserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("cityname",cityname));

               }
               else
               {

                   if (getIntent().getStringExtra("signup").equalsIgnoreCase("3"))
                   {

                       startActivity(new Intent(LocationSelectionAcitivityNew.this, LoginActivity.class).putExtra("signup","2").putExtra("cityname",cityname));

                   }
                   else if (getIntent().getStringExtra("signup").equalsIgnoreCase("4"))
                   {
                       Log.d("checkbeforesignup","signupi=1");
                       startActivity(new Intent(LocationSelectionAcitivityNew.this, EnterContactActivity.class).putExtra("signup","1").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("cityname",cityname));

                   }
                   else if (getIntent().getStringExtra("signup").equalsIgnoreCase("5"))

                   {

                       startActivity(new Intent(LocationSelectionAcitivityNew.this, HomeActivity.class).putExtra("signup","1").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("cityname",cityname));

                   }

               }

              //  startActivity(new Intent(LocationSelectionAcitivityNew.this, EnterContactActivity.class).putExtra("signup,","2"));


            }
        }
    }
}
