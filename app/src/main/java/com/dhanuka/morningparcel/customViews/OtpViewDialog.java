package com.dhanuka.morningparcel.customViews;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.activity.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/*import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;*/

public class OtpViewDialog extends Dialog implements
        View.OnClickListener/*, OnOtpCompletionListener*/ {
    Context ctx;
    Resources res;
    @BindView(R.id.backbtnicon)
    Button backbtnicon;
    @BindView(R.id.txtResend)
    TextView txtResend;
    @BindView(R.id.validateButton)
    Button validateButton;
/*    @BindView(R.id.otp_view)
    OtpView otpView;*/

    public OtpViewDialog(Context context) {
        // TODO Auto-generated constructor stub
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        ctx = context;
        res = ctx.getResources();

        context.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setOnCancelListener(null);
        LayoutInflater inflate = (LayoutInflater) ctx
                .getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View layout = inflate.inflate(R.layout.dialog_otp_view, null);
        setContentView(layout);
        ButterKnife.bind(this, layout);


        backbtnicon.setOnClickListener(this);
        txtResend.setOnClickListener(this);
        validateButton.setOnClickListener(this);

    }


 /*   @Override
    public void onOtpCompleted(String otp) {
        // do Stuff
        Utility.showCenterToast(ctx, FancyToast.ERROR, "OnOtpCompletionListener called");
    }*/

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backbtnicon) {
            this.dismiss();
        }
        if (v.getId() == R.id.txtResend) {
            this.dismiss();
        }
        if (v.getId() == R.id.validateButton) {
            ctx.startActivity(new Intent(ctx, SignUpActivity.class));
        }
    }

}
