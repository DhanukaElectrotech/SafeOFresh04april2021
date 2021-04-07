package com.dhanuka.morningparcel.activity.supplierorder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import androidx.annotation.Nullable;
//import androidx.viewpager.widget.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.activity.supplierorder.adapter.SubCategoryPagerAdapter;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.customViews.LoginView;
import com.dhanuka.morningparcel.customViews.StoreView;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.fragments.ProductFragment;
import com.dhanuka.morningparcel.utils.JKHelper;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends BaseActivity implements TextWatcher, ViewPager.OnPageChangeListener, ProductFragment.onSomeEventListener, OnTabSelectListener {
    @Nullable
    @BindView(R.id.etSearch)
    EditText etSearch;
    @Nullable
    @BindView(R.id.vpProducts)
    ViewPager vpProducts;
    @Nullable
    @BindView(R.id.tlSubCategory)
    SlidingTabLayout tlSubCategory;
    List<MainCatBean.CatBean> list;
    com.dhanuka.morningparcel.activity.supplierorder.adapter.SubCategoryPagerAdapter mAdapter;
    Context context;
    SupplierCartCountView countView;
    StoreView storeView;
    TextView msgheader;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_category;
    }

    int mPosition = 0;
    DatabaseManager dbManager;
    Preferencehelper preferencehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_category, container_body);
        ButterKnife.bind(this);
        context = CategoryActivity.this;
        list = new ArrayList<>();
        strOpen = "category";


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        list = (List<MainCatBean.CatBean>) args.getSerializable("list");
        mPosition = intent.getIntExtra("mPosition", 0);
        etSearch.addTextChangedListener(this);
        txt_set.setText("Safe'O'Fresh");
        backbtn_layout.setVisibility(View.VISIBLE);
        preferencehelper = new Preferencehelper(getApplicationContext());
        msgheader = (TextView) findViewById(R.id.msgheader);
        if (preferencehelper.getPrefsTag2Desc().isEmpty()) {

            msgheader.setVisibility(View.GONE);
        } else {
            msgheader.setVisibility(View.GONE);

        }
        msgheader.setText(preferencehelper.getPrefsTag2());
        msgheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferencehelper.getPrefsTag2Desc().isEmpty()) {


                } else {
                    JKHelper.openCommentDialog(CategoryActivity.this, preferencehelper.getPrefsTag2Desc());

                }

            }
        });

        SharedPreferences prefs11 = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);

        try {
            Log.e("listlist", list.size() + "");
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(prefs11.getString("resp1", "-1"));
            JSONArray jsonArray = jsonObject.getJSONArray("returnds");

            for (int a = 0; a < list.size(); a++) {
                int pCOunt = 0;
                if (jsonObject.getInt("success") == 1) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject newjson = jsonArray.getJSONObject(i);
                        if (newjson.getString("GroupID").equalsIgnoreCase(list.get(a).getStrId())) {
                            pCOunt++;
                        }

                    }

                }

                list.get(a).setProductCount(pCOunt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (vpProducts != null) {
            mAdapter = new SubCategoryPagerAdapter(getSupportFragmentManager(), list, Integer.valueOf("2"));

            vpProducts.setAdapter(mAdapter);
            tlSubCategory.setViewPager(vpProducts);
        }
        dbManager = DatabaseManager.getInstance(context);
        countView = (SupplierCartCountView) LayoutInflater.from(context).inflate(R.layout.action_supplier_cart, null);
        storeView = (StoreView) LayoutInflater.from(context).inflate(R.layout.action_shop, null);
        loginView = (LoginView) LayoutInflater.from(context).inflate(R.layout.action_login, null);

        vpProducts.setCurrentItem(mPosition);
        setCartCount();
    }

    private void setCartCount() {
        countView.setCount(dbManager.getTotalQty());
    }

    @Override
    protected void onSideSliderClick() {

    }

    @Override
    public void onResume() {
        super.onResume();
        etSearch.setText("");
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            setCartCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        etSearch.removeTextChangedListener(this);
        etSearch.setText("");
        etSearch.addTextChangedListener(this);
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
/*
  if (mAdapter!=null){
      if (mAdapter.mFragment!=null) {
          mAdapter.mFragment.makeFilter(editable.toString());
      }  }*/
        //     EventBus.getDefault().post(new SearchTextChangedEvent(editable.toString()));
    }

    @Override
    public void onTabSelect(int position) {
        vpProducts.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {
        vpProducts.setCurrentItem(position);
    }

    LoginView loginView;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItem item1 = menu.findItem(R.id.shop);
        item.setActionView(countView);
        item1.setActionView(storeView);
        item1.setVisible(false);
        MenuItem login = menu.findItem(R.id.login);
        login.setActionView(loginView);
        if (preferencehelper.getPREFS_trialuser().equalsIgnoreCase("0"))
        {
            item.setVisible(false);

        }
        else
        {
            item.setVisible(true);
        }
        try {
            if (prefs.getPrefsContactId().isEmpty()) {
                login.setVisible(true);
            } else {
                login.setVisible(false);

            }
        } catch (Exception e) {
            login.setVisible(true);

        }
        return true;
    }

    /*
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onSearchTextChanged(SearchTextChangedEvent event) {

        }*/
    @Override
    public void someEvent(String s) {

        countView.setCount(Integer.parseInt(s));
    }

}
