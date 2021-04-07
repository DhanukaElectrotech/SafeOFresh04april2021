package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginWithOtpActivity extends AppCompatActivity implements OnOtpCompletionListener {


    @BindView(R.id.txtLogin)
    TextView txtLogin;
    @BindView(R.id.txtVerify)
    TextView txtVerify;
    @BindView(R.id.scr1)
    LinearLayout scr1;
    @BindView(R.id.scr2)
    LinearLayout scr2;
    @BindView(R.id.edtUserName)
    EditText edtUserName;
    int isEnabled = 0;
    @BindView(R.id.otp_view)
    OtpView otpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_otp);
        ButterKnife.bind(this);
        otpView.setOtpCompletionListener(this);


        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9) {
                    txtLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isEnabled = 1;
                } else {
                    txtLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                    isEnabled = 0;
                }

            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnabled == 1) {
                    scr1.setVisibility(View.GONE);
                    scr2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (scr2.getVisibility() == View.VISIBLE) {
            scr2.setVisibility(View.GONE);
            scr1.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();

        }

    }

    public void onBackClick(View view) {

        onBackPressed();
    }

    String myOtp = "123456";

    @Override
    public void onOtpCompleted(String otp) {
        if (otp.equalsIgnoreCase(myOtp)) {
            txtVerify.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        } else {
            txtLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));

        }
    }
}
