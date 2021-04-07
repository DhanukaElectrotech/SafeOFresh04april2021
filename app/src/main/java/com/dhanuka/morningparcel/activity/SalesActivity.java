package com.dhanuka.morningparcel.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dhanuka.MMthinkBizUtils.DonutProgress;
import com.dhanuka.morningparcel.BuildConfig;
import com.dhanuka.morningparcel.R;
import com.flyco.tablayout.SlidingTabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.adapter.SalesPagerAdapter;
import com.dhanuka.morningparcel.events.ActivityCommunicator;
import com.dhanuka.morningparcel.events.Margindata;

public class SalesActivity extends AppCompatActivity implements ActivityCommunicator, Margindata {


    @Nullable
    @BindView(R.id.vpProducts)
    ViewPager vpProducts;
    @Nullable
    @BindView(R.id.tlSubCategory)
    SlidingTabLayout tlSubCategory;
    ArrayList<String> list = new ArrayList<>();

    public void makeBackClick(View v) {
        onBackPressed();
    }

    SharedPreferences prefs1;
    SharedPreferences.Editor editor;
    @BindView(R.id.txttitle)
    TextView txttitle;
    DonutProgress margintxt;
    TextView txtmargin;
    private static final String ACTION_CUSTOM_BROADCAST = "Marginupdates";



    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {


        LocalBroadcastManager.getInstance(SalesActivity.this)
                .registerReceiver(myReciever,
                        new IntentFilter("Marginupdates"));
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        txtmargin = findViewById(R.id.txtmargin);
        ButterKnife.bind(this);
        txttitle.setText("Sales Report");
        prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        margintxt = findViewById(R.id.txtmarginid);
        editor = prefs1.edit();
        editor.putString("grp", "");

        editor.commit();

        LocalBroadcastManager.getInstance(SalesActivity.this)
                .registerReceiver(myReciever,
                        new IntentFilter("Marginupdates"));
        txtmargin.setVisibility(View.GONE);
        txttitle.setVisibility(View.GONE);
        if (vpProducts != null) {
            list.add("Weekly Report");
            list.add("Item Wise Sales");
            list.add("Group Wise Sales");
            list.add("All Item Analysis");
            //   list.add("Item Supplier");
            vpProducts.setAdapter(new SalesPagerAdapter(getSupportFragmentManager(), list, 1));
            tlSubCategory.setViewPager(vpProducts);
//            tlSubCategory.setupWithViewPager(vpProducts);
        }


    }

    @Override
    public void passDataToActivity(String grp, String startdate, String todaydate) {
        Log.e("Group_data", grp);
        editor.putString("grp", grp);
        editor.putString("startdate", startdate);
        editor.putString("todaydate", todaydate);
        editor.commit();
        vpProducts.setCurrentItem(1);
        tlSubCategory.setCurrentTab(1);
        txtmargin.setVisibility(View.GONE);
        txttitle.setVisibility(View.GONE);

    }

    @Override
    public void Sendgergindata(String margindata) {
        txtmargin.setVisibility(View.VISIBLE);
        txttitle.setVisibility(View.VISIBLE);
        margintxt.setText("" + new DecimalFormat("#").format(Double.parseDouble(String.valueOf(margindata))));

    }

    BroadcastReceiver myReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String intentAction = intent.getStringExtra("margindata");
            txtmargin.setVisibility(View.VISIBLE);
            txttitle.setVisibility(View.VISIBLE);
            margintxt.setText("" + new DecimalFormat("#").format(Double.parseDouble(String.valueOf(intentAction))));


            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };


}