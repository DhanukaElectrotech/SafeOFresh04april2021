package com.dhanuka.morningparcel.activity.retail.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerBean implements Serializable {
//    {"":"9813329113/66872","":"Adbul Sattar /66872","":"Adbul Sattar ",
//    "":"9813329113","":"66872"}

    @SerializedName("EmpPhoneContact")
    String EmpPhoneContact;
    @SerializedName("EmpNameContact")
    String EmpNameContact;
    @SerializedName("FullName")
    String FullName;
    @SerializedName("Alartphonenumber")
    String Alartphonenumber;
    @SerializedName("ContactID")
    String ContactID;

    public String getEmpPhoneContact() {
        return EmpPhoneContact;
    }

    public void setEmpPhoneContact(String empPhoneContact) {
        EmpPhoneContact = empPhoneContact;
    }

    public String getEmpNameContact() {
        return EmpNameContact;
    }

    public void setEmpNameContact(String empNameContact) {
        EmpNameContact = empNameContact;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getAlartphonenumber() {
        return Alartphonenumber;
    }

    public void setAlartphonenumber(String alartphonenumber) {
        Alartphonenumber = alartphonenumber;
    }

    public String getContactID() {
        return ContactID;
    }

    public void setContactID(String contactID) {
        ContactID = contactID;
    }
}