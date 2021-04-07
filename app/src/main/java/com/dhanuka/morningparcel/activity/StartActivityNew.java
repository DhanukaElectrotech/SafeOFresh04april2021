package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.service.SendFCM;

public class StartActivityNew extends AppCompatActivity {
    TextView  VersionCodee;
    Preferencehelper prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new);
        VersionCodee = findViewById(R.id.Versioncodde);
        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);

            String version = pInfo.versionName;
            VersionCodee.setText("Version code - " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        startService(new Intent(StartActivityNew.this, SendFCM.class));

    }

    public void onGuestClick(View view) {

        prefs =new Preferencehelper(getApplicationContext());

        prefs.setPREFS_trialuser("1");
        startActivity(new Intent(StartActivityNew.this, LocationSelectionAcitivityNew.class).putExtra("signup","5"));
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(StartActivityNew.this, LoginActivity.class).putExtra("signup","3"));
        finish();
    }

    public void onSignUpClick(View view) {
        startActivity(new Intent(StartActivityNew.this, LocationSelectionAcitivityNew.class).putExtra("signup","4"));
        finish();

    }
}
