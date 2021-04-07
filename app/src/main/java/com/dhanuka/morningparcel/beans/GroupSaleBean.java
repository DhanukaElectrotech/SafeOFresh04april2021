package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GroupSaleBean implements Serializable {


    @SerializedName("filepath")
    String filepath;
  @SerializedName("FileName")
    String FileName;
    @SerializedName("GroupID")
    String GroupID;
    @SerializedName("GroupName")
    String GroupName;
    @SerializedName("Qty")
    String Qty;
    @SerializedName("BillAmount")
    String BillAmount;
    @SerializedName("Discount")
    String Discount;
    @SerializedName("DiscountP")
    String DiscountP;
    @SerializedName("CostPrice")
    String CostPrice;
    @SerializedName("Margin")
    String Margin;
    @SerializedName("MarginPerc")
    String MarginPerc;


    Boolean isOpened=false;

    public Boolean getOpened() {
        return isOpened;
    }

    public void setOpened(Boolean opened) {
        isOpened = opened;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDiscountP() {
        return DiscountP;
    }

    public void setDiscountP(String discountP) {
        DiscountP = discountP;
    }

    public String getCostPrice() {
        return CostPrice;
    }

    public void setCostPrice(String costPrice) {
        CostPrice = costPrice;
    }

    public String getMargin() {
        return Margin;
    }

    public void setMargin(String margin) {
        Margin = margin;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getMarginPerc() {
        return MarginPerc;
    }

    public void setMarginPerc(String marginPerc) {
        MarginPerc = marginPerc;
    }
}
