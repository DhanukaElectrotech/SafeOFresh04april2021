package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.dhanuka.morningparcel.beans.MainCatBean;

public class CatSpinnerAdapter extends BaseAdapter {
    Context context;
    int flags[];
    ArrayList<MainCatBean.CatBean> countryNames;
    LayoutInflater inflter;

    public CatSpinnerAdapter(Context applicationContext, ArrayList<MainCatBean.CatBean> mList) {
        this.context = applicationContext;
        this.flags = flags;
        this.countryNames = mList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(android.R.layout.simple_list_item_1, null);
        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        text1.setText(countryNames.get(i).getStrName());
        return view;
    }
}