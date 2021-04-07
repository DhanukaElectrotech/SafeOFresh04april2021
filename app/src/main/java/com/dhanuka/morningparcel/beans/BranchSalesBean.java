package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BranchSalesBean implements Serializable {
    @SerializedName("success")
    String success;
    @SerializedName("returnds")
    ArrayList<beanchinnerbean> branchdatalist;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<beanchinnerbean> getBranchdatalist() {
        return branchdatalist;
    }

    public void setBranchdatalist(ArrayList<beanchinnerbean> branchdatalist) {
        this.branchdatalist = branchdatalist;
    }

    public static class  beanchinnerbean implements Serializable
    {

        @SerializedName("BranchName")
        String BranchName;

        @SerializedName("TotalSaleAmount")
        String TotalSaleAmount;
        @SerializedName("TotalOrder")
        String TotalOrder;
        @SerializedName("ItemCount")
        String ItemCount;
        @SerializedName("BranchId")
        String BranchId;

        public String getBranchName() {
            return BranchName;
        }

        public void setBranchName(String branchName) {
            BranchName = branchName;
        }

        public String getTotalSaleAmount() {
            return TotalSaleAmount;
        }

        public void setTotalSaleAmount(String totalSaleAmount) {
            TotalSaleAmount = totalSaleAmount;
        }

        public String getTotalOrder() {
            return TotalOrder;
        }

        public void setTotalOrder(String totalOrder) {
            TotalOrder = totalOrder;
        }

        public String getItemCount() {
            return ItemCount;
        }

        public void setItemCount(String itemCount) {
            ItemCount = itemCount;
        }

        public String getBranchId() {
            return BranchId;
        }

        public void setBranchId(String branchId) {
            BranchId = branchId;
        }
    }
   }
