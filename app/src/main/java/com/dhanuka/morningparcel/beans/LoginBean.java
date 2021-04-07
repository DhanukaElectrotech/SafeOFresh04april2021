package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginBean implements Serializable {

    @SerializedName("Paymentoption")
    private String Paymentoption;
  @SerializedName("CustPayId")
    private String CustPayId;
    @SerializedName("OTP")
    private String OTP;


    @SerializedName("Country")



    private String Country;
    @SerializedName("returnmessage")
    private String strreturnmessage;
    @SerializedName("uid")
    private String struid;
    @SerializedName("val1")
    private String strval1;
    @SerializedName("gcmstatus")
    private String strgcmstatus;
    @SerializedName("usercategory")
    private String strusercategory;
    @SerializedName("Blatlong")
    private String strblatlong;
    @SerializedName("val2")
    private String strval2;
    @SerializedName("val3")
    private String strval3;
    @SerializedName("val4")

    private String strval4;

    @SerializedName("token")
    private String strtoken;

    @SerializedName("CurrentBalance")
    private String strcurrentbalance;


    public LoginBean() {
    }

    public String getPaymentoption() {
        return Paymentoption;
    }

    public void setPaymentoption(String paymentoption) {
        Paymentoption = paymentoption;
    }

    public String getStrreturnmessage() {
        return strreturnmessage;
    }

    public void setStrreturnmessage(String strreturnmessage) {
        this.strreturnmessage = strreturnmessage;
    }

    public String getStruid() {
        return struid;
    }

    public void setStruid(String struid) {
        this.struid = struid;

    }

    public String getStrval1() {
        return strval1;
    }

    public void setStrval1(String strval1) {
        this.strval1 = strval1;
    }

    public String getStrval2() {
        return strval2;
    }

    public void setStrval2(String strval2) {
        this.strval2 = strval2;
    }

    public String getStrval3() {
        return strval3;
    }

    public void setStrval3(String strval3) {
        this.strval3 = strval3;
    }

    public String getStrval4() {
        return strval4;
    }

    public void setStrval4(String strval4) {
        this.strval4 = strval4;
    }

    public String getStrgcmstatus() {
        return strgcmstatus;
    }

    public void setStrgcmstatus(String strgcmstatus) {
        this.strgcmstatus = strgcmstatus;
    }

    public String getStrusercategory() {
        return strusercategory;
    }

    public String getStrblatlong() {
        return strblatlong;
    }

    public void setStrblatlong(String strblatlong) {
        this.strblatlong = strblatlong;
    }

    public void setStrusercategory(String strusercategory) {
        this.strusercategory = strusercategory;
    }


    public String getStrtoken() {
        return strtoken;
    }

    public void setStrtoken(String strtoken) {
        this.strtoken = strtoken;
    }

    public String getStrcurrentbalance() {
        return strcurrentbalance;
    }

    public void setStrcurrentbalance(String strcurrentbalance) {
        this.strcurrentbalance = strcurrentbalance;
    }


    public String getCustPayId() {
        return CustPayId;
    }

    public void setCustPayId(String custPayId) {
        CustPayId = custPayId;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }
}
