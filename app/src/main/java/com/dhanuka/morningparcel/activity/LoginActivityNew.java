package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivityNew extends AppCompatActivity {

    @BindView(R.id.txtLogin)
    TextView txtLogin;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtUserName)
    EditText edtUserName;
    int isEnabled = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);

        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 9 && edtPassword.getText().toString().length() > 5) {
                    txtLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isEnabled = 1;
                } else {
                    txtLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                    isEnabled = 0;
                }

            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5 && edtUserName.getText().toString().length() > 9) {
                    txtLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isEnabled = 1;
                } else {
                    txtLogin.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                    isEnabled = 0;
                }

            }
        });

    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void onLoginWithOtp(View view) {
        startActivity(new Intent(LoginActivityNew.this, LoginWithOtpActivity.class));
    }
}
