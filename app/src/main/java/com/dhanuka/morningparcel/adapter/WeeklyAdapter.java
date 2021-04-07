package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.DayHelper;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.events.CartActionListener;

public class WeeklyAdapter extends RecyclerView.Adapter {

    List<DayHelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    CartItemsadd cartItemsadd;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    private List<DayHelper> filteredList;

    public void filterList(ArrayList<DayHelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public WeeklyAdapter(Context context, List<DayHelper> objects) {

        this.context = context;
        list = objects;
        filteredList = new ArrayList<>();
        this.cartItemsadd = cartItemsadd;
        filteredList.addAll(this.list);
        inflater = LayoutInflater.from(context);

        prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
//
//        try {
//            if (list.size()>0) {
//                cartItemsadd.sendtimeslotval(list.get(0).getTimestr(), list.get(0).getVal1str());
//            }else{
//                cartItemsadd.sendtimeslotval("", "0.0");
//
//            }  }catch (Exception e){
//
//        }
    }

    String selectedId = "";
    String selectedName = "";
    String Alartphonenumber = "";
    String PhonePe = "";
    String PaytmNumber = "";
    String Currency = "";
    String GooglePay = "";
    int daycount = 0;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weekly, parent, false);
        PastItemBeanHolder holder = new PastItemBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DayHelper shop = list.get(position);


        PastItemBeanHolder viewHolder = (PastItemBeanHolder) holder;


        
        viewHolder.daycount.setText(shop.getDayname());
        viewHolder.adddaytxt.setText(shop.getAddval());
        viewHolder.minusday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(list.get(position).getAddval())>0){
                 list.get(position).setAddval((Integer.parseInt(list.get(position).getAddval())-1)+"");
               notifyDataSetChanged();
                }

            }
        });


        viewHolder.addday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     list.get(position).setAddval((Integer.parseInt(list.get(position).getAddval())+1)+"");
                    notifyDataSetChanged();




            }
        });


    }
//


    @Override
    public int getItemCount() {
        return list.size();
    }


    class PastItemBeanHolder extends RecyclerView.ViewHolder {


        TextView addday, minusday, adddaytxt, daycount;


        public PastItemBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            addday = (TextView) itemView.findViewById(R.id.addday);
            minusday = (TextView) itemView.findViewById(R.id.minusday);
            daycount = (TextView) itemView.findViewById(R.id.daycount);
            adddaytxt = (TextView) itemView.findViewById(R.id.adddaytxt);


        }


    }


    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }

    int minteger;

    public int increaseInteger(View view) {
        minteger = minteger + 1;
        return minteger;

    }
    public List<DayHelper> makeOrder(){


        return list;
    }

    public int decreaseInteger(View view) {
        minteger = minteger - 1;
        return minteger;
    }


}
