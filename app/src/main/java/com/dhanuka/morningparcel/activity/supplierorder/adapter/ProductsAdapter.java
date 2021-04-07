package com.dhanuka.morningparcel.activity.supplierorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.Recurring_order;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.events.AddCartEvent;
import com.dhanuka.morningparcel.events.onAddCartListener;
import com.dhanuka.morningparcel.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.softbalance.widgets.NumberEditText;

/**
 * `1`
 */
public class ProductsAdapter extends ArrayAdapter<ItemMasterhelper> {
    Context ctx;
    List<ItemMasterhelper> list, filteredList;
    LayoutInflater inflater;
    onAddCartListener monAddCartListener;
    String currency = "";
    double dbDiscount = 0.0;


    TextView tvNamebt, tvPricebt, tvsalebt, tvSavebt, tvOffbt;
    NumberEditText tvQtybt;
    ImageView ivProductbt;
    Button submitbtnbt;
    BottomSheetDialog bottomcartdialog;

    public ProductsAdapter(Context context, List<ItemMasterhelper> objects, onAddCartListener monAddCartListener) {
        super(context, R.layout.item_product, objects);
        this.monAddCartListener = monAddCartListener;
        list = objects;
        ctx = context;
        inflater = LayoutInflater.from(context);
        filteredList = new ArrayList<>();
        filteredList.addAll(list);
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

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor mEditor;

        mEditor = prefs.edit();
        mEditor.putString("Currency", "INR");
        mEditor.commit();
        if (prefs.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddCart(AddCartEvent event) {
        ItemMasterhelper product = event.getProduct();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ItemMasterhelper product = filteredList.get(position);
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_product, parent, false);


        } else {
            view = convertView;
        }


        ImageView imgRecurring = (ImageView) view.findViewById(R.id.imgRecurring);
        ImageView ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvSave = (TextView) view.findViewById(R.id.tvSave);
        TextView tvOriginalPrice1 = (TextView) view.findViewById(R.id.tvOriginalPrice1);
        TextView tvOriginalPrice2 = (TextView) view.findViewById(R.id.tvOriginalPrice2);
        TextView tvOriginalPrice = (TextView) view.findViewById(R.id.tvOriginalPrice);
        TextView tvOff = (TextView) view.findViewById(R.id.tvOff);
        TextView tvQty = (TextView) view.findViewById(R.id.tvQty);

        //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
        Button btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);
        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        Button btnReduce = (Button) view.findViewById(R.id.btnReduce);
        //  btnAddToCart.setOnClickListener(this);

        tvName.setText(product.getItemName());
        if (!product.getItemImage().isEmpty()) {
            Log.e("MIMGGG", product.getItemImage());
        }
        Glide.with(getContext())
                .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(ivProduct);

        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.Dialog_Confirmation(getContext(), "http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath());
            }
        });   imgRecurring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, Recurring_order.class).putExtra("mData",filteredList.get(position)).putExtra("type","1"));
               // Utility.Dialog_Confirmation(getContext(), "http://mmthinkbiz.com/ImageHandler.ashx?image=" + product.getFileName() + "&filePath=" + product.getFilepath());
            }
        });

        DecimalFormat precision = new DecimalFormat("0.00");
