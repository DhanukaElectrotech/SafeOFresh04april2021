package com.dhanuka.morningparcel.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.LoginActivity;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import ru.softbalance.widgets.NumberEditText;


/**
 * Created by Mr.Mad on 4/5/2017.
 */

public class ProductsAdapterNew extends RecyclerView.Adapter {
    String currency = "";
    double dbDiscount = 0.0;

    private Context context;
    private List<ItemMasterhelper> list;
    private List<ItemMasterhelper> filteredList;
    onAddCartListener monAddCartListener;

    TextView tvNamebt,tvPricebt,tvsalebt,tvSavebt,tvOffbt;
    NumberEditText tvQtybt;
    ImageView ivProductbt;
    Button submitbtnbt;
    BottomSheetDialog bottomcartdialog;

    public ProductsAdapterNew(Context context, List<ItemMasterhelper> list, onAddCartListener mOnAddToCartListener) {
        this.context = context;
        this.list = list;
        filteredList = new ArrayList<>();
        try {
            Log.e("mProducts_size", list.size() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        filteredList.addAll(this.list);
        this.monAddCartListener = mOnAddToCartListener;


        SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);

        if (prefs.getString("discount", "0.0").isEmpty()) {
            dbDiscount = 0.0;

        } else {
            try {
                dbDiscount = Double.parseDouble(prefs.getString("discount", "0.0"));
            } catch (Exception e) {
                dbDiscount = 0.0;
            }
        }
        if (prefs.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        OrderHistoryHolder holder = new OrderHistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        try {
            ItemMasterhelper product = filteredList.get(position);
            OrderHistoryHolder viewHolder = (OrderHistoryHolder) holder;
            //imgLoader.DisplayUserEventGalleryImage(product.getImage().replace(" ", "%20"), ivProduct);

//            if (product.getItemSKU().isEmpty()) {
//                viewHolder.tvOriginalPrice2.setVisibility(View.GONE);
//            } else {
//                viewHolder.tvOriginalPrice2.setVisibility(View.VISIBLE);
//                viewHolder.tvOriginalPrice2.setText(product.getItemSKU());
//            }

            viewHolder.tvName.setText(product.getItemName());
            try {
                viewHolder.tvOriginalPrice2.setText(product.getGroupName());
            } catch (Exception e) {
                viewHolder.tvOriginalPrice2.setText(product.getGroupID());

            }
//            if (!product.getItemImage().isEmpty()) {
//                Log.e("MIMGGG", product.getItemImage());
//            }
            Glide.with(context)
                    .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                    .into(viewHolder.ivProduct);

            viewHolder.ivProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.Dialog_Confirmation(context, "http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath());
                }
            });

            DecimalFormat precision = new DecimalFormat("0.00");
//        if (dbDiscount > 0) {
            SharedPreferences prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                    context.MODE_PRIVATE);

            if (prefs.getString("Currency", "INR").equalsIgnoreCase("INR")) {
                currency = context.getResources().getString(R.string.rupee);
            } else {
                currency = "$";
            }

            if (!product.getMRP().equalsIgnoreCase("")) {
                if (product.getMRP().isEmpty()) {
                    double res;
                    double amount = Double.parseDouble(product.getMRP());
                    Float finalprice = Float.parseFloat(product.getMRP()) - Float.parseFloat(product.getSaleRate());
                    if (finalprice > 0) {
                        res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMRP());

                    } else {

                        res = Double.parseDouble(String.valueOf("0"));
                    }


                    if (Float.parseFloat(String.valueOf(product.getMRP())) < Float.parseFloat(String.valueOf(product.getSaleRate()))) {
                        viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));

                        viewHolder.tvSave.setText("");
                        viewHolder.tvOff.setText("");
                        viewHolder.tvOff.setVisibility(View.GONE);
                        viewHolder.tvOriginalPrice1.setText("");

                    } else {


                        viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));
                        viewHolder.tvOriginalPrice.setPaintFlags(viewHolder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        viewHolder.tvOriginalPrice1.setText(currency + " " + precision.format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                        viewHolder.tvOff.setVisibility(View.VISIBLE);
                        viewHolder.tvSave.setText("Save " + currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                        viewHolder.tvOff.setText(new DecimalFormat("##.##").format(res) + "% OFF");

                    }
                } else {
                    double res;
                    double amount = Double.parseDouble(product.getMRP());
                    Float finalprice = Float.parseFloat(product.getMRP()) - Float.parseFloat(product.getSaleRate());
                    if (finalprice > 0) {
                        res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMRP());

                    } else {

                        res = Double.parseDouble(String.valueOf("0"));
                    }
                    if (Float.parseFloat(String.valueOf(product.getMRP())) < Float.parseFloat(String.valueOf(product.getSaleRate()))) {

                        viewHolder.tvSave.setText("");
                        viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));
                        viewHolder.tvOff.setText("");
                        viewHolder.tvOff.setVisibility(View.GONE);
                        viewHolder.tvOriginalPrice1.setText("");

                    } else {

                        viewHolder.tvOriginalPrice.setPaintFlags(viewHolder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        viewHolder.tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));
                        viewHolder.tvOriginalPrice1.setText(currency + " " + precision.format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                        viewHolder.tvOff.setVisibility(View.VISIBLE);
                        viewHolder.tvSave.setText("Save " + currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                        viewHolder.tvOff.setText(new DecimalFormat("##.##").format(res) + "% OFF");

                    }

                }
            }


