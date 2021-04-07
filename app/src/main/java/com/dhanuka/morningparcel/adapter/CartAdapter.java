package com.dhanuka.morningparcel.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.OrderDetailActivity;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.CartActionListener;
import com.dhanuka.morningparcel.events.CartTextchnage;
import com.dhanuka.morningparcel.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import ru.softbalance.widgets.NumberEditText;

/**
 *
 */
public class CartAdapter extends ArrayAdapter<CartProduct> {

    List<CartProduct> list;
    LayoutInflater inflater;
    CartActionListener mCartActionListener;
    String currency="";
    TextView tvNamebt,tvPricebt,tvsalebt,tvSavebt,tvOffbt;
    NumberEditText tvQtybt;
    ImageView ivProductbt;
    Button submitbtnbt;
    BottomSheetDialog bottomcartdialog;
    int firstrip=0;
    CartTextchnage cartTextchnage;
    Context context;
    SharedPreferences prefs;
    double dbDiscount = 0.0;
    public CartAdapter(Context context, List<CartProduct> objects, CartActionListener cartActionListener, CartTextchnage cartTextchnage) {
        super(context, R.layout.item_cart, objects);
        list = objects;
        this.context=context;
        this.cartTextchnage=cartTextchnage;
        mCartActionListener = cartActionListener;
        inflater = LayoutInflater.from(context);
         prefs = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);


        if (prefs.getString("Currency","INR").equalsIgnoreCase("INR")){
            currency=context.getResources().getString(R.string.rupee);
        }else{
            currency="$";
        }

        if (prefs.getString("discount", "0.0").isEmpty()) {
            dbDiscount = 0.0;

        } else {
            try {
                dbDiscount = Double.parseDouble(prefs.getString("discount", "0.0"));
            } catch (Exception e) {
                dbDiscount = 0.0;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_cart, parent, false);
        } else {
            view = convertView;
        }

        CartProduct product = list.get(position);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        ImageView ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvSale = (TextView) view.findViewById(R.id.tvsale);
        TextView tvSave = (TextView) view.findViewById(R.id.tvSave);
        TextView tvOff = (TextView) view.findViewById(R.id.tvOff);

        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        TextView tvQty = view.findViewById(R.id.tvQty);
        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        Button btnReduce = (Button) view.findViewById(R.id.btnReduce);
        tvName.setText(product.getItemName());


        float price = Float.parseFloat(product.getSaleRate()) * product.getQuantity();
//        tvQty.requestFocus();


        tvPrice.setText(currency + " " + String.format("%.2f", price));

//        tvQty.requestFocus();


        tvQty.setText(""+new DecimalFormat("#").format(Double.parseDouble(String.valueOf(product.getQuantity()))));
       // tvQty.setText(""+new DecimalFormat("#").format(Double.parseDouble(String.valueOf(product.getQuantity()))));


        tvQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordercartdialog(context,tvQty.getText().toString(),tvName.getText().toString(),tvPrice.getText().toString(),tvSale.getText().toString(),tvSave.getText().toString(),tvOff.getText().toString(),position);


            }
        });
