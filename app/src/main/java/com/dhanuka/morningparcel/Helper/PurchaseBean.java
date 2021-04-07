package com.dhanuka.morningparcel.Helper;

import java.io.Serializable;

public class PurchaseBean implements Serializable {
    String itembarcode,companycosting,ItemName,mrp,taxrate,groupname,TotalSale,TotalPurchase,TotalSale1,TotalPurchase1,
            Currentstock,setchoose,suppliername,supplierid,FileName,filepath,MarginP,Saleinlastoneweek;
    boolean setselected=false;
String itemid;
String Balance;
String InQty;
String OutQty;
String BranchName;

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getInQty() {
        return InQty;
    }

    public void setInQty(String inQty) {
        InQty = inQty;
    }

    public String getOutQty() {
        return OutQty;
    }

    public void setOutQty(String outQty) {
        OutQty = outQty;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItembarcode() {
        return itembarcode;
    }

    public void setItembarcode(String itembarcode) {
        this.itembarcode = itembarcode;
    }

    public String getCompanycosting() {
        return companycosting;
    }

    public void setCompanycosting(String companycosting) {
        this.companycosting = companycosting;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(String taxrate) {
        this.taxrate = taxrate;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getTotalSale() {
        return TotalSale;
    }

    public void setTotalSale(String totalSale) {
        TotalSale = totalSale;
    }

    public String getCurrentstock() {
        return Currentstock;
    }

    public void setCurrentstock(String currentstock) {
        Currentstock = currentstock;
    }

    public boolean isSetselected() {
        return setselected;
    }

    public void setSetselected(boolean setselected) {
        this.setselected = setselected;
    }

    public String getSetchoose() {
        return setchoose;
    }

    public void setSetchoose(String setchoose) {
        this.setchoose = setchoose;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getMarginP() {
        return MarginP;
    }

    public void setMarginP(String marginP) {
        MarginP = marginP;
    }

    public String getSaleinlastoneweek() {
        return Saleinlastoneweek;
    }

    public void setSaleinlastoneweek(String saleinlastoneweek) {
        Saleinlastoneweek = saleinlastoneweek;
    }

    public String getTotalPurchase() {
        return TotalPurchase;
    }

    public void setTotalPurchase(String totalPurchase) {
        TotalPurchase = totalPurchase;
    }

    public String getTotalSale1() {
        return TotalSale1;
    }

    public void setTotalSale1(String totalSale1) {
        TotalSale1 = totalSale1;
    }

    public String getTotalPurchase1() {
        return TotalPurchase1;
    }

    public void setTotalPurchase1(String totalPurchase1) {
        TotalPurchase1 = totalPurchase1;
    }
}
