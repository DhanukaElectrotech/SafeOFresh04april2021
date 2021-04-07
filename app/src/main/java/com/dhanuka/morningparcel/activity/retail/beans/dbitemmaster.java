package com.dhanuka.morningparcel.activity.retail.beans;

/**
 * Created by LALIT2633 on 10-03-2016.
 */
public class dbitemmaster {
    private String mItemID;
    private String mItemName;
    private String mcompanycosting;
    private String mByItemNumber;
    private String mGroupID;
    private String mRoundKG;
    private String mAddWeightKG;
    private String mAllString;
    private String MRP;
    private String Salerate;
    private String TaxRate;
    private String HSNCode;

    public String getMcompanycosting() {
        return mcompanycosting;
    }

    public void setMcompanycosting(String mcompanycosting) {
        this.mcompanycosting = mcompanycosting;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getSalerate() {
        return Salerate;
    }

    public void setSalerate(String salerate) {
        Salerate = salerate;
    }

    public String getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(String taxRate) {
        TaxRate = taxRate;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

    public String getmItemID() { return mItemID; }	public void setmItemID(String ItemID) { this.mItemID= ItemID; }
    public String getmItemName() { return mItemName; }	public void setmItemName(String ItemName) { this.mItemName= ItemName; }
    public String getmcompanycosting() { return mcompanycosting; }	public void setmcompanycosting(String companycosting) { this.mcompanycosting= companycosting; }
    public String getmByItemNumber() { return mByItemNumber; }	public void setmByItemNumber(String ByItemNumber) { this.mByItemNumber= ByItemNumber; }
    public String getmGroupID() { return mGroupID; }	public void setmGroupID(String GroupID) { this.mGroupID= GroupID; }
    public String getmRoundKG() { return mRoundKG; }	public void setmRoundKG(String RoundKG) { this.mRoundKG= RoundKG; }
    public String getmAddWeightKG() { return mAddWeightKG; }	public void setmAddWeightKG(String AddWeightKG) { this.mAddWeightKG= AddWeightKG; }
    public String getmAllString() { return mAllString; }	public void setmAllString(String AllString) { this.mAllString= AllString; }

}
