package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dhanuka.morningparcel.Helper.Timehelper;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 *
 */
public class TimeSlotAdapter extends RecyclerView.Adapter {

    List<Timehelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    CartItemsadd cartItemsadd;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    public void filterList(ArrayList<Timehelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public TimeSlotAdapter(Context context, List<Timehelper> objects, CartItemsadd cartItemsadd) {

        this.context = context;
        list = objects;
        this.cartItemsadd=cartItemsadd;
        inflater = LayoutInflater.from(context);
        prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        try {
    if (list.size()>0) {
        cartItemsadd.sendtimeslotval(list.get(0).getTimestr(), list.get(0).getVal1str());
    }else{
        cartItemsadd.sendtimeslotval("", "0.0");

    }  }catch (Exception e){

}
    }

    String selectedId = "";
    String selectedName = "";
    String Alartphonenumber = "";
    String PhonePe= "";
    String PaytmNumber = "";
    String Currency = "";
    String GooglePay= "";

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shop, parent, false);
        PastItemBeanHolder holder = new PastItemBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Timehelper shop = list.get(position);

        PastItemBeanHolder viewHolder = (PastItemBeanHolder) holder;

        if (position == lastCheckedPosition) {
            viewHolder.timeselect.setChecked(true);

        } else {
            viewHolder.timeselect.setChecked(false);

        }
        viewHolder.paytype.setText(shop.getTimestr());


        viewHolder.position = position;


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PastItemBeanHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct;
        TextView paytype;
        RadioButton timeselect;
        int position;

        public PastItemBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            paytype = (TextView) itemView.findViewById(R.id.shoptext);
            timeselect = (RadioButton) itemView.findViewById(R.id.shopbtn);
            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            paytype.setTypeface(font);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    lastCheckedPosition = getAdapterPosition();
                    cartItemsadd.sendtimeslotval(list.get(getAdapterPosition()).getTimestr(),list.get(getAdapterPosition()).getVal1str());
                    notifyDataSetChanged();

                }
            });  timeselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lastCheckedPosition = getAdapterPosition();
                    cartItemsadd.sendtimeslotval(list.get(getAdapterPosition()).getTimestr(),list.get(getAdapterPosition()).getVal1str());

                    notifyDataSetChanged();

                }
            });
        }


    }

    public String getShop() {
        return selectedId;
    }   public String getpaytype() {
        return selectedName;
    } public String getCurrency() {
        return Currency;
    }
    public String getShop1() {

        try {
            if (!selectedId.isEmpty()) {
                SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                        context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = prefs.edit();
                mEditor.putString("shopId", selectedId);
                mEditor.putString("paytype", selectedName);
                mEditor.putString("Currency", Currency);
                mEditor.putString("Alartphonenumber", Alartphonenumber);
                mEditor.putString("PhonePe", PhonePe);
                mEditor.putString("PaytmNumber", PaytmNumber);
                mEditor.putString("GooglePay", GooglePay);
                mEditor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return selectedId;
    }
}
