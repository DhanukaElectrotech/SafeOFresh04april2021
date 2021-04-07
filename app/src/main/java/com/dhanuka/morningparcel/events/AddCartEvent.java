package com.dhanuka.morningparcel.events;


import com.dhanuka.morningparcel.Helper.ItemMasterhelper;

/**
  */
public class AddCartEvent {
    private int qty;
    private ItemMasterhelper product;
    private String price;

    public AddCartEvent(int qty, ItemMasterhelper product, String price) {
        this.qty = qty;
        this.product = product;
        this.price = price;
    }

    public ItemMasterhelper getProduct() {
        return product;
    }

    public String getPrice(){
        return price;
    }

    public int getQty() {
        return qty;
    }
}
