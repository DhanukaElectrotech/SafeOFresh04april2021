package com.dhanuka.morningparcel.beans;

import java.io.Serializable;

public class CartSendParams implements Serializable {

    String OID;
    String CID;
    String DetailID;
    String CreatedBy;
    String itemId;
    String ItemDesc;
    String qty;
    String sku;
    String Rate;
    String RAmount;
    String Status;
    String Type;
    String Discount;
     String MRP;
     String barcode;
     String val1;
     String val2;

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getDetailID() {
        return DetailID;
    }

    public void setDetailID(String detailID) {
        DetailID = detailID;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getRAmount() {
        return RAmount;
    }

    public void setRAmount(String RAmount) {
        this.RAmount = RAmount;
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

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getVal1() {
        return val1;
    }

    public void setVal1(String val1) {
        this.val1 = val1;
    }

    public String getVal2() {
        return val2;
    }

    public void setVal2(String val2) {
        this.val2 = val2;
    }
}

