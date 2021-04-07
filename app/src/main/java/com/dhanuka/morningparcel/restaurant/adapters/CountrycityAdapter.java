package com.dhanuka.morningparcel.restaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
//import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.InterfacePackage.TriggerClick;
import com.dhanuka.morningparcel.activity.CityActivity;
import com.dhanuka.morningparcel.activity.EnterContactActivity;
import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.List;

import butterknife.ButterKnife;


public class CountrycityAdapter extends RecyclerView.Adapter {

    List<String> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    CartItemsadd cartItemsadd;
    TriggerClick selectAddress;
    int listsizecounter;
    boolean city;



    public CountrycityAdapter(Context context, List<String> list,boolean city) {

        this.context = context;
        this.list=list;
        this.city=city;
        this.selectAddress=selectAddress;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_country, parent, false);
        CountrycityAdapter.categorybean holder = new CountrycityAdapter.categorybean(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {



        CountrycityAdapter.categorybean viewHolder = (CountrycityAdapter.categorybean) holder;
        viewHolder.cityname.setText(list.get(position));
        viewHolder.cityname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city==true)
                {
                    context.startActivity(new Intent(context, EnterContactActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("cname",list.get(position)));


                }
                else
                {
                    context.startActivity(new Intent(context, CityActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("cname",list.get(position)));


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


        public categorybean(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cityname = itemView.findViewById(R.id.cityname);


            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            Typeface boldfont = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Bold.ttf");

            cityname.setTypeface(boldfont);



        }


    }

}