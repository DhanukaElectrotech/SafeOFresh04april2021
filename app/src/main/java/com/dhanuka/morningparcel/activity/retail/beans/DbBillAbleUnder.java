package com.dhanuka.morningparcel.activity.retail.beans;


public class DbBillAbleUnder {


    private String mPrintName;
    private String mClientId;
    private String mbranchid;
    private String mcurrentstatus;
    private String mbusinesstype;
    private String mnickname;
    private String mbranchcode;
    private int mId;


    public String getmnickname() {
        return mnickname;
    }
    public void setmnickname(String mnickname) {
        this.mnickname = mnickname;
    }

    public String getmPrintName() {
        return mPrintName;
    }

    public void setmPrintName(String mPrintName) {
        this.mPrintName = mPrintName;
    }


    public String getmbranchcode() {
        return mbranchcode;
    }
    public void setmbranchcode(String mbranchcode) {
        this.mbranchcode= mbranchcode;
    }

    public String getmbranchid() {
        return mbranchid;
    }
    public void setmbranchid(String mbranchid) {
        this.mbranchid= mbranchid;
    }
    public String getmcurrentstatus() {
        return mcurrentstatus;
    }
    public void setmcurrentstatus(String mcurrentstatus) {
        this.mcurrentstatus= mcurrentstatus;
    }

    public String getmbusinesstype() {
        return mbusinesstype;
    }
    public void setmbusinesstype(String mbusinesstype) {
        this.mbusinesstype= mbusinesstype;
    }

    public String getmClientId() {
        return mClientId;
    }

    public void setmClientId(String mClientId) {
        this.mClientId = mClientId;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    @Override public String toString() {
        return getmPrintName();
    }


}
