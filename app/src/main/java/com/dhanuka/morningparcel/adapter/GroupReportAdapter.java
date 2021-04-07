package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.beans.GroupSaleBean;
import com.dhanuka.morningparcel.events.GroupClickListener;

public class GroupReportAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<GroupSaleBean> list;
    int mPos = 0;
    GroupClickListener groupClickListener;
    public GroupReportAdapter(Context context, List<GroupSaleBean> list, GroupClickListener groupClickListener) {
        this.context = context;
        this.list = list;
        this.groupClickListener = groupClickListener;
        for (int a=0;a<list.size();a++){
            list.get(a).setOpened(false);
        }

    }

    public void addItems(List<GroupSaleBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<GroupSaleBean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_sales, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroupSaleBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;


        viewHolder.txtName.setText(mCategoryBean.getGroupName() + " ");
        viewHolder.txtqqty.setText("Qty. : "+new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getQty())));
        viewHolder.txtBar.setText(mCategoryBean.getCostPrice() + " ");
        viewHolder.txtqqty.setVisibility(View.GONE);
        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupClickListener.onGroupClick(mCategoryBean.getGroupID());
              /*  if (list.get(position).getOpened()) {
                    list.get(position).setOpened(false);
                } else {
                    list.get(position).setOpened(true);
                }
                notifyDataSetChanged();*/
            }
        });
       try{ Glide.with(context)
                .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(viewHolder.ivProduct);

       }catch (Exception e){
           e.printStackTrace();
       }

        try {
            viewHolder.hiddenLayout.setVisibility(View.VISIBLE);
            viewHolder.imgArrow.setImageResource(R.drawable.up_arrow_show);
         if (list.get(position).getOpened()) {

         } else {
             viewHolder.imgArrow.setImageResource(R.drawable.down_arrow_hide);
             viewHolder.hiddenLayout.setVisibility(View.GONE);
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
     }catch (Exception e){
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
            viewHolder.txtPrice51.setText("" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getMarginPerc()))+" % " );

            viewHolder.txtPrice5.setText(context.getString(R.string.rupee)+ "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getMargin())));
        } catch (Exception e) {
            viewHolder.txtPrice5.setText(context.getString(R.string.rupee) + "0.0");
            viewHolder.txtPrice51.setText("0.0"+" % " );

        }
        viewHolder.position = position;
        mPos++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


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

        @BindView(R.id.txtPrice51)
        Button txtPrice51;
        @BindView(R.id.imgArrow)
        ImageView imgArrow;
      @BindView(R.id.ivProduct)
        ImageView ivProduct;
        @BindView(R.id.hiddenLayout)
        LinearLayout hiddenLayout;
        @BindView(R.id.llHeading)
        LinearLayout llHeading;

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
