package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.Notification_Adapter;
import com.dhanuka.morningparcel.beans.NotificationBean;
import com.dhanuka.morningparcel.beans.NotificationBeanMain;
import com.dhanuka.morningparcel.utils.AppUrls;
import com.dhanuka.morningparcel.utils.JKHelper;

public class Notification_Bell extends AppCompatActivity {

    RecyclerView recycle_notification;
    TextView toolbartitle,notfound;
    ImageView searchimageicon,backbtn,search_btn,interchange;
    LinearLayout Backbuttontoolbar,driverlist_bg,not_found;
    SearchView searchView;
    Notification_Adapter mAdapter;
    NotificationBeanMain notificationBeanMain;
    ArrayList<NotificationBean> mListNotificationBean;
    Notification_Adapter notification_adapter;
    Preferencehelper prefs;
    SharedPreferences sharedPreferences;
    String Lightheme;
    ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         intialization();
         loadnotification();

    }
    public void loadnotification()
    {

        String mCurrentTimeDataBase = JKHelper.getCurrentDate();
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.strNotification,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.dismiss();
                        try {
                            JSONObject jsonObject =new JSONObject(response);

                            if (jsonObject.getString("success").equalsIgnoreCase("1"))
                            {
                                NotificationBeanMain notifiopen = new Gson().fromJson(response, NotificationBeanMain.class);
                                notificationBeanMain = new NotificationBeanMain();
                                mListNotificationBean = notifiopen.getmListnotificationdata();
                                notification_adapter = new Notification_Adapter(mListNotificationBean, getApplicationContext());
                                recycle_notification.setAdapter(notification_adapter);
                            }
                            else
                            {
                                not_found.setVisibility(View.VISIBLE);
                                FancyToast.makeText(getApplicationContext(),"Notification not found", FancyToast.LENGTH_LONG,FancyToast.INFO, false).show();
                            }





                        } catch (Exception e) {
                            FancyToast.makeText(getApplicationContext(), "failed", FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                            e.printStackTrace();
                        }


                    }
                },
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
                // params1.put("contactid", prefs.getPrefsContactId());
                params1.put("CId", prefs.getPrefsContactId());
                params1.put("fdate", "05/01/2020");
                params1.put("tdate",mCurrentTimeDataBase);
                return params1;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);



    }
    public void intialization()
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Lightheme = sharedPreferences.getString("Theme", "");
        prefs=new Preferencehelper(getApplicationContext());
        setContentView(R.layout.activity_notification_bell);
        driverlist_bg=findViewById(R.id.driverlist_bg);
        notfound=findViewById(R.id.notfound);
        interchange=findViewById(R.id.interchange);
        interchange.setVisibility(View.GONE);

        if (Lightheme.equalsIgnoreCase("1"))
        {
            driverlist_bg.setBackgroundColor(Color.parseColor("#E131333D"));
            notfound.setTextColor(Color.parseColor("#ffffffff"));

        }
        else
        {
            driverlist_bg.setBackgroundColor(Color.parseColor("#ffffffff"));
            notfound.setTextColor(Color.parseColor("#E131333D"));
        }
        toolbartitle = findViewById(R.id.txt_set);
        not_found=findViewById(R.id.not_found);
        Backbuttontoolbar = findViewById(R.id.backbtn_layout);
        Backbuttontoolbar.setVisibility(View.VISIBLE);
        searchimageicon = findViewById(R.id.imgSearch);
        searchimageicon.setVisibility(View.GONE);
        searchView = findViewById(R.id.search);
        search_btn=findViewById(R.id.searchicon);
        search_btn.setVisibility(View.INVISIBLE);
        backbtn=findViewById(R.id.imgback);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView.setVisibility(View.GONE);
        toolbartitle.setText("Notification");
        recycle_notification = (RecyclerView) findViewById(R.id.notification_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycle_notification.setLayoutManager(linearLayoutManager);
        recycle_notification.setHasFixedSize(true);
          mProgressBar = new ProgressDialog(Notification_Bell.this);
        mProgressBar.setTitle("SafeOBuddy");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
    }


}
