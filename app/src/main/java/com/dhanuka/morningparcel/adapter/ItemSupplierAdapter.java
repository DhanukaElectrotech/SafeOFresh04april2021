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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.CategoryActivity;
import com.dhanuka.morningparcel.beans.ItemSuppliersBean;

public class ItemSupplierAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ItemSuppliersBean> list;
    private List<ItemSuppliersBean> list1;
    String currency;
    Preferencehelper prefs;

    public ItemSupplierAdapter(Context context, List<ItemSuppliersBean> list) {
        this.context = context;
        this.list  = list;
        prefs = new Preferencehelper(context);
    }

    public void addItems(List<ItemSuppliersBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_supplier, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemSuppliersBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.tvName.setText(mCategoryBean.getFullName());
        viewHolder.tvBarcode.setText(mCategoryBean.getAlartphonenumber());
        viewHolder.txtCat.setText(mCategoryBean.getGSTNO());
        //  viewHolder.img.setImageResource(mCategoryBean.getIntImg());
        viewHolder.position = position;

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
