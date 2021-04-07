package com.dhanuka.morningparcel.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONObject;

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
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.CategoryActivity;
import com.dhanuka.morningparcel.activity.ItemOrdersActivity;
import com.dhanuka.morningparcel.activity.ItemSuppliersActivity;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.events.OnAddToSToreListener;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.Utility;

public class SaleproductsAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ItemMasterhelper> list;
    String type;
    OnAddToSToreListener onAddToSToreListener;
    int mPos = 0;
    String currency;
    Preferencehelper prefs;
    public SaleproductsAdapter(Context context, List<ItemMasterhelper> list, String type, OnAddToSToreListener onAddToSToreListener) {
        this.context = context;
        this.onAddToSToreListener = onAddToSToreListener;
        this.type = type;
        this.list = list;
        prefs = new Preferencehelper(context);
    }

    public void addItems(List<ItemMasterhelper> postItems) {
        list.addAll(postItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sale_products, parent, false);
        CategoryBeanHolder holder = new CategoryBeanHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemMasterhelper mCategoryBean = list.get(position);
        CategoryBeanHolder viewHolder = (CategoryBeanHolder) holder;
        viewHolder.tvName.setText(mCategoryBean.getItemName());
        viewHolder.tvBarcode.setText(mCategoryBean.getItemBarcode());

        if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        viewHolder.tvOriginalPrice.setText(/*context.getResources().getString(R.string.rupee) +*/ currency +new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(mCategoryBean.getSaleRate()))) );
        //  viewHolder.img.setImageResource(mCategoryBean.getIntImg());
        if (!list.get(position).getFileName().isEmpty()) {
            Log.e("MIMGGG", "http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + mCategoryBean.getFilepath());
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(context)
                .load(/*ApiClient.CAT_IMAGE_BASE_URL+*/"http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + mCategoryBean.getFilepath())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(viewHolder.ivProduct);
        viewHolder.position = position;

        viewHolder.ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.Dialog_Confirmation(context, "http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + mCategoryBean.getFilepath());
            }
        });

        viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ItemOrdersActivity.class).putExtra("strDesc",mCategoryBean.getItemName()));

            }
        });

        if (Float.parseFloat(list.get(position).getSaleRate()) < Float.parseFloat(list.get(position).getMRP())) {
             viewHolder.tvOriginalPrice1.setVisibility(View.VISIBLE);
            Float finalprice = Float.parseFloat(list.get(position).getMRP()) - Float.parseFloat(list.get(position).getSaleRate());


            viewHolder.tvOriginalPrice1.setPaintFlags(viewHolder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tvOriginalPrice1.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(position).getMRP())));
            Float res = ((finalprice * 100.0f)) / Float.parseFloat(list.get(position).getMRP());

        } else {
            viewHolder.tvOriginalPrice1.setVisibility(View.GONE);
             viewHolder.tvBarcode.setVisibility(View.GONE);

        }
        viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ItemSuppliersActivity.class).putExtra("strDesc",mCategoryBean.getItemName()));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

           @BindView(R.id.btnDetail)
        TextView btnDetail;
        @BindView(R.id.btnUpdate)
        Button btnUpdate;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvOriginalPrice)
        TextView tvOriginalPrice;
            @BindView(R.id.ivProduct)
        ImageView ivProduct;
           int position;

        @BindView(R.id.tvOriginalPrice1)
        TextView tvOriginalPrice1;
        @BindView(R.id.tvBarcode)
        TextView tvBarcode;

        public CategoryBeanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  btnDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.imageView2:
                    Log.e("listlist", list.size() + "");


                    Intent intent = new Intent(context, CategoryActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("list", (Serializable) list);
                    intent.putExtra("BUNDLE", args);
                    intent.putExtra("mPosition", position);
                    context.startActivity(intent);

                    break;
                case R.id.tvName:
                    // context.startActivity(new Intent(context, ProductsActivity.class));

                    break;
            }

        }
    }

    String strType;
    String ToShow1;

    public void updateProduct(Context ctx, String ItemID, String AvailableQty, String ToShow, String SaleRate, String Type) {

        ToShow1 = ToShow;
        strType = Type;
        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ctx);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                if (type.equalsIgnoreCase("4")) {
                                }
                                notifyDataSetChanged();

                            } else {

                                FancyToast.makeText(ctx, "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

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
                        FancyToast.makeText(ctx, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());


                Preferencehelper prefs;
                prefs = new Preferencehelper(ctx);
                Map<String, String> params = new HashMap<String, String>();
                String mSaleRate = "";
                try {
                    mSaleRate = (int) (Double.parseDouble(SaleRate)) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                    mSaleRate = SaleRate;
                }
                String mQty = "";
                try {
                    mQty = (int) (Double.parseDouble(AvailableQty)) + "";
                } catch (Exception e) {
                    e.printStackTrace();
                    mQty = AvailableQty;
                }

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    int as = 0;

                    if (Type.equalsIgnoreCase("5A")) {
                        strType = Type.replace("A", "");
                        ToShow1 = "1";
                        as = 1;
                    } else if (Type.equalsIgnoreCase("5B")) {
                        strType = Type.replace("B", "");
                        ToShow1 = "0";
                        as = 1;

                    } else if (Type.equalsIgnoreCase("6C")) {
                        strType = Type.replace("C", "");
                        ToShow1 = "1";
                        as = 1;

                    } else if (Type.equalsIgnoreCase("6D")) {
                        strType = Type.replace("D", "");
                        ToShow1 = "0";
                        as = 1;

                    } else {
                        ToShow1 = ToShow;
                        strType = Type;
                        as = 0;
                    }

                    try {
                        if (mQty.isEmpty()) {
                            mQty = "0";
                        }
                    } catch (Exception e) {
                        mQty = "0";
                        e.printStackTrace();
                    }
                    if (as == 1) {
                        strType = strType + "&isdeal=" + ToShow1;
                    } else {
                        strType = strType;
                    }
                    String param = ctx.getString(R.string.CREATE_ORDER_ITEMS) + "&ItemID=" + ItemID + "&VendorID=" + prefs.getPrefsContactId() + "&Createdby=" + prefs.getPrefsContactId() + "&AvailableQty=" + mQty
                            + "&ToShow=" + ToShow1 + "&SaleRate=" + mSaleRate + "&Type=" + strType + "&SaleUOMID=" + "0" + "&SaleUOM=" + "0";
                    Log.d("Beforeencrptionadd", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrptionadd", finalparam);
                    return params;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }


            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ctx).add(postRequest);


    }

    public void filterList(ArrayList<ItemMasterhelper> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}
