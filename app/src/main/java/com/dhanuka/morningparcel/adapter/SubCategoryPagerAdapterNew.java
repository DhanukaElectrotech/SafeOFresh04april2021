package com.dhanuka.morningparcel.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.fragments.ItemsFragment;
import com.dhanuka.morningparcel.fragments.SaleItemsFragment;

import java.util.List;

/**
 *
 */
public class SubCategoryPagerAdapterNew extends FragmentStatePagerAdapter {

    List<MainCatBean.CatBean> list;
    int type;

    public SubCategoryPagerAdapterNew(FragmentManager fm, List<MainCatBean.CatBean> list, int type) {
        super(fm);
        this.list = list;
        this.type = type;
     }

    @Override
    public Fragment getItem(int position) {
        MainCatBean.CatBean category = list.get(position);
        Fragment fragment=null;
        if (type==55){
            fragment = new SaleItemsFragment();

        }else {
            fragment = new ItemsFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("sub_category", category.getStrId());
        bundle.putString("category_id", category.getStrParentId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MainCatBean.CatBean category = list.get(position);
        return category.getStrName();
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
