package com.dhanuka.morningparcel.adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.dhanuka.morningparcel.beans.StoreBean;
import com.dhanuka.morningparcel.fragments.ItemsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CategoryPagerAdapter extends FragmentStatePagerAdapter {

    List<String> list;
    int shopId, catId;
    ArrayList<ArrayList<StoreBean>> mListStoreData;
    public CategoryPagerAdapter(FragmentManager fm, List<String> list, int shopId, ArrayList<ArrayList<StoreBean>> mListStoreData) {
        super(fm);
        this.list = list;
        this.shopId = shopId;
        this.mListStoreData = mListStoreData;
        this.catId = catId;
    }

    @Override
    public Fragment getItem(int position) {
        String category = list.get(position);
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", category);
        bundle.putSerializable("mListStoreData", mListStoreData.get(position));
         fragment.setArguments(bundle);

        Log.e("HGGG",mListStoreData.get(position).size()+" = "+list.get(position));
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
