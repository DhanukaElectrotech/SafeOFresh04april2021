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

import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.beans.CartProduct;

/**
  */
public class CartView extends LinearLayout implements View.OnClickListener {

    private CartProduct product;
    private ImageView ivDelete, ivProduct;
    private TextView tvName, tvPrice, tvQty;
    private Button btnAdd, btnReduce;
    private int position;

    public CartView(Context context) {
        super(context);
    }

    public CartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ivDelete = (ImageView) findViewById(R.id.ivDelete);
            ivProduct = (ImageView) findViewById(R.id.ivProduct);
            tvName = (TextView) findViewById(R.id.tvName);
            tvPrice = (TextView) findViewById(R.id.tvPrice);
            tvQty = (TextView) findViewById(R.id.tvQty);
            btnAdd = (Button) findViewById(R.id.btnAdd);
            btnReduce = (Button) findViewById(R.id.btnReduce);
        }
    }

    public void setContent(CartProduct product, int position) {
        this.product = product;
        this.position = position;
       // Picasso.with(getContext()).load(ApiUrl.IMAGE_BASE_URL + product.getImage()).placeholder(R.drawable.no_image_icon).into(ivProduct);
        tvName.setText(product.getItemName() );
        /*if(product.getPack().toLowerCase().equals("pack")){
            if(product.getP_item().length()>0){
                int p_item=Integer.parseInt(product.getP_item());
                if(p_item>1){
                    tvPack.setText("Pack of "+product.getP_item());
                }else{
                    tvPack.setText("Single Pack");
                }
            }else{
                tvPack.setText("Single Pack");
            }
        }else{
            tvPack.setText("Loose");
        }
        if(product.getUom().equals("gm") || product.getUom().equals("kg")){
            tvWeight.setText("Unit Weight: ");
        }else{
            tvWeight.setText("Unit Vol: ");
        }
        tvWeight.setText(tvWeight.getText()+product.getUnit_weight()+" "+product.getUom());*/
        tvQty.setText(String.valueOf(product.getQuantity()));
        ivDelete.setOnClickListener(this);
        btnReduce.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        setPrice();
    }

    private void setPrice() {
        float price = Float.parseFloat(product.getSaleRate()) * product.getQuantity();
        tvPrice.setText(getContext().getString(R.string.rupee) + " " + String.format("%.2f", price));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivDelete:
              //  EventBus.getDefault().post(new DeleteProductEvent(product));
                break;
            case R.id.btnReduce:
            //    product.setQty(product.getQty() - 1);
             //   EventBus.getDefault().post(new ReduceItemEvent(product, position));
                break;
            case R.id.btnAdd:
            //    product.setQty(product.getQty() + 1);
            //    EventBus.getDefault().post(new AddItemEvent(product, position));
                break;
        }
    }
}