package com.dhanuka.morningparcel.activity.retail.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemBean implements Serializable {

    @SerializedName("barcode")
    String barcode;
    @SerializedName("itemId")
    String itemId;
    @SerializedName("RAmount")
    String RAmount;
    @SerializedName("qty")
    String qty;
    @SerializedName("Rate")
    String Rate;
    @SerializedName("sku")
    String sku;
    @SerializedName("ItemDesc")
    String ItemDesc;
    @SerializedName("Status")
    String Status;
    @SerializedName("PayemntType")
    String PayemntType;
    @SerializedName("Type")
    String Type;
    @SerializedName("Discount")
     String Discount;
     @SerializedName("DetailID")
     String DetailID;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRAmount() {
        return RAmount;
    }

    public void setRAmount(String RAmount) {
        this.RAmount = RAmount;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDetailID() {
        return DetailID;
    }

    public void setDetailID(String detailID) {
        DetailID = detailID;
    }

    public String getPayemntType() {
        return PayemntType;
    }

    public void setPayemntType(String payemntType) {
        PayemntType = payemntType;
    }
}
