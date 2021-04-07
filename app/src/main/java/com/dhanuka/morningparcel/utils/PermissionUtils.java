package com.dhanuka.morningparcel.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class PermissionUtils {
    public static boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasPermission(AppCompatActivity activity, String permission) {
        if (useRunTimePermissions()) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestPermissions(AppCompatActivity activity, String[] permission, int requestCode) {
        if (useRunTimePermissions()) {
            activity.requestPermissions(permission, requestCode);
        }
    }

    public static void requestPermissions(Fragment fragment, String[] permission, int requestCode) {
        if (useRunTimePermissions()) {
            fragment.requestPermissions(permission, requestCode);
        }
    }

    public static boolean shouldShowRational(AppCompatActivity activity, String permission) {
        if (useRunTimePermissions()) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    public static boolean shouldAskForPermission(AppCompatActivity activity, String permission) {
        if (useRunTimePermissions()) {
            return !hasPermission(activity, permission) &&
                    (!hasAskedForPermission(activity, permission) ||
                            shouldShowRational(activity, permission));
        }
        return false;
    }

    public static void goToAppSettings(AppCompatActivity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static boolean hasAskedForPermission(AppCompatActivity activity, String permission) {
        return PreferenceManager
                .getDefaultSharedPreferences(activity)
                .getBoolean(permission, false);
    }

    public static void markedPermissionAsAsked(AppCompatActivity activity, String permission) {
        PreferenceManager
                .getDefaultSharedPreferences(activity)
                .edit()
                .putBoolean(permission, true)
                .apply();
    }



    private final Context mContext;

    public PermissionUtils(Context context) {
        mContext = context;
    }

    public void checkPermissions() {
        ArrayList<String> permissionsNeeded = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionsNeeded.isEmpty()) {
            requestPermission(permissionsNeeded.toArray(new String[permissionsNeeded.size()]));
        }
    }

    private void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions((AppCompatActivity) mContext, permissions, 125);
    }


}
