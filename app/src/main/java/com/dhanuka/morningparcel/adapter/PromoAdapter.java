package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.graphics.Typeface;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.events.OnStoreSelectListener;
import com.dhanuka.morningparcel.model.PromoModel;

/**
 *
 */
public class PromoAdapter extends RecyclerView.Adapter {

    List<PromoModel> list;
    Context context;
    OnStoreSelectListener onStoreSelectListener;


    public void filterList(ArrayList<PromoModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }



    public PromoAdapter(Context context, List<PromoModel> objects,OnStoreSelectListener storeSelectListener) {

        this.context = context;
        this.onStoreSelectListener=storeSelectListener;
        list = objects;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.promo_item, parent, false);
        PastItemBeanHolder holder = new PastItemBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PromoModel shop = list.get(position);

        PastItemBeanHolder viewHolder = (PastItemBeanHolder) holder;
        viewHolder.txtheaderpercent.setText("SafeoKart " +shop.getVal1()+"%");
        viewHolder.txtmaxamount.setText("Get " +shop.getVal1()+"% discount upto "+shop.getVal2());
        viewHolder.txtcodedescription.setText(shop.getCodeDescription());
        viewHolder.applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStoreSelectListener.onPromoSelect(shop.getCodeDescription());

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PastItemBeanHolder extends RecyclerView.ViewHolder {
        RelativeLayout llclr;

        TextView txtheaderpercent;
        TextView txtmaxamount;
        TextView txtcodedescription;
        TextView applybtn;


        public PastItemBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llclr = (RelativeLayout) itemView.findViewById(R.id.llclr);
            txtheaderpercent = (TextView) itemView.findViewById(R.id.headerpercent);
            txtcodedescription = (TextView) itemView.findViewById(R.id.txtdescription);
            txtmaxamount = (TextView) itemView.findViewById(R.id.maxamounttxt);
            applybtn = (TextView) itemView.findViewById(R.id.applybtn);

            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            txtcodedescription.setTypeface(font);
            txtmaxamount.setTypeface(font);
            applybtn.setTypeface(font);


        }


    }


}
