package com.dhanuka.morningparcel.model;

import java.io.Serializable;

public class DashModel  implements Serializable {
    String cardname,strid;
    int cardimg;

    public  DashModel(String strid,String cardname,int cardimg)
    {
        this.cardimg=cardimg;
        this.cardname=cardname;
        this.strid=strid;

    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getStrid() {
        return strid;
    }

    public void setStrid(String strid) {
        this.strid = strid;
    }

    public int getCardimg() {
        return cardimg;
    }

    public void setCardimg(int cardimg) {
        this.cardimg = cardimg;
    }
}
