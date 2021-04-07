package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemOrderBean implements Serializable {


    @SerializedName("OrderID")
    String OrderID       ;
    @SerializedName("OrderDate")
    String OrderDate     ;
    @SerializedName("ItemDescription")
    String ItemDescription;
    @SerializedName("ItemID")
    String ItemID        ;
    @SerializedName("BarCode")
    String BarCode       ;
    @SerializedName("MRP")
    String MRP           ;
    @SerializedName("OrderDetailID")
    String OrderDetailID ;
    @SerializedName("RequestedQty")
    String RequestedQty  ;
    @SerializedName("Rate")
    String Rate          ;
    @SerializedName("RAmount")
    String RAmount       ;
    @SerializedName("Discount")
    String Discount      ;
    @SerializedName("CreatedDate")
    String CreatedDate   ;
    @SerializedName("OrderType")
    String OrderType     ;
    @SerializedName("CustomerID")
    String CustomerID    ;
    @SerializedName("CompanyID")
    String CompanyID     ;
    @SerializedName("MasterStatus")
    String MasterStatus  ;
    @SerializedName("DetailStatus")
    String DetailStatus  ;
    @SerializedName("PaymentMode")
    String PaymentMode   ;
    @SerializedName("DeliveryMode")
    String DeliveryMode  ;
    @SerializedName("PayemntType")
    String PayemntType   ;
    @SerializedName("GroupName")
    String GroupName     ;
    @SerializedName("GroupID")
    String GroupID       ;

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
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

    public String getOrderDetailID() {
        return OrderDetailID;
    }

    public void setOrderDetailID(String orderDetailID) {
        OrderDetailID = orderDetailID;
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

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getMasterStatus() {
        return MasterStatus;
    }

    public void setMasterStatus(String masterStatus) {
        MasterStatus = masterStatus;
    }

    public String getDetailStatus() {
        return DetailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        DetailStatus = detailStatus;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getDeliveryMode() {
        return DeliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        DeliveryMode = deliveryMode;
    }

    public String getPayemntType() {
        return PayemntType;
    }

    public void setPayemntType(String payemntType) {
        PayemntType = payemntType;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }
}
