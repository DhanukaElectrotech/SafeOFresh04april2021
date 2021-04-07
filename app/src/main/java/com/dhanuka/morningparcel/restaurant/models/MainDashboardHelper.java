package com.dhanuka.morningparcel.restaurant.models;

import java.io.Serializable;

public class MainDashboardHelper implements Serializable {
    String header,maintext,imagelink;
   public void MainDashboardHelper(String header,String maintext,String imagelink)
   {
       this.header = header;
       this.maintext = maintext;
       this.imagelink = imagelink;

   }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMaintext() {
        return maintext;
    }

    public void setMaintext(String maintext) {
        this.maintext = maintext;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
}
