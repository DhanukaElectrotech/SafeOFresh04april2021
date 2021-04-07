package com.dhanuka.morningparcel.restaurant.models;

import java.io.Serializable;

public class Addresshelper implements Serializable {
    String cityname,completeaddress;


    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCompleteaddress() {
        return completeaddress;
    }

    public void setCompleteaddress(String completeaddress) {
        this.completeaddress = completeaddress;
    }
}
