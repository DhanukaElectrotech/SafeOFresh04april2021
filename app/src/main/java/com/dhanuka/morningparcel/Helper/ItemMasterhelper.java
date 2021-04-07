package com.dhanuka.morningparcel.Helper;

import java.io.Serializable;

public class ItemMasterhelper implements Serializable {
    String companyid;
    String ItemID;
    String ItemName;
    String GroupID;
    String OpeningStock;
    String MOQ;
    String ROQ;
    String PurchaseUOM;
    String DbSalerate1;
    String DbSalerate;
    String setproduct;
    String TotalPurchase;
    String TotalSale;
    String InQty;
    String OutQty;
    String Balance;

    String Discount;
    String PurchaseUOMId;
    String SaleUOM;
    String SaleUOMID;
    String PurchaseRate;
    String SaleRate;
    String ItemSKU;
    String ItemBarcode;
    String StockUOM;
    String ItemImage;
    String Margin;
    String HSNCode;
    String MRP;
    String IsDeal;
    String isImage;
    String isname;
    String SubSGroup;
    String SubSID;
    String GroupName="";
    Boolean isChecked=false;

    public String getIsImage() {
        return isImage;
    }

    public void setIsImage(String isImage) {
        this.isImage = isImage;
    }

    public String getIsname() {
        return isname;
    }

    public void setIsname(String isname) {
        this.isname = isname;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getSubSGroup() {
        return SubSGroup;
    }

    public void setSubSGroup(String subSGroup) {
        SubSGroup = subSGroup;
    }

    public String getSubSID() {
        return SubSID;
    }

    public void setSubSID(String subSID) {
        SubSID = subSID;
    }

    public String getIsDeal() {
        return IsDeal;
    }

    public void setIsDeal(String isDeal) {
        IsDeal = isDeal;
    }

    public String getIsNewListing() {
        return IsNewListing;
    }

    public void setIsNewListing(String isNewListing) {
        IsNewListing = isNewListing;
    }

    public String getRepeatOrder() {
        return RepeatOrder;
    }

    public void setRepeatOrder(String repeatOrder) {
        RepeatOrder = repeatOrder;
    }

    String IsNewListing;
    String RepeatOrder;
    int mId;
    String FileName;
    String filepath;
    private String quantity = "0";
    String VendorID;
    String ToShow;
    String AvailableQty;
    String StoreSTatus;

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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


    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public int getmId() {
        return mId;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDbSalerate1() {
        return DbSalerate1;
    }

    public void setDbSalerate1(String dbSalerate1) {
        DbSalerate1 = dbSalerate1;
    }

    public String getTotalPurchase() {
        return TotalPurchase;
    }

    public void setTotalPurchase(String totalPurchase) {
        TotalPurchase = totalPurchase;
    }

    public String getTotalSale() {
        return TotalSale;
    }

    public void setTotalSale(String totalSale) {
        TotalSale = totalSale;
    }

    public String getInQty() {
        return InQty;
    }

    public void setInQty(String inQty) {
        InQty = inQty;
    }

    public String getOutQty() {
        return OutQty;
    }

    public void setOutQty(String outQty) {
        OutQty = outQty;
    }

    public String getSetproduct() {
        return setproduct;
    }

    public void setSetproduct(String setproduct) {
        this.setproduct = setproduct;
    }


    String BranchName;
    String BranchID;

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getDbSalerate() {
        return DbSalerate;
    }

    public void setDbSalerate(String dbSalerate) {
        DbSalerate = dbSalerate;
    }

    public String getMargin() {
        return Margin;
    }

    public void setMargin(String margin) {
        Margin = margin;
    }
}
