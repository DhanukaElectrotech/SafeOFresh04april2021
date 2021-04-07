package com.dhanuka.morningparcel.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import com.dhanuka.morningparcel.fragments.GroupSalesFragment;
import com.dhanuka.morningparcel.fragments.ItemSalesFragment;
import com.dhanuka.morningparcel.fragments.ItemsAnalysisFragment;
import com.dhanuka.morningparcel.fragments.WeeklyGRSalesFragment;
import com.dhanuka.morningparcel.fragments.WeeklySalesFragment;

/**
 *
 */
public class SalesPagerAdapter extends FragmentStatePagerAdapter {

    List<String> list;
int type=1;
    public SalesPagerAdapter(FragmentManager fm, List<String> list,int type) {
        super(fm);
        this.list = list;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            if (type==1) {
                fragment = new WeeklySalesFragment();
            }else{
                fragment = new WeeklyGRSalesFragment();
            }
        } else if (position == 1) {
            fragment = new ItemSalesFragment();
        } else if (position == 2) {
            fragment = new GroupSalesFragment();
        }else{
            fragment = new ItemsAnalysisFragment();

        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String category = list.get(position);
        return category;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }
}
