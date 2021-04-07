package com.dhanuka.morningparcel.adapter;

import android.os.Bundle;/*
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;*/

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.fragments.ProductFragment;

import java.util.List;

/**
 *
 */
public class SubCategoryPagerAdapter extends FragmentStatePagerAdapter {

    List<MainCatBean.CatBean> list;
    int type;
    public Fragment mFragment = null;

    public SubCategoryPagerAdapter(FragmentManager fm, List<MainCatBean.CatBean> list, int type) {
        super(fm);
        this.list = list;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        MainCatBean.CatBean category = list.get(position);
        ProductFragment fragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("sub_category", category.getStrId());
        bundle.putString("category_id", category.getStrParentId());
        fragment.setArguments(bundle);
        mFragment = fragment;
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MainCatBean.CatBean category = list.get(position);
        return category.getStrName() +""/*+ " (" + category.getProductCount() + ")"*/;
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
