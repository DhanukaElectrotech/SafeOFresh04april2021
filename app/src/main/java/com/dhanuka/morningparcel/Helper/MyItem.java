package com.dhanuka.morningparcel.Helper;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    public int getStoreposition() {
        return storeposition;
    }

    public void setStoreposition(int storeposition) {
        this.storeposition = storeposition;
    }

    public  int storeposition;
    private  String storename;
    String title,snippet;
    String address;

    public ArrayList<ShoplistHelper> getShoplist() {
        return shoplist;
    }

    public void setShoplist(ArrayList<ShoplistHelper> shoplist) {
        this.shoplist = shoplist;
    }

    ArrayList<ShoplistHelper> shoplist = new ArrayList<>();
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    String imagelink;

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    private  String storeid;



    public MyItem(LatLng mPosition, String storename, String storeid, String imagelink, String address, ArrayList<ShoplistHelper> shoplist,int storeposition) {
        this.mPosition=mPosition;
        this.shoplist=shoplist;
        this.address=address;
        this.storeposition=storeposition;
        this.imagelink=imagelink;
        this.storename=storename;
        this.storeid=storeid;


    }

//
//    public MyItem(LatLng mPosition, String title, String snippet) {
//        this.mPosition=mPosition;
//        this.title = title;
//        this.snippet = snippet;
//    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}