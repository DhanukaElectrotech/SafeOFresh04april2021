package com.dhanuka.morningparcel.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dhanuka.morningparcel.activity.ItemHistoryActivity;
import com.dhanuka.morningparcel.beans.StorewiseBean;
import com.dhanuka.morningparcel.events.onAddCartListener;


/**
 * Created by Mr.Mad on 4/5/2017.
 */

public class StockWiseDetailAdapter extends RecyclerView.Adapter {
    String currency = "";
    double dbDiscount = 0.0;
    boolean isSelectedAll = false;

    private Context context;
    private List<StorewiseBean.storeinnerbean> list;
    private List<StorewiseBean.storeinnerbean> filteredList;
    onAddCartListener monAddCartListener;

    public void filterList(ArrayList<StorewiseBean.storeinnerbean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }



    public StockWiseDetailAdapter(Context context, List<StorewiseBean.storeinnerbean> list) {
        this.context = context;
        this.isSelectedAll = isSelectedAll;
        this.list = list;



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store_stock, parent, false);
        OrderHistoryHolder holder = new OrderHistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        try {
            StorewiseBean.storeinnerbean product = list.get(position);
            OrderHistoryHolder viewHolder = (OrderHistoryHolder) holder;
            viewHolder.setIsRecyclable(false);
            DecimalFormat precision = new DecimalFormat("0");
            viewHolder.marginpid.setText(":  "+precision.format(Double.parseDouble(String.valueOf(product.getInQty()))));
            viewHolder.lastweeksaleid.setText(":  "+precision.format(Double.parseDouble(String.valueOf(product.getTotalSale()))));
            viewHolder.purchaseweekid.setText(":  "+precision.format(Double.parseDouble(String.valueOf(product.getTotalPurchase()))));
            viewHolder.groupnameid.setText(":  "+ precision.format(Double.parseDouble(String.valueOf(product.getOutQty()))));
            viewHolder.tvName.setText(product.getItemName());
            viewHolder.txtblc.setText(":  "+precision.format(Double.parseDouble(String.valueOf(product.getBalance()))));

            viewHolder.txtgname.setText(":  "+product.getGroupName());

            viewHolder.txtsgroup.setText(":  "+product.getSubGroupName());
            viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context,ItemHistoryActivity.class).putExtra("mData",product.getItemID()).putExtra("image", list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
            if (position==0)
            {
                viewHolder.gnametxt.setText(product.getGroupName());
            }
            else
            {
                if (list.get(position).getGroupName().equalsIgnoreCase(list.get(position-1).getGroupName()))
                {


                    viewHolder.gnametxt.setText("");


                }
                else
                {
                    viewHolder.gnametxt.setText(product.getGroupName());



                }

            }




            try {
                 Picasso.with(context).load("http://mmthinkbiz.com/ImageHandler.ashx?image="+list.get(position).getFileName()+"&filePath="+product.getFilepath()).placeholder(R.drawable.no_image).into(viewHolder.pimage);

             }
             catch (Exception e)
             {
                 e.printStackTrace();
             }


            viewHolder.position = position;
            Log.e("positionposition", position + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tvName;

        TextView tvQty, purchaseweekid;
        TextView groupnameid, lastweeksaleid, marginpid,txtblc,txtgname,txtsgroup;
        ImageView pimage;
        TextView gnametxt;


        int position;

        public OrderHistoryHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            gnametxt = (TextView) view.findViewById(R.id.gnametxt);
            groupnameid = (TextView) view.findViewById(R.id.groupnameid);
            txtgname = (TextView) view.findViewById(R.id.txtgname);
            txtsgroup = (TextView) view.findViewById(R.id.txtsgroup);
            pimage = (ImageView) view.findViewById(R.id.pimage);
            lastweeksaleid = (TextView) view.findViewById(R.id.lastweeksaleid);
            marginpid = (TextView) view.findViewById(R.id.marginpid);
            txtblc = (TextView) view.findViewById(R.id.txtblc);
            purchaseweekid = (TextView) view.findViewById(R.id.purchaseweekid);


        }

        @Override
        public void onClick(View view) {
        }
    }


}
