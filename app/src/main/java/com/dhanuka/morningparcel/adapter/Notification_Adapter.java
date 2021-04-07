package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.ArrayList;

import com.dhanuka.morningparcel.beans.NotificationBean;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.notifiholder> {


    ArrayList<NotificationBean> list;
    Context context;



    @NonNull
    @Override
    public notifiholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_bell_notification , viewGroup, false);

        return  new notifiholder(view);
    }


    public Notification_Adapter(ArrayList<NotificationBean> list, Context context) {
        this.context= context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull notifiholder notifiholder, int i) {

       NotificationBean notificationBean= list.get(i);

        notifiholder.message.setText(notificationBean.getStrmsg());
        notifiholder.datetime.setText(notificationBean.getStrCreatedate());

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class notifiholder extends RecyclerView.ViewHolder {

        TextView message,datetime;

        public notifiholder(View itemView) {
            super(itemView);


            message=(TextView)itemView.findViewById(R.id.txt_notimsg);
            datetime=(TextView)itemView.findViewById(R.id.txt_datetime);

        }
    }
}
