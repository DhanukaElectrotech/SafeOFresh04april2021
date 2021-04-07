package com.dhanuka.morningparcel.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.beans.TatReportBean;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.Payfraghelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.OrderDetailActivity;
import com.dhanuka.morningparcel.activity.WalleTransactionActivity;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.utils.JKHelper;


public class TatAdapter extends RecyclerView.Adapter<TatAdapter.payholder> {


    ArrayList<TatReportBean.tatinnerbean> list;
    Context context;
    Preferencehelper prefs;
    OrderBean orderBean;
    ArrayList<OrderBean> orderBeans = new ArrayList<>();
    String type;


    @NonNull
    @Override
    public payholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tat, viewGroup, false);

        return new payholder(view);
    }


    public TatAdapter(ArrayList<TatReportBean.tatinnerbean> list, Context context, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public void onBindViewHolder(@NonNull payholder payholder, int i) {

        TatReportBean.tatinnerbean payhelper = list.get(i);
        TatAdapter.payholder viewHolder = (TatAdapter.payholder) payholder;
        viewHolder.orderlistrecy.setHasFixedSize(true);
        viewHolder.orderlistrecy.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        ;
     //   viewHolder.orderlistrecy.setAdapter(new SubTatAdapter(context,payhelper.getTrackhistory()) );


        viewHolder.ordertotaltxt.setText("Total : "+"₹" + payhelper.getRAmount());

        viewHolder.orderidtxt.setText("Order ID : " + payhelper.getOrderID());
        viewHolder.customernametxt.setText("Customer Name : " + payhelper.getCustomerName());
        viewHolder.orderdatetxt.setText("Order Date : " + payhelper.getOrderDate());
        viewHolder.itemcount.setText("Item Count : " + payhelper.getRItemCOunt());
        viewHolder.delayreasontxt.setText("Dealy Reason : " + payhelper.getDelayReason());

        viewHolder.icextend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.orderlistrecy.getVisibility()==View.VISIBLE)
                {
                    viewHolder.orderlistrecy.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.orderlistrecy.setVisibility(View.VISIBLE);

                }


            }
        });
        viewHolder.orderlistrecy.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class payholder extends RecyclerView.ViewHolder {

        TextView ordertotaltxt, orderidtxt, customernametxt, orderdatetxt, itemcount, delayreasontxt;

        RecyclerView orderlistrecy;
        ImageView icextend;

        public payholder(View itemView) {
            super(itemView);


            ordertotaltxt = (TextView) itemView.findViewById(R.id.ordertotaltxt);
            orderidtxt = (TextView) itemView.findViewById(R.id.orderidtxt);
            customernametxt = (TextView) itemView.findViewById(R.id.customernametxt);

            orderdatetxt = itemView.findViewById(R.id.orderdatetxt);

            icextend=itemView.findViewById(R.id.icextend);

            itemcount = itemView.findViewById(R.id.itemcount);


            delayreasontxt = itemView.findViewById(R.id.delayreasontxt);


            orderlistrecy=itemView.findViewById(R.id.orderlistrecy);
//            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
//            odamount.setTypeface(font);
//            oddate.setTypeface(font);
//            odtotal.setTypeface(font);

        }
    }

}
