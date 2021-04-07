package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.CategoryActivity;
import com.dhanuka.morningparcel.events.Newclick;

public class NewStorextockwiseadapter extends RecyclerView.Adapter {

    private Context context;
    private List<ItemMasterhelper> list;
    int mPos = 0;
    String currency;
    Preferencehelper prefs;
    Newclick newclick;

    public NewStorextockwiseadapter(Context context, List<ItemMasterhelper> list, Newclick newclick) {
        this.context = context;
        this.list = list;
        this.newclick=newclick;
        prefs = new Preferencehelper(context);
    }

    public void addItems(List<ItemMasterhelper> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void changeToEdit(int intType) {
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new_storewise, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemMasterhelper mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.tvName.setText(mCategoryBean.getItemName());
        viewHolder.tvOriginalPrice2.setText(mCategoryBean.getGroupID());
        viewHolder.tvQty.setText("Qty. : " + mCategoryBean.getAvailableQty());

        if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        try {
            viewHolder.tvOriginalPrice.setText(/*context.getResources().getString(R.string.rupee) +*/ currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(mCategoryBean.getSaleRate()))));
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.tvOriginalPrice.setText(/*context.getResources().getString(R.string.rupee) +*/ currency + mCategoryBean.getSaleRate());
        }
        //  viewHolder.img.setImageResource(mCategoryBean.getIntImg());
        if (!list.get(position).getFileName().isEmpty()) {
            Log.e("MIMGGG", "http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + mCategoryBean.getFilepath());
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context)
                .load(/*ApiClient.CAT_IMAGE_BASE_URL+*/"http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + mCategoryBean.getFilepath())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(viewHolder.ivProduct);
        viewHolder.position = position;

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newclick.senddata(list.get(position),position);
            }
        });




        if (!list.get(position).getSaleRate().isEmpty() && !list.get(position).getMRP().isEmpty()) {
            if (Float.parseFloat(list.get(position).getSaleRate()) < Float.parseFloat(list.get(position).getMRP())) {
                viewHolder.tvOriginalPrice1.setVisibility(View.VISIBLE);
                Float finalprice = Float.parseFloat(list.get(position).getMRP()) - Float.parseFloat(list.get(position).getSaleRate());


                viewHolder.tvOriginalPrice1.setPaintFlags(viewHolder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.tvOriginalPrice1.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(position).getMRP())));
                Float res = ((finalprice * 100.0f)) / Float.parseFloat(list.get(position).getMRP());
                viewHolder.tvSave.setText("Save " + currency + "" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))) + "");

            } else {
                viewHolder.tvOriginalPrice1.setVisibility(View.GONE);
                viewHolder.tvBarcode.setVisibility(View.GONE);
                viewHolder.tvSave.setVisibility(View.GONE);

            }
        } else {
            viewHolder.tvOriginalPrice1.setVisibility(View.GONE);
            viewHolder.tvBarcode.setVisibility(View.GONE);
            viewHolder.tvSave.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvQty)
        TextView tvQty;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvOriginalPrice)
        TextView tvOriginalPrice;
        @BindView(R.id.tvOriginalPrice2)
        TextView tvOriginalPrice2;
        @BindView(R.id.ivProduct)
        ImageView ivProduct;
        int position;

        @BindView(R.id.tvSave)
        TextView tvSave;

        @BindView(R.id.tvOriginalPrice1)
        TextView tvOriginalPrice1;
        @BindView(R.id.tvBarcode)
        TextView tvBarcode;
        @Nullable
        @BindView(R.id.card_view)
        CardView card_view;

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



    public void filterList(ArrayList<ItemMasterhelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public List<ItemMasterhelper> getChckedProducts() {
        List<ItemMasterhelper> mList = new ArrayList<>();
        for (int a = 0; a < list.size(); a++) {
            if (list.get(a).getChecked()) {
                mList.add(list.get(a));
            }
        }
        return mList;
    }

}
