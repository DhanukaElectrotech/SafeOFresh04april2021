package com.dhanuka.morningparcel.activity.barcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
 import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Bar extends AppCompatActivity implements ZXingScannerView.ResultHandler {
     public ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
     }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
       // prefSetup.setbar(rawResult.getText());
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(Bar.this);
           SharedPreferences.Editor mEditor=prefs.edit();
        mEditor.putString("mBar",rawResult.getText());
        mEditor.commit();

        Intent intent = new Intent();
        intent.putExtra("mBar", rawResult.getText());
        setResult(RESULT_OK, intent);
       // finish();
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();*/

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
        onBackPressed();

    }
}