//        tvQty.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int x = (int) event.getX();
//                int y = (int) event.getY();
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        ordercartdialog(context,tvQty.getText().toString(),tvName.getText().toString(),tvPrice.getText().toString(),tvSale.getText().toString(),tvSave.getText().toString(),tvOff.getText().toString(),position);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        ordercartdialog(context,tvQty.getText().toString(),tvName.getText().toString(),tvPrice.getText().toString(),tvSale.getText().toString(),tvSave.getText().toString(),tvOff.getText().toString(),position);
//                        break;
//                }
//
//                return false;
//            }
//        });



        if (prefs.getString("Currency", "INR").equalsIgnoreCase("INR")) {

            if (!product.getMRP().equalsIgnoreCase(""))
            {
                if (product.getMRP().isEmpty()) {
                    double amount = Double.parseDouble(product.getMRP());

                    Float finalprice =  Float.parseFloat(product.getMRP())- Float.parseFloat(product.getSaleRate());

                    double res ;
                    if (finalprice>0)
                    {
                        res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMRP());

                    }
                    else
                    {

                        res = Double.parseDouble(String.valueOf("0"));
                    }
                    if   (Float.parseFloat(String.valueOf(product.getMRP())) < Float.parseFloat(String.valueOf(product.getSaleRate())) )
                    {

                        tvPrice.setText(currency + " " +  new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));
                        // tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tvSale.setText("");
                        tvSave.setText("");
                        tvOff.setVisibility(View.GONE);
                        tvOff.setText("");

                    }
                    else
                    {
                        tvPrice.setText(currency + " " +  new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));
                        tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tvSale.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                        tvSave.setText("Save "+currency+new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                        tvOff.setText( new DecimalFormat("##.##").format(res)+ "% OFF");


                    }

                } else {
                    double amount = Double.parseDouble(product.getMRP());
                    Float finalprice =  Float.parseFloat(product.getMRP())- Float.parseFloat(product.getSaleRate());
                    double res ;
                    if (finalprice>0)
                    {
                        res = ((finalprice * 100.0f)) / Float.parseFloat(product.getMRP());

                    }
                    else
                    {

                        res = Double.parseDouble(String.valueOf("0"));
                    }
                    if   (Float.parseFloat(String.valueOf(product.getMRP())) < Float.parseFloat(String.valueOf(product.getSaleRate())) )
                    {

                        tvOff.setVisibility(View.GONE);
                        tvPrice.setText(currency + " " +  new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));
                        // tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tvSale.setText("");
                        tvSave.setText("");
                        tvOff.setText("");

                    }
                    else
                    {
                        tvPrice.setText(currency + " " +  new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getMRP()))));
                        tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tvSale.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                        tvSave.setText("Save "+currency+new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                        tvOff.setText( new DecimalFormat("##.##").format(res)+ "% OFF");


                    } }
            }





        }
        else {

            if (!product.getMRP().equalsIgnoreCase("")) {

                double res;

                tvOff.setVisibility(View.GONE);
                tvSale.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(product.getSaleRate()))));
                res = ((Float.parseFloat(product.getSaleRate()) * Float.parseFloat(String.valueOf(dbDiscount))) / 100.0f);
                Float finalprice = Float.parseFloat(product.getSaleRate()) + Float.parseFloat(String.valueOf(res));


                if (finalprice > 0) {
                    tvPrice.setPaintFlags(tvSale.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalprice))));
                    tvSave.setText(  " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(dbDiscount))) +" % discount");


                } else {

                    tvOff.setVisibility(View.GONE);
                    tvSave.setVisibility(View.GONE);
                    tvPrice.setVisibility(View.GONE);
                }



            }
        }





        //  setPrice();
        Glide.with(getContext())
                .load("http://mmthinkbiz.com/ImageHandler.ashx?image="+product.getItemImage())
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(ivProduct);


        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setQuantity(list.get(position).getQuantity() - 1);
                mCartActionListener.onDeleteClick((ArrayList<CartProduct>) list,position);
//                list.remove(position);
                notifyDataSetChanged();
            }
        });        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utility.Dialog_Confirmation(getContext(),"http://mmthinkbiz.com/ImageHandler.ashx?image="+product.getItemImage());
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setQuantity(list.get(position).getQuantity() + 1);
                //  checkedPosition = getAdapterPosition();
                mCartActionListener.onAddClick((ArrayList<CartProduct>) list,position);
                notifyDataSetChanged();

            }
        });
        btnReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getQuantity() > 1) {
                    list.get(position).setQuantity(list.get(position).getQuantity() - 1);
                    mCartActionListener.onMinusClick((ArrayList<CartProduct>) list,position);
                }else{
                    list.get(position).setQuantity(list.get(position).getQuantity() - 1);
                    mCartActionListener.onDeleteClick((ArrayList<CartProduct>) list,position);

                }

                notifyDataSetChanged();

            }
        });



        //  view.setContent(list.get(position),position);
        return view;
    }



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


                    list.get(position).setQuantity(Integer.parseInt(tvQtybt.getText().toString()));
                    //  checkedPosition = getAdapterPosition();
                    mCartActionListener.onAddClick((ArrayList<CartProduct>) list,position);
                    closeKeyboard();
                    bottomcartdialog.dismiss();
                }


            }
        });

        showKeyboard();


        Glide.with(getContext())
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
