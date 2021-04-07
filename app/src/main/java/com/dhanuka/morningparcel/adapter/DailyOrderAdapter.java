package com.dhanuka.morningparcel.adapter;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.GRReportActivity;
import com.dhanuka.morningparcel.activity.HomeActivity;
import com.dhanuka.morningparcel.activity.NewOrderActivity;
import com.dhanuka.morningparcel.beans.CollectionBean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.Returnlistner;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionModule;

import org.json.JSONObject;

public class DailyOrderAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<CollectionBean> list;
    Preferencehelper prefs;
    OrderBean orderBean;
    ArrayList<OrderBean> orderBeans = new ArrayList<>();
    int mPos = 0;
    String image = "";
    Returnlistner returnlistner;
    SharedPreferences prefss;
    int firsttrip = 0;

    public DailyOrderAdapter(Context context, List<CollectionBean> list, String image, Returnlistner returnlistner) {
        this.context = context;
        this.list = list;
        this.returnlistner = returnlistner;

        this.image = image;
        prefss = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);


    }


    public void addItems(List<CollectionBean> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<CollectionBean> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dailyorder, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CollectionBean mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;


        viewHolder.txttransaction.setText(mCategoryBean.getContactNo() + " ");
        viewHolder.txtPayment.setText(mCategoryBean.getCustomerID() + " ");
        viewHolder.txtCustsup.setText(mCategoryBean.getCustomerName() + " ");
        viewHolder.txtflatno.setText(mCategoryBean.getBalance());

        Preferencehelper prefs4 = new Preferencehelper(context);


        viewHolder.ordercust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                prefs4.setPREFS_trialuser("1");
                prefs4.setPrefsTempContactid(prefs4.getPrefsContactId());
                prefs4.setPrefsContactId(mCategoryBean.getCustomerID());
                context.startActivity(new Intent(context, HomeActivity.class).putExtra("signup", "1").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("cityname", "GURUGRAM"));


            }
        });


        try {


            viewHolder.txtinv.setText(mCategoryBean.getCreatedDate());


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (Integer.parseInt(String.valueOf(mCategoryBean.getOrderflag()))==1) {
            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.highlighted_text_material_light));
        } else {
            viewHolder.llMain.setBackgroundColor(context.getResources().getColor(R.color.white));
        }


        viewHolder.calltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeCall(mCategoryBean.getContactNo().trim(),context);

            }
        });

        mPos++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable
        @BindView(R.id.txtTotalPurchase)
        TextView txtTotalPurchase;
        @Nullable
        @BindView(R.id.txtOutQty)
        TextView txtOutQty;
        @Nullable
        @BindView(R.id.txtBalance)
        TextView txtBalance;
        @Nullable
        @BindView(R.id.txtTotalSale)
        TextView txtTotalSale;
        @Nullable
        @BindView(R.id.txtInQty)
        TextView txtInQty;

        @Nullable
        @BindView(R.id.txtPayment)
        TextView txtPayment;
        @Nullable
        @BindView(R.id.txttransaction)
        TextView txttransaction;

        @Nullable
        @BindView(R.id.txtPrice2)
        Button txtPrice2;
        @Nullable
        @BindView(R.id.txtPrice3)
        Button txtPrice3;
        @Nullable
        @BindView(R.id.txtPrice4)
        Button txtPrice4;
        @Nullable
        @BindView(R.id.txtPrice5)
        Button txtPrice5;
        @Nullable
        @BindView(R.id.llMain)
        LinearLayout llMain;
        @Nullable
        @BindView(R.id.txtCustsup)
        TextView txtCustsup;
        @Nullable
        @BindView(R.id.stockLayout)
        LinearLayout stockLayout;
        @Nullable
        @BindView(R.id.txtmarginp)
        Button txtmarginp;
        @Nullable
        @BindView(R.id.mrptxt)
        Button mrptxt;
        @Nullable
        @BindView(R.id.txtmargin)
        Button txtmargin;
        @Nullable
        @BindView(R.id.txtinv)
        TextView txtinv;
        @Nullable
        @BindView(R.id.unittxt)
        TextView unittxt;
        @Nullable
        @BindView(R.id.txtflatno)
        TextView txtflatno;
        @Nullable
        @BindView(R.id.card_view)
        CardView card_view;
        @Nullable
        @BindView(R.id.ordercust)
        ImageView ordercust;
        @Nullable
        @BindView(R.id.txtfinancialbtnblock)
        Button blockbtn;


        @Nullable
        @BindView(R.id.calltxt)
        ImageView calltxt;

        int position;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {


        }
    }

    public void assignOrder(CollectionBean mCategoryBean, String statusblock, CategoryBeanHolder viewHolder) {
        final ProgressDialog mProgressBar = new ProgressDialog(context);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");


        mProgressBar.show();


        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mProgressBar.dismiss();


                        try {

                            Log.d("resp_delivery", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, context);
                            Log.d("resp_delivery", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                if (mCategoryBean.getIsFinanceBlock().equalsIgnoreCase("0")) {

                                    mCategoryBean.setIsFinanceBlock("1");
                                    viewHolder.blockbtn.setText("BLOCKED");
                                } else if (mCategoryBean.getIsFinanceBlock().equalsIgnoreCase("1")) {
                                    mCategoryBean.setIsFinanceBlock("0");
                                    viewHolder.blockbtn.setText("NOT BLOCK");
                                }
                                notifyDataSetChanged();


                                FancyToast.makeText(context, "Order Assigned Successfully to ", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();


                            } else {
                                FancyToast.makeText(context, "failed to assign order, try again.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                            }
                        } catch (Exception e) {
                            FancyToast.makeText(context, "failed to assign order, try again.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mProgressBar.dismiss();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(context);

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//=35&=ashwani%20sahu&=7&=59298
                    String finalstatusblock = "";
                    if (statusblock.equalsIgnoreCase("1")) {
                        finalstatusblock = "0";
                        ;
                    } else if (statusblock.equalsIgnoreCase("0")) {
                        finalstatusblock = "1";

                    }
                    String param = context.getString(R.string.UPDATE_ORDER_STATUS) + "&type=15" + "&orderid=" + 0 + "&status=" + finalstatusblock + "&contactId=" + mCategoryBean.getCustomerID() /*+ "&fdate=" + mStartDate + "&tdate=" + mTodayDate + "&type=" + mType*/;
                    Log.d("before_enc_odr", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, context);
                    params1.put("val", finalparam);
                    Log.d("after_enc_odr", finalparam);
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

    public void makeCall(String strH,Context ctx) {
        try {
            if (strH != null && strH.length() > 2) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + strH));

                    if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                        PermissionModule permissionModule = new PermissionModule(ctx);
                        permissionModule.checkPermissions();

                    } else {
                        ctx.startActivity(callIntent);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    com.shashank.sony.fancytoastlib.FancyToast.makeText(ctx, "Phone Number is not available", com.shashank.sony.fancytoastlib.FancyToast.LENGTH_SHORT, com.shashank.sony.fancytoastlib.FancyToast.INFO, false).show();
                }

            }
        } catch (Exception e) {
            com.shashank.sony.fancytoastlib.FancyToast.makeText(ctx, "Phone Number is not available", com.shashank.sony.fancytoastlib.FancyToast.LENGTH_SHORT, com.shashank.sony.fancytoastlib.FancyToast.INFO, false).show();
            e.printStackTrace();
        }
    }


}
