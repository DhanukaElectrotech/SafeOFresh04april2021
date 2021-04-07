package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;

import com.dhanuka.morningparcel.Helper.Orderfraghelper;


public class OrderFragmentAdapter extends RecyclerView.Adapter<OrderFragmentAdapter.notifiholder> {


    ArrayList<Orderfraghelper> list;
    Context context;



    @NonNull
    @Override
    public notifiholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_fragmnt , viewGroup, false);

        return  new notifiholder(view);
    }


    public OrderFragmentAdapter(ArrayList<Orderfraghelper> list, Context context) {
        this.context= context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull notifiholder notifiholder, int i) {

        Orderfraghelper orderfraghelper= list.get(i);
        OrderFragmentAdapter.notifiholder viewHolder = ( OrderFragmentAdapter.notifiholder) notifiholder; ;
        viewHolder.odamount.setText(orderfraghelper.getOrderamount());
        viewHolder.oddate.setText(orderfraghelper.getOrderdate());
        viewHolder.odtotal.setText(orderfraghelper.getOrdertotal());


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

      class notifiholder extends RecyclerView.ViewHolder {

        TextView odamount,odtotal,oddate;

        public notifiholder(View itemView) {
            super(itemView);


            odtotal=(TextView)itemView.findViewById(R.id.odtotal);
            oddate=(TextView)itemView.findViewById(R.id.oddate);
            odamount=(TextView)itemView.findViewById(R.id.odamount);
//            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
//            odamount.setTypeface(font);
//            oddate.setTypeface(font);
//            odtotal.setTypeface(font);

        }
    }
}
