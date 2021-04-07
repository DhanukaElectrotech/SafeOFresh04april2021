package com.dhanuka.morningparcel.Helper;

import java.io.Serializable;

public class BranchwiseHelper implements Serializable {

    String companyid;
    String ItemID;
    String ItemName;
    String BranchName;
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
    String BranchID;
    String Balance;
    String CurrentStock;
    String Stockdiff;
    String CreatedDate;
    String totalpurchase;

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

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
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

    public String getDbSalerate1() {
        return DbSalerate1;
    }

    public void setDbSalerate1(String dbSalerate1) {
        DbSalerate1 = dbSalerate1;
    }

    public String getDbSalerate() {
        return DbSalerate;
    }

    public void setDbSalerate(String dbSalerate) {
        DbSalerate = dbSalerate;
    }

    public String getSetproduct() {
        return setproduct;
    }

    public void setSetproduct(String setproduct) {
        this.setproduct = setproduct;
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

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getCurrentStock() {
        return CurrentStock;
    }

    public void setCurrentStock(String currentStock) {
        CurrentStock = currentStock;
    }

    public String getStockdiff() {
        return Stockdiff;
    }

    public void setStockdiff(String stockdiff) {
        Stockdiff = stockdiff;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getTotalpurchase() {
        return totalpurchase;
    }

    public void setTotalpurchase(String totalpurchase) {
        this.totalpurchase = totalpurchase;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
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

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
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

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }
}


