package com.dhanuka.morningparcel.beans;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;

import java.io.Serializable;
import java.util.ArrayList;

public class StoreBean implements Serializable {

    MainCatBean.CatBean mBean;
    ArrayList<ItemMasterhelper> mListProducts;


    public MainCatBean.CatBean getmBean() {
        return mBean;
    }

    public void setmBean(MainCatBean.CatBean mBean) {
        this.mBean = mBean;
    }

    public ArrayList<ItemMasterhelper> getmListProducts() {
        return mListProducts;
    }

    public void setmListProducts(ArrayList<ItemMasterhelper> mListProducts) {
        this.mListProducts = mListProducts;
    }
}
