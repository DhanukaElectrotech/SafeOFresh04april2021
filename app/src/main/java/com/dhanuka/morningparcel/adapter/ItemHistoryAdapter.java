package com.dhanuka.morningparcel.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.GRReportActivity;
import com.dhanuka.morningparcel.activity.NewOrderActivity;
import com.dhanuka.morningparcel.activity.OrderDetailActivity;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.ItemHistoryBean;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.utils.JKHelper;

public class ItemHistoryAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ItemHistoryBean> list;
    Preferencehelper prefs;
    OrderBean orderBean;
    ArrayList<OrderBean> orderBeans = new ArrayList<>();
    int mPos = 0;
    String image = "";
    SharedPreferences prefss;

    public ItemHistoryAdapter(Context context, List<ItemHistoryBean> list, String image) {
        this.context = context;
        this.list = list;
        this.image = image;
        prefss = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);


    }

    public void addItems(List<ItemHistoryBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<ItemHistoryBean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_history, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemHistoryBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;


        viewHolder.txttransaction.setText(mCategoryBean.getTransactionType() + " ");
        viewHolder.txtPayment.setText(mCategoryBean.getPayemntType() + " ");
        viewHolder.txtName.setText(mCategoryBean.getItemdescription() + " ");
        viewHolder.txtCat.setText("#" + mCategoryBean.getOrderid() + " ");
        String[] strArr = mCategoryBean.getCreatedDate().split(" ");
        viewHolder.txtBar.setText(strArr[0] + " ");
        viewHolder.txtCustsup.setText(mCategoryBean.getUOM());
        try {
            viewHolder.mrptxt.setText(mCategoryBean.getMRP());
            viewHolder.unittxt.setText(mCategoryBean.getUnitCost());
            viewHolder.txtinv.setText(mCategoryBean.getInvNumber());
            viewHolder.txtmargin.setText(mCategoryBean.getMargin());

        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.txtmarginp.setText(new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getMarginPerc())));
        if (prefss.getString("isIntent", "0").equalsIgnoreCase("1")) {
            viewHolder.stockLayout.setVisibility(View.GONE);
            viewHolder.txtTotalPurchase.setText("0");

            try {
                if (mCategoryBean.getTotalPurchase().isEmpty()) {
                    viewHolder.txtTotalPurchase.setText("0");

                } else {
                    viewHolder.txtTotalPurchase.setText(mCategoryBean.getTotalPurchase());

                }
            } catch (Exception e) {
                viewHolder.txtTotalPurchase.setText("0");

            }
            try {
                if (mCategoryBean.getInQty().isEmpty()) {
                    viewHolder.txtInQty.setText("0");

                } else {
                    viewHolder.txtInQty.setText(mCategoryBean.getInQty());
                }
            } catch (Exception e) {
                viewHolder.txtInQty.setText("0");

            }
            try {
                if (mCategoryBean.getTotalSale().isEmpty()) {
                    viewHolder.txtTotalSale.setText("0");

                } else {
                    viewHolder.txtTotalSale.setText(mCategoryBean.getTotalSale());
                }
            } catch (Exception e) {
                viewHolder.txtTotalSale.setText("0");

            }
            try {
                if (mCategoryBean.getOutQty().isEmpty()) {
                    viewHolder.txtOutQty.setText("0");

                } else {
                    viewHolder.txtOutQty.setText(mCategoryBean.getOutQty());
                }
            } catch (Exception e) {
                viewHolder.txtOutQty.setText("0");

            }
            try {
                if (mCategoryBean.getBalance().isEmpty()) {
                    viewHolder.txtBalance.setText("0");

                } else {
                    viewHolder.txtBalance.setText(mCategoryBean.getBalance());
                }
            } catch (Exception e) {
                viewHolder.txtBalance.setText("0");

            }
        } else {
            viewHolder.stockLayout.setVisibility(View.GONE);
        }


        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context)
                .load(/*ApiClient.CAT_IMAGE_BASE_URL+*/"http://mmthinkbiz.com/ImageHandler.ashx?image=" + image)
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(viewHolder.img);

        viewHolder.txtCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCategoryBean.getTransactionType().equalsIgnoreCase("Sale")) {
                    loadHistory(mCategoryBean.getOrderid());
                } else {
                    loadHistoryGr(mCategoryBean.getOrderid());

                }


            }
        });


        if (mCategoryBean.getTransactionType().equalsIgnoreCase("Sale")) {
            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else if (mCategoryBean.getTransactionType().equalsIgnoreCase("Inward")) {
            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.bluelight));
        } else if (mCategoryBean.getTransactionType().equalsIgnoreCase("Purchase Order")) {
            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.red_light));
        } else if (mCategoryBean.getTransactionType().equalsIgnoreCase("Purchase")) {
            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.bluelight));
        } else if (mCategoryBean.getTransactionType().equalsIgnoreCase("Outward")) {
            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.red_light));
        } else {
            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.light_yellow));

        }

        try {
            viewHolder.txtPrice2.setText(new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getRequestedQty())));
        } catch (Exception e) {
            viewHolder.txtPrice2.setText(context.getString(R.string.rupee) + "0");
        }
        try {
            viewHolder.txtPrice3.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getRate())));
        } catch (Exception e) {
            viewHolder.txtPrice3.setText(context.getString(R.string.rupee) + "0.0");
        }
        try {
            viewHolder.txtPrice4.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getDetailDiscount())));
        } catch (Exception e) {
            viewHolder.txtPrice4.setText(context.getString(R.string.rupee) + "0.0");
        }

        try {
            viewHolder.txtPrice5.setText(context.getString(R.string.rupee) + "" + new DecimalFormat("##.##").format(Double.parseDouble(mCategoryBean.getRAmount())));
        } catch (Exception e) {
            viewHolder.txtPrice5.setText(context.getString(R.string.rupee) + "0.0");
        }
        viewHolder.position = position;
        viewHolder.txtCustsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCategoryBean.getTransactionType().equalsIgnoreCase("Sale")) {
                    context.startActivity(new Intent(context, NewOrderActivity.class).putExtra("isCustomerlist", "1").putExtra("isCustomer", list.get(position).getCustomerID()));

                } else {
                    context.startActivity(new Intent(context, GRReportActivity.class).putExtra("isCustomer", list.get(position).getCustomerID()));


                }

            }
        });
        mPos++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.txtTotalPurchase)
        TextView txtTotalPurchase;
        @BindView(R.id.txtOutQty)
        TextView txtOutQty;
        @BindView(R.id.txtBalance)
        TextView txtBalance;
        @BindView(R.id.txtTotalSale)
        TextView txtTotalSale;
        @BindView(R.id.txtInQty)
        TextView txtInQty;


        @BindView(R.id.txtCat)
        TextView txtCat;
        @BindView(R.id.txtPayment)
        TextView txtPayment;
        @BindView(R.id.txttransaction)
        TextView txttransaction;
        @BindView(R.id.txtBar)
        TextView txtBar;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtPrice2)
        Button txtPrice2;
        @BindView(R.id.txtPrice3)
        Button txtPrice3;
        @BindView(R.id.txtPrice4)
        Button txtPrice4;
        @BindView(R.id.txtPrice5)
        Button txtPrice5;
        @BindView(R.id.llMain)
        LinearLayout llMain;
        @BindView(R.id.txtCustsup)
        TextView txtCustsup;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.stockLayout)
        LinearLayout stockLayout;
        @BindView(R.id.txtmarginp)
        Button txtmarginp;
        @BindView(R.id.mrptxt)
        Button mrptxt;
        @BindView(R.id.unittxt)
        Button unittxt;
        @BindView(R.id.txtmargin)
        Button txtmargin;
        @BindView(R.id.txtinv)
        TextView txtinv;


        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.imageView2:

                    break;
                case R.id.tvName:

                    break;
            }

        }
    }

    public void loadHistory(String orderno) {

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

                    String param = context.getString(R.string.GET_ORDER_DETAIL) + "&CID=" + prefs.getPrefsContactId() + "&fdate=" + mCurrentTimeDataBase + "&tdate=" + mCurrentTimeDataBase + "&type=" + "12" + "&OrderNo=" + orderno;
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


    public void loadHistoryGr(String orderno) {

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

                    String param = context.getString(R.string.GET_ORDER_DETAIL_GR) + "&CID=" + prefs.getPrefsContactId() + "&fdate=" + mCurrentTimeDataBase + "&tdate=" + mCurrentTimeDataBase + "&type=" + "12" + "&GrMasterId=" + orderno;
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
