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

import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.restaurant.models.Addresshelper;


public class AddressHistoryAdapter extends RecyclerView.Adapter {

    List<Addresshelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    CartItemsadd cartItemsadd;
    public void filterList(ArrayList<Addresshelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public AddressHistoryAdapter(Context context, List<Addresshelper> objects) {

        this.context = context;
        list = objects;



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_address, parent, false);
        AddressHistoryAdapter.categorybean holder = new AddressHistoryAdapter.categorybean(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Addresshelper adressact = list.get(position);

        AddressHistoryAdapter.categorybean viewHolder = (AddressHistoryAdapter.categorybean) holder;
        viewHolder.cityname.setText(adressact.getCityname());
        viewHolder.completeaddress.setText(adressact.getCompleteaddress());
        if (position==3)
        {
            viewHolder.historyimage.setBackgroundResource(R.drawable.restaurant_hut);
        }
        else if (position==4)
        {
            viewHolder.historyimage.setBackgroundResource(R.drawable.blank_frame);

        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class categorybean extends RecyclerView.ViewHolder {

        TextView cityname,completeaddress;
        ImageView historyimage;


        public categorybean(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cityname =  itemView.findViewById(R.id.cityname);
            historyimage=itemView.findViewById(R.id.historyimage);
            completeaddress = itemView.findViewById(R.id.completeaddress);

            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            Typeface boldfont = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Bold.ttf");

            cityname.setTypeface(boldfont);
            completeaddress.setTypeface(font);



        }


    }

}