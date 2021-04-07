package com.dhanuka.morningparcel.Helper;

import java.io.Serializable;

public class ShoplistHelper implements Serializable {
    String shopid,shopname;


    String Alartphonenumber ;
    String PhonePe;
    String PaytmNumber ;
    String GooglePay;
    String Currency;
    String DeliveryCharge;
     String ServiceFees;
    String MaxOrderAmt;
    String MinOrderAmt;
    String CheckOutMessage;
    String ImageName;
    String filepath;
    String discount;
    String Tax;
    String Distance;
    String City;

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }



    public String getCheckOutMessage() {
        return CheckOutMessage;
    }

    public void setCheckOutMessage(String checkOutMessage) {
        CheckOutMessage = checkOutMessage;
    }

    public String getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public String getServiceFees() {
        return ServiceFees;
    }

    public void setServiceFees(String serviceFees) {
        ServiceFees = serviceFees;
    }

    public String getMaxOrderAmt() {
        return MaxOrderAmt;
    }

    public void setMaxOrderAmt(String maxOrderAmt) {
        MaxOrderAmt = maxOrderAmt;
    }

    public String getMinOrderAmt() {
        return MinOrderAmt;
    }

    public void setMinOrderAmt(String minOrderAmt) {
        MinOrderAmt = minOrderAmt;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getAlartphonenumber() {
        return Alartphonenumber;
    }

    public void setAlartphonenumber(String alartphonenumber) {
        Alartphonenumber = alartphonenumber;
    }

    public String getPhonePe() {
        return PhonePe;
    }

    public void setPhonePe(String phonePe) {
        PhonePe = phonePe;
    }

    public String getPaytmNumber() {
        return PaytmNumber;
    }

    public void setPaytmNumber(String paytmNumber) {
        PaytmNumber = paytmNumber;
    }

    public String getGooglePay() {
        return GooglePay;
    }

    public void setGooglePay(String googlePay) {
        GooglePay = googlePay;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

 }
