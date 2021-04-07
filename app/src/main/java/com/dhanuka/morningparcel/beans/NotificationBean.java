package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationBean implements Serializable {

    @SerializedName("SRNO")
    private String strSrno;
    @SerializedName("Name")
    private String strName;
    @SerializedName("RouteNo")
    private String strRouteno;
    @SerializedName("CreatedDate")
    private String strCreatedate;
    @SerializedName("DeviceId")
    private String strDeviceid;

    public String getStrmsg() {
        return strmsg;
    }

    public void setStrmsg(String strmsg) {
        this.strmsg = strmsg;
    }

    @SerializedName("Message")
    private String strmsg;

    public String getStrSrno() {
        return strSrno;
    }

    public void setStrSrno(String strSrno) {
        this.strSrno = strSrno;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrRouteno() {
        return strRouteno;
    }

    public void setStrRouteno(String strRouteno) {
        this.strRouteno = strRouteno;
    }

    public String getStrCreatedate() {
        return strCreatedate;
    }

    public void setStrCreatedate(String strCreatedate) {
        this.strCreatedate = strCreatedate;
    }

    public String getStrDeviceid() {
        return strDeviceid;
    }

    public void setStrDeviceid(String strDeviceid) {
        this.strDeviceid = strDeviceid;
    }





}
