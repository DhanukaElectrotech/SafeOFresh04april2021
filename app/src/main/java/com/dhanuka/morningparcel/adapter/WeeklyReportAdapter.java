package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dhanuka.morningparcel.activity.BranchWiseACtivity;
import com.dhanuka.morningparcel.activity.CollectionDetailActivity;
import com.dhanuka.morningparcel.activity.GRReportActivity;
import com.dhanuka.morningparcel.beans.WeeklySalesBean;

public class WeeklyReportAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<WeeklySalesBean> list;
    int mPos = 0;
    String[] strColorsOne = {"E92064", "30A4FF", "F44438", "41B545", "F6A835", "D33FED"};
    String[] strColorsTwo = {"CD1050", "148CEB", "DB2F22", "459E49", "E58A03", "B22AC8"};
    int type = 1;

    public WeeklyReportAdapter(Context context, List<WeeklySalesBean> list, int type) {
        this.context = context;
        this.type = type;
        this.list = list;


    }

    public void addItems(List<WeeklySalesBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<WeeklySalesBean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weekly_sales, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WeeklySalesBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;

        String dat1 = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Date date1 = sdf.parse(mCategoryBean.getDt());
            dat1 = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            dat1 = mCategoryBean.getDt();
        }
        viewHolder.txtDay.setText(mCategoryBean.getDays().toUpperCase() + " " + dat1);
        try {
            viewHolder.txtPrice1.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getTotal())));
        } catch (Exception e) {
            viewHolder.txtPrice1.setText(context.getString(R.string.rupee) + "0.0");
        }
        try {
            viewHolder.txtPrice2.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getMorning_Before_12())));
        } catch (Exception e) {
            viewHolder.txtPrice2.setText(context.getString(R.string.rupee) + "0.0");
        }
        try {
            viewHolder.txtPrice3.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getNoon_between_12_16())));
        } catch (Exception e) {
            viewHolder.txtPrice3.setText(context.getString(R.string.rupee) + "0.0");
        }
        try {
            viewHolder.txtPrice4.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getEvening_between_16_20())));
        } catch (Exception e) {
            viewHolder.txtPrice4.setText(context.getString(R.string.rupee) + "0.0");
        }

        try {
            viewHolder.txtPrice5.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getNight_After_20())));
        } catch (Exception e) {
            viewHolder.txtPrice5.setText(context.getString(R.string.rupee) + "0.0");
        }
        if (mPos >= strColorsOne.length) {
            mPos = 0;
        }
        viewHolder.mainCardlayout.setBackgroundColor(Color.parseColor("#" + strColorsOne[mPos]));
        viewHolder.cardTotal.setBackgroundColor(Color.parseColor("#" + strColorsTwo[mPos]));

        viewHolder.position = position;
        mPos++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtDay)
        TextView txtDay;
        @BindView(R.id.txtPrice1)
        TextView txtPrice1;
        @BindView(R.id.txtPrice2Title)
        TextView txtPrice2Title;
        @BindView(R.id.txtPrice2)
        Button txtPrice2;
        @BindView(R.id.txtPrice3Title)
        TextView txtPrice3Title;
        @BindView(R.id.txtPrice3)
        Button txtPrice3;
        @BindView(R.id.txtPrice4Title)
        TextView txtPrice4Title;
        @BindView(R.id.txtPrice4)
        Button txtPrice4;
        @BindView(R.id.txtPrice5Title)
        TextView txtPrice5Title;
        @BindView(R.id.txtPrice5)
        Button txtPrice5;
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
            mainCardlayout.setOnClickListener(this);
            card_view.setOnClickListener(this);
            txtPrice2.setOnClickListener(this);
            txtPrice3.setOnClickListener(this);
            txtPrice4.setOnClickListener(this);
            txtPrice5.setOnClickListener(this);
        }

        public void setClick(String mTime, String timeinterval) {


            String dateToSend = "";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Date date1 = sdf.parse(list.get(getAdapterPosition()).getDt());

                dateToSend = sdf2.format(date1);


                Log.e("dateToSend", dateToSend);

            } catch (Exception er) {
                er.printStackTrace();
                dateToSend = list.get(getAdapterPosition()).getDt();
            }
            if (type == 1) {

                context.startActivity(new Intent(context, BranchWiseACtivity.class).putExtra("tinterval", timeinterval).putExtra("mDate", dateToSend).putExtra("mTime", mTime));

                //context.startActivity(new Intent(context, OrderHistoryActivity.class).putExtra("mDate", dateToSend).putExtra("mTime", mTime));
            } else if (type == 676528) {

                context.startActivity(new Intent(context, CollectionDetailActivity.class).putExtra("tinterval", timeinterval).putExtra("mDate", dateToSend).putExtra("mTime", mTime).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));




            } else {
                context.startActivity(new Intent(context, GRReportActivity.class).putExtra("mDate", dateToSend).putExtra("mTime", mTime).putExtra("isGR", "isGR"));

            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.cardTotal:
                    setClick("1", "");
                    break;
                case R.id.mainCardlayout:
                    //   setClick(0);
                    break;
                case R.id.card_view:
                    // setClick(0);
                    break;
                case R.id.txtPrice2:


                    setClick("2", "B12");
                    break;

                case R.id.txtPrice3:

                    setClick("3", "B1216");
                    break;
                case R.id.txtPrice4:

                    setClick("4", "B1620");
                    break;
                case R.id.txtPrice5:


                    setClick("5", "A20");
                    break;
                case R.id.tvName:

                    break;
            }

        }
    }


}
