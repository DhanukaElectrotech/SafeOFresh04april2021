package com.dhanuka.morningparcel.restaurant.models;

import java.io.Serializable;

public class circularbrandhelper implements Serializable {
    String brandimage,brandname,timplace;

    public String getBrandimage() {
        return brandimage;
    }

    public void setBrandimage(String brandimage) {
        this.brandimage = brandimage;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getTimplace() {
        return timplace;
    }

    public void setTimplace(String timplace) {
        this.timplace = timplace;
    }
}
