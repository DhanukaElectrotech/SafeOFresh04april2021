package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dhanuka.morningparcel.activity.ItemStockActivity;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.beans.ItemSalesReport;

public class ItemReportAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ItemSalesReport> list;
    int mPos = 0;
    int start = 0;
    int isSelectedAll=2;


    public void selectAll(){
        Log.e("onClickSelectAll","yes");
        isSelectedAll=1;
        notifyDataSetChanged();
    }

    public void UnselectAll(){
        Log.e("onClickSelectAll","no");
        isSelectedAll=0;
        notifyDataSetChanged();
    }

    public List<ItemSalesReport> getWhatsappordersend() {
        List<ItemSalesReport> mList1 = new ArrayList<>();

        for (int a = 0; a < list.size(); a++) {

            if (isSelectedAll==1)
            {
                mList1.add(list.get(a));

            }
            else
            {
                if (list.get(a).getWhtsappcheck()) {
                    mList1.add(list.get(a));
                }
            }



        }
        Log.d("checklistsize", String.valueOf(mList1.size()));


        return mList1;
    }

    public ItemReportAdapter(Context context, List<ItemSalesReport> list) {
        this.context = context;
        this.list = list;
        for (int a = 0; a < list.size(); a++) {
            list.get(a).setOpened(false);
        }

    }


    public void addItems(List<ItemSalesReport> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<ItemSalesReport> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_item_sales, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemSalesReport mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;


        if (start == 0) {
            list.get(position).setWhtsappcheck(false);
        }




        viewHolder.txtName.setText(mCategoryBean.getItemDescription() + " ");
        viewHolder.txtCat.setText(mCategoryBean.getGroupName() + " ");
        viewHolder.txtqqty.setText("Qty. : " + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getQty())));
        viewHolder.txtBar.setText(mCategoryBean.getBarCode() + " ");
        viewHolder.txtProfit.setText(mCategoryBean.getProfitP() + " ");
        viewHolder.txtCostPrice.setText(mCategoryBean.getProfit() + " ");
        viewHolder.txtUnitCost.setText(mCategoryBean.getUnitCost() + " ");
        viewHolder.txtMRP.setText(mCategoryBean.getMRP() + " ");
        viewHolder.txtqqty.setVisibility(View.GONE);

        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, ItemHistoryActivity.class).putExtra("mData", list.get(position).getItemID()).putExtra("image", list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath()).putExtra("mtype","1"));

                context.startActivity(new Intent(context, ItemStockActivity.class).putExtra("itemId", list.get(position).getItemID()).putExtra("image", list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath()));


            }
        });

        try {
            Glide.with(context)
                    .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                    .into(viewHolder.ivProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (list.get(position).getOpened()) {


                viewHolder.hiddenLayout.setVisibility(View.VISIBLE);
                viewHolder.imgArrow.setImageResource(R.drawable.up_arrow_show);
            } else {


                viewHolder.imgArrow.setImageResource(R.drawable.down_arrow_hide);
                viewHolder.hiddenLayout.setVisibility(View.GONE);
            }

            //in some cases, it will prevent unwanted situations
            viewHolder.wtsappcheckitem.setOnCheckedChangeListener(null);

            if (isSelectedAll==1)
            {
                viewHolder.wtsappcheckitem.setChecked(true);
            }
            else  if (isSelectedAll==0)
            {
                viewHolder.wtsappcheckitem.setChecked(false);
            }
            else if (isSelectedAll==2)
            {
                if (list.get(position).getWhtsappcheck() == true) {
                    viewHolder.wtsappcheckitem.setChecked(true);

                } else {
                    viewHolder.wtsappcheckitem.setChecked(false);

                }


            }



            viewHolder.llHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getOpened()) {
                        list.get(position).setOpened(false);
                    } else {
                        list.get(position).setOpened(true);
                    }
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            viewHolder.txtPrice2.setText(new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getQty())));
        } catch (Exception e) {
            viewHolder.txtPrice2.setText(context.getString(R.string.rupee) + "0");
        }
        try {
            viewHolder.txtPrice3.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getBillAmount())));
        } catch (Exception e) {
            viewHolder.txtPrice3.setText(context.getString(R.string.rupee) + "0.0");
        }
        try {
            viewHolder.txtPrice4.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getDiscount())));
        } catch (Exception e) {
            viewHolder.txtPrice4.setText(context.getString(R.string.rupee) + "0.0");
        }

        try {
            viewHolder.txtPrice5.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getMargin())));
        } catch (Exception e) {
            viewHolder.txtPrice5.setText(context.getString(R.string.rupee) + "0.0");
        }
        viewHolder.position = position;

        viewHolder.wtsappcheckitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = 1;
                isSelectedAll=2;
                if (list.get(position).getWhtsappcheck()) {

                    list.get(position).setWhtsappcheck(false);

                } else {
                    list.get(position).setWhtsappcheck(true);


                }
                notifyDataSetChanged();


            }
        });
        mPos++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.imgArrow)
        ImageView imgArrow;
        @BindView(R.id.hiddenLayout)
        LinearLayout hiddenLayout;
        @BindView(R.id.llHeading)
        LinearLayout llHeading;
        @BindView(R.id.ivProduct)
        ImageView ivProduct;

        @BindView(R.id.txtCat)
        TextView txtCat;
        @BindView(R.id.txtBar)
        TextView txtBar;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtqqty)
        TextView txtqqty;
        @BindView(R.id.txtPrice2)
        Button txtPrice2;

        @BindView(R.id.txtPrice3)
        Button txtPrice3;
        @BindView(R.id.txtPrice4)
        Button txtPrice4;
        @BindView(R.id.txtPrice5)
        Button txtPrice5;


        @BindView(R.id.txtCostPrice)
        Button txtCostPrice;

        @BindView(R.id.txtProfit)
        Button txtProfit;

        @BindView(R.id.txtMRP)
        Button txtMRP;

        @BindView(R.id.hidden2)
        LinearLayout hidden2;

        @BindView(R.id.txtUnitCost)
        Button txtUnitCost;

        @Nullable
        @BindView(R.id.wtsappcheckitem)
        CheckBox wtsappcheckitem;

        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.imageView2:

                    break;
                case R.id.tvName:

                    break;
            }

        }
    }


}
