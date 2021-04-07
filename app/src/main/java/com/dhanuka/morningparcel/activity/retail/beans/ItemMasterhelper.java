package com.dhanuka.morningparcel.activity.retail.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemMasterhelper implements Serializable {
    @SerializedName("ItemID")
    String ItemID;
    @SerializedName("ItemName")
    String ItemName;
    @SerializedName("companyid")
    String companyid;
    @SerializedName("GroupID")
    String GroupID;
    @SerializedName("OpeningStock")
    String OpeningStock;
    @SerializedName("ROQ")
    String ROQ;
    @SerializedName("MOQ")
    String MOQ;
    @SerializedName("PurchaseUOM")
    String PurchaseUOM;
    @SerializedName("PurchaseUOMId")
    String PurchaseUOMId;
    @SerializedName("SaleUOM")
    String SaleUOM;
    @SerializedName("SaleUOMID")
    String SaleUOMID;
    @SerializedName("PurchaseRate")
    String PurchaseRate;
    @SerializedName("SaleRate")
    String SaleRate;
    @SerializedName("ItemSKU")
    String ItemSKU;
    @SerializedName("ItemBarcode")
    String ItemBarcode;
    @SerializedName("StockUOM")
    String StockUOM;
    @SerializedName("ItemImage")
    String ItemImage;
    @SerializedName("HSNCode")
    String HSNCode;
    @SerializedName("FileName")
    String FileName;
    @SerializedName("filepath")
    String filepath;
    @SerializedName("VendorID")
    String VendorID;
   @SerializedName("MRP")
    String MRP;
    @SerializedName("ToShow")
    String ToShow;
    @SerializedName("AvailableQty")
    String AvailableQty;
    @SerializedName("StoreSTatus")
    String StoreSTatus;
     String weight;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getOpeningStock() {
        return OpeningStock;
    }

    public void setOpeningStock(String openingStock) {
        OpeningStock = openingStock;
    }

    public String getROQ() {
        return ROQ;
    }

    public void setROQ(String ROQ) {
        this.ROQ = ROQ;
    }

    public String getMOQ() {
        return MOQ;
    }

    public void setMOQ(String MOQ) {
        this.MOQ = MOQ;
    }

    public String getPurchaseUOM() {
        return PurchaseUOM;
    }

    public void setPurchaseUOM(String purchaseUOM) {
        PurchaseUOM = purchaseUOM;
    }

    public String getPurchaseUOMId() {
        return PurchaseUOMId;
    }

    public void setPurchaseUOMId(String purchaseUOMId) {
        PurchaseUOMId = purchaseUOMId;
    }

    public String getSaleUOM() {
        return SaleUOM;
    }

    public void setSaleUOM(String saleUOM) {
        SaleUOM = saleUOM;
    }

    public String getSaleUOMID() {
        return SaleUOMID;
    }

    public void setSaleUOMID(String saleUOMID) {
        SaleUOMID = saleUOMID;
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

    public String getItemSKU() {
        return ItemSKU;
    }

    public void setItemSKU(String itemSKU) {
        ItemSKU = itemSKU;
    }

    public String getItemBarcode() {
        return ItemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        ItemBarcode = itemBarcode;
    }

    public String getStockUOM() {
        return StockUOM;
    }

    public void setStockUOM(String stockUOM) {
        StockUOM = stockUOM;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

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

    public String getVendorID() {
        return VendorID;
    }

    public void setVendorID(String vendorID) {
        VendorID = vendorID;
    }

    public String getToShow() {
        return ToShow;
    }

    public void setToShow(String toShow) {
        ToShow = toShow;
    }

    public String getAvailableQty() {
        return AvailableQty;
    }

    public void setAvailableQty(String availableQty) {
        AvailableQty = availableQty;
    }

    public String getStoreSTatus() {
        return StoreSTatus;
    }

    public void setStoreSTatus(String storeSTatus) {
        StoreSTatus = storeSTatus;
    }
}