package com.dhanuka.morningparcel.activity.supplierorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.dhanuka.morningparcel.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import com.dhanuka.morningparcel.fragments.OrderFragment;
import com.dhanuka.morningparcel.fragments.PayFragment;


public class SupplierOrderActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;

    public void onBackClick(View view) {
        onBackPressed();
    }

    //This is our viewPager
    private ViewPager viewPager;
    String isCustomer = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        if (getIntent().hasExtra("isCustomer")) {
            isCustomer = getIntent().getStringExtra("isCustomer");
        }
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Payments"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);
        ArrayList<String> mListpager = new ArrayList<>();
        mListpager.add("Order");
        mListpager.add("Payments");
        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), mListpager, tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {


    }


    public class Pager extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        int tabCount;
        ArrayList<String> mListpager;

        //Constructor to the class
        public Pager(FragmentManager fm, ArrayList<String> mListpager, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount = tabCount;
            this.mListpager = mListpager;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String category = mListpager.get(position);
            return category;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    OrderFragment orderFragment = new OrderFragment();
                    bundle.putString("isCustomer", isCustomer);
                    orderFragment.setArguments(bundle);

                    return orderFragment;
                case 1:
                    PayFragment payFragment = new PayFragment();
                    bundle.putString("isCustomer", isCustomer);
                    payFragment.setArguments(bundle);
                    return payFragment;
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }
}