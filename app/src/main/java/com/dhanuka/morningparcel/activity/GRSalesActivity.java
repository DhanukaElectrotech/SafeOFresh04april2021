package com.dhanuka.morningparcel.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dhanuka.morningparcel.R;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.adapter.SalesPagerAdapter;

public class GRSalesActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.vpProducts)
    ViewPager vpProducts;
    @Nullable
    @BindView(R.id.tlSubCategory)
    SlidingTabLayout tlSubCategory;
    ArrayList<String> list = new ArrayList<>();
    @BindView(R.id.txttitle)
    TextView txttitle;

    public void makeBackClick(View v) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        ButterKnife.bind(this);
        txttitle.setText("GR Report");
        if (vpProducts != null) {
            list.add("Weekly Report");
        /*   list.add("Item Wise Sales");
            list.add("Group Wise Sales");
            list.add("All Item Analysis");*/
       //   list.add("Item Supplier");
            vpProducts.setAdapter(new SalesPagerAdapter(getSupportFragmentManager(), list,2));
            tlSubCategory.setViewPager(vpProducts);
            tlSubCategory.setVisibility(View.GONE);
//            tlSubCategory.setupWithViewPager(vpProducts);
        }


    }
}