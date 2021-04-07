package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.activity.HomeStoreProductsActivity;
import com.dhanuka.morningparcel.beans.StorewiseBean;

public class StoreWiseAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<StorewiseBean.storeinnerbean> list;
    int mPos = 0;
    String[] strColorsOne = {"E92064", "30A4FF", "F44438", "41B545", "F6A835", "D33FED"};
    String[] strColorsTwo = {"CD1050", "148CEB", "DB2F22", "459E49", "E58A03", "B22AC8"};
    int type = 1;
    String mTime, mDate;
    ArrayList<String> branchlist = new ArrayList<>();
    HashMap<String, String> branchhash = new HashMap<>();

    public StoreWiseAdapter(Context context, List<StorewiseBean.storeinnerbean> list, String mDate, String mTime, HashMap<String, String> branchhash, ArrayList<String> branchlist) {
        this.context = context;
        this.type = type;
        this.list = list;
        this.branchlist = branchlist;
        this.branchhash = branchhash;
        this.mTime = mTime;
        this.mDate = mDate;


    }

    public void addItems(List<StorewiseBean.storeinnerbean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<StorewiseBean.storeinnerbean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_storewise, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StorewiseBean.storeinnerbean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.txtbname.setText(mCategoryBean.getStoreName());
        viewHolder.txttotalorder.setText(mCategoryBean.getInQty());
        viewHolder.txtPrice4.setText(mCategoryBean.getTotalSale());
        viewHolder.txtitemcount.setText(mCategoryBean.getOutQty());
        viewHolder.txttotalpurchase.setText(mCategoryBean.getTotalPurchase());
        viewHolder.txtPrice1.setText(mCategoryBean.getTotalSale());
        if (mPos >= strColorsOne.length) {
            mPos = 0;
        }
        viewHolder.mainCardlayout.setBackgroundColor(Color.parseColor("#" + strColorsOne[mPos]));
        viewHolder.cardTotal.setBackgroundColor(Color.parseColor("#" + strColorsTwo[mPos]));
        viewHolder.position = position;
        viewHolder.mainCardlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //   context.startActivity(new Intent(context, StoreWiseCountActivity.class).putExtra("mDate1", mDate).putExtra("map", branchhash).putExtra("branchname", list.get(position).getStoreName()).putExtra("branchlist",branchlist).putExtra("branchid", mCategoryBean.getBranchID()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                context.startActivity(new Intent(context, HomeStoreProductsActivity.class).putExtra("mDate1", mDate).putExtra("map", branchhash).putExtra("branchname", list.get(position).getStoreName()).putExtra("branchlist", branchlist).putExtra("branchid", mCategoryBean.getBranchID()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });
        viewHolder.cardTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // context.startActivity(new Intent(context, StoreWiseCountActivity.class).putExtra("mDate1", mDate).putExtra("map", branchhash).putExtra("branchname", list.get(position).getStoreName()).putExtra("branchlist",branchlist).putExtra("branchid", mCategoryBean.getBranchID()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                context.startActivity(new Intent(context, HomeStoreProductsActivity.class).putExtra("mDate1", mDate).putExtra("map", branchhash).putExtra("branchname", list.get(position).getStoreName()).putExtra("branchlist", branchlist).putExtra("branchid", mCategoryBean.getBranchID()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }
        });
        mPos++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtitemcount)
        TextView txtitemcount;
        @BindView(R.id.txttotalpurchase)
        TextView txttotalpurchase;
        @BindView(R.id.txttotalorder)
        TextView txttotalorder;
        @BindView(R.id.txtbname)
        TextView txtbname;
        @BindView(R.id.txtPrice1)
        TextView txtPrice1;
        @BindView(R.id.txtPrice4)
        TextView txtPrice4;

        @BindView(R.id.mainCardlayout)
        CardView mainCardlayout;
        @BindView(R.id.cardTotal)
        CardView cardTotal;

        @BindView(R.id.card_view)
        CardView card_view;

        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardTotal.setOnClickListener(this);

            card_view.setOnClickListener(this);

        }

        public void setClick(int mTime) {


            String dateToSend = "";
            try {


                Log.e("dateToSend", dateToSend);

            } catch (Exception er) {
                er.printStackTrace();
                //dateToSend = list.get(getAdapterPosition()).getDt();
            }

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.cardTotal:
                    //   context.startActivity(new Intent(context, OrderHistoryActivity.class).putExtra("mDate1", mDate).putExtra("mTime1", mTime).putExtra("branchid", list.get(getAdapterPosition()).getBranchID()).putExtra("isCustomerbranch", 1).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

//                    setClick();
                    break;

                case R.id.card_view:
//                     setClick(0);
                    break;

            }

        }
    }


}
