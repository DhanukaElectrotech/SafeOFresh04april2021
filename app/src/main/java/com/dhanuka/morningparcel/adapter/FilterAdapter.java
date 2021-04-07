package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.beans.ChildCatBean;
import com.dhanuka.morningparcel.events.OnItemFilterListener;

//import androidx.recyclerview.widget.RecyclerView;


public class FilterAdapter extends RecyclerView.Adapter {
    OnItemFilterListener onLocationClickListener;
    List<ChildCatBean> list;
    Context context;
    private int lastCheckedPosition = -1;
    Preferencehelper prefs;
    int intArr[];

    public FilterAdapter(Context context, List<ChildCatBean> list, OnItemFilterListener onLocationClickListener, int lastCheckedPosition) {

        this.context = context;
        this.list = list;
        this.onLocationClickListener = onLocationClickListener;
        this.lastCheckedPosition = lastCheckedPosition;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_filter, parent, false);
        FilterAdapter.categorybean holder = new FilterAdapter.categorybean(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        FilterAdapter.categorybean viewHolder = (FilterAdapter.categorybean) holder;
        viewHolder.cityname.setText(list.get(position).getDescription());
        if (lastCheckedPosition == position) {
            viewHolder.mainL.setBackground(context.getResources().getDrawable(R.drawable.round_filter_circle_active));
            viewHolder.cityname.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.mainL.setBackground(context.getResources().getDrawable(R.drawable.round_country_circle_new));
            viewHolder.cityname.setTextColor(context.getResources().getColor(R.color.black));

        }
        viewHolder.mainL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastCheckedPosition = position;/*
                    for (int a=0;a<intArr.length;a++){
                        if (a==position){
                            intArr[a]=0;
                        }
                    }*/


                onLocationClickListener.onFilterClick(list.get(position));
                notifyDataSetChanged();
                //context.startActivity(new Intent(context, CityActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("cname",list.get(position)));


            }
        });


    }

    @Override
    public int getItemCount() {


        return list.size();

    }

    class categorybean extends RecyclerView.ViewHolder {

        TextView cityname;
        LinearLayout mainL;


        public categorybean(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cityname = itemView.findViewById(R.id.cityname);
            mainL = itemView.findViewById(R.id.mainL);


        }


    }

}