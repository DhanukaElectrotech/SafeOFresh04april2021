package com.dhanuka.morningparcel.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.CartActionListener;
import com.dhanuka.morningparcel.utils.Utility;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 *
 */
public class PastOrderItemAdapter extends RecyclerView.Adapter {

    List<CartProduct> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    int start=0;

    public PastOrderItemAdapter(Context context, List<CartProduct> objects) {

        this.context = context;
        list = objects;

        inflater = LayoutInflater.from(context);
    }

    public List<CartProduct> getWhatsapporder() {
        List<CartProduct> mList1 = new ArrayList<>();
        for (int a = 0; a < list.size(); a++) {
            if (list.get(a).isSetorder()) {
                mList1.add(list.get(a));
            }


        }
        Log.d("checklistsize", String.valueOf(mList1.size()));


        return mList1;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_past_order, parent, false);
        PastItemBeanHolder holder = new PastItemBeanHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        CartProduct product = list.get(position);
        PastItemBeanHolder viewHolder = (PastItemBeanHolder) holder;
        if (start==0)
        {
            product.setSetorder(false);
        }

        viewHolder.tvName.setText(product.getItemName());
        viewHolder.tvQty.setText("Qty. : " + String.valueOf(product.getQuantity()));
        //float price = Float.parseFloat(product.getSaleRate()) * product.getQuantity();
        // viewHolder.tvPrice.setText(context.getString(R.string.rupee) + " " + String.format("%.2f", price));
        viewHolder.tvPrice.setText("Category : " + product.getSaleRate());


        Glide.with(context)
                .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getHSNCode() + "&filePath=" + product.getItemBarcode())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(viewHolder.ivProduct);
        viewHolder.position = position;
        viewHolder.ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.Dialog_Confirmation(context, "http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getHSNCode() + "&filePath=" + product.getItemBarcode());
            }
        });




        viewHolder.wtsappcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start=1;

                product.setSetorder(true);
                notifyDataSetChanged();

            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PastItemBeanHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct;
        TextView tvName, tvPrice, tvQty;
        int position;
        CheckBox wtsappcheck;

        public PastItemBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivProduct = (ImageView) itemView.findViewById(R.id.pivProduct);
            tvName = (TextView) itemView.findViewById(R.id.ptvName);
            tvPrice = (TextView) itemView.findViewById(R.id.ptvprice);

            tvQty = (TextView) itemView.findViewById(R.id.ptvqty);
            wtsappcheck=(CheckBox) itemView.findViewById(R.id.wtsappcheck);
        }


    }
}
