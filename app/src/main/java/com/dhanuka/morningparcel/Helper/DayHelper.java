package com.dhanuka.morningparcel.Helper;

import java.io.Serializable;

public class DayHelper implements Serializable {
    String dayname,dayid,dayaddition,daysubstract,addval;

    public String getDayname() {
        return dayname;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }

    public String getDayid() {
        return dayid;
    }

    public void setDayid(String dayid) {
        this.dayid = dayid;
    }

    public String getDayaddition() {
        return dayaddition;
    }

    public void setDayaddition(String dayaddition) {
        this.dayaddition = dayaddition;
    }

    public String getDaysubstract() {
        return daysubstract;
    }

    public void setDaysubstract(String daysubstract) {
        this.daysubstract = daysubstract;
    }

    public String getAddval() {
        return addval;
    }

    public void setAddval(String addval) {
        this.addval = addval;
    }
}


