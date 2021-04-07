package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryBoysBean implements Serializable {
    //    {"":"971","":"66917","":"","":"","":"523","":"8348380984"}
    @SerializedName("roleid")
    String roleid;
    @SerializedName("ContactID")
    String ContactID;
    @SerializedName("FirstName")
    String FirstName;
    @SerializedName("CompanyName")
    String CompanyName;
    @SerializedName("MainCompanyID")
    String MainCompanyID;
    @SerializedName("Alartphonenumber")
    String Alartphonenumber;
    @SerializedName("FlatNo")
    String FlatNo;



    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getContactID() {
        return ContactID;
    }

    public void setContactID(String contactID) {
        ContactID = contactID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getMainCompanyID() {
        return MainCompanyID;
    }

    public void setMainCompanyID(String mainCompanyID) {
        MainCompanyID = mainCompanyID;
    }

    public String getAlartphonenumber() {
        return Alartphonenumber;
    }

    public void setAlartphonenumber(String alartphonenumber) {
        Alartphonenumber = alartphonenumber;
    }

    public String getFlatNo() {
        return FlatNo;
    }

    public void setFlatNo(String flatNo) {
        FlatNo = flatNo;
    }
}
