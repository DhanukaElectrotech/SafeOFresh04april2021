package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginBeanMain implements Serializable {

    @SerializedName("success")
    private int strSuccess;
    @SerializedName("loginvalidation")
    private ArrayList<LoginBean> mListLogin;

    public LoginBeanMain() {
    }

    public int getStrSuccess() {
        return this.strSuccess;
    }

    public void setStrSuccess(int strSuccess) {
        this.strSuccess = strSuccess;
    }

    public ArrayList<LoginBean> getmListLogin() {
        return this.mListLogin;
    }

    public void setmListVehicle(ArrayList<LoginBean> mListLogin) {
        this.mListLogin = mListLogin;
    }
}
