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


public class SubTatAdapter extends RecyclerView.Adapter<SubTatAdapter.payholder> {


    ArrayList<OrderBean.Trackhistoryclass> list;
    Context context;

    String type;



    @NonNull
    @Override
    public payholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sub_tat, viewGroup, false);

        return new payholder(view);
    }


    public SubTatAdapter(Context context,ArrayList<OrderBean.Trackhistoryclass> list) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public void onBindViewHolder(@NonNull payholder payholder, int i) {

        int mpos=i+1;

        OrderBean.Trackhistoryclass payhelper = list.get(i);
        SubTatAdapter.payholder viewHolder = (SubTatAdapter.payholder) payholder;
        viewHolder.orderststaustxt.setText("Order Status : "+payhelper.getOrderStatus());
        viewHolder.createdbytxt.setText("Created By : "+payhelper.getCreatedBy());
        viewHolder.suborderdatetxt.setText("Created Date : "+payhelper.getCreatedDate());
        viewHolder.txtsrno.setText(String.valueOf(mpos)+".");



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class payholder extends RecyclerView.ViewHolder {

        TextView  orderststaustxt, createdbytxt, suborderdatetxt,txtsrno;


        public payholder(View itemView) {
            super(itemView);
            orderststaustxt = (TextView) itemView.findViewById(R.id.orderststaustxt);
            createdbytxt = (TextView) itemView.findViewById(R.id.createdbytxt);
            suborderdatetxt = (TextView) itemView.findViewById(R.id.suborderdatetxt);
            txtsrno = (TextView) itemView.findViewById(R.id.txtsrno);

//            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
//            odamount.setTypeface(font);
//            oddate.setTypeface(font);
//            odtotal.setTypeface(font);

        }
    }

}
