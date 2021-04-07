package com.dhanuka.morningparcel.utils;

/**
 * Created by Mr.Mad on 1/24/2019.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionModule {

    private final Context mContext;

    public PermissionModule(Context context) {
        mContext = context;
    }

    public void checkPermissions() {
        ArrayList<String> permissionsNeeded = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
  if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


        if (permissionsNeeded.size() > 0) {
            String arr[] = new String[permissionsNeeded.size()];
            for (int a = 0; a < permissionsNeeded.size(); a++) {
                arr[a] = permissionsNeeded.get(a);
            }

            requestPermission(arr);
        }


    }

    private void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions((AppCompatActivity) mContext, permissions, 125);
    }

}