//        else {
//             viewHolder.tvOriginalPrice1.setVisibility(View.GONE);
//             viewHolder.tvOff.setVisibility(View.GONE);
//             viewHolder.tvOriginalPrice.setText(currency + " " + precision.format(Double.parseDouble(product.getSaleRate())));
//
//        }
            if (Integer.parseInt(product.getQuantity()) > 0) {
                viewHolder.btnAddToCart.setVisibility(View.GONE);
                viewHolder.tvQty.setVisibility(View.VISIBLE);
                viewHolder.btnAdd.setVisibility(View.VISIBLE);
                viewHolder.btnReduce.setVisibility(View.VISIBLE);
            } else {
                viewHolder.btnAddToCart.setVisibility(View.VISIBLE);
                viewHolder.tvQty.setVisibility(View.GONE);
                viewHolder.btnAdd.setVisibility(View.GONE);
                viewHolder.btnReduce.setVisibility(View.GONE);
            }
            viewHolder.tvQty.setText(product.getQuantity());

            viewHolder.tvQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ordercartdialog(context,viewHolder.tvQty.getText().toString(),viewHolder.tvName.getText().toString(),viewHolder.tvOriginalPrice.getText().toString(),viewHolder.tvOriginalPrice1.getText().toString(),viewHolder.tvSave.getText().toString(),viewHolder.tvOff.getText().toString(),position);


                }
            });

//         viewHolder.tvOriginalPrice1.setVisibility(View.GONE);
            viewHolder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Preferencehelper prefs;
                    prefs = new Preferencehelper(context);

                    try {
                        if (!TextUtils.isEmpty(prefs.getPrefsContactId()) || prefs.getPrefsContactId().equalsIgnoreCase("7777")) {
                            filteredList.get(position).setQuantity("1");

                            //   if (Integer.parseInt(filteredList.get(position).getQuantity())>0){
                            monAddCartListener.onAddCart(filteredList.get(position), 1);
                            notifyDataSetChanged();
                        } else {
                            context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            //  new LoginDialog(ctx).show();
                        }
                    } catch (Exception e) {
                        //  new LoginDialog(ctx).show();
                        context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    }
                /* }else{
                    com.dhanuka.morningparcel.beans.FancyToast.makeText(getContext(), "Please add atleast one quantity.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();

                }*/
                }
            });
            viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    int qty = Integer.parseInt(viewHolder.tvQty.getText().toString());
                    filteredList.get(position).setQuantity((qty + 1) + "");
                    monAddCartListener.onAddCart(filteredList.get(position), 2);
                    //  viewHolder.tvQty.setText((qty+1)+"");

                    notifyDataSetChanged();


                  /*  if (qty < Integer.parseInt(product.getUom())) {
                        qty++;
                         viewHolder.tvQty.setText(String.valueOf(qty));
                    } else {
                        Toast.makeText(getContext(), "Only " + product.getUom() + " packages available", Toast.LENGTH_SHORT).show();
                    }*/
                }
            });


            viewHolder.btnReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.parseInt(viewHolder.tvQty.getText().toString());


                    if (qty > 1) {

                        filteredList.get(position).setQuantity((qty - 1) + "");
                        monAddCartListener.onAddCart(filteredList.get(position), 2);
                        notifyDataSetChanged();
                    } else if (qty == 1) {



                        filteredList.get(position).setQuantity((qty - 1) + "");
                        monAddCartListener.onAddCart(filteredList.get(position), 3);
                        notifyDataSetChanged();

                    }
                }
            });
            Preferencehelper prefs5 = new Preferencehelper(context);


            if (prefs5.getPREFS_trialuser().equalsIgnoreCase("0")) {
                viewHolder.tvOff.setVisibility(View.GONE);
                viewHolder.imgRecurring.setVisibility(View.GONE);

                viewHolder.btnAddToCart.setVisibility(View.GONE);
                viewHolder.tvQty.setVisibility(View.GONE);
                viewHolder.btnAdd.setVisibility(View.GONE);
                viewHolder.btnReduce.setVisibility(View.GONE);
            } else {
                viewHolder.tvOff.setVisibility(View.VISIBLE);
                viewHolder.imgRecurring.setVisibility(View.VISIBLE);
                if (Integer.parseInt(product.getQuantity()) > 0) {
                    viewHolder.btnAddToCart.setVisibility(View.GONE);
                    viewHolder.tvQty.setVisibility(View.VISIBLE);
                    viewHolder.btnAdd.setVisibility(View.VISIBLE);
                    viewHolder.btnReduce.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnAddToCart.setVisibility(View.VISIBLE);
                    viewHolder.tvQty.setVisibility(View.GONE);
                    viewHolder.btnAdd.setVisibility(View.GONE);
                    viewHolder.btnReduce.setVisibility(View.GONE);
                }
            }


            if (new Preferencehelper(context).getPrefsViewType() == 1 && new Preferencehelper(context).getPrefsUsercategory().equalsIgnoreCase("1058")) {
                viewHolder.btnAddToCart.setVisibility(View.GONE);
                viewHolder.tvQty.setVisibility(View.GONE);
                viewHolder.imgRecurring.setVisibility(View.GONE);
                viewHolder.btnAdd.setVisibility(View.GONE);
                viewHolder.btnReduce.setVisibility(View.GONE);

            }


            viewHolder.position = position;
            Log.e("positionposition", position + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class OrderHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProduct;
        TextView tvName;
        TextView tvSave;
        TextView tvOriginalPrice1;
        TextView tvOriginalPrice2;
        TextView tvOriginalPrice;
        TextView tvOff;
        ImageView imgRecurring;
        TextView tvQty;

        //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
        Button btnAddToCart;
        Button btnAdd;
        Button btnReduce;

        int position;

        public OrderHistoryHolder(View view) {
            super(view);
            ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvSave = (TextView) view.findViewById(R.id.tvSave);
            imgRecurring = (ImageView) view.findViewById(R.id.imgRecurring);
            tvOriginalPrice1 = (TextView) view.findViewById(R.id.tvOriginalPrice1);
            tvOriginalPrice2 = (TextView) view.findViewById(R.id.tvOriginalPrice2);
            tvOriginalPrice = (TextView) view.findViewById(R.id.tvOriginalPrice);
            tvOff = (TextView) view.findViewById(R.id.tvOff);
            tvQty = (TextView) view.findViewById(R.id.tvQty);

            //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
            btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            btnReduce = (Button) view.findViewById(R.id.btnReduce);


        }

        @Override
        public void onClick(View view) {
        }
    }

