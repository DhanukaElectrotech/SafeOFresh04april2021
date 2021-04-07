package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.List;

import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.events.Dashclickevent;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.model.DashModel;

public class DashAdapter extends RecyclerView.Adapter {
    String currency = "";
    double dbDiscount = 0.0;

    private Context context;
    private List<DashModel> list;
    private List<DashModel> filteredList;
    onAddCartListener monAddCartListener;
    Dashclickevent dashclickevent;
    Preferencehelper prefs;

    public DashAdapter(Context context, List<DashModel> list, Dashclickevent dashclickevent) {
        this.context=context;
        this.dashclickevent=dashclickevent;
        this.list=list;



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dash_item, parent, false);
        customerholder holder = new customerholder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        DashModel product = list.get(position);
        prefs=new Preferencehelper(context);
        customerholder viewHolder = (customerholder) holder;
        viewHolder.cardname.setText(product.getCardname());
        viewHolder.cardimg.setImageResource(product.getCardimg());
        viewHolder.cardid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashclickevent.dashboardclick(Integer.parseInt(product.getStrid()));


            }
        });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class customerholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView cardname;
        ImageView cardimg;
        CardView cardid;



        int position;

        public customerholder(View view) {
            super(view);
            cardname = view.findViewById(R.id.cardname);
            cardid = view.findViewById(R.id.cardid);
            cardimg=view.findViewById(R.id.cardimg);


        }

        @Override
        public void onClick(View view) {
        }
    }


}
