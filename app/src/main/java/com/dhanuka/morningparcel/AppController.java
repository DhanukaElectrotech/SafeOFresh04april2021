package com.dhanuka.morningparcel;

import android.app.Application;
import android.content.Context;

public class AppController extends Application {
    public  Context ctx;
    @Override
    public void onCreate() {
        super.onCreate();
ctx=getApplicationContext();
    }

    public static final String URL_API_KEY = "AIzaSyDinORzMw4KdhVEs38p3TdL7H0zsXtqlKU";
    public static final String URL_UPLOAD_PROFILE_PHOTO ="http://mmthinkbiz.com/MobileService.aspx/NewRegistration?method=UploadImage";



    public static String getValTypeFromDesc(String description) {
        //JKHelper.showLog("Description:"+description);
        String valType = "10";
        if (description.equalsIgnoreCase("WO Before Status")) {
            valType = "10";
        } else if (description.equalsIgnoreCase("WO After Status")) {
            valType = "11";
        } else if (description.equalsIgnoreCase("WO Before Client Signature")) {
            valType = "12";
        } else if (description.equalsIgnoreCase("WO After Client Signature")) {
            valType = "13";
        } else if (description.equalsIgnoreCase("WO Item Image")) {
            valType = "14";
        } else if (description.equalsIgnoreCase("Client WO Created")) {
            valType = "17";
        } else if (description.equalsIgnoreCase("Coming Late")) {
            valType = "19";
        } else if (description.equalsIgnoreCase("Attendance Start")) {
            valType = "22";
        } else if (description.equalsIgnoreCase("Attendance Close")) {
            valType = "23";
        } else if (description.equalsIgnoreCase("Expenditures")) {
            valType = "26";
        }else if (description.equalsIgnoreCase("trip_image")) {
            valType = "28";
        }
        return valType;
    }
}
