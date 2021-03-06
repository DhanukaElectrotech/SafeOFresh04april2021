package com.dhanuka.morningparcel.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.Payfraghelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.OrderDetailActivity;
import com.dhanuka.morningparcel.activity.WalleTransactionActivity;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.utils.JKHelper;


public class PaymentFragAdapter extends RecyclerView.Adapter<PaymentFragAdapter.payholder> {


    ArrayList<Payfraghelper.paybean> list;
    Context context;
    Preferencehelper prefs;
    OrderBean orderBean;
    ArrayList<OrderBean> orderBeans = new ArrayList<>();
    String type;


    @NonNull
    @Override
    public payholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pay_frag, viewGroup, false);

        return new payholder(view);
    }


    public PaymentFragAdapter(ArrayList<Payfraghelper.paybean> list, Context context,String type) {
        this.context = context;
        this.list = list;
        this.type=type;
    }

    @Override
    public void onBindViewHolder(@NonNull payholder payholder, int i) {

        Payfraghelper.paybean payhelper = list.get(i);
        PaymentFragAdapter.payholder viewHolder = (PaymentFragAdapter.payholder) payholder;
        ;
        viewHolder.textamount.setText("???" + payhelper.getAmount());
        viewHolder.extendll.setVisibility(View.GONE);
        viewHolder.textdes.setText(payhelper.getExpType());
        viewHolder.customernametxt.setText("Customer Name : " +payhelper.getCustomerName());
        viewHolder.colletbytxt.setText("Collected By : "+ payhelper.getCollectedBy());
        viewHolder.textdate.setText(payhelper.getCreartionDate());
        if (payhelper.getExpType().equalsIgnoreCase("Wallet-Recharged")) {
            viewHolder.repeatbtn.setText("Repeat");
            viewHolder.txnID.setText("Transaction ID : " + payhelper.getBillNo());
        } else {
            viewHolder.repeatbtn.setText("View Order");
            viewHolder.txnID.setText("Order ID : " + payhelper.getBillNo());
        }
        viewHolder.payModeid.setText("Payment Mode :" + payhelper.getPaymentMode());

        if (payhelper.getExpType().equalsIgnoreCase("CASH PAYMENT"))

        {
            viewHolder.llcomp.setBackgroundColor(context.getResources().getColor(R.color.selected));

        }
        else
        {
            viewHolder.llcomp.setBackgroundColor(context.getResources().getColor(R.color.white));

        }
        viewHolder.commntid.setText("Comment :" + payhelper.getComment());

        viewHolder.openll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.extendll.getVisibility() == View.VISIBLE) {
                    viewHolder.extendll.setVisibility(View.GONE);
                } else {
                    viewHolder.extendll.setVisibility(View.VISIBLE);

                }

            }
        });
        viewHolder.icextend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.extendll.getVisibility() == View.VISIBLE) {
                    viewHolder.extendll.setVisibility(View.GONE);
                } else {
                    viewHolder.extendll.setVisibility(View.VISIBLE);

                }

            }
        });
        if (type.equalsIgnoreCase("3"))
        {
            viewHolder.repeatbtn.setVisibility(View.GONE);
            viewHolder.customernametxt.setVisibility(View.VISIBLE);
            viewHolder.colletbytxt.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.repeatbtn.setVisibility(View.VISIBLE);
            viewHolder.customernametxt.setVisibility(View.GONE);
            viewHolder.colletbytxt.setVisibility(View.GONE);
        }

        viewHolder.repeatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equalsIgnoreCase("3"))
                {
                    if (payhelper.getExpType().equalsIgnoreCase("Wallet-Recharged")) {
                        context.startActivity(new Intent(context, WalleTransactionActivity.class).putExtra("strAmt", list.get(i).getAmount()));
                    } else {
                        loadHistory(payhelper.getExpDate(), payhelper.getBillNo());


                    }

                }

            }
        });
        if (payhelper.getExpType().equalsIgnoreCase("Wallet-Return")) {
            viewHolder.repeatbtn.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class payholder extends RecyclerView.ViewHolder {

        TextView textdes, textamount, txnID, textdate, payModeid, commntid,customernametxt,colletbytxt;
        Button repeatbtn;
        LinearLayout extendll, openll,llcomp;
        ImageView icextend;

        public payholder(View itemView) {
            super(itemView);
            payModeid = (TextView) itemView.findViewById(R.id.payModeid);
            commntid = (TextView) itemView.findViewById(R.id.commntid);
            llcomp=(LinearLayout)itemView.findViewById(R.id.llcomp);
            textdes = (TextView) itemView.findViewById(R.id.textdes);
            customernametxt = (TextView) itemView.findViewById(R.id.customernametxt);
            colletbytxt = (TextView) itemView.findViewById(R.id.collectedbytxt);
            icextend = (ImageView) itemView.findViewById(R.id.icextend);
            extendll = (LinearLayout) itemView.findViewById(R.id.extendll);
            openll = (LinearLayout) itemView.findViewById(R.id.openll);
            repeatbtn = (Button) itemView.findViewById(R.id.repeatbtn);
            textdate = (TextView) itemView.findViewById(R.id.textdate);
            textamount = (TextView) itemView.findViewById(R.id.textamount);
            txnID = (TextView) itemView.findViewById(R.id.txnID);
//            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
//            odamount.setTypeface(font);
//            oddate.setTypeface(font);
//            odtotal.setTypeface(font);

        }
    }

    public void loadHistory(String fromdate, String orderno) {

        prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(context);
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String mCurrentTimeDataBase = formattedDate;

        final ProgressDialog mProgressBar = new ProgressDialog(context);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("responsiveorderhistory", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, context);
                            Log.d("responsive", responses);

                            orderBean = new Gson().fromJson(responses, OrderBean.class);
                            orderBean = orderBean.getReturnds().get(0);

                            Intent intent = new Intent(context, OrderDetailActivity.class);
                            Bundle args = new Bundle();
                            args.putSerializable("list", (Serializable) orderBean);
                            intent.putExtra("BUNDLE", args);
                            intent.putExtra("type", "2");
                            context.startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();

                        FancyToast.makeText(context, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = context.getString(R.string.GET_ORDER_DETAIL) + "&CID=" + prefs.getPrefsContactId() + "&fdate=" + fromdate + "&tdate=" + mCurrentTimeDataBase + "&type=" + "12" + "&OrderNo=" + orderno;
                    Log.d("Beforeencrptionhistory", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, context);
                    params1.put("val", finalparam);
                    Log.d("afterencrptionhistory", finalparam);
                    return params1;


                } catch (Exception e) {
                    e.printStackTrace();
                    return params1;
                }

            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(postRequest);


    }
}
