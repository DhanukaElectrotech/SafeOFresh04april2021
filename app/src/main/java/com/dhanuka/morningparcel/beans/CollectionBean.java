package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CollectionBean  implements Serializable {

    @SerializedName("CustomerName")
    String CustomerName;

    @SerializedName("ContactNo")
    String ContactNo;

    @SerializedName("Balance")
    String Balance;

    @SerializedName("FlatNo")
    String FlatNo;

    @SerializedName("Createdby")
    String Createdby;

    @SerializedName("CreatedDate")
    String CreatedDate;

    @SerializedName("Paytype")
    String Paytype;

    @SerializedName("CustomerID")
    String CustomerID;
    @SerializedName("IsFinanceBlock")
    String IsFinanceBlock;

    @SerializedName("orderflag")
    String orderflag;




    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getCreatedby() {
        return Createdby;
    }

    public void setCreatedby(String createdby) {
        Createdby = createdby;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getPaytype() {
        return Paytype;
    }

    public void setPaytype(String paytype) {
        Paytype = paytype;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getFlatNo() {
        return FlatNo;
    }

    public void setFlatNo(String flatNo) {
        FlatNo = flatNo;
    }




    public String getIsFinanceBlock() {
        return IsFinanceBlock;
    }

    public void setIsFinanceBlock(String isFinanceBlock) {
        IsFinanceBlock = isFinanceBlock;
    }

    public String getOrderflag() {
        return orderflag;
    }

    public void setOrderflag(String orderflag) {
        this.orderflag = orderflag;
    }
}
