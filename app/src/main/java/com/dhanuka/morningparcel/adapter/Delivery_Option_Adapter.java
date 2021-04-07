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

import com.dhanuka.morningparcel.InterfacePackage.Addressadd;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 *
 */
public class Delivery_Option_Adapter extends RecyclerView.Adapter {

    List<DeliveryHelper> list;
    LayoutInflater inflater;
    Addressadd addressadd;
    CartActionListener mCartActionListener;
    CartItemsadd cartItemsadd;
    Context context;

    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    public void filterList(ArrayList<DeliveryHelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public Delivery_Option_Adapter(Context context, List<DeliveryHelper> objects, CartItemsadd cartItemsadd,Addressadd addressadd) {

        this.context = context;
        this.cartItemsadd=cartItemsadd;
        this.addressadd=addressadd;
        list = objects;
        inflater = LayoutInflater.from(context);
        cartItemsadd.senddeliveryval(list.get(1));

        prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
//        for (int a = 0; a < list.size(); a++) {
//            if (list.get(a).getShopid().equalsIgnoreCase(prefs.getString("shopId", ""))) {
//                lastCheckedPosition = a;
//                selectedName = list.get(a).getShopname();
//                selectedId = list.get(a).getShopid();
//                Currency = list.get(a).getCurrency();
//                Alartphonenumber = list.get(a).getAlartphonenumber();
//                PhonePe = list.get(a).getPhonePe();
//                PaytmNumber = list.get(a).getPaytmNumber();
//                GooglePay = list.get(a).getGooglePay();
//            }
//        }
    }

    String selectedId = "";
    String selectedName = "";
    String Alartphonenumber = "";
    String PhonePe= "";
    int firsttype=0;
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

        DeliveryHelper shop = list.get(position);

        PastItemBeanHolder viewHolder = (PastItemBeanHolder) holder;

        if (firsttype==0)
        {
            if (shop.getDeliverytype().contains("DOOR STEP DELIVERY"))
            {
                viewHolder.deliveryselect.setChecked(true);
            }
            else
            {
                viewHolder.deliveryselect.setChecked(false);
            }

        }
        else
        {
            if (position == lastCheckedPosition) {
                viewHolder.deliveryselect.setChecked(true);


            } else {
                viewHolder.deliveryselect.setChecked(false);

            }
        }


        viewHolder.deliverytype.setText(shop.getDeliverytype());


        viewHolder.position = position;


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PastItemBeanHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct;
        TextView deliverytype;
        RadioButton deliveryselect;
        int position;

        public PastItemBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            deliverytype = (TextView) itemView.findViewById(R.id.shoptext);
            deliveryselect = (RadioButton) itemView.findViewById(R.id.shopbtn);
            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            deliverytype.setTypeface(font);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firsttype=1;
//                    selectedId = list.get(getAdapterPosition()).getShopid();
//                    Currency = list.get(getAdapterPosition()).getCurrency();
//                    selectedName = list.get(getAdapterPosition()).getdeliverytype();
//                    Alartphonenumber = list.get(getAdapterPosition()).getAlartphonenumber();
//                    PhonePe = list.get(getAdapterPosition()).getPhonePe();
//                    PaytmNumber = list.get(getAdapterPosition()).getPaytmNumber();
//                    GooglePay = list.get(getAdapterPosition()).getGooglePay();

                    lastCheckedPosition = getAdapterPosition();
                    cartItemsadd.senddeliveryval(list.get(getAdapterPosition()));
                    //because of this blinking problem occurs so
                    //i have a suggestion to add notifyDataSetChanged();
                    //   notifyItemRangeChanged(0, list.length);//blink list problem
                    notifyDataSetChanged();

                }
            });  deliveryselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    firsttype=1;
//                    selectedId = list.get(getAdapterPosition()).getShopid();
//                    Alartphonenumber = list.get(getAdapterPosition()).getAlartphonenumber();
//                    Currency = list.get(getAdapterPosition()).getCurrency();
//                    selectedName = list.get(getAdapterPosition()).getdeliverytype();
//                    PhonePe = list.get(getAdapterPosition()).getPhonePe();
//                    PaytmNumber = list.get(getAdapterPosition()).getPaytmNumber();
//                    GooglePay = list.get(getAdapterPosition()).getGooglePay();

                    lastCheckedPosition = getAdapterPosition();

                    cartItemsadd.senddeliveryval(list.get(getAdapterPosition()));
                    addressadd.selectaddress(list.get(getAdapterPosition()));
                    //because of this blinking problem occurs so
                    //i have a suggestion to add notifyDataSetChanged();
                    //   notifyItemRangeChanged(0, list.length);//blink list problem
                    notifyDataSetChanged();

                }
            });
        }


    }

    public String getShop() {
        return selectedId;
    }   public String getdeliverytype() {
        return selectedName;
    } public String getCurrency() {
        return Currency;
    }

}
