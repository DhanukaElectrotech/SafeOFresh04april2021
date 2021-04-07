package com.dhanuka.morningparcel.restaurant.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
//import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.R;
import com.squareup.picasso.Picasso;

import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.restaurant.models.Discoountedhelper;


public class DiscountedCardAdapter extends RecyclerView.Adapter {

    List<Discoountedhelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    CartItemsadd cartItemsadd;
    public void filterList(ArrayList<Discoountedhelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public DiscountedCardAdapter(Context context, List<Discoountedhelper> objects) {

        this.context = context;
        list = objects;



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_discountcard, parent, false);
        DiscountedCardAdapter.categorybean holder = new DiscountedCardAdapter.categorybean(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Discoountedhelper categorycard = list.get(position);

        DiscountedCardAdapter.categorybean viewHolder = (DiscountedCardAdapter.categorybean) holder;
        viewHolder.discounttag.setText(categorycard.getMaintext());
        Picasso.with(context).load(categorycard.getImagelink()).placeholder(R.drawable.no_image).into(viewHolder.categoryimg);




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class categorybean extends RecyclerView.ViewHolder {

        ImageView categoryimg;
        TextView discounttag;

        public categorybean(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            categoryimg =  itemView.findViewById(R.id.categoryimg);
            discounttag = itemView.findViewById(R.id.categoryname);

            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            discounttag.setTypeface(font);



        }


    }

}