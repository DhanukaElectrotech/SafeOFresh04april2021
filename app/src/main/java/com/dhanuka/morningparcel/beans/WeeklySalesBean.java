package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeeklySalesBean implements Serializable {
   @SerializedName("Days")
   String Days;
    @SerializedName("Dt")
    String Dt;
   @SerializedName("Total")
    String Total;
   @SerializedName("Morning_Before_12")
    String Morning_Before_12;
   @SerializedName("Noon_between_12_16")
    String Noon_between_12_16;
   @SerializedName("Evening_between_16_20")
    String Evening_between_16_20;
   @SerializedName("Night_After_20")
    String Night_After_20;

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getDt() {
        return Dt;
    }

    public void setDt(String dt) {
        Dt = dt;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getMorning_Before_12() {
        return Morning_Before_12;
    }

    public void setMorning_Before_12(String morning_Before_12) {
        Morning_Before_12 = morning_Before_12;
    }

    public String getNoon_between_12_16() {
        return Noon_between_12_16;
    }

    public void setNoon_between_12_16(String noon_between_12_16) {
        Noon_between_12_16 = noon_between_12_16;
    }

    public String getEvening_between_16_20() {
        return Evening_between_16_20;
    }

    public void setEvening_between_16_20(String evening_between_16_20) {
        Evening_between_16_20 = evening_between_16_20;
    }

    public String getNight_After_20() {
        return Night_After_20;
    }

    public void setNight_After_20(String night_After_20) {
        Night_After_20 = night_After_20;
    }
}
