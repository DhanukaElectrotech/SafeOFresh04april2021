package com.dhanuka.morningparcel.pdfsupport;

public class ProductModel {

    public String name, hsncode, qty, mrp, rate, amount, status;

    public ProductModel(String name, String hsncode, String qty, String mrp, String rate, String amount, String status) {

        this.name = name;
        this.hsncode = hsncode;
        this.qty = qty;
        this.mrp = mrp;
        this.rate = rate;
        this.amount = amount;
        this.status = status;


    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHsncode() {
        return hsncode;
    }

    public void setHsncode(String hsncode) {
        this.hsncode = hsncode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}