package com.dhanuka.morningparcel.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.dhanuka.morningparcel.activity.ItemStockActivity;
import com.dhanuka.morningparcel.activity.OrderDetailActivity;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.AddCartEvent;
import com.dhanuka.morningparcel.events.OnUpdateOrderListener;
import com.dhanuka.morningparcel.utils.Utility;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.softbalance.widgets.NumberEditText;

/**
 * `1`
 */
public class OrderItemsAdapter extends ArrayAdapter<OrderBean.OrderItemsBean> {

    List<OrderBean.OrderItemsBean> list, filteredList;
    LayoutInflater inflater;
    double finalqty = 0;
    String orderstatus;
    int mPos = 0;
    String strcomment="";
    Dialog deletedialog;
    EditText commenttxtdelte;
    TextView Yesdeletbtn, nodeletebtn;
    LinearLayout cmtll;
    int ordercanel = 0, ordercancelnot = 0;
    Preferencehelper prefs;
    OnUpdateOrderListener listener;
    String currency = "";
    Context ctx;
    int type = 0;
    OrderBean.OrderItemsBean deleteBean;

    int checkfirst = 0;

    TextView tvNamebt, tvPricebt, tvsalebt, tvSavebt, tvOffbt;
    NumberEditText tvQtybt;
    ImageView ivProductbt;
    Button submitbtnbt;
    BottomSheetDialog bottomcartdialog;

    public void changeToEdit(int intType) {
        type = intType;
        notifyDataSetChanged();
    }

    public void Removedata() {

        filteredList.remove(mPos);
    }

    String deliverymode;

    public OrderItemsAdapter(Context context, List<OrderBean.OrderItemsBean> objects, String orderstatus, OnUpdateOrderListener onUpdateOrderListener, int type, String deliverytype) {
        super(context, R.layout.item_order_products, objects);
        list = objects;
        this.type = type;
        this.deliverymode = deliverymode;
        ctx = context;
        filteredList = objects;
        listener = onUpdateOrderListener;
        inflater = LayoutInflater.from(context);
      /*  filteredList = new ArrayList<>();
        filteredList.addAll(list);bg
      */


        for (int a = 0; a < list.size(); a++) {
            if (type == 55) {

                Log.e("TYPECONDITION", type + "");


                //   filteredList.get(a).setChecked(true);
                //  filteredList.get(a).setQuantity((int) Double.parseDouble(filteredList.get(a).getRequestedQty()));


            } else {
                list.get(a).setChecked(false);
                list.get(a).setQuantity((int) Double.parseDouble(list.get(a).getRequestedQty()));

            }


        }


        this.orderstatus = orderstatus;
//        Log.e("orderstatus", orderstatus);
        prefs = new Preferencehelper(ctx);

        SharedPreferences prefs1 = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);


        if (prefs.getPrefsCurrency().equalsIgnoreCase("INR")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
    }

    public List<OrderBean.OrderItemsBean> getChckedProducts() {
        List<OrderBean.OrderItemsBean> mList = new ArrayList<>();
        for (int a = 0; a < list.size(); a++) {
            if (list.get(a).getChecked()) {
                mList.add(list.get(a));
            }
        }
        return mList;
    }

    public List<OrderBean.OrderItemsBean> getChckedProductsAccept() {
        List<OrderBean.OrderItemsBean> mList1 = new ArrayList<>();
        for (int a = 0; a < filteredList.size(); a++) {
            if (filteredList.get(a).getChecked()) {
                mList1.add(filteredList.get(a));
            }


        }
        Log.d("checklistsize", String.valueOf(mList1.size()));


        return mList1;
    }

    public List<OrderBean.OrderItemsBean> getReplace() {
        List<OrderBean.OrderItemsBean> mList2 = new ArrayList<>();
        for (int a = 0; a < filteredList.size(); a++) {

            if (filteredList.get(a).getIsreplace()) {
                mList2.add(filteredList.get(a));
            }


        }
        Log.d("checklistreturnadap", String.valueOf(mList2.size()));


        return mList2;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddCart(AddCartEvent event) {
        ItemMasterhelper product = event.getProduct();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        OrderBean.OrderItemsBean product = filteredList.get(position);
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_order_products, parent, false);
        } else {
            view = convertView;
        }


        ImageView ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
        Button ivEditQty = (Button) view.findViewById(R.id.editQty);
        RelativeLayout showreturnlayout = (RelativeLayout) view.findViewById(R.id.showreturnll);
        RelativeLayout quantityL = (RelativeLayout) view.findViewById(R.id.quantityL);
        RelativeLayout checkedLayout = (RelativeLayout) view.findViewById(R.id.checkedLayout);
        ImageView imgScan = (ImageView) view.findViewById(R.id.imgScan);
        ImageView deletebtn = (ImageView) view.findViewById(R.id.deletebtn);
        Button editPrice = (Button) view.findViewById(R.id.editPrice);

        TextView tvOff = (TextView) view.findViewById(R.id.tvOff);
        TextView tvBarcode = (TextView) view.findViewById(R.id.tvBarcode);
        TextView totalpricequant = (TextView) view.findViewById(R.id.totalpricequant);
        TextView tvOriginalPrice1 = (TextView) view.findViewById(R.id.tvOriginalPrice1);

        Button btnHistory = (Button) view.findViewById(R.id.btnHistory);
        Button returnacceptbtn = (Button) view.findViewById(R.id.returnacceptbtn);
        Button ivreturn = (Button) view.findViewById(R.id.editreturn);
        Button ivEditStts = (Button) view.findViewById(R.id.editStatus);
        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        TextView tvCnqty = (TextView) view.findViewById(R.id.tvCnqty);
        RadioGroup rbreturngroup = (RadioGroup) view.findViewById(R.id.rbreplcgroup);

        TextView returntext = (TextView) view.findViewById(R.id.returntext);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvOriginalPrice2 = (TextView) view.findViewById(R.id.tvOriginalPrice2);
        TextView tvOriginalPrice = (TextView) view.findViewById(R.id.tvOriginalPrice);
        TextView tvOriginalPriceSaving = (TextView) view.findViewById(R.id.tvSave);
        RelativeLayout extrarL = (RelativeLayout) view.findViewById(R.id.extrarL);
        TextView check55txt = (TextView) view.findViewById(R.id.check55txt);


