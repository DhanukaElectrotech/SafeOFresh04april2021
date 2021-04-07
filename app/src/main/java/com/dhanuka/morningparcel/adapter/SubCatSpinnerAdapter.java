package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.dhanuka.morningparcel.beans.ChildCatBean;

public class SubCatSpinnerAdapter extends BaseAdapter {
    Context context;
    int flags[];
    ArrayList<ChildCatBean>  countryNames;
    LayoutInflater inflter;

    public SubCatSpinnerAdapter(Context applicationContext,  ArrayList<ChildCatBean>  mList) {
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
        text1.setText(countryNames.get(i).getDescription());
        return view;
    }
}