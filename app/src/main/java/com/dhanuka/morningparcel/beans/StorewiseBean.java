package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class StorewiseBean implements Serializable {
    @SerializedName("success")
    String success;
    @SerializedName("returnds")
    ArrayList<storeinnerbean> storedatalist;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<storeinnerbean> getstoredatalist() {
        return storedatalist;
    }

    public void setstoredatalist(ArrayList<storeinnerbean> storedatalist) {
        this.storedatalist = storedatalist;
    }

    public  class  storeinnerbean implements Serializable
    {

        @SerializedName("StoreName")
        String StoreName;

        @SerializedName("TotalPurchase")
        String TotalPurchase;
        @SerializedName("TotalSale")
        String TotalSale;
        @SerializedName("InQty")
        String InQty;
        @SerializedName("OutQty")
        String OutQty;
        @SerializedName("Balance")
        String Balance;
        @SerializedName("BranchID")
        String BranchID;
        @SerializedName("ItemName")
        String ItemName;

        @SerializedName("GroupName")
        String GroupName;
        @SerializedName("FileName")
        String FileName;
        @SerializedName("filepath")
        String filepath;
        @SerializedName("SubGroupName")
        String SubGroupName;
        @SerializedName("ItemID")
        String ItemID;





        public String getStoreName() {
            return StoreName;
        }

        public void setStoreName(String StoreName) {
            StoreName = StoreName;
        }

        public String getTotalPurchase() {
            return TotalPurchase;
        }

        public void setTotalPurchase(String TotalPurchase) {
            TotalPurchase = TotalPurchase;
        }

        public String getTotalSale() {
            return TotalSale;
        }

        public void setTotalSale(String TotalSale) {
            TotalSale = TotalSale;
        }

        public String getInQty() {
            return InQty;
        }

        public void setInQty(String InQty) {
            InQty = InQty;
        }

        public String getOutQty() {
            return OutQty;
        }

        public void setOutQty(String OutQty) {
            OutQty = OutQty;
        }

        public String getBalance() {
            return Balance;
        }

        public void setBalance(String balance) {
            Balance = balance;
        }

        public String getBranchID() {
            return BranchID;
        }

        public void setBranchID(String branchID) {
            BranchID = branchID;
        }

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String itemName) {
            ItemName = itemName;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String groupName) {
            GroupName = groupName;
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

        public String getSubGroupName() {
            return SubGroupName;
        }

        public void setSubGroupName(String subGroupName) {
            SubGroupName = subGroupName;
        }

        public String getItemID() {
            return ItemID;
        }

        public void setItemID(String itemID) {
            ItemID = itemID;
        }
    }
}
