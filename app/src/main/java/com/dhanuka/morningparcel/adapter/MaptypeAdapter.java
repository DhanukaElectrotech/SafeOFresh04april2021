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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.ShoplistHelper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.events.OnStoreSelectListener;

/**
 *
 */
public class MaptypeAdapter extends RecyclerView.Adapter {

    List<ShoplistHelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = -1;
    SharedPreferences prefs;

    public void filterList(ArrayList<ShoplistHelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    OnStoreSelectListener onStoreSelectListener;

    public MaptypeAdapter(Context context, List<ShoplistHelper> objects, OnStoreSelectListener onStoreSelectListener) {

        this.onStoreSelectListener = onStoreSelectListener;
        this.context = context;
        list = objects;
        inflater = LayoutInflater.from(context);

        prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
        for (int a = 0; a < list.size(); a++) {
            if (list.get(a).getShopid().equalsIgnoreCase(prefs.getString("shopId", ""))) {
                lastCheckedPosition = a;
                selectedName = list.get(a).getShopname();
                selectedId = list.get(a).getShopid();
                Currency = list.get(a).getCurrency();
                Alartphonenumber = list.get(a).getAlartphonenumber();
                PhonePe = list.get(a).getPhonePe();
                PaytmNumber = list.get(a).getPaytmNumber();
                GooglePay = list.get(a).getGooglePay();
                DeliveryCharge = list.get(a).getDeliveryCharge();
                ServiceFees = list.get(a).getServiceFees();
                MaxOrderAmt = list.get(a).getMaxOrderAmt();
                MinOrderAmt = list.get(a).getMinOrderAmt();
                discount = list.get(a).getDiscount();
            }
        }
    }

    String selectedId = "";
    String selectedName = "";
    String Alartphonenumber = "";
    String PhonePe = "";
    String PaytmNumber = "";
    String Currency = "";
    String GooglePay = "";
    String DeliveryCharge;
    String ServiceFees;
    String MaxOrderAmt;
    String MinOrderAmt;
    String discount;
    String Tax;
    String CheckOutMessage;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stores_item, parent, false);
        PastItemBeanHolder holder = new PastItemBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ShoplistHelper shop = list.get(position);

