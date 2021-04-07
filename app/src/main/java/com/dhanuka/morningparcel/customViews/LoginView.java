package com.dhanuka.morningparcel.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.StartActivityNew;

/**
 *
 */

public class LoginView extends FrameLayout implements View.OnClickListener {

    Preferencehelper prefsOld;
    private FrameLayout tvCount;

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoginView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        prefsOld = new Preferencehelper(getContext());
        setOnClickListener(this);

    }

    public void setCount(int value) {

    }

    @Override
    public void onClick(View view) {

        //Utility.selectShop(getContext());

          getContext().startActivity(new Intent(getContext(), StartActivityNew.class));
    }


}
