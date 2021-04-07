package com.dhanuka.morningparcel.Helper;

import java.io.Serializable;

public class ItemUomHelper implements Serializable {
    String  ItemUOMID,ItemID,ItemDescription,UOMID,UOMDesc,Qty,PurchaseRate,SaleRate;
    int mID;

    public String getItemUOMID() {
        return ItemUOMID;
    }

    public void setItemUOMID(String itemUOMID) {
        ItemUOMID = itemUOMID;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getUOMID() {
        return UOMID;
    }

    public void setUOMID(String UOMID) {
        this.UOMID = UOMID;
    }

    public String getUOMDesc() {
        return UOMDesc;
    }

    public void setUOMDesc(String UOMDesc) {
        this.UOMDesc = UOMDesc;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getPurchaseRate() {
        return PurchaseRate;
    }

    public void setPurchaseRate(String purchaseRate) {
        PurchaseRate = purchaseRate;
    }

    public String getSaleRate() {
        return SaleRate;
    }

    public void setSaleRate(String saleRate) {
        SaleRate = saleRate;
    }


    public void setmID(int mID) {
        this.mID = mID;
    }
}
