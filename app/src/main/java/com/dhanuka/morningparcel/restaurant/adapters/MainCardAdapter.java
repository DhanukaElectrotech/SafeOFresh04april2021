package com.dhanuka.morningparcel.restaurant.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
//import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;

import com.dhanuka.morningparcel.R;
import com.squareup.picasso.Picasso;

import com.dhanuka.morningparcel.events.CartActionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import com.dhanuka.morningparcel.restaurant.models.MainDashboardHelper;


public class MainCardAdapter extends RecyclerView.Adapter {

    List<MainDashboardHelper> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    Context context;
    private int lastCheckedPosition = 0;
    SharedPreferences prefs;
    CartItemsadd cartItemsadd;

    public void filterList(ArrayList<MainDashboardHelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public MainCardAdapter(Context context, List<MainDashboardHelper> objects) {

        this.context = context;
        list = objects;


    }

    String selectedId = "";
    String selectedName = "";
    String Alartphonenumber = "";
    String PhonePe = "";
    String PaytmNumber = "";
    String Currency = "";
    String GooglePay = "";

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_maincard, parent, false);
        MainCardAdapter.Maincardbeanholder holder = new MainCardAdapter.Maincardbeanholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MainDashboardHelper maincard = list.get(position);


        MainCardAdapter.Maincardbeanholder viewHolder = (MainCardAdapter.Maincardbeanholder) holder;


        viewHolder.textheader.setText(maincard.getHeader());
        viewHolder.txtmsg.setText(maincard.getMaintext());
        viewHolder.clickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        Picasso.with(context).load(maincard.getImagelink()).placeholder(R.drawable.no_image).into(viewHolder.imagelink);
        if (position == 0) {
            viewHolder.backgroundimage.setBackgroundColor(context.getResources().getColor(R.color.new3));


        }
        if (position == 1) {
            viewHolder.backgroundimage.setBackgroundColor(context.getResources().getColor(R.color.new2));


        }
        if (position == 2) {
            viewHolder.backgroundimage.setBackgroundColor(context.getResources().getColor(R.color.new1));


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Maincardbeanholder extends RecyclerView.ViewHolder {

        TextView textheader, txtmsg, clickbtn;

        ImageView imagelink;
        LinearLayout backgroundimage;

        public Maincardbeanholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textheader = itemView.findViewById(R.id.textheader);
            txtmsg = itemView.findViewById(R.id.textmsg);
            clickbtn = itemView.findViewById(R.id.clickbtn);
            imagelink = itemView.findViewById(R.id.imglink);
            backgroundimage = itemView.findViewById(R.id.backgroundimage);
            Typeface font = Typeface.createFromAsset(
                    context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
            textheader.setTypeface(font);
            txtmsg.setTypeface(font);
            clickbtn.setTypeface(font);


        }


    }

}