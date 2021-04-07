package com.dhanuka.morningparcel.utils;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.google.firebase.FirebaseApp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.DecimalFormat;
import java.util.Date;


public class AppClass extends MultiDexApplication {

    private double latestlat;
    private double latestlong;
    private double latestaltitude;
    private double latestspeed;
    private double latestodometer;
    private double latestdodometer;
    private Date latestgdate;
    private double latestbearing;
    private String latestaddress;
    private String latestcity;
    private String lateststate;
    private String lastrunningtime;
    private String laststoppagetime;
    private double idlesince;
    private double runningsince;
    private double didlesince;
    private double drunningsince;

    private double idletime;
    private double runningtime;
    private double tripavgspeed;
    private double tripmaxspeed;
    private double nightdrivingtime;
    private double unproductivedistance;
    private double maxstoppageduration;
    private double Distancefromlastpoint;
    private double Transittimefromlastpoint;
    private double maxrunningduration;
    private double maxdrunningduration;

    private double dmaxstoppageduration;
    private double dmaxspeed;
    private double davgspeed;
    private double drunningtime;
    private double didletime;
    private double tripspeedcount;
    private double dspeedcount;
    private double vehicledistanceatreportingtime;
    private String rfirsttime;
    private String maxstoppagelocation;
    private String dmaxstoppagelocation;
    private String vehiclelocationatreportingtime;
    private String vehiclelatatreportingtime;
    private String vehiclelongatreportingtime;
    private String tripstarttime;
    private String maxstoppagetime;
    private DecimalFormat numberFormat4decimal = new DecimalFormat("#.####");
    private DecimalFormat numberFormat7decimal = new DecimalFormat("#.#######");


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public String getlastrunningtime() {
        return lastrunningtime;
    }

    public void setlastrunningtime(String mlastrunningtime) {
        lastrunningtime = mlastrunningtime;
    }

    public String getlaststoppagetime() {
        return laststoppagetime;
    }

    public void setlaststoppagetime(String mlaststoppagetime) {
        laststoppagetime = mlaststoppagetime;
    }

    public double getlat() {
        return latestlat;
    }

    public void setlat(double lat) {
        latestlat = Double.valueOf(numberFormat7decimal.format(lat));
    }

    public double getlong() {
        return latestlong;
    }

    public void setlong(double longs) {
        latestlong = Double.valueOf(numberFormat7decimal.format(longs));
    }

    public double getaltitude() {
        return latestaltitude;
    }

    public void setaltitude(double altitudes) {
        latestaltitude = Double.valueOf(numberFormat7decimal.format(altitudes));
    }

    public double getspeed() {
        return latestspeed;
    }

    public void setspeed(double speed) {
        //latestspeed = Double.valueOf(numberFormat4decimal.format(speed)) ;
        latestspeed = speed;
    }

    public double getodometer() {
        return latestodometer;
    }

    public void setodometer(double odometers) {
        latestodometer = Double.valueOf(numberFormat7decimal.format(odometers));
    }

    public double getdodometer() {
        return latestdodometer;
    }

    public void setdodometer(double dodometers) {
        latestdodometer = Double.valueOf(numberFormat7decimal.format(dodometers));
    }

    public double getbearing() {
        return latestbearing;
    }

    public void setbearing(double bearings) {
        latestbearing = Double.valueOf(numberFormat7decimal.format(bearings));
    }

    public Date getgdate() {
        return latestgdate;
    }

    public void setgdate(Date mgdate) {
        latestgdate = mgdate;
    }

    public String getaddress() {
        return latestaddress;
    }

    public void setaddress(String maddress) {
        latestaddress = maddress;
    }

    public String getcity() {
        return latestcity;
    }

    public void setcity(String mcity) {
        latestcity = mcity;
    }

    public String getstate() {
        return lateststate;
    }

    public void setstate(String mstate) {
        lateststate = mstate;
    }

    public String gettripstarttime() {
        return tripstarttime;
    }

    public void settripstarttime(String tripstarttimes) {
        tripstarttime = tripstarttimes;
    }

    public double gettripspeedcount() {
        return tripspeedcount;
    }

    public void settripspeedcount(double mtripspeedcount) {
        tripspeedcount = mtripspeedcount;
    }

    public double getdspeedcount() {
        return dspeedcount;
    }

    public void setdspeedcount(double mdspeedcount) {
        dspeedcount = mdspeedcount;
    }

    public double getvehicledistanceatreportingtime() {
        return vehicledistanceatreportingtime;
    }

    public void setvehicledistanceatreportingtime(double mvehicledistanceatreportingtime) {
        vehicledistanceatreportingtime = Double.valueOf(numberFormat4decimal.format(mvehicledistanceatreportingtime));
    }

    public String getvehiclelocationatreportingtime() {
        return vehiclelocationatreportingtime;
    }

    public void setvehiclelocationatreportingtime(String mvehiclelocationatreportingtime) {
        vehiclelocationatreportingtime = mvehiclelocationatreportingtime;
    }

    public String getvehiclelatatreportingtime() {
        return vehiclelatatreportingtime;
    }

    public void setvehiclelatatreportingtime(String mvehiclelatatreportingtime) {
        vehiclelatatreportingtime = mvehiclelatatreportingtime;
    }

