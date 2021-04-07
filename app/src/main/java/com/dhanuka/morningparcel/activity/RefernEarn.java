package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefernEarn extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.sharecode)
    TextView sharecode;
    @BindView(R.id.sharecode1)
    ImageView sharecode1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refern_earn);
        ButterKnife.bind(this);
        sharecode1.setOnClickListener(this);
        sharecode.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())

        {
            case R.id.sharecode1:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey download this app and you ll get 100Rs cashback on every referal by you and to you";
                String shareSub = "Safe'O'Fresh Grocery App";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

                break;
            case R.id.sharecode:
                Intent sharingIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent1.setType("text/plain");
                String shareBody1= "Hey download this app and you ll get 100Rs cashback on every referal by you and to you";
                String shareSub1 = "Safe'O'Fresh Grocery App";
                sharingIntent1.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub1);
                sharingIntent1.putExtra(android.content.Intent.EXTRA_TEXT, shareBody1);
                startActivity(Intent.createChooser(sharingIntent1, "Share using"));
                break;

        }

    }
}