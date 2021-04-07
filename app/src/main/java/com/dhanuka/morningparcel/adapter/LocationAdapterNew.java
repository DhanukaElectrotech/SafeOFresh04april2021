package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.InterfacePackage.OnLocationClickListener;
import com.dhanuka.morningparcel.InterfacePackage.TriggerClick;
import com.dhanuka.morningparcel.events.CartActionListener;

//import androidx.recyclerview.widget.RecyclerView;


public class LocationAdapterNew extends RecyclerView.Adapter {
    OnLocationClickListener onLocationClickListener;
    List<CatcodeHelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = -1;
    Preferencehelper prefs;
    CartItemsadd cartItemsadd;
    TriggerClick selectAddress;
    int listsizecounter;
    boolean city;
    int intArr[];

    public LocationAdapterNew(Context context, List<CatcodeHelper> list, boolean city, OnLocationClickListener onLocationClickListener) {

        this.context = context;
        this.list = list;
        this.onLocationClickListener = onLocationClickListener;
        this.city = city;
        this.selectAddress = selectAddress;
        intArr = new int[list.size()];
        for (int a = 0; a < list.size(); a++) {
            intArr[a] = 0;
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_country, parent, false);
        LocationAdapterNew.categorybean holder = new LocationAdapterNew.categorybean(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        LocationAdapterNew.categorybean viewHolder = (LocationAdapterNew.categorybean) holder;
        viewHolder.cityname.setText(list.get(position).getCityname());
        if (lastCheckedPosition == position) {
            viewHolder.mainL.setBackground(context.getResources().getDrawable(R.drawable.round_country_circle_active));
           // viewHolder.cityname.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.mainL.setBackground(context.getResources().getDrawable(R.drawable.round_country_circle));
          //  viewHolder.cityname.setTextColor(context.getResources().getColor(R.color.black));

        }
        viewHolder.cityname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs= new Preferencehelper(context);
                if (city == true) {
                    prefs.setPREFS_city(list.get(position).getCityname());
                    onLocationClickListener.onLocationClick(list.get(position).getCityid(), "1",list.get(position).getCityname());


                } else {

                    lastCheckedPosition = position;/*
                    for (int a=0;a<intArr.length;a++){
                        if (a==position){
                            intArr[a]=0;
                        }
                    }*/


                    onLocationClickListener.onLocationClick(list.get(position).getCityid(), "2",list.get(position).getCityname());
                    notifyDataSetChanged();
                    //context.startActivity(new Intent(context, CityActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("cname",list.get(position)));


                }
            }
        });


    }

    @Override
    public int getItemCount() {


        return list.size();

    }

    class categorybean extends RecyclerView.ViewHolder {

        TextView cityname, completeaddress, chooseadd;
        LinearLayout frame;
        LinearLayout mainL;


        public categorybean(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cityname = itemView.findViewById(R.id.cityname);
            mainL = itemView.findViewById(R.id.mainL);


            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            Typeface boldfont = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Bold.ttf");

          // cityname.setTypeface(boldfont);


        }


    }

}