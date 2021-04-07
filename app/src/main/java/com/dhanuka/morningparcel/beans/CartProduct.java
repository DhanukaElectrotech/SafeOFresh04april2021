package com.dhanuka.morningparcel.beans;

import java.io.Serializable;

/**
  */
public class CartProduct implements Serializable {



    private int ItemID;
    private String companyid;
    private String ItemName;
    private String GroupID;
    private String OpeningStock;
    private  String ItemTaxRate;
    private  String ItemTaxAmount;
    private String MOQ;
    private String ROQ;
    private String PurchaseUOM;
    private String PurchaseUOMId;
    private String SaleUOM;
    private String SaleUOMID;
    private String PurchaseRate;
    private String SaleRate;
    private String ItemSKU;
    private String ItemBarcode;
    private String StockUOM;
    private String ItemImage;
    private String HSNCode;
    private int quantity;
    private String quantity1;
    private String subCategory;
    private String shopId;
    private String MRP;
    String Status;
    String Type;
    String RAmount;
    String Discount;
    String DetailID;
    boolean setorder;

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
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

    public String getMOQ() {
        return MOQ;
    }

    public void setMOQ(String MOQ) {
        this.MOQ = MOQ;
    }

    public String getROQ() {
        return ROQ;
    }

    public void setROQ(String ROQ) {
        this.ROQ = ROQ;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
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

    public String getRAmount() {
        return RAmount;
    }

    public void setRAmount(String RAmount) {
        this.RAmount = RAmount;
    }

    public String getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(String quantity1) {
        this.quantity1 = quantity1;
    }

    public String getItemTaxRate() {
        return ItemTaxRate;
    }

    public void setItemTaxRate(String itemTaxRate) {
        ItemTaxRate = itemTaxRate;
    }

    public String getItemTaxAmount() {
        return ItemTaxAmount;
    }

    public void setItemTaxAmount(String itemTaxAmount) {
        ItemTaxAmount = itemTaxAmount;
    }

    public boolean isSetorder() {
        return setorder;
    }

    public void setSetorder(boolean setorder) {
        this.setorder = setorder;
    }
}
