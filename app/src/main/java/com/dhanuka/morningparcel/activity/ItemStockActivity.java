package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.fragments.ItemStockFragment;

public class ItemStockActivity extends AppCompatActivity {

    Context mActivity;
    ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
    SharedPreferences prefsss;
    SharedPreferences.Editor mEditor;
    @BindView(R.id.btn_back)
    TextView btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // binding = DataBindingUtil.setContentView(this, R.layout.activity_item_stock);


          setContentView(R.layout.activity_item_stock);
        ButterKnife.bind(this);
        prefsss = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mEditor = prefsss.edit();
        mEditor.putString("isIntent", "1");
        mEditor.commit();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemStockActivity.super.onBackPressed();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment mFragment = new ItemStockFragment();
        Bundle bundle = new Bundle();
        bundle.putString("itemId", getIntent().getStringExtra("itemId"));
      //  bundle.putString("branchName", getIntent().getStringExtra("branchName"));
       // bundle.putString("branchId", getIntent().getStringExtra("branchId"));
        mFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameLayout, mFragment).commit();


    }


}