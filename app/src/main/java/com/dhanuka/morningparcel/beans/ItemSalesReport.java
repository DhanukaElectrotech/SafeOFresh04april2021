package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemSalesReport implements Serializable {

    @SerializedName("ItemDescription")
    String ItemDescription;
    @SerializedName("ItemID")
    String ItemID;
    @SerializedName("BarCode")
    String BarCode;
    @SerializedName("MRP")
    String MRP;
    @SerializedName("Qty")
    String Qty;
    @SerializedName("BillAmount")
    String BillAmount;
    @SerializedName("Discount")
    String Discount;
    @SerializedName("CostPrice")
    String CostPrice;

    @SerializedName("Profit")
    String Profit;

    @SerializedName("ProfitP")
    String ProfitP;
    @SerializedName("UnitCost")
    String UnitCost;





    @SerializedName("DiscountP")
    String DiscountP;

    @SerializedName("Margin")
    String Margin;
    @SerializedName("GroupID")
    String GroupID;
    @SerializedName("GroupName")
    String GroupName;
    @SerializedName("FileName")
    String FileName;
  @SerializedName("filepath")
    String filepath;
    Boolean isOpened=false;
    Boolean whtsappcheck=false;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Boolean getOpened() {
        return isOpened;
    }

    public void setOpened(Boolean opened) {
        isOpened = opened;
    }


    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
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

    public String getProfit() {
        return Profit;
    }

    public void setProfit(String profit) {
        Profit = profit;
    }

    public String getUnitCost() {
        return UnitCost;
    }

    public void setUnitCost(String unitCost) {
        UnitCost = unitCost;
    }

    public String getProfitP() {
        return ProfitP;
    }

    public void setProfitP(String profitP) {
        ProfitP = profitP;
    }

    public Boolean getWhtsappcheck() {
        return whtsappcheck;
    }

    public void setWhtsappcheck(Boolean whtsappcheck) {
        this.whtsappcheck = whtsappcheck;
    }
}