/*
    public void filter(String text) {
        text = text.toLowerCase();
        filteredList.clear();
        if (text.length() == 0) {
            filteredList.addAll(list);
        } else {
            for (ItemMasterhelper product : list) {
                if (product.getProduct_name().toLowerCase().contains(text)) {
                    filteredList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
*/



    public void ordercartdialog(Context context,String qty,String tvname,String tvprice,String tvsale,String tvoff,String tvsave,int position) {
        bottomcartdialog = new BottomSheetDialog(context);
        bottomcartdialog.setContentView(R.layout.cart_bottomsheet);
        bottomcartdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tvNamebt=bottomcartdialog.findViewById(R.id.tvNamebt);
        tvPricebt=bottomcartdialog.findViewById(R.id.tvPricebt);
        tvsalebt=bottomcartdialog.findViewById(R.id.tvsalebt);
        tvSavebt=bottomcartdialog.findViewById(R.id.tvSavebt);
        tvOffbt=bottomcartdialog.findViewById(R.id.tvOffbt);
        ivProductbt=bottomcartdialog.findViewById(R.id.ivProductbt);
        tvQtybt=bottomcartdialog.findViewById(R.id.tvQtybt);
        submitbtnbt=bottomcartdialog.findViewById(R.id.submitbtnbt);
        tvQtybt.setText(qty);
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

                if (!tvQtybt.getText().toString().equalsIgnoreCase("") )
                {
                    FancyToast.makeText(context, "Quantity shoulld be grater than 1 and less then 100k", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                }

            }
        });



        submitbtnbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tvQtybt.getText().toString().equalsIgnoreCase("")|| Integer.parseInt(tvQtybt.getText().toString())>100000|| Integer.parseInt(tvQtybt.getText().toString())==0)
                {
                    FancyToast.makeText(context, "Quantity shoulld be grater than 1 and less then 100k", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                }
                else
                {

                    int qty=0;
                    qty  = Integer.parseInt(tvQtybt.getText().toString());
                    filteredList.get(position).setQuantity((qty) + "");
                    notifyDataSetChanged();
                    monAddCartListener.onAddCart(filteredList.get(position), 6);
                    closeKeyboard();
                    bottomcartdialog.dismiss();

                }


            }
        });

        showKeyboard();


        Glide.with(context)
                .load("http://mmthinkbiz.com/ImageHandler.ashx?image="+list.get(position).getItemImage())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(ivProductbt);

        bottomcartdialog.show();

    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
