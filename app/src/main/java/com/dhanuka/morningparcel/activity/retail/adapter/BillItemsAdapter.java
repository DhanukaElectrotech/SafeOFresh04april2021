package com.dhanuka.morningparcel.activity.retail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.activity.retail.beans.ItemMasterhelper;


public class BillItemsAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ItemMasterhelper> list;
    OnDeleteListener onDeleteListener;
    int mSelected;

    public BillItemsAdapter(Context context, List<ItemMasterhelper> list, OnDeleteListener onDeleteListener, int mSelected) {
        this.onDeleteListener = onDeleteListener;
        this.context = context;
        this.list = list;
        this.mSelected = mSelected;

    }

    public void addItems(List<ItemMasterhelper> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_items, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final  int position) {
        ItemMasterhelper mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.txtname.setText(mCategoryBean.getItemName());

        viewHolder.qty.setText(mCategoryBean.getAvailableQty());

        viewHolder.mRate1.setText(mCategoryBean.getSaleRate());
        viewHolder.mRate.setText(mCategoryBean.getSaleRate());
        if (mSelected == position) {
            viewHolder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.selected_color));
        } else {
            try {
                if (Double.parseDouble(mCategoryBean.getWeight()) < 0) {
                    viewHolder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.red_light));

                } else {
                    viewHolder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));

                }
                viewHolder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));

            } catch (Exception e) {
                e.printStackTrace();
                viewHolder.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.red_light));


            }
        }
        try {
            viewHolder.mRate2.setText((Double.parseDouble(mCategoryBean.getAvailableQty()) * Double.parseDouble(mCategoryBean.getSaleRate())) + "");
        } catch (Exception e) {
            e.printStackTrace();
            viewHolder.mRate2.setText(Double.parseDouble(mCategoryBean.getSaleRate()) + "");
        }
        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteListener.onItemDelete(position);
            }
        });

        //  viewHolder.mainLayout.setBackgroundResource(mListColors.get(position));
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   for (int a = 0; a < mListColors.size(); a++) {
                    if (position == a) {
                        mListColors.set(a, context.getResources().getColor(R.color.white));
                    } else {
                        mListColors.set(a, context.getResources().getColor(R.color.unselected));

                    }
                }*/
                onDeleteListener.onItemUpdate(position, list.get(position));
                notifyDataSetChanged();
            }
        });
        viewHolder.position = position;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtname)
        TextView txtname;
        @BindView(R.id.mDelete)
        ImageView imgDelete;
        @BindView(R.id.mRate)
        TextView mRate;
        @BindView(R.id.mRate1)
        TextView mRate1;
        @BindView(R.id.mRate2)
        TextView mRate2;
        @BindView(R.id.qty)
        TextView qty;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        /*
                @BindView(R.id.imageView2)
                ImageView img2;*/
        int position;
        View mView;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }

        @Override
        public void onClick(View view) {

        }
    }


}