    public String getvehiclelongatreportingtime() {
        return vehiclelongatreportingtime;
    }

    public void setvehiclelongatreportingtime(String mvehiclelongatreportingtime) {
        vehiclelongatreportingtime = mvehiclelongatreportingtime;
    }

    public String getmaxstoppagelocation() {
        return maxstoppagelocation;
    }

    public void setmaxstoppagelocation(String mmaxstoppagelocation) {
        maxstoppagelocation = mmaxstoppagelocation;
    }

    public String getdmaxstoppagelocation() {
        return dmaxstoppagelocation;
    }

    public void setdmaxstoppagelocation(String mdmaxstoppagelocation) {
        dmaxstoppagelocation = mdmaxstoppagelocation;
    }

    public String getrfirsttime() {
        return rfirsttime;
    }

    public void setrfirsttime(String mrfirsttime) {
        rfirsttime = mrfirsttime;
    }

    public double getdrunningtime() {
        return drunningtime;
    }

    public void setdrunningtime(double mdrunningtime) {
        drunningtime = mdrunningtime;
    }

    public double getdidletime() {
        return didletime;
    }

    public void setdidletime(double mdidletime) {
        didletime = mdidletime;
    }

    public double getdmaxspeed() {
        return dmaxspeed;
    }

    public void setdmaxspeed(double mdmaxspeed) {
        dmaxspeed = Double.valueOf(numberFormat4decimal.format(mdmaxspeed));
    }

    public double getdavgspeed() {
        return davgspeed;
    }

    public void setdavgspeed(double mdavgspeed) {
        davgspeed = Double.valueOf(numberFormat4decimal.format(mdavgspeed));
    }

    public double gettripmaxspeed() {
        return tripmaxspeed;
    }

    public void settripmaxspeed(double mtripmaxspeed) {
        tripmaxspeed = Double.valueOf(numberFormat4decimal.format(mtripmaxspeed));
    }

    public double getunproductivedistance() {
        return unproductivedistance;
    }

    public void setunproductivedistance(double munproductivedistance) {
        unproductivedistance = Double.valueOf(numberFormat4decimal.format(munproductivedistance));
    }

    public double getmaxstoppageduration() {
        return maxstoppageduration;
    }

    public void setmaxstoppageduration(double mmaxstoppageduration) {
        maxstoppageduration = Double.valueOf(numberFormat4decimal.format(mmaxstoppageduration));
    }

    public double getDistancefromlastpoint() {
        return Distancefromlastpoint;
    }

    public void setDistancefromlastpoint(double mDistancefromlastpoint) {
        Distancefromlastpoint = Double.valueOf(numberFormat4decimal.format(mDistancefromlastpoint));
    }

    public double getTransittimefromlastpoint() {
        return Transittimefromlastpoint;
    }

    public void setTransittimefromlastpoint(double mTransittimefromlastpoint) {
        Transittimefromlastpoint = mTransittimefromlastpoint;
    }

    public double getdmaxstoppageduration() {
        return dmaxstoppageduration;
    }

    public void setdmaxstoppageduration(double mdmaxstoppageduration) {
        dmaxstoppageduration = Double.valueOf(numberFormat4decimal.format(mdmaxstoppageduration));
    }

    public double getnightdrivingtime() {
        return nightdrivingtime;
    }

    public void setnightdrivingtime(double mnightdrivingtime) {
        nightdrivingtime = mnightdrivingtime;
    }

    public String getmaxstoppagetime() {
        return maxstoppagetime;
    }

    public void setmaxstoppagetime(String mmaxstoppagetime) {
        maxstoppagetime = mmaxstoppagetime;
    }

    public double getrunningtime() {
        return runningtime;
    }

    public void setrunningtime(double mrunningtime) {
        runningtime = mrunningtime;
    }

    public double gettripavgspeed() {
        return tripavgspeed;
    }

    public void settripavgspeed(double mtripavgspeed) {
        tripavgspeed = Double.valueOf(numberFormat4decimal.format(mtripavgspeed));
    }

    public double getidletime() {
        return idletime;
    }

    public void setidletime(double midletime) {
        idletime = Double.valueOf(numberFormat4decimal.format(midletime));
    }

    public double getidlesince() {
        return idlesince;
    }

    public void setidlesince(double midlesince) {
        idlesince = midlesince;
    }

    public double getdidlesince() {
        return didlesince;
    }

    public void setdidlesince(double mdidlesince) {
        didlesince = mdidlesince;
    }

    public double getrunningsince() {
        return runningsince;
    }

    public void setrunningsince(double mrunningsince) {
        runningsince = mrunningsince;
    }

    public double getdrunningsince() {
        return drunningsince;
    }

    public void setdrunningsince(double mdrunningsince) {
        drunningsince = mdrunningsince;
    }


    public double getmaxrunningduration() {
        return maxrunningduration;
    }

    public void setmaxrunningduration(double mmaxrunningduration) {
        maxrunningduration = mmaxrunningduration;
    }

    public double getmaxdrunningduration() {
        return maxdrunningduration;
    }

    public void setmaxdrunningduration(double mmaxdrunningduration) {
        maxdrunningduration = mmaxdrunningduration;
    }
}