//        if (dbDiscount > 0) {
        SharedPreferences prefs = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                ctx.MODE_PRIVATE);

        if (prefs.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = ctx.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }


        if (prefs.getString("Currency", "INR").equalsIgnoreCase("INR")) {

            if (!product.getMRP().equalsIgnoreCase(""))
            {
                if (product.getMRP().isEmpty()) {
                    double res;
                    double amount = Double.parseDouble(product.getMRP());
                    Float finalprice = Float.parseFloat(product.getMRP()) - Float.parseFloat(product.getSaleRate());
                    if (finalprice > 0) {
                        res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMRP());

                    } else {

                        res = Double.parseDouble(String.valueOf("0"));
                    }
                    tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));
                    // tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    // tvOriginalPrice1.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                    // tvSave.setText("Save " + currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                    //tvOff.setText(new DecimalFormat("##.##").format(res) + "% OFF");
                    if (Float.parseFloat(String.valueOf(product.getMRP())) < Float.parseFloat(String.valueOf(product.getSaleRate()))) {
                        tvSave.setText("");
                        tvOff.setText("");
                        tvOff.setVisibility(View.GONE);
                        tvOriginalPrice1.setText("");

                    } else {

                        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tvOriginalPrice1.setText(currency + " " + precision.format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                        tvOff.setVisibility(View.GONE);
                        //  tvOff.setVisibility(View.VISIBLE);
                        tvSave.setText("Save " + currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                        tvOff.setText(new DecimalFormat("##.##").format(res) + "% OFF");

                    }
                } else {


                    double res;

                    if (!product.getMRP().equalsIgnoreCase(""))
                    {

                        Float finalprice = Float.parseFloat(product.getMRP()) - Float.parseFloat(product.getSaleRate());
                        if (finalprice > 0) {
                            res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMRP());

                        } else {

                            res = Double.parseDouble(String.valueOf("0"));
                        }
                        tvOriginalPrice.setText(currency + " " + Double.parseDouble(product.getMRP()));
                        if (Float.parseFloat(String.valueOf(product.getMRP())) < Float.parseFloat(String.valueOf(product.getSaleRate()))) {
                            tvSave.setText("");
                            tvOff.setText("");
                            tvOff.setVisibility(View.GONE);
                            tvOriginalPrice1.setText("");

                        } else {
                            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            tvOriginalPrice1.setText(currency + " " + precision.format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                            tvOff.setVisibility(View.GONE);
                            //  tvOff.setVisibility(View.VISIBLE);
                            tvSave.setText("Save " + currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                            tvOff.setText(new DecimalFormat("##.##").format(res) + "% OFF");

                        }
                    }


                }
            }

        } else {


            if (!product.getMRP().equalsIgnoreCase("")) {

                tvOff.setVisibility(View.GONE);
                tvOriginalPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                double res;
                res = ((Float.parseFloat(product.getSaleRate()) * Float.parseFloat(String.valueOf(dbDiscount))) / 100.0f);
                Float finalprice = Float.parseFloat(product.getSaleRate()) - Float.parseFloat(String.valueOf(res));


                if (finalprice > 0) {
                    tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvOriginalPrice1.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                    tvSave.setText(" " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(dbDiscount))) + " % discount");


                } else {

                    tvOff.setVisibility(View.GONE);
                    tvSave.setVisibility(View.GONE);
                    tvOriginalPrice1.setVisibility(View.GONE);
                }

            }
        }


//        }
//        else {
//            tvOriginalPrice1.setVisibility(View.GONE);
//            tvOff.setVisibility(View.GONE);
//            tvOriginalPrice.setText(currency + " " + precision.format(Double.parseDouble(product.getSaleRate())));
//
//        }
        Preferencehelper prefs5= new Preferencehelper(ctx);

        if (prefs5.getPREFS_trialuser().equalsIgnoreCase("0"))
            {

                btnAddToCart.setVisibility(View.GONE);
                tvQty.setVisibility(View.GONE);
                imgRecurring.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                btnReduce.setVisibility(View.GONE);
            }
        else
        {
            imgRecurring.setVisibility(View.VISIBLE);
            if (Integer.parseInt(product.getQuantity()) > 0) {
                btnAddToCart.setVisibility(View.GONE);
                tvQty.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.VISIBLE);
                btnReduce.setVisibility(View.VISIBLE);

            } else {
                btnAddToCart.setVisibility(View.VISIBLE);
                tvQty.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                btnReduce.setVisibility(View.GONE);
            }
        }


        tvQty.setText(product.getQuantity());

        tvQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordercartdialog(ctx, tvQty.getText().toString(), tvName.getText().toString(), tvOriginalPrice.getText().toString(), tvOriginalPrice1.getText().toString(), tvSave.getText().toString(), tvOff.getText().toString(), position);


            }
        });
