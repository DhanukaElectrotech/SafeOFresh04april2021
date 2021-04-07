package com.dhanuka.morningparcel.restaurant.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
//import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.restaurant.models.SecondaCardHelper;


public class SecondCardAdapter extends RecyclerView.Adapter {

    List<SecondaCardHelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    CartItemsadd cartItemsadd;
    public void filterList(ArrayList<SecondaCardHelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public SecondCardAdapter(Context context, List<SecondaCardHelper> objects) {

        this.context = context;
        list = objects;



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_smallcard, parent, false);
        SecondCardAdapter.Secondbeanholder holder = new SecondCardAdapter.Secondbeanholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SecondaCardHelper secondcard = list.get(position);

        SecondCardAdapter.Secondbeanholder viewHolder = (SecondCardAdapter.Secondbeanholder) holder;


        viewHolder.textheader.setText(secondcard.getHeader());


        viewHolder.txtmsg.setText(secondcard.getMaintext());


        Picasso.with(context).load(secondcard.getImagelink()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                viewHolder.imagelink.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Secondbeanholder extends RecyclerView.ViewHolder {

        TextView textheader, txtmsg, clickbtn;

        LinearLayout imagelink;

        public Secondbeanholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textheader =  itemView.findViewById(R.id.textheader);
            txtmsg = itemView.findViewById(R.id.textmsg);
            imagelink=itemView.findViewById(R.id.textlinks);
            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            textheader.setTypeface(font);
            txtmsg.setTypeface(font);


        }


    }

}