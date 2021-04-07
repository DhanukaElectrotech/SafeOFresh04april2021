package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.restaurant.adapters.CountrycityAdapter;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBean;
import com.dhanuka.morningparcel.utils.SpacesItemDecoration;

public class NewSignUp extends AppCompatActivity {

    @BindView(R.id.spinnerCountry)
    RecyclerView spinnerCountry;
    ArrayList<String> arrayList= new ArrayList<>();
    GeoFenceBean geofence= new GeoFenceBean();
    CountrycityAdapter countrycityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_screennew);
        ButterKnife.bind(this);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin5dp);
        spinnerCountry.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        spinnerCountry.setLayoutManager(new GridLayoutManager(this, 3));
        spinnerCountry.setHasFixedSize(true);
        arrayList.add("India");
        arrayList.add("United States");
        arrayList.add("Europe");
        arrayList.add("Australia");
        countrycityAdapter= new CountrycityAdapter(getApplicationContext(),arrayList,false);
        spinnerCountry.setAdapter(countrycityAdapter);
    }
}