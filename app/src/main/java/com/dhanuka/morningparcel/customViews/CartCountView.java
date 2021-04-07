package com.dhanuka.morningparcel.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.CartActivity;

/**
  */

public class CartCountView extends FrameLayout implements View.OnClickListener{

    private TextView tvCount;

    public CartCountView(Context context) {
        super(context);
    }

    public CartCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CartCountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CartCountView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
      try{  if (getContext()!=(CartActivity)getContext()) {
            getContext().startActivity(new Intent(getContext(), CartActivity.class));
        }
        } catch (Exception e){
          getContext().startActivity(new Intent(getContext(), CartActivity.class));
          e.printStackTrace();
      }

    }
}
