package com.dhanuka.morningparcel.Helper;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Payfraghelper implements Serializable {
//    String paymentamount,payid,paystatus,paydate;


    @SerializedName("success")
    String success;
 @SerializedName("CurrentBalance")
    String CurrentBalance;

    @SerializedName("returnds")
    ArrayList<paybean> order_data;

    public String getCurrentBalance() {
        return CurrentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        CurrentBalance = currentBalance;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<paybean> getOrder_data() {
        return order_data;
    }

    public void setOrder_data(ArrayList<paybean> order_data) {
        this.order_data = order_data;
    }

    public class paybean implements Serializable {


        @SerializedName("Amount")
        String Amount;
        @SerializedName("ContactID")
        String ContactID;
        @SerializedName("OrderId")
        String OrderId;
        @SerializedName("ExpType")
        String ExpType;
        @SerializedName("ExpDate")
        String ExpDate;
        @SerializedName("CreartionDate")
        String CreartionDate;
        @SerializedName("CreatedBy")
        String CreatedBy;
        @SerializedName("Comment")
        String Comment;
        @SerializedName("MCompanyID")
        String MCompanyID;
        @SerializedName("BillNo")
        String BillNo;

        @SerializedName("PaymentMode")
        String PaymentMode;

        @SerializedName("CollectedBy")
        String CollectedBy;

        @SerializedName("CustomerName")
        String CustomerName;

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }


        public String getAmount() {
            return Amount;
        }

        public void setAmount(String amount) {
            Amount = amount;
        }

        public String getContactID() {
            return ContactID;
        }

        public void setContactID(String contactID) {
            ContactID = contactID;
        }

        public String getExpType() {
            return ExpType;
        }

        public void setExpType(String expType) {
            ExpType = expType;
        }

        public String getExpDate() {
            return ExpDate;
        }

        public void setExpDate(String expDate) {
            ExpDate = expDate;
        }

        public String getCreartionDate() {
            return CreartionDate;
        }

        public void setCreartionDate(String creartionDate) {
            CreartionDate = creartionDate;
        }

        public String getCreatedBy() {
            return CreatedBy;
        }

        public void setCreatedBy(String createdBy) {
            CreatedBy = createdBy;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String comment) {
            Comment = comment;
        }

        public String getMCompanyID() {
            return MCompanyID;
        }

        public void setMCompanyID(String MCompanyID) {
            this.MCompanyID = MCompanyID;
        }

        public String getBillNo() {
            return BillNo;
        }

        public void setBillNo(String billNo) {
            BillNo = billNo;
        }

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String orderId) {
            OrderId = orderId;
        }

        public String getPaymentMode() {
            return PaymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            PaymentMode = paymentMode;
        }

        public String getCollectedBy() {
            return CollectedBy;
        }

        public void setCollectedBy(String collectedBy) {
            CollectedBy = collectedBy;
        }
    }

}
