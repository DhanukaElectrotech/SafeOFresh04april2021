package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.restaurant.adapters.CountrycityAdapter;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBean;

public class CityActivity extends AppCompatActivity {
    @BindView(R.id.countrytxt)
    TextView countrytxt;
    @BindView(R.id.countryadap)
    RecyclerView countryadap;
    ArrayList<String> arrayList = new ArrayList<>();
    GeoFenceBean geofence = new GeoFenceBean();
    CountrycityAdapter countrycityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        countryadap.setLayoutManager(new GridLayoutManager(this, 3));
        countryadap.setHasFixedSize(true);
        if (getIntent().getStringExtra("cname").equalsIgnoreCase("India")) {
            arrayList.add("Gurgaon");
            arrayList.add("Faridabad");
            countrytxt.setText(getIntent().getStringExtra("cname"));

        } else {
            arrayList.add("Chicago");
            arrayList.add("Atlanta");
            arrayList.add("New Jersey");
            arrayList.add("SanFrancisco");
            countrytxt.setText(getIntent().getStringExtra("cname"));


        }

        countrycityAdapter = new CountrycityAdapter(getApplicationContext(), arrayList,true);
        countryadap.setAdapter(countrycityAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), NewSignUp.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}