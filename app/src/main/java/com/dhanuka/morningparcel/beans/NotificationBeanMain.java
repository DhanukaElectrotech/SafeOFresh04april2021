package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class NotificationBeanMain implements Serializable {

    @SerializedName("success")
    private int strSuccess;
    @SerializedName("Notificationdata")
    private ArrayList<NotificationBean> mListnotificationdata;

    public int getStrSuccess() {
        return strSuccess;
    }

    public void setStrSuccess(int strSuccess) {
        this.strSuccess = strSuccess;
    }

    public ArrayList<NotificationBean> getmListnotificationdata() {
        return mListnotificationdata;
    }

    public void setmListnotificationdata(ArrayList<NotificationBean> mListnotificationdata) {
        this.mListnotificationdata = mListnotificationdata;
    }




    public NotificationBeanMain() {
    }


}
