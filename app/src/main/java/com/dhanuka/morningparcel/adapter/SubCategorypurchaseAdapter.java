package com.dhanuka.morningparcel.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dhanuka.morningparcel.Helper.PurchaseBean;
import com.dhanuka.morningparcel.fragments.PurchaseFragment;

import java.util.List;

/**
 *
 */
public class SubCategorypurchaseAdapter extends FragmentStatePagerAdapter {

    List<PurchaseBean> list;
    int type;

    public SubCategorypurchaseAdapter(FragmentManager fm, List<PurchaseBean> list, int type) {
        super(fm);
        this.list = list;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        PurchaseBean category = list.get(position);
        Fragment fragment=null;
        fragment = new PurchaseFragment();
//        if (type==55){
//            fragment = new SaleItemsFragment();
//
//        }else {
//            fragment = new ItemsFragment();
//        }
        Bundle bundle = new Bundle();
        bundle.putString("suppliername", category.getSuppliername());
        bundle.putString("supplier_id", category.getSupplierid());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        PurchaseBean category = list.get(position);
        return category.getSuppliername();
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
