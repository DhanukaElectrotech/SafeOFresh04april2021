package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.CategoryActivity;
import com.dhanuka.morningparcel.beans.ItemOrderBean;

public class ItemOrderAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ItemOrderBean> list;
    private List<ItemOrderBean> list1;
    String currency;
    Preferencehelper prefs;

    public ItemOrderAdapter(Context context, List<ItemOrderBean> list) {
        this.context = context;
        this.list = list;
        this.list1 = list;
        prefs = new Preferencehelper(context);
    }

    public void addItems(List<ItemOrderBean> postItems) {
        list.addAll(postItems);
        list1.addAll(postItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_order, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemOrderBean mCategoryBean = list.get(position);
         CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.tvName.setText(mCategoryBean.getItemDescription());
        viewHolder.tvBarcode.setText(mCategoryBean.getBarCode());
        viewHolder.txtCat.setText(mCategoryBean.getGroupName());
        viewHolder.txtODType.setText(mCategoryBean.getOrderType());
        viewHolder.txtPayment.setText(mCategoryBean.getPayemntType());
        viewHolder.txtQty.setText(mCategoryBean.getRequestedQty());

        if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        //  viewHolder.img.setImageResource(mCategoryBean.getIntImg());
        viewHolder.position = position;

        try {
            viewHolder.txtMRP.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(position).getMRP())));
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.txtMRP.setText(currency + "0.0");
        }
        try {
            viewHolder.txtDiscount.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(position).getDiscount())));
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.txtDiscount.setText(currency + "0.0");
        }
        try {
            viewHolder.txtRAmt.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(position).getRAmount())));
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.txtRAmt.setText(currency + "0.0");
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtName)
        TextView tvName;

        int position;

        @BindView(R.id.txtBar)
        TextView tvBarcode;
        @BindView(R.id.txtCat)
        TextView txtCat;
        @BindView(R.id.txtODType)
        TextView txtODType;
        @BindView(R.id.txtDiscount)
        TextView txtDiscount;
        @BindView(R.id.txtRAmt)
        TextView txtRAmt;
        @BindView(R.id.txtQty)
        TextView txtQty;
        @BindView(R.id.txtMRP)
        TextView txtMRP;
        @BindView(R.id.txtPayment)
        TextView txtPayment;


        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  btnDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.imageView2:
                    Log.e("listlist", list.size() + "");


                    Intent intent = new Intent(context, CategoryActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("list", (Serializable) list);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("mPosition", position);
                    context.startActivity(intent);

                    break;
                case R.id.tvName:
                    // context.startActivity(new Intent(context, ProductsActivity.class));

                    break;
            }

        }
    }


}
