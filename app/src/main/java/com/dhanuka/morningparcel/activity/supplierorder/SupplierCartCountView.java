package com.dhanuka.morningparcel.activity.supplierorder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

/**
 */

public class SupplierCartCountView extends FrameLayout implements View.OnClickListener{

    private TextView tvCount;

    public SupplierCartCountView(Context context) {
        super(context);
    }

    public SupplierCartCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupplierCartCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SupplierCartCountView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvCount = (TextView)findViewById(R.id.tvCount);
        setOnClickListener(this);
    }

    public void setCount(int value){
        if(value>99){
            tvCount.setText("99+");
        }else{
            tvCount.setText(String.valueOf(value));
        }
    }

    @Override
    public void onClick(View view) {
        getContext().startActivity(new Intent(getContext(), SupplierCartActivity.class));
//        try{  if (getContext()!=(SupplierCartActivity)getContext()) {
//            getContext().startActivity(new Intent(getContext(), SupplierCartActivity.class));
//        }
//        } catch (Exception e){
//            getContext().startActivity(new Intent(getContext(), SupplierCartActivity.class));
//            e.printStackTrace();
//        }

    }
}
