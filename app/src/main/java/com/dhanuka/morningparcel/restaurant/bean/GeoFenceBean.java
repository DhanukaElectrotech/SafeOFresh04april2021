package com.dhanuka.morningparcel.restaurant.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GeoFenceBean implements Serializable {
    @SerializedName("GeocodeID")
    private String strgeocodeid;

    @SerializedName("ClientID")
    private String strclientid;


    @SerializedName("ObjType")
    private String strobjtyp;

    @SerializedName("Description")
    private String strdescription;

    @SerializedName("Lat")
    private String strlat;

    @SerializedName("Long")
    private String strlong;

    @SerializedName("Radiousinkm")
    private String strradiusinkm;

    @SerializedName("Iconname")
    private String striconname;


    @SerializedName("Status")
    private String strstatus;
    @SerializedName("Geopolygon")
    private ArrayList<GeoFenceBean> strgeopolyg;

    public String getStrgeocodeid() {
        return strgeocodeid;
    }

    public void setStrgeocodeid(String strgeocodeid) {
        this.strgeocodeid = strgeocodeid;
    }

    public String getStrclientid() {
        return strclientid;
    }

    public void setStrclientid(String strclientid) {
        this.strclientid = strclientid;
    }

    public String getStrdescription() {
        return strdescription;
    }

    public void setStrdescription(String strdescription) {
        this.strdescription = strdescription;
    }

    public String getStrlat() {
        return strlat;
    }

    public void setStrlat(String strlat) {
        this.strlat = strlat;
    }

    public String getStrlong() {
        return strlong;
    }

    public void setStrlong(String strlong) {
        this.strlong = strlong;
    }

    public String getStrradiusinkm() {
        return strradiusinkm;
    }

    public void setStrradiusinkm(String strradiusinkm) {
        this.strradiusinkm = strradiusinkm;
    }

    public String getStriconname() {
        return striconname;
    }

    public void setStriconname(String striconname) {
        this.striconname = striconname;
    }



    public String getStrstatus() {
        return strstatus;
    }

    public void setStrstatus(String strstatus) {
        this.strstatus = strstatus;
    }

    public ArrayList<GeoFenceBean> getStrgeopolyg() {
        return strgeopolyg;
    }

    public void setStrgeopolyg(ArrayList<GeoFenceBean> strgeopolyg) {
        this.strgeopolyg = strgeopolyg;
    }

    public String getStrobjtyp() {
        return strobjtyp;
    }

    public void setStrobjtyp(String strobjtyp) {
        this.strobjtyp = strobjtyp;
    }
}
