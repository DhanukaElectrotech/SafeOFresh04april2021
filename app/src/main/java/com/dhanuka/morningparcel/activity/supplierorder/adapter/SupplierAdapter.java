package com.dhanuka.morningparcel.activity.supplierorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dhanuka.morningparcel.R;

import java.util.List;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.supplierorder.SupplierHomeActivity;
import com.dhanuka.morningparcel.activity.supplierorder.bean.DeliveryBoysBean;
import com.dhanuka.morningparcel.events.onAddCartListener;


/**
 * Created by Mr.JAtin Sharma on 4/5/2017.
 */

public class SupplierAdapter extends RecyclerView.Adapter {
    String currency = "";
    double dbDiscount = 0.0;

    private Context context;
    private List<DeliveryBoysBean> list;
    private List<DeliveryBoysBean> filteredList;
    onAddCartListener monAddCartListener;

    public SupplierAdapter(Context context, List<DeliveryBoysBean> list) {
        this.context=context;
        this.list=list;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item, parent, false);
        customerholder holder = new customerholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        DeliveryBoysBean product = list.get(position);
        customerholder viewHolder = (customerholder) holder;
        viewHolder.customername.setText(product.getFirstName()+"( "+product.getAlartphonenumber()+" )");
        viewHolder.customrercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Preferencehelper prefs4 = new Preferencehelper(context);
                prefs4.setPrefsContactId(list.get(position).getContactID());
                prefs4.setPREFS_trialuser("1");
                context.startActivity(new Intent(context, SupplierHomeActivity.class).putExtra("signup", "1").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("cityname", "GURUGRAM"));

//                if (prefs4.getPREFS_trialuser().equalsIgnoreCase("0"))
//                {
//
//                    context.startActivity(new Intent(context, HomeActivity.class).putExtra("signup", "1").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("cityname", "GURUGRAM"));
//
//                }
//                else
//                {
//
//                }


                //  context.startActivity(new Intent(context, OrderHistoryActivity.class).putExtra("isCustomerlist", "1").putExtra("isCustomercid", list.get(position).getContactID()));
                //context.startActivity(new Intent(context, NewOrderActivity.class).putExtra("isCustomerlist", "1").putExtra("isCustomer", list.get(position).getContactID()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class customerholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView customername,clikorderhis;
        CardView customrercard;



        int position;

        public customerholder(View view) {
            super(view);
            customername = view.findViewById(R.id.customername);
            clikorderhis = view.findViewById(R.id.clikorderhis);
            customrercard=view.findViewById(R.id.customrercard);


        }

        @Override
        public void onClick(View view) {
        }
    }


}