//        tvOriginalPrice1.setVisibility(View.GONE);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferencehelper prefs;
                prefs = new Preferencehelper(ctx);

                filteredList.get(position).setQuantity("1");

                //   if (Integer.parseInt(filteredList.get(position).getQuantity())>0){
                monAddCartListener.onAddCart(filteredList.get(position), 1);
                notifyDataSetChanged();
//                try {
//                    if (!TextUtils.isEmpty(prefs.getPrefsContactId()) || prefs.getPrefsContactId().equalsIgnoreCase("7777")) {
//
//                    } else {
//                        ctx.startActivity(new Intent(ctx, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                        //  new LoginDialog(ctx).show();
//                    }
//                } catch (Exception e) {
//                    //  new LoginDialog(ctx).show();
//                    ctx.startActivity(new Intent(ctx, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                }
                /* }else{
                    com.dhanuka.morningparcel.beans.FancyToast.makeText(getContext(), "Please add atleast one quantity.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();

                }*/
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                monAddCartListener.onAddCart(filteredList.get(position), 1);


                int qty = Integer.parseInt(tvQty.getText().toString());
                // tvQty.setText((qty+1)+"");
                filteredList.get(position).setQuantity((qty + 1) + "");

                notifyDataSetChanged();

                  /*  if (qty < Integer.parseInt(product.getUom())) {
                        qty++;
                        tvQty.setText(String.valueOf(qty));
                    } else {
                        Toast.makeText(getContext(), "Only " + product.getUom() + " packages available", Toast.LENGTH_SHORT).show();
                    }*/
            }
        });


        btnReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(tvQty.getText().toString());


                if (qty > 1) {
                    monAddCartListener.onAddCart(filteredList.get(position), 2);
                    filteredList.get(position).setQuantity((qty - 1) + "");

                    notifyDataSetChanged();
                } else if (qty == 1) {

                    monAddCartListener.onAddCart(filteredList.get(position), 3);

                    filteredList.get(position).setQuantity((qty - 1) + "");

                    notifyDataSetChanged();

                }
            }
        });

        if (new Preferencehelper(ctx).getPrefsViewType() == 1 && new Preferencehelper(ctx).getPrefsUsercategory().equalsIgnoreCase("1058")) {
            btnAddToCart.setVisibility(View.GONE);
            tvQty.setVisibility(View.GONE);
            imgRecurring.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
            btnReduce.setVisibility(View.GONE);

        }
        //view.setContent(product);
        return view;
    }

    public void filter(String text) {
        text = text.toLowerCase();
        filteredList.clear();
        if (text.length() == 0) {
            filteredList.addAll(list);
        } else {
            for (ItemMasterhelper product : list) {
                if (product.getItemBarcode().toLowerCase().contains(text) || product.getItemName().toLowerCase().contains(text) || product.getHSNCode().toLowerCase().contains(text) || product.getItemSKU().toLowerCase().contains(text)) {
                    filteredList.add(product);
                }
            }
        }
        Log.e("MYTEXT", text);


        //  notify();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredList.size();
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

                if (!tvQtybt.getText().toString().equalsIgnoreCase("")) {
                    FancyToast.makeText(context, "Quantity shoulld be grater than 1 and less then 100k", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                }

            }
        });


        submitbtnbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (tvQtybt.getText().toString().equalsIgnoreCase("") || Integer.parseInt(tvQtybt.getText().toString()) > 100000 || Integer.parseInt(tvQtybt.getText().toString()) == 0) {
                    FancyToast.makeText(context, "Quantity shoulld be grater than 1 and less then 100k", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                } else {


                    int qty = 0;
                    qty = Integer.parseInt(tvQtybt.getText().toString());
                    filteredList.get(position).setQuantity((tvQtybt.getText().toString()));
                    Log.d("filterquantitypop",filteredList.get(position).getQuantity());
                    monAddCartListener.onAddCart(filteredList.get(position), 6);
                    closeKeyboard();
                    bottomcartdialog.dismiss();
                    notifyDataSetChanged();
                }


            }
        });

        showKeyboard();


        Glide.with(getContext())
                .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + list.get(position).getFileName() + "&filePath=" +  list.get(position).getFilepath())
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