        PastItemBeanHolder viewHolder = (PastItemBeanHolder) holder;
        //
        if (position == lastCheckedPosition) {
            viewHolder.llclr.setBackgroundColor(context.getResources().getColor(R.color.clr_one));
            viewHolder.shopselect.setChecked(true);

        } else {
            viewHolder.llclr.setBackgroundColor(context.getResources().getColor(R.color.clr_two));
            viewHolder.shopselect.setChecked(false);

        }
        viewHolder.shopname.setText(shop.getShopname());
        if (prefs.getString("typer", "com").equalsIgnoreCase("com")) {
            Glide.with(context)
                    .load(/*ApiClient.CAT_IMAGE_BASE_URL+*/"http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getImageName() + "&filePath=" + list.get(position).getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.grocery).error(R.drawable.grocery))
                    .into(viewHolder.shopImg);
        }else{
            Glide.with(context)
                    .load(/*ApiClient.CAT_IMAGE_BASE_URL+*/"http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getImageName() + "&filePath=" + list.get(position).getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.restaurents).error(R.drawable.restaurents))
                    .into(viewHolder.shopImg);

        }
        Preferencehelper prefs;
        prefs = new Preferencehelper(context);
        if (prefs.getPrefsContactId().isEmpty() || prefs.getPrefsContactId().equalsIgnoreCase("7777")) {
            viewHolder.txtDistance.setVisibility(View.GONE);
        }else{
            viewHolder.txtDistance.setVisibility(View.VISIBLE);
            viewHolder.txtDistance.setText(list.get(position).getDistance()+"Km");
        }
        viewHolder.txtCity.setText(list.get(position).getCity());




        viewHolder.position = position;


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PastItemBeanHolder extends RecyclerView.ViewHolder {
        RelativeLayout llclr;
        ImageView shopImg;
        TextView shopname;
        TextView txtCity;
        TextView txtDistance;
        RadioButton shopselect;
        int position;

        public PastItemBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llclr = (RelativeLayout) itemView.findViewById(R.id.llclr);
            shopname = (TextView) itemView.findViewById(R.id.shoptext);
            txtCity = (TextView) itemView.findViewById(R.id.txtCity);
            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);
            shopImg = (ImageView) itemView.findViewById(R.id.shopImg);
            shopselect = (RadioButton) itemView.findViewById(R.id.shopbtn);
            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            shopname.setTypeface(font);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedId = list.get(getAdapterPosition()).getShopid();
                    Currency = list.get(getAdapterPosition()).getCurrency();
                    selectedName = list.get(getAdapterPosition()).getShopname();
                    Alartphonenumber = list.get(getAdapterPosition()).getAlartphonenumber();
                    PhonePe = list.get(getAdapterPosition()).getPhonePe();
                    PaytmNumber = list.get(getAdapterPosition()).getPaytmNumber();
                    GooglePay = list.get(getAdapterPosition()).getGooglePay();
                    DeliveryCharge = list.get(getAdapterPosition()).getDeliveryCharge();
                    ServiceFees = list.get(getAdapterPosition()).getServiceFees();
                    MaxOrderAmt = list.get(getAdapterPosition()).getMaxOrderAmt();
                    MinOrderAmt = list.get(getAdapterPosition()).getMinOrderAmt();
                    discount = list.get(getAdapterPosition()).getDiscount();
                    CheckOutMessage = list.get(getAdapterPosition()).getCheckOutMessage();
                    Tax = list.get(getAdapterPosition()).getTax();

                    lastCheckedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    if (!prefs.getString("cntry", "India").equalsIgnoreCase("India")) {
                        onStoreSelectListener.onStoreSelect();
                    }
                }
            });
            shopselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedId = list.get(getAdapterPosition()).getShopid();
                    Alartphonenumber = list.get(getAdapterPosition()).getAlartphonenumber();
                    Currency = list.get(getAdapterPosition()).getCurrency();
                    selectedName = list.get(getAdapterPosition()).getShopname();
                    PhonePe = list.get(getAdapterPosition()).getPhonePe();
                    PaytmNumber = list.get(getAdapterPosition()).getPaytmNumber();
                    GooglePay = list.get(getAdapterPosition()).getGooglePay();
                    DeliveryCharge = list.get(getAdapterPosition()).getDeliveryCharge();
                    ServiceFees = list.get(getAdapterPosition()).getServiceFees();
                    MaxOrderAmt = list.get(getAdapterPosition()).getMaxOrderAmt();
                    MinOrderAmt = list.get(getAdapterPosition()).getMinOrderAmt();
                    CheckOutMessage = list.get(getAdapterPosition()).getCheckOutMessage();
                    discount = list.get(getAdapterPosition()).getDiscount();
                    Tax = list.get(getAdapterPosition()).getTax();

                    lastCheckedPosition = getAdapterPosition();
                    //because of this blinking problem occurs so
                    //i have a suggestion to add notifyDataSetChanged();
                    //   notifyItemRangeChanged(0, list.length);//blink list problem
                    notifyDataSetChanged();
                    if (!prefs.getString("cntry", "India").equalsIgnoreCase("India")) {
                        onStoreSelectListener.onStoreSelect();
                    }

                }
            });
        }


    }

    public String getShop() {
        return selectedId;
    }

    public String getShopName() {
        return selectedName;
    }

    public String getDeliveryCharge() {
        return DeliveryCharge;
    }

    public String getServiceFees() {
        return ServiceFees;
    }

    public String getCheckOutMessage() {
        return CheckOutMessage;
    }

    public String getMaxOrderAmt() {
        return MaxOrderAmt;
    }

    public String getMinOrderAmt() {
        return MinOrderAmt;
    }

    public String getDiscount() {
        return discount;
    }   public String getTax() {
        return Tax;
    }

    public String getCurrency() {
        return Currency;
    }

    public String getShop1() {

        try {
            if (!selectedId.isEmpty()) {
                SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                        context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = prefs.edit();
                mEditor.putString("shopId", selectedId);
                mEditor.putString("shopName", selectedName);
                mEditor.putString("Currency", Currency);
                mEditor.putString("Alartphonenumber", Alartphonenumber);
                mEditor.putString("PhonePe", PhonePe);
                mEditor.putString("PaytmNumber", PaytmNumber);
                mEditor.putString("discount", discount);
                mEditor.putString("GooglePay", GooglePay);
                mEditor.putString("Tax", Tax);
                mEditor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return selectedId;
    }
}
