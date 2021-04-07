package com.dhanuka.morningparcel.restaurant.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GeoFenceBeanMain {


    @SerializedName("success")
    private int strSuccess;
    @SerializedName("returnds")
    private ArrayList<GeoFenceBean> strreturns;



    public int getStrSuccess() {
        return strSuccess;
    }

    public void setStrSuccess(int strSuccess) {
        this.strSuccess = strSuccess;
    }


    public ArrayList<GeoFenceBean> getStrreturns() {
        return strreturns;
    }

    public void setStrreturns(ArrayList<GeoFenceBean> strreturns) {
        this.strreturns = strreturns;
    }

}