        TextView tvQty = (TextView) view.findViewById(R.id.tvQty);
        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        Button btnReduce = (Button) view.findViewById(R.id.btnReduce);


        if (type == 55) {


            extrarL.setVisibility(View.GONE);
            checkedLayout.setVisibility(View.VISIBLE);

            ivProduct.setVisibility(View.VISIBLE);
            tvBarcode.setVisibility(View.VISIBLE);
            tvOriginalPrice.setVisibility(View.VISIBLE);
            tvOriginalPrice1.setVisibility(View.VISIBLE);
            tvOriginalPrice2.setVisibility(View.GONE);
            tvOff.setVisibility(View.VISIBLE);
            btnHistory.setVisibility(View.GONE);
            deletebtn.setVisibility(View.GONE);
            ivEditStts.setVisibility(View.GONE);
            tvCnqty.setVisibility(View.GONE);


            Double mTotal = (Double.parseDouble(product.getRate()) * Double.parseDouble(product.getRequestedQty()));
            Double MRP = Double.parseDouble(product.getMRP());
            Float finalprice = Float.parseFloat(product.getMRP()) - Float.parseFloat(product.getRate());

            tvOriginalPrice1.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvOriginalPrice1.setText(currency + " " + new DecimalFormat("#").format(MRP));
            Float res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMRP());
            tvOff.setText(" " + new DecimalFormat("#").format(res) + "% OFF");
            tvQty.setText(" " + new DecimalFormat("#").format(product.getQuantity()));

            tvOriginalPriceSaving.setText(" " + new DecimalFormat("#").format(res) + "% OFF");

            try {
                tvOriginalPrice.setText(currency + " " + new DecimalFormat("#").format(Double.parseDouble(product.getRate().trim())) + " ");

            } catch (Exception e) {
                tvOriginalPrice.setText(currency + " " + new DecimalFormat("#").format(Double.parseDouble(product.getRate().trim())) + " ");

                e.printStackTrace();
            }
            tvName.setText(product.getItemDescription());


            tvQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ordercartdialog(ctx, tvQty.getText().toString(), tvName.getText().toString(), tvOriginalPrice.getText().toString(), tvOriginalPrice1.getText().toString(), tvOff.getText().toString(), tvOff.getText().toString(), position);

                }
            });
            Glide.with(getContext())
                    .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                    .into(ivProduct);


            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(tvQty.getText().toString().trim());
                    finalqty = Double.parseDouble(String.valueOf(filteredList.get(position).getRequestedQty()));

                    if (qty < (int) finalqty) {
                        filteredList.get(position).setQuantity((qty + 1));


                        notifyDataSetChanged();
                        tvQty.setText(String.valueOf(qty + 1));


                    }


                }
            });


            btnReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(tvQty.getText().toString().trim());


                    if (qty > 0) {
                        filteredList.get(position).setQuantity((qty - 1));

                        notifyDataSetChanged();
                        tvQty.setText(String.valueOf(qty - 1));
                    }

                }
            });


            rbreturngroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    checkfirst = 1;
                    if (checkedId == R.id.rbreplace) {


                        //int totalvalue=Integer.parseInt(tvQty.getText().toString().trim());
                        filteredList.get(position).setIsreplace(true);
                        filteredList.get(position).setChecked(false);
//                        for (int i=0;i<totalvalue;i++)
//                        {
//
//                            Log.d("replaceqaunt",product.getItemID());
//
//                        }

                    } else if (checkedId == R.id.rbreturn) {
                        filteredList.get(position).setIsreplace(false);
                        filteredList.get(position).setChecked(true);
                    }
                    notifyDataSetChanged();


                }
            });

            checkedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkfirst = 1;


                    if (product.getChecked()) {
                        filteredList.get(position).setChecked(false);
                        rbreturngroup.setVisibility(View.GONE);

                        quantityL.setVisibility(View.GONE);
                        check55txt.setVisibility(View.GONE);

                        checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));

                    } else {
                        filteredList.get(position).setChecked(true);
                        quantityL.setVisibility(View.VISIBLE);
                        rbreturngroup.setVisibility(View.VISIBLE);
                        check55txt.setVisibility(View.VISIBLE);
                        rbreturngroup.check(R.id.rbreturn);
                        checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.selected));


                    }

                    notifyDataSetChanged();
                }
            });

            if (checkfirst == 0) {


                checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));

