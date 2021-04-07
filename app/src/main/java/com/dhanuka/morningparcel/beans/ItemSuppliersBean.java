package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemSuppliersBean implements Serializable {

    @SerializedName("vendorid")
    String vendorid;
    @SerializedName("FullName")
    String FullName;
    @SerializedName("GSTNO")
    String GSTNO;
    @SerializedName("Alartphonenumber")
    String Alartphonenumber;
    @SerializedName("ItemName")
    String ItemName;
    @SerializedName("ItemBarcode")
    String ItemBarcode;

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getGSTNO() {
        return GSTNO;
    }

    public void setGSTNO(String GSTNO) {
        this.GSTNO = GSTNO;
    }

    public String getAlartphonenumber() {
        return Alartphonenumber;
    }

    public void setAlartphonenumber(String alartphonenumber) {
        Alartphonenumber = alartphonenumber;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemBarcode() {
        return ItemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        ItemBarcode = itemBarcode;
    }
}
