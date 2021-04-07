package com.dhanuka.morningparcel.restaurant.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.InterfacePackage.TriggerClick;
import com.dhanuka.morningparcel.restaurant.adapters.SavedAdressadapter;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBean;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBeanMain;
import com.dhanuka.morningparcel.restaurant.models.Addresshelper;
import com.dhanuka.morningparcel.utils.JKHelper;

public class SavedAddress extends AppCompatActivity {
    @BindView(R.id.saveaddcontainer)
    RecyclerView saveaddcontainer;

    GeoFenceBeanMain Mgeofencebeanmain;
    int activitytype;
    private GoogleMap mMap;
    ArrayList<GeoFenceBean> geofencelist = new ArrayList<GeoFenceBean>();
    ArrayList<Addresshelper> addresslist = new ArrayList<>();
    Addresshelper addresshelper;
    SavedAdressadapter savedAdressadapter;
    BottomSheetDialog dialog;
    RecyclerView savedrecysheet;
    Button savebutton;
    TextView alladdress;

    TextView selectlocation, selectdelivtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_address);
        ButterKnife.bind(this);


        addresshelper = new Addresshelper();
        saveaddcontainer.setHasFixedSize(true);
        saveaddcontainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//        saveaddcontainer.setAdapter(new AddressHistoryAdapter(getApplicationContext(),addresslist));

//        View view = getLayoutInflater().inflate(R.layout.bottomsheet_addressselection, null);
//        dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog); // Style here
//        dialog.setContentView(view);
//        savedrecysheet = dialog.findViewById(R.id.saveaddcontainer);
//        savebutton = dialog.findViewById(R.id.btn_submit);
//        alladdress= dialog.findViewById(R.id.alladdress);
//
//        selectdelivtxt = dialog.findViewById(R.id.selectdelivtxt);
//        selectlocation = dialog.findViewById(R.id.selectlocation);
//
//        Typeface boldfont = Typeface.createFromAsset(
//                getApplicationContext().getAssets(),
//                "fonts/Roboto-Bold.ttf");
//        selectdelivtxt.setTypeface(boldfont);
//           activitytype=Integer.parseInt(getIntent().getStringExtra("apptype"));
//        selectlocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SavedAddress.this, AddLatLong.class).putExtra("type", "1").putExtra("tripdids", "").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//            }
//        });
//        alladdress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                activitytype=2;
//                showaddresslist();
//            }
//        });
//
//        savedrecysheet.setHasFixedSize(true);
//        savedrecysheet.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
//        //   savedrecysheet.setAdapter(new SavedAdressadapter(getApplicationContext(),addresslist));
        showaddresslist();
//        dialog.setCancelable(false);
//        dialog.show();

    }

    public void showaddresslist() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        final ProgressDialog mProgressBar = new ProgressDialog(SavedAddress.this);
        mProgressBar.setTitle("SafeOBuddy");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responsive", response);
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, SavedAddress.this);
                            mProgressBar.dismiss();

                            GeoFenceBeanMain mgeofencebeanmain = new Gson().fromJson(responses, GeoFenceBeanMain.class);
                            Mgeofencebeanmain = mgeofencebeanmain;


                            if (mgeofencebeanmain.getStrSuccess() == 1) {
                                geofencelist = mgeofencebeanmain.getStrreturns();
                                savedAdressadapter = new SavedAdressadapter(getApplicationContext(), geofencelist, activitytype, (TriggerClick) SavedAddress.this);
                                saveaddcontainer.setAdapter(savedAdressadapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                com.dhanuka.morningparcel.Helper.Preferencehelper prefs = new Preferencehelper(getApplicationContext());


                try {

                    String param = getString(R.string.URL_GEOFENCE) + "&ClientID=" + prefs.getPrefsContactId();
                    Log.e("beforeenc", param);
                    JKHelper jkHelper = new JKHelper();
                    param = jkHelper.Encryptapi(param, SavedAddress.this);
                    params1.put("val", param);
                    Log.e("afterenc", param);
                    return params1;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params1;
                }

            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(SavedAddress.this).add(postRequest);


    }
}