//
                for (int a = 0; a < filteredList.size(); a++) {

                    if (product.getChecked()) {
                        rbreturngroup.setVisibility(View.GONE);
                        product.setChecked(false);
                        product.setIsreplace(false);
                        check55txt.setVisibility(View.GONE);
                        quantityL.setVisibility(View.GONE);


                        checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));

                    } else {
                        rbreturngroup.setVisibility(View.GONE);
                        product.setChecked(false);
                        product.setIsreplace(false);
                        check55txt.setVisibility(View.GONE);
                        quantityL.setVisibility(View.GONE);


                        checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));


                    }

                }


            }
            else
            {

                if (rbreturngroup.getCheckedRadioButtonId()==R.id.rbreplace)
                {
                    int totalvalue=Integer.parseInt(tvQty.getText().toString().trim());

                    for (int i=0;i<totalvalue;i++)
                    {
                        product.setIsreplace(true);
                        Log.d("replaceqaunt",product.getItemID());

                    }
                    notifyDataSetChanged();


                }

            }


        } else {

            rbreturngroup.setVisibility(View.GONE);
            check55txt.setVisibility(View.GONE);
            if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                extrarL.setVisibility(View.GONE);


                if (list.get(position).getStatus().equalsIgnoreCase("10")) {

                }
            } else {
                extrarL.setVisibility(View.VISIBLE);

            }
            checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (product.getStatus().equalsIgnoreCase("92")) {
                        int qty = Integer.parseInt(tvQty.getText().toString());
                        Toast.makeText(ctx, String.valueOf(qty), Toast.LENGTH_LONG).show();
                        finalqty = Double.parseDouble(String.valueOf(filteredList.get(position).getRequestedQty())) - Double.parseDouble(String.valueOf(product.getVal3()));

                        // tvQty.setText((qty+1)+"");


                        if (qty < (int) finalqty) {
                            filteredList.get(position).setQuantity((qty + 1));


                            notifyDataSetChanged();
                            tvQty.setText(String.valueOf(qty + 1));


                        }
                        if (type == 55) {
                            filteredList.get(position).setQuantity((qty));
                            tvQty.setText(String.valueOf(qty));
                            notifyDataSetChanged();

                        }


                    } else {
                        int qty = filteredList.get(position).getQuantity();
                        Toast.makeText(ctx, String.valueOf(qty), Toast.LENGTH_LONG).show();


                        // tvQty.setText((qty+1)+"");
                        if (qty < (int) Double.parseDouble(list.get(position).getRequestedQty())) {
                            filteredList.get(position).setQuantity((qty + 1));
                            tvQty.setText(String.valueOf(qty + 1));
                            notifyDataSetChanged();
                        }
                        if (type == 55) {
                            filteredList.get(position).setQuantity(qty);
                            tvQty.setText(String.valueOf(qty));
                            notifyDataSetChanged();

                        }

                    }


                }
            });


            btnReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(tvQty.getText().toString());


                    if (qty > 0) {
                        filteredList.get(position).setQuantity((qty - 1));
                        Toast.makeText(ctx, String.valueOf(filteredList.get(position).getQuantity()), Toast.LENGTH_LONG).show();


                        tvQty.setText(String.valueOf(qty - 1));
                        notifyDataSetChanged();
                    }

                    if (type == 55) {
                        qty = filteredList.get(position).getQuantity();
                        filteredList.get(position).setQuantity((filteredList.get(position).getQuantity() - 1));
                        Toast.makeText(ctx, String.valueOf(filteredList.get(position).getQuantity()), Toast.LENGTH_LONG).show();


                        tvQty.setText(String.valueOf(filteredList.get(position).getQuantity() - 1));
                        notifyDataSetChanged();

                    }


                }
            });

            if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));


            } else {
                if (product.getStatus().equalsIgnoreCase("92")) {
                    checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.red_light));

                    if (checkfirst == 0) {
                        finalqty = Double.parseDouble(String.valueOf(filteredList.get(position).getRequestedQty())) - Double.parseDouble(String.valueOf(product.getVal3()));
                        tvCnqty.setText("CN Qty :" + finalqty);
                        tvCnqty.setTextColor(ctx.getResources().getColor(R.color.red));
                        tvQty.setText(new DecimalFormat("#").format(finalqty) + "");
                        if (type == 55) {
                            filteredList.get(position).setQuantity((filteredList.get(position).getQuantity() - 1));
                            Toast.makeText(ctx, String.valueOf(filteredList.get(position).getQuantity()), Toast.LENGTH_LONG).show();


                            tvQty.setText(String.valueOf(filteredList.get(position).getQuantity() - 1));
                            notifyDataSetChanged();
                        }

                    } else {

//                finalqty = Double.parseDouble(String.valueOf(filteredList.get(position).getRequestedQty())) - Double.parseDouble(String.valueOf(product.getVal3()));
//                tvQty.setText(new DecimalFormat("#").format(finalqty) + "");


                    }
                    checkfirst++;
                    double newqty = Double.parseDouble(String.valueOf(filteredList.get(position).getRequestedQty())) - Double.parseDouble(String.valueOf(product.getVal3()));

                    tvCnqty.setText("CN Qty :" + newqty);
                    tvCnqty.setTextColor(ctx.getResources().getColor(R.color.red));


                } else {
                    tvCnqty.setVisibility(View.GONE);
                    tvQty.setText(filteredList.get(position).getQuantity() + "");
                    if (type == 55) {
                        filteredList.get(position).setQuantity((filteredList.get(position).getQuantity() - 1));
                        Toast.makeText(ctx, String.valueOf(filteredList.get(position).getQuantity()), Toast.LENGTH_LONG).show();


                        tvQty.setText(String.valueOf(filteredList.get(position).getQuantity() - 1));
                        notifyDataSetChanged();
                    }

                }

                if (product.getChecked()) {
                    quantityL.setVisibility(View.VISIBLE);
                    checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.selected));
                } else {
                    quantityL.setVisibility(View.GONE);
                    checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));
                    if (product.getStatus().equalsIgnoreCase("92")) {
                        checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.selected));

                    } else {
                        checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));


                    }
                }

                if (product.getStatus().equalsIgnoreCase("92")) {
                    checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.selected));

                } else {
                    checkedLayout.setBackgroundColor(ctx.getResources().getColor(R.color.un_selected));


                }
            }


            if (type == 1) {
                checkedLayout.setVisibility(View.VISIBLE);
                checkedLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (product.getChecked()) {
                            list.get(position).setChecked(false);
                        } else {
                            list.get(position).setChecked(true);


                        }


                        notifyDataSetChanged();
                    }
                });

            } else {
                checkedLayout.setVisibility(View.GONE);

                if (product.getStatus().equalsIgnoreCase("92")) {
                    checkedLayout.setVisibility(View.VISIBLE);
                } else {
                    checkedLayout.setVisibility(View.GONE);
                }

                if (type == 55) {

                    checkedLayout.setVisibility(View.VISIBLE);
                    quantityL.setVisibility(View.VISIBLE);
                    btnHistory.setVisibility(View.GONE);
                    deletebtn.setVisibility(View.GONE);
                    ivEditStts.setVisibility(View.GONE);

                    btnAdd.setVisibility(View.VISIBLE);
                    btnReduce.setVisibility(View.VISIBLE);
                    tvQty.setVisibility(View.VISIBLE);


                    checkedLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (product.getChecked()) {
                                list.get(position).setChecked(false);
                            } else {
                                list.get(position).setChecked(true);


                            }
                            notifyDataSetChanged();
                        }
                    });


                }

            }
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (product.getItemDescription().equalsIgnoreCase("FRUIT AND VEGETABLE")) {


                        loadHistory(product.getVal2(), product.getCustomerID());


//                    ctx.startActivity(new Intent(ctx, FruitHistoryActivity.class).putExtra("orderdetailid",product.getVal2()));
                    }
                }
            });
            try {
                Double mTotal1 = (Double.parseDouble(product.getRate()) * Double.parseDouble(product.getRequestedQty()));
                totalpricequant.setText(new DecimalFormat("##.##").format(Double.parseDouble(product.getRate())) + " X " + new DecimalFormat("##.##").format(Double.parseDouble(product.getRequestedQty())) + " = " + currency + " " + new DecimalFormat("##.##").format(mTotal1));

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                if (product.getStatus().equalsIgnoreCase("4")) {
                    showreturnlayout.setVisibility(View.VISIBLE);
                    returntext.setVisibility(View.VISIBLE);
                    returntext.setText("Requested for return");
                    returnacceptbtn.setVisibility(View.GONE);
                } else if (product.getStatus().equalsIgnoreCase("5")) {
                    showreturnlayout.setVisibility(View.VISIBLE);
                    returntext.setText("Returned to store");
                    returntext.setVisibility(View.VISIBLE);
                    returnacceptbtn.setVisibility(View.GONE);
                } else {
                    showreturnlayout.setVisibility(View.GONE);

                }
                imgScan.setVisibility(View.GONE);


            } else {
                btnHistory.setVisibility(View.VISIBLE);
                tvOff.setVisibility(View.VISIBLE);
                tvOriginalPrice1.setVisibility(View.VISIBLE);
                btnHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                    if (prefs.getCID().equalsIgnoreCase("523")) {
//                        ctx.startActivity(new Intent(ctx, ItemStockActivity.class).putExtra("itemId", list.get(position).getItemID()).putExtra("image", list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath()));
//
//
//                    } else {
//
//                        ctx.startActivity(new Intent(ctx, ItemHistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("mData", list.get(position).getItemID()).putExtra("branchData", "1").putExtra("mtype", "2"));
//
//                    }

                        ctx.startActivity(new Intent(ctx, ItemStockActivity.class).putExtra("itemId", list.get(position).getItemID()).putExtra("image", list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath()));

                        //  ctx.startActivity(new Intent(ctx, ItemHistoryActivity.class).putExtra("mData", list.get(position).getItemID()).putExtra("image", list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath()));

                    }
                });
                imgScan.setVisibility(View.VISIBLE);

                if (product.getStatus().equalsIgnoreCase("4")) {
                    showreturnlayout.setVisibility(View.VISIBLE);
                    returntext.setVisibility(View.VISIBLE);
                    returnacceptbtn.setVisibility(View.VISIBLE);
                } else if (product.getStatus().equalsIgnoreCase("5")) {
                    showreturnlayout.setVisibility(View.VISIBLE);
                    returntext.setText("Customer returned the product.");
                    returntext.setVisibility(View.VISIBLE);
                    returnacceptbtn.setVisibility(View.GONE);
                } else {
                    showreturnlayout.setVisibility(View.GONE);

                }


            }

            imgScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onPopupListener(position, product);

                }
            });
            returnacceptbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setStatus("5");
                    updateOrderStatus(getContext(), product.getOrderdetailID(), "2", "5", mPos,strcomment);


                }
            });
            try {
                tvBarcode.setText(product.getBarCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (orderstatus.equalsIgnoreCase("10")) {

                tvStatus.setTextColor(getContext().getResources().getColor(R.color.green));
                deletebtn.setVisibility(View.GONE);
                ivEditQty.setVisibility(View.GONE);
                ivEditStts.setVisibility(View.GONE);
                editPrice.setVisibility(View.GONE);


                if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                    checkedLayout.setVisibility(View.GONE);


                    if (product.getStatus().equalsIgnoreCase("4")) {
                        showreturnlayout.setVisibility(View.VISIBLE);
                        returntext.setVisibility(View.VISIBLE);
                        returntext.setText("Requested for return");
                        returnacceptbtn.setVisibility(View.GONE);
                        tvStatus.setText("");
                        ivreturn.setVisibility(View.GONE);
                    } else if (product.getStatus().equalsIgnoreCase("5")) {
                        showreturnlayout.setVisibility(View.VISIBLE);
                        returntext.setText("Returned to store");
                        tvStatus.setText("");
                        returntext.setVisibility(View.VISIBLE);
                        tvStatus.setVisibility(View.INVISIBLE);
                        ivreturn.setVisibility(View.GONE);
                        returnacceptbtn.setVisibility(View.GONE);
                    } else if (product.getStatus().equalsIgnoreCase("91")) {
                        tvStatus.setText("CANCELLED");
                        showreturnlayout.setVisibility(View.GONE);
                        ivreturn.setVisibility(View.GONE);
                    } else if (product.getStatus().equalsIgnoreCase("20")) {
                        tvStatus.setText("REPLACED");
                        deletebtn.setVisibility(View.GONE);
                        showreturnlayout.setVisibility(View.GONE);
                        ivreturn.setVisibility(View.VISIBLE);
                    } else {
                        tvStatus.setText("DELIVERED");
                        deletebtn.setVisibility(View.GONE);
                        showreturnlayout.setVisibility(View.GONE);
                        ivreturn.setVisibility(View.VISIBLE);

                    }


                } else {
                    if (product.getStatus().equalsIgnoreCase("4")) {
                        showreturnlayout.setVisibility(View.VISIBLE);
                        returntext.setVisibility(View.VISIBLE);
                        returnacceptbtn.setVisibility(View.VISIBLE);
                    } else if (product.getStatus().equalsIgnoreCase("5")) {
                        showreturnlayout.setVisibility(View.VISIBLE);
                        tvStatus.setVisibility(View.INVISIBLE);
                        returntext.setText("Customer returned the product.");
                        returntext.setVisibility(View.VISIBLE);
                        returnacceptbtn.setVisibility(View.GONE);
                    } else if (product.getStatus().equalsIgnoreCase("91")) {
                        tvStatus.setText("CANCELLED");
                        showreturnlayout.setVisibility(View.GONE);
                        imgScan.setVisibility(View.GONE);

                    } else {
                        tvStatus.setText("");
                        showreturnlayout.setVisibility(View.GONE);
                        ivreturn.setVisibility(View.GONE);

                    }

                }


            } else if (product.getOrderStatus().equalsIgnoreCase("11")) {

                tvStatus.setTextColor(getContext().getResources().getColor(R.color.green));
                deletebtn.setVisibility(View.GONE);
                ivEditQty.setVisibility(View.GONE);
                ivEditStts.setVisibility(View.GONE);
                editPrice.setVisibility(View.GONE);
                tvStatus.setText("DELIVERED");
                deletebtn.setVisibility(View.GONE);
                checkedLayout.setVisibility(View.GONE);
                showreturnlayout.setVisibility(View.GONE);
                ivreturn.setVisibility(View.VISIBLE);


            } else if (orderstatus.equalsIgnoreCase("12")) {

                tvStatus.setTextColor(getContext().getResources().getColor(R.color.green));
                deletebtn.setVisibility(View.GONE);
                ivEditQty.setVisibility(View.GONE);
                ivEditStts.setVisibility(View.GONE);
                editPrice.setVisibility(View.GONE);
                tvStatus.setText("DELIVERED");
                deletebtn.setVisibility(View.GONE);
                checkedLayout.setVisibility(View.GONE);
                showreturnlayout.setVisibility(View.GONE);
                ivreturn.setVisibility(View.VISIBLE);


            } else if (!orderstatus.equalsIgnoreCase("91")) {
                Log.e("orderstatus12", orderstatus);


                try {
                    if (product.getStatus().equalsIgnoreCase("0")) {
                        tvStatus.setText("PENDING");

                        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                            //recently reoved
                            ivEditQty.setVisibility(View.GONE);
                            ivEditStts.setVisibility(View.GONE);
                            editPrice.setVisibility(View.GONE);
                            ivreturn.setVisibility(View.GONE);
                            if (orderstatus.equalsIgnoreCase("0")) {

                                deletebtn.setVisibility(View.VISIBLE);
                            } else {

                                deletebtn.setVisibility(View.GONE);
                            }
                            tvStatus.setTextColor(getContext().getResources().getColor(R.color.black));


                        } else {
                            //recently reoved
                            ivEditQty.setVisibility(View.GONE);
                            ivreturn.setVisibility(View.GONE);
                            ivEditStts.setVisibility(View.VISIBLE);
                            //recently reoved
                            editPrice.setVisibility(View.GONE);


                        }
                    } else if (product.getStatus().equalsIgnoreCase("91")) {
                        tvStatus.setText("CANCELLED");
                        tvStatus.setTextColor(getContext().getResources().getColor(R.color.red));
                        deletebtn.setVisibility(View.GONE);
                        ivEditQty.setVisibility(View.GONE);
                        ivEditStts.setVisibility(View.GONE);
                        editPrice.setVisibility(View.GONE);
                        ivreturn.setVisibility(View.GONE);


                    } else if (product.getStatus().equalsIgnoreCase("1")) {
                        tvStatus.setText("ACCEPTED BY STORE");


                        tvStatus.setTextColor(getContext().getResources().getColor(R.color.black));
                        ivreturn.setVisibility(View.GONE);
                        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                            ivEditQty.setVisibility(View.GONE);
                            ivEditStts.setVisibility(View.GONE);
                            deletebtn.setVisibility(View.GONE);
                            editPrice.setVisibility(View.GONE);


                        } else {
                            //recently reoved
                            ivEditQty.setVisibility(View.GONE);
                            ivEditStts.setVisibility(View.VISIBLE);
                            //recently reoved
                            editPrice.setVisibility(View.GONE);

                        }


                    } else if (product.getStatus().equalsIgnoreCase("2")) {
                        tvStatus.setText("ON HOLD");
                        tvStatus.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                        ivreturn.setVisibility(View.GONE);
                        deletebtn.setVisibility(View.GONE);
                        if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                            //recently reoved
                            ivEditQty.setVisibility(View.GONE);
                            ivEditStts.setVisibility(View.VISIBLE);
                            //recently reoved
                            editPrice.setVisibility(View.GONE);


                        } else {
                            //recently reoved
                            ivEditQty.setVisibility(View.GONE);
                            ivEditStts.setVisibility(View.GONE);
                            editPrice.setVisibility(View.GONE);

                        }

                    } else if (product.getStatus().equalsIgnoreCase("10")) {
                        tvStatus.setText("COMPLETED");
                        tvStatus.setTextColor(getContext().getResources().getColor(R.color.green));
                        ivEditQty.setVisibility(View.GONE);
                        deletebtn.setVisibility(View.GONE);

                        if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                            ivreturn.setVisibility(View.GONE);

                        } else {
                            ivreturn.setVisibility(View.VISIBLE);

                        }
                        deletebtn.setVisibility(View.GONE);
                        ivEditStts.setVisibility(View.GONE);
                        editPrice.setVisibility(View.GONE);
                    } else if (product.getStatus().equalsIgnoreCase(null)) {
                        tvStatus.setText("");
                        ivreturn.setVisibility(View.GONE);

                        //recently reoved
                        ivEditQty.setVisibility(View.GONE);
                        if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                            ivEditStts.setVisibility(View.GONE);
                            editPrice.setVisibility(View.GONE);


                        } else {
                            ivEditStts.setVisibility(View.VISIBLE);

                            //recently reoved
                            editPrice.setVisibility(View.GONE);

                        }
                    } else if (product.getStatus().equalsIgnoreCase("4")) {
                        tvStatus.setText("");
                        deletebtn.setVisibility(View.VISIBLE);
                        if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                            ivEditStts.setVisibility(View.VISIBLE);

                            editPrice.setVisibility(View.GONE);
                            //   showreturnlayout.setVisibility(View.VISIBLE);
                            //    returnacceptbtn.setVisibility(View.GONE);
                        } else {
                            editPrice.setVisibility(View.GONE);
                            ivEditStts.setVisibility(View.GONE);

                        }
                        ivEditQty.setVisibility(View.GONE);
                        ivreturn.setVisibility(View.GONE);

                    } else if (product.getStatus().equalsIgnoreCase("5")) {
                        tvStatus.setText("");
                        deletebtn.setVisibility(View.GONE);
                        if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                            ivEditStts.setVisibility(View.VISIBLE);

                            editPrice.setVisibility(View.GONE);

                        } else {
                            editPrice.setVisibility(View.GONE);
                            ivEditStts.setVisibility(View.GONE);

                        }
                        ivEditQty.setVisibility(View.GONE);
                        ivreturn.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    tvStatus.setTextColor(getContext().getResources().getColor(R.color.black));
                    tvStatus.setText("PENDING");
                    deletebtn.setVisibility(View.GONE);
                    //recently reoved
                    ivEditQty.setVisibility(View.GONE);
                    ivreturn.setVisibility(View.GONE);
                    if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                        ivEditStts.setVisibility(View.VISIBLE);

                        //recently reoved
                        editPrice.setVisibility(View.GONE);
                    } else {
                        ivEditStts.setVisibility(View.GONE);
                        editPrice.setVisibility(View.GONE);

                    }
                }

                //   ivEditQty.setVisibility(View.GONE);
            } else {
                imgScan.setVisibility(View.GONE);
                Log.e("orderstatus13", orderstatus);
                tvStatus.setText("CANCELLED");

                tvStatus.setTextColor(getContext().getResources().getColor(R.color.red));
                deletebtn.setVisibility(View.GONE);
                ivEditQty.setVisibility(View.GONE);
                ivEditStts.setVisibility(View.GONE);
                editPrice.setVisibility(View.GONE);

            }
            for (int k = 0; k < filteredList.size(); k++) {

                if (filteredList.get(k).getStatus().equalsIgnoreCase("91")) {
                    ordercanel = ordercanel + 1;


                } else {
                    ordercancelnot = ordercancelnot + 1;
                }


            }
            if (ordercancelnot == 0) {
                listener.Cancelclk(true, filteredList.get(mPos), mPos);

            }


            ivreturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setStatus("4");

                    updateOrderStatus(getContext(), product.getOrderdetailID(), "2", "4", mPos,strcomment);

                }
            });

            deletedialog = new Dialog(ctx);

            deletedialog.setTitle("Are you sure want to cancel this Order?");
            deletedialog.setContentView(R.layout.cancel_item_dialog);
            cmtll = deletedialog.findViewById(R.id.cmtll);
            Yesdeletbtn = deletedialog.findViewById(R.id.yesdelte);
            nodeletebtn = deletedialog.findViewById(R.id.nodelte);
            commenttxtdelte = deletedialog.findViewById(R.id.commenttxtdelte);

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {





                      if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                    commenttxtdelte.setVisibility(View.GONE);
                    cmtll.setVisibility(View.GONE);
                    deletedialog.show();
                } else {
                    commenttxtdelte.setVisibility(View.VISIBLE);
                    cmtll.setVisibility(View.VISIBLE);
                    deletedialog.show();


                }

                Yesdeletbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPos = position;
                        ordercanel = 0;
                        ordercancelnot = 0;
                        tvStatus.setTextColor(getContext().getResources().getColor(R.color.red));
                        tvStatus.setText("CANCELLED");
                        deletebtn.setVisibility(View.GONE);
                        deletedialog.dismiss();
                        strcomment = commenttxtdelte.getText().toString();



                        updateOrderStatus(filteredList.get(mPos).getOrderdetailID(), "2", "91", mPos, ordercanel, deliverymode,strcomment);

                    }
                });
                nodeletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deletedialog.dismiss();

                    }
                });







                   }
            });


            ivEditQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.edit_qty);

                    final ImageView ivProduct = (ImageView) dialog.findViewById(R.id.ivProduct);
                    final TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
                    final TextView tvOriginalPrice2 = (TextView) dialog.findViewById(R.id.tvOriginalPrice2);
                    final TextView tvOriginalPrice = (TextView) dialog.findViewById(R.id.tvOriginalPrice);
                    final TextView tvQty = (TextView) dialog.findViewById(R.id.tvQty);
                    tvName.setText(product.getItemDescription());
                    tvQty.setText(product.getRequestedQty());
                    tvOriginalPrice2.setText("Qty. : " + new DecimalFormat("##.##").format(Double.parseDouble(product.getRequestedQty())));
                    Glide.with(getContext())
                            .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath())
                            .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                            .into(ivProduct);

                    ivProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utility.Dialog_Confirmation(getContext(), "http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath());
                        }
                    });


                    try {

                        Double mTotal = (Double.parseDouble(product.getRate()) * Double.parseDouble(product.getRequestedQty()));

                        tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(product.getRate()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(product.getRate())));
                    }


                    //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
                    final Button btnAddToCart = (Button) dialog.findViewById(R.id.btnAddToCart);
                    final Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
                    final Button btnReduce = (Button) dialog.findViewById(R.id.btnReduce);


                    btnAddToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            filteredList.get(position).setRequestedQty(tvQty.getText().toString());
                            updateOrderStatus(getContext(), product.getOrderdetailID(), "3", tvQty.getText().toString(), mPos,strcomment);
                            dialog.dismiss();


                        }
                    });
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            int qty = Integer.parseInt(tvQty.getText().toString());
                            tvQty.setText((qty + 1) + "");
                            // filteredList.get(position).setRequestedQty((qty + 1) + "");

                        }
                    });


                    btnReduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int qty = Integer.parseInt(tvQty.getText().toString());


                            if (qty > 1) {

                                tvQty.setText((qty - 1) + "");

                                // filteredList.get(position).setRequestedQty((qty - 1) + "");
                                notifyDataSetChanged();
                            }
                        }
                    });


                    // Display the custom alert dialog on interface
                    dialog.show();


                }
            });

            if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                try {
                    if (Float.parseFloat(product.getDiscount()) > 0) {
                        tvOriginalPriceSaving.setVisibility(View.VISIBLE);

                        tvOriginalPriceSaving.setText("Saved " + currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(product.getDiscount())));
                    } else {
                        tvOriginalPriceSaving.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    tvOriginalPriceSaving.setVisibility(View.GONE);
                    e.printStackTrace();
                }


            } else {
                tvOriginalPriceSaving.setVisibility(View.GONE);

            }
      /*  String finalprice = "0";
        try {
            if (!product.getMRP().isEmpty()) {
                if (Float.parseFloat(mArrayList.get(checkoutTime).getMRP()) > Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate())) {
                    finalprice = "" + (Float.parseFloat(mArrayList.get(checkoutTime).getMRP()) - Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate()));
                } else {
                    finalprice = "0";
                }
            }else{
                finalprice = "0";
            }
        } catch (Exception e) {
            finalprice = "0";
            e.printStackTrace();
        }
        */


            ivEditStts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_edit_status_new);

                    final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.rg);
                    final ImageView ivProduct = (ImageView) dialog.findViewById(R.id.ivProduct);
                    final TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
                    final TextView tvOriginalPrice2 = (TextView) dialog.findViewById(R.id.tvOriginalPrice2);
                    final TextView tvOriginalPrice = (TextView) dialog.findViewById(R.id.tvOriginalPrice);
                    tvName.setText(product.getItemDescription());
                    tvOriginalPrice2.setText("Qty. : " + product.getRequestedQty());
                    Glide.with(getContext())
                            .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath())
                            .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                            .into(ivProduct);


                    try {

                        Double mTotal = (Double.parseDouble(product.getRate()) * Double.parseDouble(product.getRequestedQty()));
                        tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(mTotal));
                    } catch (Exception e) {
                        e.printStackTrace();
                        tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(product.getRate())));
                    }
                    //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
                    final Button btnAddToCart = (Button) dialog.findViewById(R.id.btnAddToCart);


                    btnAddToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String status = "0";

                            if (rg.getCheckedRadioButtonId() == R.id.rbP) {
                                status = "0";
                            } else if (rg.getCheckedRadioButtonId() == R.id.rbH) {
                                status = "2";
                            } else if (rg.getCheckedRadioButtonId() == R.id.rbC) {
                                status = "91";
                            } else if (rg.getCheckedRadioButtonId() == R.id.rbCm) {
                                status = "10";
                            } else if (rg.getCheckedRadioButtonId() == R.id.rbA) {
                                status = "1";
                            }
                            deleteBean = filteredList.get(position);
                            filteredList.get(position).setStatus(status);
                            updateOrderStatus(getContext(), product.getOrderdetailID(), "2", status, mPos,strcomment);
                            dialog.dismiss();

                        }
                    });


                    // Display the custom alert dialog on interface
                    dialog.show();


                }
            });
            editPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_edit_price);

                    final EditText rg = (EditText) dialog.findViewById(R.id.edtPrice);
                    final ImageView ivProduct = (ImageView) dialog.findViewById(R.id.ivProduct);
                    final TextView tvName = (TextView) dialog.findViewById(R.id.tvName);
                    final TextView tvOriginalPrice2 = (TextView) dialog.findViewById(R.id.tvOriginalPrice2);
                    final TextView tvOriginalPrice = (TextView) dialog.findViewById(R.id.tvOriginalPrice);
                    rg.setText(new DecimalFormat("##.##").format(Double.parseDouble(product.getRate())));
                    tvName.setText(product.getItemDescription());
                    tvOriginalPrice2.setText("Qty. : " + product.getRequestedQty());
                    rg.requestFocus();
                    rg.setSelectAllOnFocus(true);
                    rg.selectAll();
                    Glide.with(getContext())
                            .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath())
                            .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                            .into(ivProduct);


                    try {

                        Double mTotal = (Double.parseDouble(product.getRate()) * Double.parseDouble(product.getRequestedQty()));
                        tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(mTotal));
                    } catch (Exception e) {
                        e.printStackTrace();
                        tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(product.getRate())));
                    }

                    //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
                    final Button btnAddToCart = (Button) dialog.findViewById(R.id.btnAddToCart);


                    btnAddToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String status = rg.getText().toString();

                     /*   if (rg.getCheckedRadioButtonId() == R.id.rbP) {
                            status = "0";
                        } else if (rg.getCheckedRadioButtonId() == R.id.rbH) {
                            status = "2";
                        } else if (rg.getCheckedRadioButtonId() == R.id.rbC) {
                            status = "91";
                        } else if (rg.getCheckedRadioButtonId() == R.id.rbCm) {
                            status = "10";
                        } else if (rg.getCheckedRadioButtonId() == R.id.rbA) {
                            status = "1";
                        }*/
                            if (status.isEmpty()) {
                                FancyToast.makeText(getContext(), "Please enter amount.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                            } else {
                                filteredList.get(position).setOrderRequestedAmount(status);
                                updateOrderStatus(getContext(), product.getOrderdetailID(), "4", status, mPos,strcomment);
                                dialog.dismiss();
                            }

                        }
                    });


                    // Display the custom alert dialog on interface
                    dialog.show();


                }
            });

            //  btnAddToCart.setOnClickListener(this);

            tvOriginalPrice2.setText("Qty. : " + product.getRequestedQty());
            tvName.setText(product.getItemDescription());


            Glide.with(getContext())
                    .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                    .into(ivProduct);


            try {


                Double mTotal = (Double.parseDouble(product.getRate()) * Double.parseDouble(product.getRequestedQty()));
                Double MRP = Double.parseDouble(product.getMRP());
                Float finalprice = Float.parseFloat(product.getMRP()) - Float.parseFloat(product.getRate());


                if (Float.parseFloat(product.getRate()) < Float.parseFloat(product.getMRP())) {
                    tvOff.setVisibility(View.VISIBLE);
                    tvOriginalPrice1.setVisibility(View.VISIBLE);

                    tvOriginalPrice1.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvOriginalPrice1.setText(currency + " " + new DecimalFormat("##.##").format(MRP));
                    Float res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMRP());
                    tvOff.setText(" " + new DecimalFormat("##.##").format(res) + "% OFF");

                } else {
                    tvOriginalPrice1.setVisibility(View.GONE);
                    tvOff.setVisibility(View.GONE);

                }

                tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(product.getRate())));


            } catch (Exception e) {
                e.printStackTrace();
                try {
                    tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(product.getRate())));
                } catch (Exception ef) {
                    ef.printStackTrace();
                }

            }


        }

        //view.setContent(product);
        return view;
    }


    @Override
    public int getCount() {
        return filteredList.size();
    }


    public void updateOrderStatus(Context ctx, String oid, String type, String status, int mPos,String strcomment) {


        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ctx);
                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                if (status.equalsIgnoreCase("91")) {
                                    Double mTotal1 = (Double.parseDouble(filteredList.get(mPos).getRate()) * Double.parseDouble(filteredList.get(mPos).getRequestedQty()));

                                    Double totalamountcr = Double.parseDouble(prefs.getPREFS_currentbal()) + mTotal1;

                                    prefs.setPREFS_currentbal(String.valueOf(totalamountcr));

                                    listener.Cancelclk(false, filteredList.get(mPos), mPos);


                                    if (deleteBean != null) {
                                        listener.onCancelItem(deleteBean);
                                    }
                                }
                                listener.onUpdateOrder(filteredList);
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
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = ctx.getString(R.string.UPDATE_ORDER_STATUS) + "&orderid=" + oid + "&status=" + status + "&type=" + type + "&contactId=" + prefs.getPrefsContactId()+ "&Comment=" + strcomment;
                    Log.d("Beforeencrptionmeupdate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrptionmeupdate", finalparam);
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

    public void loadHistory(String referenceid, String sytrContactId) {


        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ArrayList<OrderBean> listOrders = new ArrayList<>();


                            Log.d("responsiveorderhistory", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ctx);
                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {


                                listOrders = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<OrderBean>>() {
                                }.getType());

                                OrderBean orderBean = listOrders.get(0);

                                Intent intent = new Intent(ctx, OrderDetailActivity.class);
                                Bundle args = new Bundle();
                                args.putSerializable("list", (Serializable) orderBean);
                                args.putInt("mType", 5);
                                intent.putExtra("BUNDLE", args);
                                ctx.startActivity(intent);


                            } else {

                                FancyToast.makeText(ctx, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1062")) {
//                        mType = "99";
//                    }


                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    String mTodayDate = df.format(c.getTime()) + " 23:59";
                    // formattedDate have current date/time

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(mTodayDate));
                    calendar.add(Calendar.DAY_OF_YEAR, -30);

                    Date newDate = calendar.getTime();
                    String mStartDate = df.format(calendar.getTime());
                    mStartDate = mStartDate + " 00:00";
                    String param;
                    param = ctx.getString(R.string.GET_ORDER_DETAIL) + "&CID=" + sytrContactId + "&fdate=" + mStartDate + "&tdate=" + mTodayDate + "&type=" + "100" + "&OrderNo=" + referenceid;


                    Log.d("order_params", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
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

        Volley.newRequestQueue(ctx).add(postRequest);


    }

    public void updateOrderStatus(String oid, String type, String status, int mPos,
                                  int ordercanel, String deliverymode,String strcomment) {


        final ProgressDialog mProgressBar = new ProgressDialog(getContext());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getContext().getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getContext());
                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                if (status.equalsIgnoreCase("91")) {
                                    if (deliverymode.equalsIgnoreCase("COD")) {

                                    } else {
                                        Double mTotal1 = (Double.parseDouble(filteredList.get(mPos).getRate()) * Double.parseDouble(filteredList.get(mPos).getRequestedQty()));
                                        Double totalamountcr = Double.parseDouble(prefs.getPREFS_currentbal()) + mTotal1;
                                        prefs.setPREFS_currentbal(String.valueOf(totalamountcr));
                                    }


                                    listener.Cancelclk(false, filteredList.get(mPos), mPos);


                                }

                            } else {

                                FancyToast.makeText(getContext(), "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(getContext(), "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(getContext());
                Map<String, String> params = new HashMap<String, String>();
                //www.mmthinkbiz.com/Mobileservice.aspx?method=updateorderstatus_web&=45454&=1&=2&=


                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);


                    String param = getContext().getString(R.string.UPDATE_ORDER_STATUS) + "&orderid=" + oid + "&status=" + status + "&type=" + type + "&contactId=" + prefs.getPrefsContactId()+ "&Comment=" + strcomment;
                    Log.d("Beforeencrptiondelete", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getContext());
                    params.put("val", finalparam);
                    Log.d("afterencrptiondelete", finalparam);
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

        Volley.newRequestQueue(getContext()).add(postRequest);


    }




    public void ordercartdialog(Context context, String qty, String tvname, String tvprice, String tvsale, String tvoff, String tvsave, int position) {
        bottomcartdialog = new BottomSheetDialog(context);
        bottomcartdialog.setContentView(R.layout.cart_bottomsheet);
        bottomcartdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tvNamebt = bottomcartdialog.findViewById(R.id.tvNamebt);
        tvPricebt = bottomcartdialog.findViewById(R.id.tvPricebt);
        tvsalebt = bottomcartdialog.findViewById(R.id.tvsalebt);
        tvSavebt = bottomcartdialog.findViewById(R.id.tvSavebt);
        tvOffbt = bottomcartdialog.findViewById(R.id.tvOffbt);
        ivProductbt = bottomcartdialog.findViewById(R.id.ivProductbt);
        tvQtybt = bottomcartdialog.findViewById(R.id.tvQtybt);
        submitbtnbt = bottomcartdialog.findViewById(R.id.submitbtnbt);
        tvQtybt.setText(qty.trim());
        tvNamebt.setText(tvname);
        tvPricebt.setText(tvprice);
        tvsalebt.setText(tvsale);
        tvOffbt.setText(tvoff);
        tvSavebt.setText(tvsave);
        tvQtybt.requestFocus();
        tvQtybt.setInputType(InputType.TYPE_CLASS_NUMBER);


//        KeyListener keylistner= DigitsKeyListener.getInstance("1234567890");
//        tvQtybt.setKeyListener(keylistner);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tvQtybt, InputMethodManager.SHOW_IMPLICIT);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        tvSavebt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!tvQtybt.getText().toString().equalsIgnoreCase("")) {
                    FancyToast.makeText(context, "Quantity shoulld be grater than 1 and less then 100k", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                }

            }
        });


        submitbtnbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.parseInt(tvQtybt.getText().toString().trim());
                finalqty = Double.parseDouble(String.valueOf(filteredList.get(position).getRequestedQty()));

                String qytbt=tvQtybt.getText().toString().trim();
                if (qytbt.equalsIgnoreCase("")|| Integer.parseInt(qytbt) > 100000 || Integer.parseInt(qytbt) == 0) {
                    FancyToast.makeText(context, "Quantity shoulld be grater than 1 and less then "+String.valueOf(finalqty), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                } else if (Integer.parseInt(qytbt)< (int)finalqty ||qty==(int)finalqty ){



                    int qty1 = Integer.parseInt(tvQtybt.getText().toString().trim());
                    finalqty = Double.parseDouble(String.valueOf(filteredList.get(position).getRequestedQty()));

                    if (qty1 < (int) finalqty ||qty1==(int)finalqty ) {

                        filteredList.get(position).setQuantity((qty1));

                        notifyDataSetChanged();
                        closeKeyboard();
                        bottomcartdialog.dismiss();



                    }
                    else
                    {
                        FancyToast.makeText(context, "Quantity shoulld  be less then "+String.valueOf(finalqty), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                    }




                }
                else
                {
                    FancyToast.makeText(context, "Quantity shoulld  be less then "+String.valueOf(finalqty), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                }


            }
        });

        showKeyboard();


        Glide.with(getContext())
                .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" + list.get(position).getFilepath())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(ivProductbt);

        bottomcartdialog.show();

    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }


}
