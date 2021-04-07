package com.dhanuka.morningparcel.restaurant.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.events.CartActionListener;
import com.dhanuka.morningparcel.restaurant.models.circularbrandhelper;

public class CircularBrandAdapter extends RecyclerView.Adapter {

    List<circularbrandhelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    CartItemsadd cartItemsadd;
    public void filterList(ArrayList<circularbrandhelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public CircularBrandAdapter(Context context, List<circularbrandhelper> objects) {

        this.context = context;
        list = objects;



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_circular_banner, parent, false);
        CircularBrandAdapter.categorybean holder = new CircularBrandAdapter.categorybean(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        circularbrandhelper circularcard = list.get(position);

        CircularBrandAdapter.categorybean viewHolder = (CircularBrandAdapter.categorybean) holder;
        viewHolder.brandname.setText(circularcard.getBrandname());
        viewHolder.brandname.setText(circularcard.getTimplace());
        Picasso.with(context).load(circularcard.getBrandimage()).placeholder(R.drawable.no_image).into(viewHolder.circularbrand);




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class categorybean extends RecyclerView.ViewHolder {

        ImageView circularbrand;
        TextView brandname,timeplace;

        public categorybean(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            brandname =  itemView.findViewById(R.id.brandname);
            timeplace = itemView.findViewById(R.id.timeplace);
            circularbrand = itemView.findViewById(R.id.imgbrand);

            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            brandname.setTypeface(font);
            timeplace.setTypeface(font);



        }


    }

}