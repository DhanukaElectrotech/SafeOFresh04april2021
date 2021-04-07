package com.dhanuka.morningparcel.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.events.AddCartEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hitesh on 11/24/2016.
 */
public class ProductView extends LinearLayout implements View.OnClickListener {

    private ItemMasterhelper product;
    private String price;
    private ImageView ivProduct;
    private TextView tvName, tvOriginalPrice, tvNewPrice, tvQty, tvOriginalPrice2, tvOriginalPrice1;
    private Button btnAddToCart, btnAdd, btnReduce;
    private int qty = 1;

    public ProductView(Context context) {
        super(context);
    }

    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProductView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ivProduct = (ImageView) findViewById(R.id.ivProduct);
            tvName = (TextView) findViewById(R.id.tvName);
            tvOriginalPrice1 = (TextView) findViewById(R.id.tvOriginalPrice1);
            tvOriginalPrice2 = (TextView) findViewById(R.id.tvOriginalPrice2);
            tvOriginalPrice = (TextView) findViewById(R.id.tvOriginalPrice);
            tvQty = (TextView) findViewById(R.id.tvQty);
            //tvNewPrice=(TextView)findViewById(R.id.tvNewPrice);
            btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
            btnAdd = (Button) findViewById(R.id.btnAdd);
            btnReduce = (Button) findViewById(R.id.btnReduce);
            btnAddToCart.setOnClickListener(this);
            btnAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  if (qty < Integer.parseInt(product.getUom())) {
                        qty++;
                        tvQty.setText(String.valueOf(qty));
                    } else {
                        Toast.makeText(getContext(), "Only " + product.getUom() + " packages available", Toast.LENGTH_SHORT).show();
                    }*/
                }
            });
            btnReduce.setOnClickListener(this);
        }
    }

    public void setContent(ItemMasterhelper product) {
        this.product = product;
      //  Picasso.with(getContext()).load(ApiUrl.IMAGE_BASE_URL + product.getImage()).placeholder(R.drawable.no_image_icon).into(ivProduct);
        tvOriginalPrice2.setText(product.getItemSKU());
         tvName.setText(product.getItemName());
        tvOriginalPrice.setText(getContext().getString(R.string.rupee) + " " +   product.getSaleRate()+"/"+product.getSaleUOM());
        tvQty.setText(String.valueOf(qty));
        tvOriginalPrice1.setVisibility(GONE);

/*         price = product.getPrice();

        float productPrice = Float.parseFloat(price);
        tvOriginalPrice.setText(getContext().getString(R.string.rupee) + " " + String.format("%.2f", productPrice));
        tvQty.setText(String.valueOf(qty));

        if (!product.getDis_rate().equals("")) {
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            try {
                Float rs = (Float.parseFloat(product.getPrice()) * Float.parseFloat(product.getDis_rate())) / 100;


                tvOriginalPrice1.setText((Float.parseFloat(price) - rs) + "");

                price = (Float.parseFloat(price) - rs) + "";


//                double per = (rs / Double.parseDouble(product.getRegular_price()) * 100);
                tvOriginalPrice2.setText("Save " + String.format("%.2f", Double.parseDouble(product.getDis_rate())) + "%");

            } catch (Exception e) {
                e.printStackTrace();
                tvOriginalPrice2.setVisibility(View.GONE);
            }

        } else {
            price = product.getPrice();
            tvOriginalPrice1.setVisibility(GONE);
            tvOriginalPrice2.setVisibility(GONE);
        }*/



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddToCart:
                EventBus.getDefault().post(new AddCartEvent(qty, product, price));
                break;

            case R.id.btnReduce:
                if (qty > 1) {
                    qty--;
                    tvQty.setText(String.valueOf(qty));
                }
                break;
        }
    }
}