package com.dhanuka.morningparcel.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;
import java.util.List;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.OnQueryChangeListener;


/**
 * Created by Mr.Mad on 10/3/2017.
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 10;
    private Context mContext;
    private List<ItemMasterhelper> resultList = new ArrayList<ItemMasterhelper>();
    private List<ItemMasterhelper> filteredList = new ArrayList<ItemMasterhelper>();
     OnQueryChangeListener onQueryChangeListener;
    public AutoCompleteAdapter(Context context, List<ItemMasterhelper> resultList,  OnQueryChangeListener onQueryChangeListener) {

        mContext = context;
        this.resultList = resultList;
        this.onQueryChangeListener = onQueryChangeListener;
         filteredList.addAll(resultList);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public ItemMasterhelper getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ItemMasterhelper product = filteredList.get(position);
        if (convertView == null) {
            try {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_product, parent, false);

                //  view = (View) inflater.inflate(R.layout.item_product, parent, false);
            } catch (Exception e) {
                e.printStackTrace();
                //   view = (View) inflater.inflate(R.layout.item_product, parent, false);
            }
        } else {
            view = (View) convertView;
        }
        String price;
        ImageView ivProduct;
        TextView tvName, tvOriginalPrice, tvOriginalPrice1, tvOriginalPrice2, tvQty;
        Button btnAddToCart, btnAdd, btnReduce;
        int qty = 1;
        TextView tvManuName, btnOOS;

            //     view.setContent(product);
        return view;
    }
    /*{
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_product, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.countryName)).setText(getItem(position).getBrand());
        return convertView;
    }*/

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint.toString().length() >= 2) {
                    Log.e("MY_DATA", constraint.toString());
//                    List<GuideLinesBean> books = findBooks(mContext, constraint.toString());

                    onQueryChangeListener.onQueryChange(constraint.toString());

                  }else{
                    onQueryChangeListener.onBlank();

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<ItemMasterhelper>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
                Log.e("hjasdfhj","ksdf");
            }
        };
        return filter;
    }

    //download mGuideLinesBean list
 }
