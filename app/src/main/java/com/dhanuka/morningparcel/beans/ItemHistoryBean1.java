package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class ItemHistoryBean1 implements Serializable {


    @SerializedName("success")
    String successstr;

    @SerializedName("returnds")
    ArrayList<Itemsarray> itembeanarr;

    public String getSuccessstr() {
        return successstr;
    }

    public void setSuccessstr(String successstr) {
        this.successstr = successstr;
    }

    public ArrayList<Itemsarray> getItembeanarr() {
        return itembeanarr;
    }

    public void setItembeanarr(ArrayList<Itemsarray> itembeanarr) {
        this.itembeanarr = itembeanarr;
    }

    public static class Itemsarray  implements Serializable
    {
        @SerializedName("TransactionType")
        String TransactionType;

        @SerializedName("OrderDate")
        String OrderDate;

        @SerializedName("RequestedQty")
        String RequestedQty;

        @SerializedName("Rate")
        String Rate;

        @SerializedName("RAmount")
        String RAmount;

        @SerializedName("MasterDiscount")
        String MasterDiscount;

        @SerializedName("orderid")
        String orderid;

        @SerializedName("CustomerID")
        String CustomerID;

        @SerializedName("CreatedDate")
        String CreatedDate;

        @SerializedName("CurrentPaidAmou")
        String CurrentPaidAmou;

        @SerializedName("DetailDiscount")
        String DetailDiscount;

        @SerializedName("PayemntType")
        String PayemntType;
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
        @SerializedName("MRP")
        String MRP;
        @SerializedName("UnitCost")
        String UnitCost;
        @SerializedName("Margin")
        String Margin;
        @SerializedName("MarginPerc")
        String MarginPerc;

        @SerializedName("InvNumber")
        String InvNumber;


        @SerializedName("Orderdetailid")
        String Orderdetailid;

        @SerializedName("itemid")
        String itemid;

        @SerializedName("Itemdescription")
        String Itemdescription;

        @SerializedName("UOM")
        String UOM;

        public String getTransactionType() {
            return TransactionType;
        }

        public void setTransactionType(String transactionType) {
            TransactionType = transactionType;
        }

        public String getOrderDate() {
            return OrderDate;
        }

        public void setOrderDate(String orderDate) {
            OrderDate = orderDate;
        }

        public String getRequestedQty() {
            return RequestedQty;
        }

        public void setRequestedQty(String requestedQty) {
            RequestedQty = requestedQty;
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

        public String getMasterDiscount() {
            return MasterDiscount;
        }

        public void setMasterDiscount(String masterDiscount) {
            MasterDiscount = masterDiscount;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getCustomerID() {
            return CustomerID;
        }

        public void setCustomerID(String customerID) {
            CustomerID = customerID;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }

        public String getCurrentPaidAmou() {
            return CurrentPaidAmou;
        }

        public void setCurrentPaidAmou(String currentPaidAmou) {
            CurrentPaidAmou = currentPaidAmou;
        }

        public String getDetailDiscount() {
            return DetailDiscount;
        }

        public void setDetailDiscount(String detailDiscount) {
            DetailDiscount = detailDiscount;
        }

        public String getPayemntType() {
            return PayemntType;
        }

        public void setPayemntType(String payemntType) {
            PayemntType = payemntType;
        }

        public String getOrderdetailid() {
            return Orderdetailid;
        }

        public void setOrderdetailid(String orderdetailid) {
            Orderdetailid = orderdetailid;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemdescription() {
            return Itemdescription;
        }

        public void setItemdescription(String itemdescription) {
            Itemdescription = itemdescription;
        }

        public String getUOM() {
            return UOM;
        }

        public void setUOM(String UOM) {
            this.UOM = UOM;
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

        public String getBalance() {
            return Balance;
        }

        public void setBalance(String balance) {
            Balance = balance;
        }

        public String getMRP() {
            return MRP;
        }

        public void setMRP(String MRP) {
            this.MRP = MRP;
        }

        public String getUnitCost() {
            return UnitCost;
        }

        public void setUnitCost(String unitCost) {
            UnitCost = unitCost;
        }

        public String getMargin() {
            return Margin;
        }

        public void setMargin(String margin) {
            Margin = margin;
        }

        public String getMarginPerc() {
            return MarginPerc;
        }

        public void setMarginPerc(String marginPerc) {
            MarginPerc = marginPerc;
        }

        public String getInvNumber() {
            return InvNumber;
        }

        public void setInvNumber(String invNumber) {
            InvNumber = invNumber;
        }

    }
}
