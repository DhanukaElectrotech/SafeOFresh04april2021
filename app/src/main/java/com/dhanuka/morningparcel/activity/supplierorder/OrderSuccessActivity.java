package com.dhanuka.morningparcel.activity.supplierorder;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.utils.PermissionModule;

import com.shashank.sony.fancytoastlib.FancyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderSuccessActivity extends AppCompatActivity {
    public void makeBackClick(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OrderSuccessActivity.this, SupplierHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    @BindView(R.id.tvOrderID)
    TextView tvOrderID;

    @BindView(R.id.textView2)
    TextView textView2;

    @BindView(R.id.callorder)
    ImageView callorder;


    @BindView(R.id.amounttotal)
    TextView totalamount;

    String orderId = "", amounttotal = "";


    @BindView(R.id.txContact)
    TextView txContact;
    @BindView(R.id.txtPaytm)
    TextView txtPaytm;
    @BindView(R.id.txtGooglePay)
    TextView txtGooglePay;
    @BindView(R.id.txtPhonePe)
    TextView txtPhonePe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        ButterKnife.bind(this);
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();


      /*  prefs.getString("PhonePe", PhonePe);
        prefs.getString("PaytmNumber", PaytmNumber);
        prefs.getString("GooglePay", GooglePay);
*/
        orderId = getIntent().getStringExtra("orderId");
        amounttotal = getIntent().getStringExtra("totalamount");
        totalamount.setText(amounttotal);
        tvOrderID.setText("");
        if (orderId.length() < 3) {
            tvOrderID.setText("#000" + orderId);
        } else {
            tvOrderID.setText("#" + orderId);

        }

        txContact.setText(prefs.getString("Alartphonenumber", "-"));


        txtPaytm.setText(prefs.getString("GooglePay", "-"));
        txtGooglePay.setText(prefs.getString("PaytmNumber", "-"));
        txtPhonePe.setText(prefs.getString("PhonePe", ""));
        textView2.setText(getResources().getString(R.string.hlp).replace("", ""));
        callorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(prefs.getString("Alartphonenumber", ""));
            }
        });


    }

    public void makeCalls(View view) {
        TextView mText = (TextView) view;
        makeCall(mText.getText().toString());
    }

    public void makeCall(String strH) {
        try {
            if (strH != null && strH.length() > 2) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + strH));

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                        PermissionModule permissionModule = new PermissionModule(getApplicationContext());
                        permissionModule.checkPermissions();

                    } else {
                        startActivity(callIntent);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    FancyToast.makeText(getApplicationContext(), "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }

            }
        } catch (Exception e) {
            FancyToast.makeText(getApplicationContext(), "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            e.printStackTrace();
        }
    }
}
